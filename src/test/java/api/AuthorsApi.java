package api;

import client.AuthorsClient;
import data.DataCleaner;
import io.restassured.response.Response;
import models.Author;

/**
 * Test-level API abstraction for Authors.
 * Automatically registers created authors for cleanup in DataCleaner.
 */
public class AuthorsApi {

    private static final AuthorsApi INSTANCE = new AuthorsApi();

    private final AuthorsClient authorsClient;
    private final DataCleaner dataCleaner;

    private AuthorsApi() {
        this.authorsClient = AuthorsClient.getInstance();
        this.dataCleaner = DataCleaner.getInstance();
    }

    public static AuthorsApi getInstance() {
        return INSTANCE;
    }

    /**
     * Create an author and register it in DataCleaner.
     *
     * @param author Author object to create
     * @return Response from the API
     */
    public Response createAuthor(Author author) {
        Response response = authorsClient.createAuthor(author);

        try {
            Author createdAuthor = response.as(Author.class);
            // Register cleanup action for this author
            dataCleaner.registerCleanupAction(() ->
                    authorsClient.deleteAuthor(createdAuthor.getId())
            );
            return response;
        } catch (Exception e) {
            //will be passed to test for proper error message displaying
            return response;
        }
    }

    /**
     * Get all authors.
     *
     * @return Response containing all authors
     */
    public Response getAllAuthors() {
        return authorsClient.getAllAuthors();
    }

    /**
     * Get authors by book ID.
     *
     * @param bookId ID of the book to get authors for
     * @return Response containing authors for the book
     */
    public Response getAuthorsByBookId(Integer bookId) {
        return authorsClient.getAuthorsByBookId(bookId);
    }

    /**
     * Get an author by ID.
     *
     * @param authorId ID of the author to retrieve
     * @return Response containing the author
     */
    public Response getAuthorById(Integer authorId) {
        return authorsClient.getAuthorById(authorId);
    }

    /**
     * Update an author.
     *
     * @param authorId ID of the author to update
     * @param author Updated author data
     * @return Response from the API
     */
    public Response updateAuthor(Integer authorId, Author author) {
        return authorsClient.updateAuthor(authorId, author);
    }

    /**
     * Delete an author.
     *
     * @param authorId ID of the author to delete
     * @return Response from the API
     */
    public Response deleteAuthor(Integer authorId) {
        return authorsClient.deleteAuthor(authorId);
    }
}
