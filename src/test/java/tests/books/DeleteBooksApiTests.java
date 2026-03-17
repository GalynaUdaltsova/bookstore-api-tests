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

import static assertions.ApiAssertions.assertStatusCode;

/**
 * Test class for DELETE /api/v1/Books/{id} endpoint.
 * Covers happy path, negative path, and edge case scenarios.
 */
@Feature("Books API")
@Story("Delete Book")
@Tag(TestTags.BOOKS)
public class DeleteBooksApiTests extends BaseTest {

    private static final BooksApi booksApi = BooksApi.getInstance();

    @Test
    @DisplayName("Delete book with valid ID - Happy Path")
    @Description("Verify that a book can be deleted successfully with valid ID")
    @Tag(TestTags.HAPPY_PATH)
    @Tag(TestTags.SMOKE)
    public void testDeleteBook_ExistingId_DeletesBookSuccessfully() {
        Book newBook = BookBuilder.randomBook();
        booksApi.createBook(newBook);
        Integer bookId = newBook.getId();

        Response responseDelete = booksApi.deleteBook(bookId);

        assertStatusCode(responseDelete, HttpStatus.SC_OK);

        Response responseGet = booksApi.getBookById(bookId);
        assertStatusCode(responseGet, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Delete book with non-existing ID - Edge Case")
    @Description("Verify that deleting a non-existent book returns 404")
    @Tag(TestTags.EDGE_CASE)
    @Tag(TestTags.REGRESSION)
    public void testDeleteBook_NonExistingId_ReturnsNotFound() {
        Integer nonExistingId = Integer.MAX_VALUE;

        Response response = booksApi.deleteBook(nonExistingId);

        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }
}