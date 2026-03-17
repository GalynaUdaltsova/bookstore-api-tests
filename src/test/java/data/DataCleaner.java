package data;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Automatic test data cleanup utility.
 * Tracks created entities and removes them after tests.
 */
@Slf4j
public class DataCleaner {

    private static final DataCleaner INSTANCE = new DataCleaner();

    // Thread-safe collections for tracking created entities
    private final ThreadLocal<List<Runnable>> cleanupActions = ThreadLocal.withInitial(ArrayList::new);

    private DataCleaner() {
    }

    public static DataCleaner getInstance() {
        return INSTANCE;
    }

    public void registerCleanupAction(Runnable cleanupAction) {
        cleanupActions.get().add(cleanupAction);
    }

    public void cleanupAll() {
        log.info("Starting full cleanup...");
        if(cleanupActions.get().isEmpty()) {
            log.info("No data for clean up");
            return;
        }
        executeCleanupActions();
        log.info("Cleanup completed.");
    }
    
    private void executeCleanupActions() {
        for (Runnable cleanupAction : cleanupActions.get()) {
            try {
                cleanupAction.run();
            } catch (Exception e) {
                log.warn("Cleanup action failed: {}", e.getMessage());
            }
        }
        cleanupActions.get().clear();
    }
}
