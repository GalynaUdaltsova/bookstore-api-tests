package client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSenderOptions;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.BiFunction;

import static logging.ApiLogger.logRequest;
import static logging.ApiLogger.logResponse;

/**
 * Central HTTP client with retry logic and logging.
 * Handles GET, POST, PUT, DELETE operations with RestAssured.
 * Uses type-safe HttpMethod enum for method validation.
 */
@Slf4j
public class ApiClient {

    private static final ApiClient INSTANCE = new ApiClient();

    private ApiClient() {
    }

    public static ApiClient getInstance() {
        return INSTANCE;
    }

    /**
     * POST request with path parameters and headers
     */
    public Response post(String endpoint, Object body, Map<String, Object> pathParams,
                         Map<String, String> headers) {
        return executeRequest(endpoint, body, pathParams, headers, HttpMethod.POST, RequestSenderOptions::post);
    }

    /**
     * GET request with path parameters and headers
     */
    public Response get(String endpoint, Map<String, Object> pathParams,
                        Map<String, String> headers) {
        return executeRequest(endpoint, null, pathParams, headers, HttpMethod.GET, RequestSenderOptions::get);
    }

    /**
     * PUT request with path parameters and headers
     */
    public Response put(String endpoint, Object body, Map<String, Object> pathParams,
                        Map<String, String> headers) {
        return executeRequest(endpoint, body, pathParams, headers, HttpMethod.PUT, RequestSenderOptions::put);
    }

    /**
     * DELETE request with path parameters and headers
     */
    public Response delete(String endpoint, Map<String, Object> pathParams,
                       Map<String, String> headers) {
        return executeRequest(endpoint, null, pathParams, headers, HttpMethod.DELETE, RequestSenderOptions::delete);
    }

    /**
     * Execute HTTP request with common setup and logging
     */
    private Response executeRequest(String endpoint, Object body, Map<String, Object> pathParams,
                                    Map<String, String> headers, HttpMethod method,
                                    BiFunction<RequestSpecification, String, Response> requestExecutor) {
        logRequest(endpoint, body, pathParams, headers, method.getValue());
        RequestSpecification request = RestAssured.given();

        if (headers != null) {
            request.headers(headers);
        }

        if (pathParams != null) {
            request.pathParams(pathParams);
        }

        if (body != null) {
            request.body(body);
        }

        Response response = requestExecutor.apply(request, endpoint);

        logResponse(response);
        return response;
    }
}
