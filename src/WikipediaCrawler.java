import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
public class WikipediaCrawler {
    private static final int MAX_PAGES = 500;
    private static final String DOMAIN = "wikipedia.org";
    private HashSet<String> visitedLinks = new HashSet<>();
    private Queue<String> queue = new LinkedList<>();
    private DatabaseManager db;

    public WikipediaCrawler(DatabaseManager db) {
        this.db = db;
    }

    public void crawl(String startUrl) {
        queue.add(startUrl);

        while (!queue.isEmpty() && visitedLinks.size() < MAX_PAGES) {
            String url = queue.poll();
            if (url == null || visitedLinks.contains(url) || !url.contains(DOMAIN)) continue;

            try {
                Document doc = Jsoup.connect(url).get();
                visitedLinks.add(url);

                String title = doc.title();
                String text = doc.body().text();

                PageData page = new PageData(title, url, text);
                db.savePage(page);
                System.out.println("Indexed: " + title + " -> " + url);

                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String nextUrl = link.absUrl("href");
                    if (!visitedLinks.contains(nextUrl) && nextUrl.contains(DOMAIN)) {
                        queue.add(nextUrl);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }
    }
}
