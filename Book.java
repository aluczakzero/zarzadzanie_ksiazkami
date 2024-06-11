
// klasa przechowująca parametry książek
public class Book {

    // pola klasy Book
    private String title;
    private String author;
    private int year;
    private String genre;

    // przypisanie parametrów konstruktora
    public Book(String title, String author, int year, String genre) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
    }

    // publiczna metoda get do pozyskania title
    public String getTitle() {
        return title;
    }

    // publiczna metoda get do pozyskania author
    public String getAuthor() {
        return author;
    }

    // publiczna metoda get do pozyskania year
    public int getYear() {
        return year;
    }

    // publiczna metoda get do pozyskania year
    public String getGenre() {
        return genre;
    }

    // nadpisanie metody toString aby opisać tekstem wszystkie parametry klasy
    @Override
    public String toString() {
        return title + " by " + author + ", " + year + " (" + genre + ")";
    }
}