package dao;

import db.DBConnection;
import model.Card;
import model.KanbanStatus;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDao {

    public List<Card> getAllCards() throws Exception {
        String sql = """
            SELECT id, title, description, status, created_at, due_date, owner, assignee, expedite
            FROM cards
            ORDER BY expedite DESC, id ASC
            """;

        List<Card> results = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                boolean expedite = rs.getInt("expedite") == 1;

                results.add(new Card(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        KanbanStatus.fromDb(rs.getString("status")),
                        rs.getString("created_at"),
                        rs.getString("due_date"),
                        rs.getString("owner"),
                        rs.getString("assignee"),
                        expedite
                ));
            }
        }
        return results;
    }

    public int insertCard(String title, String description, KanbanStatus status,
                          String dueDate, String owner, String assignee, boolean expedite) throws Exception {
        String sql = """
            INSERT INTO cards (title, description, status, created_at, due_date, owner, assignee, expedite)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, status.name());
            ps.setString(4, nowLocalTimestamp());   
            ps.setString(5, dueDate);
            ps.setString(6, owner);
            ps.setString(7, assignee);
            ps.setInt(8, expedite ? 1 : 0);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public void deleteCard(int id) throws Exception {
        String sql = "DELETE FROM cards WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void updateStatus(int id, KanbanStatus newStatus) throws Exception {
        String sql = "UPDATE cards SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void updateExpedite(int id, boolean expedite) throws Exception {
        String sql = "UPDATE cards SET expedite = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expedite ? 1 : 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
    
    public void updateCard(int id, String title, String description, KanbanStatus status,
                       String dueDate, String owner, String assignee, boolean expedite) throws Exception {

        String sql = """
            UPDATE cards SET title = ?, description = ?, status = ?, created_at = ?, due_date = ?, owner = ?, assignee = ?, expedite = ?
            WHERE id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, status.name());
            ps.setString(4, nowLocalTimestamp());
            ps.setString(5, dueDate);
            ps.setString(6, owner);
            ps.setString(7, assignee);
            ps.setInt(8, expedite ? 1 : 0);
            ps.setInt(9, id);

            ps.executeUpdate();
        }
    }

    
    private static final DateTimeFormatter SQLITE_TS =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String nowLocalTimestamp() {
        return LocalDateTime
                .now(ZoneId.systemDefault())
                .format(SQLITE_TS);
    }
}
