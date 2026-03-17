package tests.authors;

import api.AuthorsApi;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tags.TestTags;
import tests.BaseTest;

import java.util.List;
import java.util.Map;

import static assertions.ApiAssertions.assertResponseNotEmpty;
import static assertions.ApiAssertions.assertStatusCode;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for GET /api/v1/Authors/books/{idBook} endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Authors API")
@Story("Get Authors by Book ID")
@Tag(TestTags.AUTHORS)
public class GetAuthorsByBookIdApiTests extends BaseTest {

    private static final AuthorsApi authorsApi = AuthorsApi.getInstance();

    @Test
    @DisplayName("Get authors by book ID - Success")
    @Description("Verify that authors can be retrieved for an existing book")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testGetAuthorsByBookId_Success() {
        Integer bookId = 1;

        Response response = authorsApi.getAuthorsByBookId(bookId);

        assertStatusCode(response, HttpStatus.SC_OK);

        List<Map<String, Object>> authors = response.jsonPath().getList("$");
        assertResponseNotEmpty(authors);

        authors.forEach(authorObj -> {
            assert authorObj.get("id") != null : "Author ID should not be null";
            assert authorObj.get("idBook") != null : "Author book ID should not be null";
        });
    }

    @Test
    @DisplayName("Get authors by book ID - Non-existent book ID")
    @Description("Verify API returns 404 when book does not exist")
    @Tag(TestTags.NEGATIVE)
    @Tag(TestTags.REGRESSION)
    public void testGetAuthorsByBookId_NonExistentBookId() {
        Integer nonExistentBookId = Integer.MAX_VALUE;

        Response response = authorsApi.getAuthorsByBookId(nonExistentBookId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Get authors by book ID - Book without authors")
    @Description("Verify API returns empty list when book exists but has no authors")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testGetAuthorsByBookId_BookWithoutAuthors() {
        Integer bookWithoutAuthors = 999;

        Response response = authorsApi.getAuthorsByBookId(bookWithoutAuthors);

        assertStatusCode(response, HttpStatus.SC_OK);

        List<Map<String, Object>> authors = response.jsonPath().getList("$");
        assertTrue(authors.isEmpty(), "Authors list should be empty for book without authors");
    }
}
