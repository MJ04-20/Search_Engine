import javax.swing.SwingUtilities;

public class MiniSearchApp {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();

        // Uncomment if you want to crawl again:
        // WikipediaCrawler crawler = new WikipediaCrawler(db);
        // crawler.crawl("https://en.wikipedia.org/wiki/Main_Page");

        SwingUtilities.invokeLater(() -> new SearchUI());
    }
}
