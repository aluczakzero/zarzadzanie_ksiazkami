import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;


// klasa zarządzająca książkami oraz tworząca GUI
public class BookManager {
    private JFrame frame;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private DefaultListModel<Book> bookListModel;
    private JList<Book> bookList;
    private List<Book> books;

    public BookManager() {
        books = loadBooksFromFile("books.txt");

        frame = new JFrame("Book Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        searchField = new JTextField();
        panel.add(new JLabel("Search:"));
        panel.add(searchField);

        sortComboBox = new JComboBox<>(new String[]{"Title", "Author", "Year", "Genre"});
        panel.add(new JLabel("Sort by:"));
        panel.add(sortComboBox);

        JButton addButton = new JButton("Add Book");
        panel.add(addButton);

        JButton deleteButton = new JButton("Delete Book");
        panel.add(deleteButton);

        frame.add(panel, BorderLayout.NORTH);

        bookListModel = new DefaultListModel<>();
        books.forEach(bookListModel::addElement);
        bookList = new JList<>(bookListModel);
        frame.add(new JScrollPane(bookList), BorderLayout.CENTER);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText();
                Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
                List<Book> filteredBooks = books.stream()
                        .filter(book -> pattern.matcher(book.toString()).find())
                        .collect(Collectors.toList());
                updateBookList(filteredBooks);
            }
        });

        sortComboBox.addActionListener(e -> {
            String criteria = (String) sortComboBox.getSelectedItem();
            if (criteria != null) {
                Comparator<Book> comparator = getComparator(criteria);
                books.sort(comparator);
                updateBookList(books);
            }
        });

        addButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(frame, "Enter book title:");
            String author = JOptionPane.showInputDialog(frame, "Enter book author:");
            int year = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter book year:"));
            String genre = JOptionPane.showInputDialog(frame, "Enter book genre:");

            Book newBook = new Book(title, author, year, genre);
            books.add(newBook);
            updateBookList(books);
            saveBooksToFile("books.txt");
        });

        deleteButton.addActionListener(e -> {
            Book selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                books.remove(selectedBook);
                updateBookList(books);
                saveBooksToFile("books.txt");
            }
        });

        frame.setVisible(true);
    }

    // Metoda do ładowania książek z pliku
    private List<Book> loadBooksFromFile(String filename) {
        List<Book> bookList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String title = parts[0];
                    String author = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    String genre = parts[3];
                    bookList.add(new Book(title, author, year, genre));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    // Metoda do zapisywania książek do pliku
    private void saveBooksToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                bw.write(book.getTitle() + ";" + book.getAuthor() + ";" + book.getYear() + ";" + book.getGenre());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Comparator<Book> getComparator(String criteria) {
        switch (criteria) {
            case "Title":
                return Comparator.comparing(Book::getTitle);
            case "Author":
                return Comparator.comparing(Book::getAuthor);
            case "Year":
                return Comparator.comparingInt(Book::getYear);
            case "Genre":
                return Comparator.comparing(Book::getGenre);
            default:
                throw new IllegalArgumentException("Unknown sorting criteria: " + criteria);
        }
    }

    private void updateBookList(List<Book> books) {
        bookListModel.clear();
        books.forEach(bookListModel::addElement);
    }

    public static void main(String[] args) {
        new BookManager();
    }
}