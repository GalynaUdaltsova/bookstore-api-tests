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

import java.util.UUID;
import java.util.function.Consumer;

import static assertions.ApiAssertions.assertBookEquals;
import static assertions.ApiAssertions.assertStatusCode;

/**
 * Test class for PUT /api/v1/Books/{id} endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Books API")
@Story("Update Book")
@Tag(TestTags.BOOKS)
public class UpdateBooksApiTests extends BaseTest {

    private static final BooksApi booksApi = BooksApi.getInstance();

    @Test
    @DisplayName("Update book - Success")
    @Description("Verify that a book can be updated successfully with valid data")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testUpdateBook_Success() {
        String title = "Updated " + UUID.randomUUID();
        Consumer<Book> updatedTitleBook = (book) -> book.setTitle(title);
        Book updatedBook = BookBuilder.specificFieldBook(updatedTitleBook);

        Response response = booksApi.updateBook(updatedBook.getId(), updatedBook);

        assertStatusCode(response, HttpStatus.SC_OK);
        assertBookEquals(updatedBook, response);
    }
}
