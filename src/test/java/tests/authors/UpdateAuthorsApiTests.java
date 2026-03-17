package tests.authors;

import api.AuthorsApi;
import data.AuthorBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.Author;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tags.TestTags;
import tests.BaseTest;

import java.util.UUID;
import java.util.function.Consumer;

import static assertions.ApiAssertions.assertAuthorEquals;
import static assertions.ApiAssertions.assertStatusCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for PUT /api/v1/Authors/{id} endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Authors API")
@Story("Update Author")
@Tag(TestTags.AUTHORS)
public class UpdateAuthorsApiTests extends BaseTest {

    private static final AuthorsApi authorsApi = AuthorsApi.getInstance();

    @Test
    @DisplayName("Update author - Success")
    @Description("Verify that an author can be updated successfully with valid data")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testUpdateAuthor_Success() {
        Author newAuthor = AuthorBuilder.buildRandomAuthor();
        Response createResponse = authorsApi.createAuthor(newAuthor);
        Author createdAuthor = createResponse.as(Author.class);

        String updatedFirstName = "Updated " + UUID.randomUUID();
        String updatedLastName = "Updated " + UUID.randomUUID();

        Consumer<Author> updateAuthorData = (author) -> {
            author.setFirstName(updatedFirstName);
            author.setLastName(updatedLastName);
        };

        Author updatedAuthor = AuthorBuilder.buildRandomAuthor();
        updateAuthorData.accept(updatedAuthor);

        Response response = authorsApi.updateAuthor(createdAuthor.getId(), updatedAuthor);

        assertStatusCode(response, HttpStatus.SC_OK);
        assertAuthorEquals(updatedAuthor, response);
    }

    @Test
    @DisplayName("Update author - Invalid book ID")
    @Description("Verify API returns an error when updating author with non-existing book ID")
    @Tag(TestTags.NEGATIVE)
    @Tag(TestTags.REGRESSION)
    public void testUpdateAuthor_InvalidBookId() {
        Author newAuthor = AuthorBuilder.buildRandomAuthor();
        Response createResponse = authorsApi.createAuthor(newAuthor);
        Author createdAuthor = createResponse.as(Author.class);

        Consumer<Author> invalidBookIdAuthor = (author) -> author.setIdBook(Integer.MAX_VALUE);
        Author invalidAuthor = AuthorBuilder.buildRandomAuthor();
        invalidBookIdAuthor.accept(invalidAuthor);

        Response response = authorsApi.updateAuthor(createdAuthor.getId(), invalidAuthor);

        assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        assertNotNull(response.jsonPath().getString("errors"), "Error message should be present");
    }

    @Test
    @DisplayName("Update author - Non-existent ID")
    @Description("Verify behavior when updating an author that doesn't exist")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testUpdateAuthor_NonExistentId() {
        Integer nonExistentAuthorId = Integer.MAX_VALUE;
        Author updateData = AuthorBuilder.buildRandomAuthor();

        Response response = authorsApi.updateAuthor(nonExistentAuthorId, updateData);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }
}
