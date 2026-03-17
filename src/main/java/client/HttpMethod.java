package client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of HTTP methods supported by the API client.
 * Provides type safety and compile-time validation for HTTP method names.
 * Uses annotation-based approach for cleaner implementation.
 */
@Getter
@RequiredArgsConstructor
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH");

    private final String value;
}
