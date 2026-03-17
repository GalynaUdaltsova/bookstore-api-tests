package config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for handling application settings.
 * Supports environment variables, system properties, and property files.
 * Priority: Environment Variables > System Properties > Property Files
 */
@Slf4j
public class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();

    private static final String DEFAULT_CONFIG = "config.properties";
    private static final String DEFAULT_ENV = "local";
    private static final String ENVIRONMENT_CONFIG_TEMPLATE = "%s.properties";
    private static final String BASE_URL_KEY = "base.url";
    private static final String CLEANUP_ENABLED_KEY = "test.data.cleanup.enabled";

    private Properties properties;

    private ConfigManager() {
        loadProperties();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    /**
     * Get base URL for API
     */
    public String getBaseUrl() {
        String baseUrl = properties.getProperty(BASE_URL_KEY);
        if (baseUrl == null || StringUtils.isEmpty(baseUrl)) {
            throw new RuntimeException("Base url can not be null or empty");
        }
        return baseUrl;
    }

    /**
     * Check if test data cleanup is enabled
     */
    public boolean isTestDataCleanupEnabled() {
        String value = properties.getProperty(CLEANUP_ENABLED_KEY);
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Load properties from files
     */
    private void loadProperties() {
        properties = new Properties();

        try {
            // Load default configuration
            loadPropertiesFile(DEFAULT_CONFIG);
            // Load environment-specific configuration
            String environment = System.getenv("ENV");
            log.info("Env from environment variable: {}", environment);
            if (environment == null || environment.isEmpty()) {
                environment = DEFAULT_ENV;
                log.info("Env from default value: {}", environment);
            }
            String envConfigFile = String.format(ENVIRONMENT_CONFIG_TEMPLATE, environment);
            loadPropertiesFile(envConfigFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }

    /**
     * Load properties from specific file
     */
    private void loadPropertiesFile(String filename) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                return;
            }
            Properties props = new Properties();
            props.load(inputStream);

            // Merge with existing properties (environment-specific overrides default)
            props.forEach((key, value) -> {
                String valueStr = value.toString();
                if (isPlaceholder(valueStr)) {
                    String envVarName = resolveEnvVarKey(valueStr);
                    String resolvedValue = System.getenv(envVarName);
                    if (resolvedValue != null) {
                        properties.setProperty(key.toString(), resolvedValue);
                        log.info("Resolved env var: {}, value: {}", key, resolvedValue);
                    }
                    return;
                }
                properties.setProperty(key.toString(), value.toString());
            });
        }
    }

    private String resolveEnvVarKey(String value) {
        return value.substring(2, value.length() - 1);
    }

    private boolean isPlaceholder(String value) {
        if (value == null || StringUtils.isEmpty(value)) {
            return false;
        }
        return (value.startsWith("${") && value.endsWith("}"));
    }
}
