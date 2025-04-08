import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:searchengine.db";

    public DatabaseManager() {
        createTable();
    }

    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS pages (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT, url TEXT UNIQUE, content TEXT)";
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePage(PageData page) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR IGNORE INTO pages (title, url, content) VALUES (?, ?, ?)")) {

            stmt.setString(1, page.title);
            stmt.setString(2, page.url);
            stmt.setString(3, page.content);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PageData> getAllPages() {
        List<PageData> pages = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pages")) {

            while (rs.next()) {
                pages.add(new PageData(
                        rs.getString("title"),
                        rs.getString("url"),
                        rs.getString("content")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pages;
    }
}
