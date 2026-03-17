package api;

import client.BooksClient;
import data.DataCleaner;
import io.restassured.response.Response;
import models.Book;

/**
 * Test-level API abstraction for Books.
 * Automatically registers created books for cleanup in TestDataCleaner.
 */
public class BooksApi {

    private static final BooksApi INSTANCE = new BooksApi();

    private final BooksClient booksClient;
    private final DataCleaner dataCleaner;

    private BooksApi() {
        this.booksClient = BooksClient.getInstance();
        this.dataCleaner = DataCleaner.getInstance();
    }

    public static BooksApi getInstance() {
        return INSTANCE;
    }

    /**
     * Create a book and register it in DataCleaner.
     *
     * @param book Book to create
     * @return Response from the API
     */
    public Response createBook(Book book) {
        Response response = booksClient.createBook(book);
        try {
            Book createdBook = response.as(Book.class);
            // Register cleanup action for this book
            dataCleaner.registerCleanupAction(() ->
                    booksClient.deleteBook(createdBook.getId())
            );
            return response;
        } catch (Exception e) {
            return response;
        }
    }

    /**
     * Get all books.
     *
     * @return Response containing all books
     */
    public Response getAllBooks() {
        return booksClient.getAllBooks();
    }

    /**
     * Get a book by ID.
     *
     * @param bookId ID of the book to retrieve
     * @return Response containing the book
     */
    public Response getBookById(Integer bookId) {
        return booksClient.getBookById(bookId);
    }

    /**
     * Update a book.
     *
     * @param bookId ID of the book to update
     * @param book Updated book data
     * @return Response from the API
     */
    public Response updateBook(Integer bookId, Book book) {
        return booksClient.updateBook(bookId, book);
    }

    /**
     * Delete a book.
     *
     * @param bookId ID of the book to delete
     */
    public Response deleteBook(Integer bookId) {
        return booksClient.deleteBook(bookId);
    }
}
