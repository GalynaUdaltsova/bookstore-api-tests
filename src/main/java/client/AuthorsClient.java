package client;

import io.restassured.response.Response;
import models.Author;

import java.util.Map;

/**
 * Client for Authors API operations.
 * Encapsulates all Authors API calls with retry logic and logging.
 * Uses Fake REST API: https://fakerestapi.azurewebsites.net/api/v1
 */
public class AuthorsClient {
    private static final AuthorsClient INSTANCE = new AuthorsClient();

    private static final String AUTHORS_ENDPOINT = "/Authors";
    private final ApiClient apiClient;

    private AuthorsClient() {
        this.apiClient = ApiClient.getInstance();
    }

    public static AuthorsClient getInstance() {
        return INSTANCE;
    }

    /**
     * Create a new author
     *
     * @param author Author object to create
     * @return Response containing created author
     */
    public Response createAuthor(Author author) {
        return apiClient.post(AUTHORS_ENDPOINT, author, null, Map.of());
    }

    /**
     * Get all authors
     *
     * @return Response containing all authors
     */
    public Response getAllAuthors() {
        return apiClient.get(AUTHORS_ENDPOINT, null, Map.of());
    }

    /**
     * Get authors by book ID
     *
     * @param bookId ID of the book to get authors for
     * @return Response containing authors for the book
     */
    public Response getAuthorsByBookId(Integer bookId) {
        return apiClient.get(AUTHORS_ENDPOINT + "/books/{idBook}", Map.of("idBook", bookId), Map.of());
    }

    /**
     * Get an author by ID
     *
     * @param authorId ID of the author to retrieve
     * @return Response containing the author
     */
    public Response getAuthorById(Integer authorId) {
        return apiClient.get(AUTHORS_ENDPOINT + "/{id}", Map.of("id", authorId), Map.of());
    }

    /**
     * Update an author by ID
     *
     * @param authorId ID of the author to update
     * @param author Updated author data
     * @return Response from the API
     */
    public Response updateAuthor(Integer authorId, Author author) {
        return apiClient.put(AUTHORS_ENDPOINT + "/{id}", author, Map.of("id", authorId), Map.of());
    }

    /**
     * Delete an author by ID
     *
     * @param authorId ID of the author to delete
     * @return Response from the API
     */
    public Response deleteAuthor(Integer authorId) {
        return apiClient.delete(AUTHORS_ENDPOINT + "/{id}", Map.of("id", authorId), Map.of());
    }
}
