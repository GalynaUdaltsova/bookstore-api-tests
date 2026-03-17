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

import java.util.function.Consumer;

import static assertions.ApiAssertions.assertAuthorEquals;
import static assertions.ApiAssertions.assertStatusCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for POST /api/v1/Authors endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Authors API")
@Story("Create Author")
@Tag(TestTags.AUTHORS)
public class CreateAuthorsApiTests extends BaseTest {

    private static final AuthorsApi authorsApi = AuthorsApi.getInstance();

    @Test
    @DisplayName("Create author - Success")
    @Description("Verify that a new author can be created successfully")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testCreateAuthor_Success() {
        Author newAuthor = AuthorBuilder.buildRandomAuthor();

        Response response = authorsApi.createAuthor(newAuthor);

        assertStatusCode(response, HttpStatus.SC_OK);
        assertAuthorEquals(newAuthor, response);
    }

    @Test
    @DisplayName("Create author - Empty payload")
    @Description("Verify API behavior when creating an author with empty payload")
    @Tag(TestTags.NEGATIVE)
    @Tag(TestTags.REGRESSION)
    public void testCreateAuthor_EmptyPayload_BehaviorValidation() {
        Author emptyAuthor = Author.builder().build();

        Response response = authorsApi.createAuthor(emptyAuthor);

        assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        assertNotNull(response.jsonPath().getString("errors"), "Error message should be present");
    }

    @Test
    @DisplayName("Create author - Invalid book ID")
    @Description("Verify API returns an error when creating author with non-existing book ID")
    @Tag(TestTags.NEGATIVE)
    @Tag(TestTags.REGRESSION)
    public void testCreateAuthor_InvalidBookId() {
        Consumer<Author> invalidBookIdAuthor = (author) -> author.setIdBook(Integer.MAX_VALUE);
        Author invalidAuthor = AuthorBuilder.buildRandomAuthor();
        invalidBookIdAuthor.accept(invalidAuthor);

        Response response = authorsApi.createAuthor(invalidAuthor);

        assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        assertNotNull(response.jsonPath().getString("errors"), "Error message should be present");
    }

    @Test
    @DisplayName("Create author - Maximum name length")
    @Description("Verify behavior when creating author with maximum name length")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testCreateAuthor_MaximumNameLength_BehaviorValidation() {
        String maxFirstName = "FirstName".repeat(20);
        String maxLastName = "LastName".repeat(20);

        Consumer<Author> maxNamesAuthor = (author) -> {
            author.setFirstName(maxFirstName);
            author.setLastName(maxLastName);
        };

        Author expectedAuthor = AuthorBuilder.specificFieldAuthor(maxNamesAuthor);

        Response response = authorsApi.createAuthor(expectedAuthor);

        assertStatusCode(response, HttpStatus.SC_OK);
        assertAuthorEquals(expectedAuthor, response);
    }
}
