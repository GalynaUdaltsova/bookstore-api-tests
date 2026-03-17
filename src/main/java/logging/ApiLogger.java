package logging;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * API-focused logging with Allure integration.
 * Handles request/response logging and API error logging.
 */
@Slf4j
public final class ApiLogger {

    private ApiLogger() {
        // Private constructor for singleton pattern
    }

    /**
     * Log HTTP request details
     */
    public static void logRequest(String endpoint, Object body, Map<String, Object> pathParams,
                                  Map<String, String> headers, String methodName) {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("API Request:\n");
        requestLog.append("  Method: ").append(methodName).append("\n");
        requestLog.append("  URI: ").append(endpoint).append("\n");

        if (headers != null && !headers.isEmpty()) {
            requestLog.append("  Headers: ").append(headers).append("\n");
        }

        if (body != null) {
            requestLog.append("  Body: ").append(body).append("\n");
        }

        if (pathParams != null && !pathParams.isEmpty()) {
            requestLog.append("  Params: ").append(pathParams).append("\n");
        }

        log.info(requestLog.toString());
    }

    /**
     * Log HTTP response details
     */
    public static void logResponse(Response response) {
        int statusCode = response.getStatusCode();
        String statusLine = response.getStatusLine();
        Map<String, String> headers = new HashMap<>();
        response.getHeaders().forEach(header -> headers.put(header.getName(), header.getValue()));
        String body = response.getBody().asString();
        long responseTime = response.getTime();

        StringBuilder responseLog = new StringBuilder();
        responseLog.append("API Response:\n");
        responseLog.append("  Status: ").append(statusCode).append(" ").append(statusLine).append("\n");
        responseLog.append("  Response Time: ").append(responseTime).append("ms\n");
        
        if (!headers.isEmpty()) {
            responseLog.append("  Headers: ").append(headers).append("\n");
        }
        
        if (body != null && !body.isEmpty()) {
            responseLog.append("  Body: ").append(body).append("\n");
        }
        
        log.info(responseLog.toString());
    }
}
