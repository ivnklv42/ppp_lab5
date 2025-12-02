package application.dao;

import application.Database;
import onlinestore.Customer;
import onlinestore.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrdersDao {
    public static void insert(Customer customer, Product product, int quantity) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, product_id, quantity, total) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, customer.getId());
            stmt.setLong(2, product.getId());
            stmt.setInt(3, quantity);
            stmt.setDouble(4, quantity * product.getPrice());
            stmt.executeUpdate();
        }
    }
}
