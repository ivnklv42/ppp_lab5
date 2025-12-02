package application.dao;

import application.Database;
import onlinestore.Customer;
import onlinestore.Product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CustomersDao {
    public static void insert(Customer customer) throws SQLException {
        String sql = """
                INSERT INTO customers (name)
                VALUES (?)
                ON CONFLICT(name) DO UPDATE SET
                    name = excluded.name
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.executeUpdate();
        }
    }

    public static Customer findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM customers WHERE name = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String cname = rs.getString("name");

                    return new Customer(id, cname);
                } else {
                    return null;
                }
            }
        }
    }

    public static void delete(Customer customer) throws SQLException {
        String sql = "DELETE FROM customers WHERE name = ?";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.executeUpdate();
        }
    }

    public static String findAllAsStrings() throws SQLException {
        String sql = """
            SELECT o.quantity AS quantity, o.total AS total, p.title AS title, p.price AS price,
            p.product_type AS product_type, c.name AS name
              FROM orders o
              JOIN customers c ON o.customer_id = c.id
              JOIN products p  ON o.product_id = p.id
             ORDER BY c.name, o.total DESC
            """;
        HashMap<String, String> orders = new HashMap<>();
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                String title = rs.getString("title");
                double price = rs.getDouble("price");
                String productType = rs.getString("product_type");
                String name = rs.getString("name");

                String current = orders.getOrDefault(name, "");
                if (current.isEmpty()) {
                    current += "\nЗаказы:";
                }

                current += String.format(Locale.US, "\n%s - %.2f - %s - %d - %.2f",
                        title, price, productType, quantity, quantity * price);
                orders.put(name, current);
            }
        }

        ArrayList<String> customers = new ArrayList<>();
        sql = "SELECT name FROM customers";
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(rs.getString("name"));
            }
        }

        String result = "";
        for (Map.Entry<String, String> entry : orders.entrySet()) {
            result += entry.getKey() + entry.getValue() + "\n\n";
            customers.remove(entry.getKey());
        }

        for (String customer : customers) {
            result += customer + "\nЗаказы:\n\n";
        }

        return result;
    }

    public static boolean updateFromMap(HashMap<String, String> content) throws SQLException, IOException {
        ArrayList<Object[]> inserts = new ArrayList<>();
        for (Map.Entry<String, String> entry : content.entrySet()) {
            String name = entry.getKey();
            CustomersDao.insert(new Customer(name));

            String[] orders = entry.getValue().split("\n");
            for (String order : orders) {
                String[] elements = order.split(" - ");
                Customer customer = CustomersDao.findByName(name);
                Product product = ProductsDao.findByTitle(elements[0]);
                if (customer == null || product == null) {
                    return false;
                }

                inserts.add(new Object[]{customer, product, elements[3]});
            }
        }

        for (Object[] insert : inserts) {
            OrdersDao.insert((Customer) insert[0], (Product) insert[1], Integer.parseInt((String) insert[2]));
        }

        return true;
    }

    public static void clearTable() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM customers");
            stmt.executeUpdate("DELETE FROM orders");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='customers'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='orders'");
        }
    }

    public static boolean replaceFromMap(HashMap<String, String> content) throws SQLException, IOException {
        CustomersDao.clearTable();
        return CustomersDao.updateFromMap(content);
    }

    public static boolean saveToFile(File file) throws SQLException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CustomersDao.findAllAsStrings());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
