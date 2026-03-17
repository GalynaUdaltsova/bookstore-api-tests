package util;

import java.util.Random;
import java.util.UUID;

public final class RandomUtils {

    private static final Random RANDOM = new Random();
    private static final int INT_BOUND = 10000;

    public static String randomString(String prefix) {
        return prefix + UUID.randomUUID();
    }

    public static int randomInt() {
        return RANDOM.nextInt(INT_BOUND);
    }

    public static int randomInt(int range) {
        return RANDOM.nextInt(range);
    }
}
