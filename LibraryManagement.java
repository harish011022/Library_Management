import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Borrowable {
    void borrowBook(String userId);
    void returnBook(String userId);
}

class Book implements Borrowable {
    private String title;
    private String author;
    private String category;
    private boolean isBorrowed;
    private String borrowedBy;

    public Book(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.isBorrowed = false;
        this.borrowedBy = null;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isBorrowed() { return isBorrowed; }

    public void borrowBook(String userId) {
        if (!isBorrowed) {
            isBorrowed = true;
            borrowedBy = userId;
            System.out.println(userId + " borrowed " + title);
        } else {
            System.out.println("Sorry, this book is already borrowed by " + borrowedBy);
        }
    }

    public void returnBook(String userId) {
        if (isBorrowed && borrowedBy.equals(userId)) {
            isBorrowed = false;
            borrowedBy = null;
            System.out.println(userId + " returned " + title);
        } else {
            System.out.println("You cannot return a book you didn't borrow.");
        }
    }

    public String toString() {
        return title + "," + author + "," + category + "," + isBorrowed + "," + (borrowedBy == null ? "None" : borrowedBy);
    }
}

class Library {
    private static Library instance;
    private List<Book> books;
    private final String FILE_NAME = "C:\\Nexturn\\Library\\books.txt";

    private Library() {
        books = new ArrayList<>();
        loadBooksFromFile();
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    private void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing book records found in the library.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 5) {
                    Book book = new Book(details[0].trim(), details[1].trim(), details[2].trim());
                    if (Boolean.parseBoolean(details[3].trim())) {
                        book.borrowBook(details[4].trim());
                    }
                    books.add(book);
                }
            }
            System.out.println("Books loaded successfully from file.");
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    public void saveBooksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < books.size(); i++) {
                writer.println(books.get(i).toString());
            }
            System.out.println("Books saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
        System.out.println("Book added successfully in the library!");
    }

    public void removeBook(String title) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equalsIgnoreCase(title)) {
                books.remove(i);
                break;
            }
        }
        saveBooksToFile();
        System.out.println("Book " + title + " removed.");
    }

    public void borrowBook(String title, String userId) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equalsIgnoreCase(title)) {
                books.get(i).borrowBook(userId);
                saveBooksToFile();
                return;
            }
        }
        System.out.println("Book is not found in the library.");
    }

    public void returnBook(String title, String userId) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equalsIgnoreCase(title)) {
                books.get(i).returnBook(userId);
                saveBooksToFile();
                return;
            }
        }
        System.out.println("Book is not found in the libarry.");
    }

    public void listBooks() {
        if (books.isEmpty()) {
            System.out.println("No books are available.");
            return;
        }
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println(book.getTitle() + " by " + book.getAuthor() + " [" + book.getCategory() + "] " +
                    (book.isBorrowed() ? "(Borrowed by " + book.toString().split(",")[4] + ")" : "(Available)"));
        }
    }
}

public class LibraryManagement {
    public static void main(String[] args) {
        Library library = Library.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add the Book you want");
            System.out.println("2. Remove a Book from the library");
            System.out.println("3. List the Books that are available in library");
            System.out.println("4. Borrow a Book from the library");
            System.out.println("5. Return a Book to the library");
            System.out.println("6. Exit");
            System.out.print("Enter the choice you want: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter the Title of the book: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter the Author of the book: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter the Category of the book: ");
                    String category = scanner.nextLine();
                    library.addBook(new Book(title, author, category));
                    break;
                case 2:
                    System.out.print("Enter Book Title to Remove: ");
                    String removeTitle = scanner.nextLine();
                    library.removeBook(removeTitle);
                    break;
                case 3:
                    library.listBooks();
                    break;
                case 4:
                    System.out.print("Enter Book Title to Borrow: ");
                    String borrowTitle = scanner.nextLine();
                    System.out.print("Enter Your User ID: ");
                    String userId = scanner.nextLine();
                    library.borrowBook(borrowTitle, userId);
                    break;
                case 5:
                    System.out.print("Enter Book Title to Return: ");
                    String returnTitle = scanner.nextLine();
                    System.out.print("Enter Your User ID: ");
                    String returnUserId = scanner.nextLine();
                    library.returnBook(returnTitle, returnUserId);
                    break;
                case 6:
                    System.out.println("Exiting.......");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again with the valid option that is there");
            }
        }
    }
}
