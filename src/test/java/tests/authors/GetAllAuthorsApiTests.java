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
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for GET /api/v1/Authors endpoint.
 * Covers happy path scenarios.
 */
@Feature("Authors API")
@Story("Get All Authors")
@Tag(TestTags.AUTHORS)
public class GetAllAuthorsApiTests extends BaseTest {

    private static final AuthorsApi authorsApi = AuthorsApi.getInstance();

    @Test
    @DisplayName("Get all authors - Success")
    @Description("Verify that all authors can be retrieved successfully")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testGetAllAuthors_Success() {
        Response response = authorsApi.getAllAuthors();

        assertStatusCode(response, HttpStatus.SC_OK);

        List<Map<String, Object>> authors = response.jsonPath().getList("$");
        assertResponseNotEmpty(authors);

        authors.forEach(authorObj -> {
            assertNotNull(authorObj.get("id"), "Author ID should not be null");
            assertNotNull(authorObj.get("firstName"), "Author ID should not be null");
            assertNotNull(authorObj.get("lastName"), "Author ID should not be null");
        });
    }
}
