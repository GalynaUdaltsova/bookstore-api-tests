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
import util.RandomUtils;

import static assertions.ApiAssertions.assertStatusCode;

/**
 * Test class for DELETE /api/v1/Authors/{id} endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Authors API")
@Story("Delete Author")
@Tag(TestTags.AUTHORS)
public class DeleteAuthorsApiTests extends BaseTest {

    private static final AuthorsApi authorsApi = AuthorsApi.getInstance();

    @Test
    @DisplayName("Delete author with valid ID - Happy Path")
    @Description("Verify that an author can be deleted successfully with valid ID")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testDeleteAuthor_ExistingId_DeletesAuthorSuccessfully() {
        Author newAuthor = AuthorBuilder.buildRandomAuthor();
        Response createResponse = authorsApi.createAuthor(newAuthor);
        Author createdAuthor = createResponse.as(Author.class);
        Integer authorId = createdAuthor.getId();

        Response responseDelete = authorsApi.deleteAuthor(authorId);

        assertStatusCode(responseDelete, HttpStatus.SC_OK);

        Response responseGet = authorsApi.getAuthorById(authorId);
        assertStatusCode(responseGet, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Delete author with invalid ID - Edge Case")
    @Description("Verify that deleting an author with invalid ID returns 404")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testDeleteAuthor_InvalidId_ReturnsNotFound() {
        Integer invalidAuthorId = -RandomUtils.randomInt();

        Response response = authorsApi.deleteAuthor(invalidAuthorId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Delete author with non-existing ID - Edge Case")
    @Description("Verify that deleting a non-existent author returns 404")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testDeleteAuthor_NonExistingId_ReturnsNotFound() {
        Integer nonExistingId = Integer.MAX_VALUE;

        Response response = authorsApi.deleteAuthor(nonExistingId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }
}
