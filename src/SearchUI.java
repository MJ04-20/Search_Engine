import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.List;

public class SearchUI {
    private SearchEngine searchEngine;
    private DatabaseManager db;

    public SearchUI() {
        db = new DatabaseManager();
        searchEngine = new SearchEngine();
        initUI();
    }

    private void initUI() {
        JFrame frame = new JFrame("Mini Search Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JTextField queryField = new JTextField();
        JButton searchButton = new JButton("Search");

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> resultsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(resultsList);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(queryField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            listModel.clear();
            String query = queryField.getText().trim();
            if (!query.isEmpty()) {
                List<PageData> pages = db.getAllPages();
                List<PageData> results = searchEngine.search(query, pages);
                for (PageData result : results) {
                    listModel.addElement(result.title + " - " + result.url);
                }
            }
        });

        resultsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = resultsList.locationToIndex(evt.getPoint());
                    String item = listModel.get(index);
                    String url = item.substring(item.indexOf("http")); // get the URL part
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}
