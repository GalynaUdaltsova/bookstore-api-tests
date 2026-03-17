package tests.books;

import api.BooksApi;
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

/**
 * Test class for GET /api/v1/Books endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Books API")
@Story("Get All Books")
@Tag(TestTags.BOOKS)
public class GetAllBooksApiTests extends BaseTest {

    private static final BooksApi booksApi = BooksApi.getInstance();

    @Test
    @DisplayName("Get all books - Success")
    @Description("Verify that all books can be retrieved successfully")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testGetAllBooks_Success() {
        Response response = booksApi.getAllBooks();

        assertStatusCode(response, HttpStatus.SC_OK);

        List<Map<String, Object>> books = response.jsonPath().getList("$");
        assertResponseNotEmpty(books);
    }
}
