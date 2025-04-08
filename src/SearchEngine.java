import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {

    // Count the number of occurrences of a keyword in the page content
    private int countKeyword(String keyword, String content) {
        int count = 0;
        String[] words = content.toLowerCase().split("\\W+");

        for (String word : words) {
            if (word.equals(keyword.toLowerCase())) {
                count++;
            }
        }

        return count;
    }

    public List<PageData> search(String query, List<PageData> pages) {
        String[] keywords = query.toLowerCase().split("\\s+");

        // Create a map of PageData -> score
        Map<PageData, Integer> scoreMap = new HashMap<>();

        for (PageData page : pages) {
            int score = 0;
            for (String keyword : keywords) {
                score += countKeyword(keyword, page.content);
            }

            if (score > 0) {
                scoreMap.put(page, score);
            }
        }

        // Sort by score in descending order
        return scoreMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
