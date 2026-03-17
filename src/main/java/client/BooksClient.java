package client;

import io.restassured.response.Response;
import models.Book;

import java.util.Map;

/**
 * Client for Books API operations.
 * Encapsulates all Books API calls with retry logic and logging.
 * Uses Fake REST API: https://fakerestapi.azurewebsites.net/api/v1
 */
public class BooksClient {
    private static final BooksClient INSTANCE = new BooksClient();

    private static final String BOOKS_ENDPOINT = "/Books";
    private final ApiClient apiClient;

    private BooksClient() {
        this.apiClient = ApiClient.getInstance();
    }

    public static BooksClient getInstance() {
        return INSTANCE;
    }

    /**
     * Create a new book
     *
     * @param book Book object to create
     * @return Response containing created book
     */
    public Response createBook(Book book) {
        return apiClient.post(BOOKS_ENDPOINT, book, null, Map.of());
    }

    /**
     * Get all books
     *
     * @return Response containing all books
     */
    public Response getAllBooks() {
        return apiClient.get(BOOKS_ENDPOINT, null, Map.of());
    }

    /**
     * Get a book by ID
     *
     * @param bookId ID of the book to retrieve
     * @return Response containing the book
     */
    public Response getBookById(Integer bookId) {
        return apiClient.get(BOOKS_ENDPOINT + "/{id}", Map.of("id", bookId), Map.of());
    }

    /**
     * Update a book by ID
     *
     * @param bookId ID of the book to update
     * @param book   Updated book data
     * @return Response from the API
     */
    public Response updateBook(Integer bookId, Book book) {
        return apiClient.put(BOOKS_ENDPOINT + "/{id}", book, Map.of("id", bookId), Map.of());
    }

    /**
     * Delete a book by ID
     *
     * @param bookId ID of the book to delete
     */
    public Response deleteBook(Integer bookId) {
        return apiClient.delete(BOOKS_ENDPOINT + "/{id}", Map.of("id", bookId), Map.of());
    }
}
