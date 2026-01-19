package dao;

import db.DBConnection;
import model.Card;
import model.KanbanStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardDao {

    public List<Card> getAllCards() throws Exception {
        String sql = """
            SELECT id, title, description, status, created_at, due_date, owner, assignee, expedite
            FROM cards
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
}
