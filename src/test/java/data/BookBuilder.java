package data;

import models.Book;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static util.RandomUtils.randomInt;
import static util.RandomUtils.randomString;

/**
 * Builder pattern for Book objects.
 * Provides fluent API for creating test data with realistic values.
 */
public class BookBuilder {

    /**
     * Create book with random data
     *
     * @return BookBuilder with random values
     */
    public static Book randomBook() {
        return Book.builder()
                .id(randomInt())
                .title(randomString("Title-"))
                .description(randomString("Description-"))
                .pageCount(randomInt())
                .excerpt(randomString("Excert-"))
                .publishDate(LocalDateTime.now().minusDays(randomInt(3645) + 1))
                .build();
    }

    public static Book specificFieldBook(Consumer<Book> fieldSupplier) {
        Book book = randomBook();
        fieldSupplier.accept(book);
        return book;
    }
}
