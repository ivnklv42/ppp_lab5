package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private static final String APP_NAME = "OnlineStore";
    private static final String DB_FILE_NAME = "onlinestoredb.sqlite";

    private static String jdbcUrl;

    private Database() { }

    public static void init() throws SQLException {
        String appData = System.getenv("APPDATA");
        if (appData == null || appData.isEmpty()) {
            throw new IllegalStateException("APPDATA не найдена");
        }

        Path dir = Paths.get(appData, APP_NAME);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось создать каталог данных: " + dir, e);
        }

        Path dbPath = dir.resolve(DB_FILE_NAME);
        jdbcUrl = "jdbc:sqlite:" + dbPath.toString();

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");

            String createCustomers = """
                    CREATE TABLE IF NOT EXISTS customers (
                        id      INTEGER PRIMARY KEY AUTOINCREMENT,
                        name    TEXT NOT NULL UNIQUE
                    );
                    """;

            String createProducts = """
                    CREATE TABLE IF NOT EXISTS products (
                        id            INTEGER PRIMARY KEY AUTOINCREMENT,
                        title         TEXT NOT NULL UNIQUE,
                        price         REAL NOT NULL,
                        product_type  TEXT NOT NULL
                    );
                    """;

            String createOrders = """
                    CREATE TABLE IF NOT EXISTS orders (
                        id          INTEGER PRIMARY KEY AUTOINCREMENT,
                        customer_id     INTEGER NOT NULL,
                        product_id  INTEGER NOT NULL,
                        quantity    INTEGER NOT NULL,
                        total       REfAL NOT NULL,
                        FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
                        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
                    );
                    """;

            stmt.execute(createCustomers);
            stmt.execute(createProducts);
            stmt.execute(createOrders);
        }
     }

    public static Connection getConnection() throws SQLException {
        if (jdbcUrl == null) {
            throw new IllegalStateException("Database.init() не был вызван");
        }

        return DriverManager.getConnection(jdbcUrl);
    }
}

