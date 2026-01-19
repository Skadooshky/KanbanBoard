package db;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_FILE_NAME = "kanban.db";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        Path dbPath = Paths.get(DB_FILE_NAME).toAbsolutePath();
        System.out.println("Using DB at: " + dbPath); 
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }
}
