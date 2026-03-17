package tests;

import config.ConfigManager;
import data.DataCleaner;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

/**
 * Base test class for all API tests.
 * Provides common setup, configuration, and cleanup functionality.
 * Integrates with ConfigManager, ApiClient, and DataCleaner.
 */
@Slf4j
public abstract class BaseTest {

    protected static final ConfigManager config = ConfigManager.getInstance();
    protected static final DataCleaner dataCleaner = DataCleaner.getInstance();

    @BeforeAll
    @Step("Setup test class")
    @Description("Initialize test class with common configuration")
    public static void setupClass() {
        log.info("Setting up test class...");

        configureRestAssured();
    }

    @AfterAll
    @Step("Cleanup test class")
    @Description("Perform class-level cleanup and reset configuration")
    public static void cleanupClass() {
        log.info("Cleaning up test class...");

        try {
            // Final cleanup of any remaining test data
            if (config.isTestDataCleanupEnabled()) {
                dataCleaner.cleanupAll();
            }

            RestAssured.reset();

            log.info("Test class cleanup completed");
        } catch (Exception e) {
            log.error("Error during class cleanup: {}", e.getMessage(), e);
        }
    }

    /**
     * Configure RestAssured with common settings
     */
    private static void configureRestAssured() {
        // Set base URI from configuration
        baseURI = config.getBaseUrl();

        // Configure default headers
        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("Content-Type", "application/json");

        // Apply configuration
        RestAssured.requestSpecification = given()
                .headers(defaultHeaders)
                .relaxedHTTPSValidation() // For HTTPS APIs
                .when()
                .request();

        log.info("RestAssured configured with base URI: {}", baseURI);
    }
}
