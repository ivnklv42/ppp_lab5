package application.dao;

import application.Database;
import onlinestore.Product;
import onlinestore.ProductType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductsDao {
    public static void insert(Product product) throws SQLException {
        String sql = """
                INSERT INTO products (title, price, product_type)
                VALUES (?, ?, ?)
                ON CONFLICT(title) DO UPDATE SET
                    price = excluded.price,
                    product_type = excluded.product_type
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getTitle());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getProductType().toString());
            stmt.executeUpdate();
        }
    }

    public static Product findByTitle(String title) throws IOException, SQLException {
        String sql = "SELECT id, title, price, product_type FROM products WHERE title = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String ptitle = rs.getString("title");
                    double price = rs.getDouble("price");
                    ProductType productType = ProductType.fromString(rs.getString("product_type"));

                    return new Product(id, ptitle, price, productType);
                } else {
                    return null;
                }
            }
        }
    }

    public static void update(Product oldProduct, Product newProduct) throws SQLException {
        String sql = "UPDATE products SET title = ?, price = ?, product_type = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newProduct.getTitle());
            stmt.setDouble(2, newProduct.getPrice());
            stmt.setString(3, newProduct.getProductType().toString());
            stmt.setLong(4, oldProduct.getId());
            stmt.executeUpdate();
        }
    }

    public static void delete(Product product) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, product.getId());
            stmt.executeUpdate();
        }
    }

    public static String findAllAsStrings() throws SQLException {
        String sql = """
            SELECT title, price, product_type
              FROM products
             ORDER BY price DESC, title
            """;
        String products = "";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String title = rs.getString("title");
                double price = rs.getDouble("price");
                String productType = rs.getString("product_type");
                products += String.format("%s - %.2f - %s\n", title, price, productType);
            }
        }

        return products;
    }

    public static void clearTable() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM products");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='products'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='orders'");
        }
    }

    public static void replaceFromList(ArrayList<Product> products) throws SQLException {
        ProductsDao.clearTable();
        ProductsDao.updateFromList(products);
    }

    public static void updateFromList(ArrayList<Product> products) throws SQLException {
        for (Product product : products) {
            ProductsDao.insert(product);
        }
    }

    public static boolean saveToFile(File file)  {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(ProductsDao.findAllAsStrings());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

