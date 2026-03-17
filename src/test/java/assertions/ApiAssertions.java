package assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Author;
import models.Book;

import java.util.List;

import static io.qameta.allure.Allure.addAttachment;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Custom assertion methods with integrated logging.
 * Provides reusable validation for API responses.
 * Integrates with Allure for test reporting.
 */
public class ApiAssertions {

    /**
     * Assert HTTP status code
     *
     * @param response     HTTP response
     * @param expectedCode Expected status code
     */
    @Step("Assert status code is {expectedCode} (actual: {actualCode})")
    public static void assertStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();

        assertEquals(expectedCode, actualCode,
                "Expected status code " + expectedCode + " but got " + actualCode);
    }

    /**
     * Assert list is not empty
     *
     * @param list List to check
     */
    @Step("Assert list is not empty")
    public static void assertResponseNotEmpty(List<?> list) {
        assertNotNull(list, "List should not be null");
        assertFalse(list.isEmpty(), "List should not be empty");
    }

    /**
     * Assert book object equality from response
     *
     * @param expectedBook Expected book object
     * @param response     HTTP response containing book
     */
    @Step("Assert book from response equals expected")
    public static void assertBookEquals(Book expectedBook, Response response) {
        Book actualBook = response.as(Book.class);

        assertNotNull(actualBook, "Actual book should not be null");
        assertNotNull(expectedBook, "Expected book should not be null");

        assertNotNull(actualBook.getId(), "Created book should have an ID");

        // Add complete objects as attachments
        addAttachment("Expected Book", "application/json", expectedBook.toString());
        addAttachment("Actual Book", "application/json", actualBook.toString());

        // Compare title
        String expectedTitle = expectedBook.getTitle();
        String actualTitle = actualBook.getTitle();
        assertEquals(expectedTitle, actualTitle,
                "Book title mismatch - expected: " + expectedTitle + ", actual: " + actualTitle);

        // Compare description
        String expectedDescription = expectedBook.getDescription();
        String actualDescription = actualBook.getDescription();
        assertEquals(expectedDescription, actualDescription,
                "Book description mismatch - expected: " + expectedDescription + ", actual: " + actualDescription);

        // Compare page count
        Integer expectedPageCount = expectedBook.getPageCount();
        Integer actualPageCount = actualBook.getPageCount();
        assertEquals(expectedPageCount, actualPageCount,
                "Book page count mismatch - expected: " + expectedPageCount + ", actual: " + actualPageCount);

        // Compare excerpt
        String expectedExcerpt = expectedBook.getExcerpt();
        String actualExcerpt = actualBook.getExcerpt();
        assertEquals(expectedExcerpt, actualExcerpt,
                "Book excerpt mismatch - expected: " + expectedExcerpt + ", actual: " + actualExcerpt);

        // Handle date comparison with null safety and flexible precision
        if (expectedBook.getPublishDate() != null && actualBook.getPublishDate() != null) {
            var expectedPublishDate = expectedBook.getPublishDate().withNano(0);
            var actualPublishDate = actualBook.getPublishDate().withNano(0);
            assertEquals(expectedPublishDate, actualPublishDate,
                    "Book publish date mismatch - expected: " + expectedPublishDate + ", actual: " + actualPublishDate);
        }
    }

    /**
     * Assert author object equality from response
     *
     * @param expectedAuthor Expected author object
     * @param response       HTTP response containing author
     */
    @Step("Assert author objects are equal")
    public static void assertAuthorEquals(Author expectedAuthor, Response response) {
        Author actualAuthor = response.as(Author.class);

        // Add complete objects as attachments
        addAttachment("Expected Author", "application/json", expectedAuthor.toString());
        addAttachment("Actual Author", "application/json", actualAuthor.toString());

        assertNotNull(actualAuthor, "Actual author should not be null");
        assertNotNull(expectedAuthor, "Expected author should not be null");

        // Compare ID
        if (expectedAuthor.getId() != null) {
            Integer expectedId = expectedAuthor.getId();
            Integer actualId = actualAuthor.getId();
            assertEquals(expectedId, actualId,
                    "Author ID mismatch - expected: " + expectedId + ", actual: " + actualId);
        }

        // Compare book ID
        Integer expectedIdBook = expectedAuthor.getIdBook();
        Integer actualIdBook = actualAuthor.getIdBook();
        assertEquals(expectedIdBook, actualIdBook,
                "Author book ID mismatch - expected: " + expectedIdBook + ", actual: " + actualIdBook);

        // Compare first name
        String expectedFirstName = expectedAuthor.getFirstName();
        String actualFirstName = actualAuthor.getFirstName();
        assertEquals(expectedFirstName, actualFirstName,
                "Author first name mismatch - expected: " + expectedFirstName + ", actual: " + actualFirstName);

        // Compare last name
        String expectedLastName = expectedAuthor.getLastName();
        String actualLastName = actualAuthor.getLastName();
        assertEquals(expectedLastName, actualLastName,
                "Author last name mismatch - expected: " + expectedLastName + ", actual: " + actualLastName);
    }
}
