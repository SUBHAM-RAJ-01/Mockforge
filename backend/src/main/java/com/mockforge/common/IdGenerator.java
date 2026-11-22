package com.mockforge.common;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility for generating unique identifiers for entities.
 * Uses URL-safe Base64 encoded random bytes with entity-specific prefixes.
 */
public class IdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    /**
     * Generate a unique ID with the given prefix.
     * Format: {prefix}_{base64RandomBytes}
     * 
     * @param prefix Entity type prefix (e.g., "proj", "res", "ep")
     * @return Unique identifier string
     */
    public static String generate(String prefix) {
        byte[] randomBytes = new byte[12]; // 96 bits
        RANDOM.nextBytes(randomBytes);
        String encoded = ENCODER.encodeToString(randomBytes);
        return prefix + "_" + encoded;
    }

    public static String generateProjectId() {
        return generate("proj");
    }

    public static String generateResourceId() {
        return generate("res");
    }

    public static String generateFieldId() {
        return generate("fld");
    }

    public static String generateEndpointId() {
        return generate("ep");
    }

    public static String generateScenarioId() {
        return generate("scn");
    }

    public static String generateRequestId() {
        return generate("req");
    }

    public static String generateRecordId() {
        return generate("rec");
    }
}
