package data;

import models.Author;

import java.util.function.Consumer;

import static util.RandomUtils.randomInt;
import static util.RandomUtils.randomString;

/**
 * Builder pattern for Author objects.
 * Provides fluent API for creating test data with realistic values.
 */
public final class AuthorBuilder {

    private AuthorBuilder() {
        // Private constructor
    }

    public static Author buildRandomAuthor() {
        return Author.builder()
                .id(randomInt())
                .idBook(randomInt())
                .firstName(randomString("FirstName"))
                .lastName(randomString("LastName"))
                .build();
    }

    public static Author specificFieldAuthor(Consumer<Author> authorModifier) {
        Author author = buildRandomAuthor();
        authorModifier.accept(author);
        return author;
    }
}
