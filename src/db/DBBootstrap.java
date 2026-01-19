package db;

import java.sql.Connection;
import java.sql.Statement;

public class DBBootstrap {
    private DBBootstrap() {}

    public static void ensureSchema() throws Exception {
        String ddl = """
            CREATE TABLE IF NOT EXISTS cards (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                status TEXT NOT NULL,
                created_at TEXT NOT NULL DEFAULT (datetime('now')),
                due_date TEXT NOT NULL,
                owner TEXT NOT NULL,
                assignee TEXT NOT NULL
            );
            """;

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(ddl);
        }
    }
}
