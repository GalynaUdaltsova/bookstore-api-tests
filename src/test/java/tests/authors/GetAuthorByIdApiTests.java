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
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for GET /api/v1/Authors/{id} endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Authors API")
@Story("Get Author by ID")
@Tag(TestTags.AUTHORS)
public class GetAuthorByIdApiTests extends BaseTest {

    private static final AuthorsApi authorsApi = AuthorsApi.getInstance();

    @Test
    @DisplayName("Get author by ID - Success")
    @Description("Verify that a specific author can be retrieved by its ID")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testGetAuthorById_Success() {
        Author newAuthor = AuthorBuilder.buildRandomAuthor();
        Response createResponse = authorsApi.createAuthor(newAuthor);
        Author createdAuthor = createResponse.as(Author.class);

        Response response = authorsApi.getAuthorById(createdAuthor.getId());

        assertStatusCode(response, HttpStatus.SC_OK);

        Author expectedAuthor = response.as(Author.class);
        assertNotNull(expectedAuthor, "Author should not be null");
        assertNotNull(expectedAuthor.getId(), "Author ID should not be null");
        assertNotNull(expectedAuthor.getIdBook(), "Author book ID should not be null");
    }

    @Test
    @DisplayName("Get author by ID - Invalid ID")
    @Description("Verify API returns an error when author ID is invalid")
    @Tag(TestTags.NEGATIVE)
    @Tag(TestTags.REGRESSION)
    public void testGetAuthorById_InvalidId() {
        Integer invalidAuthorId = -RandomUtils.randomInt();

        Response response = authorsApi.getAuthorById(invalidAuthorId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Get author by ID - Non-existent ID")
    @Description("Verify behavior when requesting an author that doesn't exist")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testGetAuthorById_NonExistentId() {
        Integer nonExistentAuthorId = Integer.MAX_VALUE;

        Response response = authorsApi.getAuthorById(nonExistentAuthorId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }
}
