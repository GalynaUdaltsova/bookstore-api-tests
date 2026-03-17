package tags;

/**
 * Test tag constants for categorizing tests.
 * Used with JUnit 5 @Tag annotation for test selection.
 */
public final class TestTags {
    
    private TestTags() {}
    
    // Test priority
    public static final String SMOKE = "smoke";
    public static final String REGRESSION = "regression";

    // Entity categories
    public static final String BOOKS = "books";
    public static final String AUTHORS = "authors";
    
    // Test types
    public static final String HAPPY_PATH = "happy-path";
    public static final String NEGATIVE = "negative";
    public static final String EDGE_CASE = "edge-case";
}
