package tests.books;

import api.BooksApi;
import data.BookBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.Book;
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
 * Test class for GET /api/v1/Books/{id} endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Books API")
@Story("Get Book by ID")
@Tag(TestTags.BOOKS)
public class GetBookByIdApiTests extends BaseTest {

    private static final BooksApi booksApi = BooksApi.getInstance();

    @Test
    @DisplayName("Get book by ID - Success")
    @Description("Verify that a specific book can be retrieved by its ID")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testGetBookById_Success() {
        Book newBook = BookBuilder.randomBook();
        booksApi.createBook(newBook);

        Response response = booksApi.getBookById(newBook.getId());

        assertStatusCode(response, HttpStatus.SC_OK);

        Book expectedBook = response.as(Book.class);
        assertNotNull(expectedBook, "Book should not be null");
        assertNotNull(expectedBook.getId(), "Book id should not be null");
    }

    @Test
    @DisplayName("Get book by ID - Invalid ID")
    @Description("Verify API returns an error when book ID is invalid")
    @Tag(TestTags.NEGATIVE)
    @Tag(TestTags.REGRESSION)
    public void testGetBookById_InvalidId() {
        Integer invalidBookId = -RandomUtils.randomInt();

        Response response = booksApi.getBookById(invalidBookId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Get book by ID - Non-existent ID")
    @Description("Verify behavior when requesting a book that doesn't exist")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testGetBookById_NonExistentId() {
        Integer nonExistentBookId = Integer.MAX_VALUE;

        Response response = booksApi.getBookById(nonExistentBookId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }
}
