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

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static assertions.ApiAssertions.assertBookEquals;
import static assertions.ApiAssertions.assertStatusCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Create Books API test suite.
 * Tests POST /books endpoint with positive, negative, and edge cases.
 */
@Feature("Books API")
@Story("Book Creation")
@Tag(TestTags.BOOKS)
public class CreateBooksApiTests extends BaseTest {

    private static final BooksApi booksApi = BooksApi.getInstance();

    @Test
    @DisplayName("Create book - Success")
    @Description("Verify that a new book can be created successfully")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testCreateBook_Success() {
        // Arrange: Create test data
        Book newBook = BookBuilder.randomBook();

        // Act: Call API
        Response response = booksApi.createBook(newBook);

        // Assert: Verify response
        assertStatusCode(response, HttpStatus.SC_OK);
        assertBookEquals(newBook, response);
    }

    @Test
    @DisplayName("Create book - invalid publishDate")
    @Description("Verify API returns an error when publishDate has an invalid format")
    @Tag(TestTags.NEGATIVE)
    @Tag(TestTags.REGRESSION)
    public void testCreateBook_InvalidPublishDate() {
        // Act
        Consumer<Book> invalidDateBook = (book) -> book.setPublishDate(LocalDateTime.MAX);

        Book invalidBook = BookBuilder.specificFieldBook(invalidDateBook);
        Response response = booksApi.createBook(invalidBook);

        // Assert
        assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        assertNotNull(response.jsonPath().getString("errors"), "Error message should be present");
    }

    @Test
    @DisplayName("Create book - empty payload")
    @Description("Verify API behavior when creating a book with empty payload")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testCreateBook_EmptyPayload_BehaviorValidation() {
        // Arrange
        Book emptyBook = Book.builder().build();

        // Act
        Response response = booksApi.createBook(emptyBook);

        // Assert
        assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        assertEquals("One or more validation errors occurred.",
                response.jsonPath().getString("title"));
    }
}
