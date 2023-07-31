package org.practice.utils;

import java.util.UUID;

public class Utils {

    public static long generateId() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        // XOR the most and least significant bits to generate a long ID
        long souvenirId = mostSigBits ^ leastSigBits;

        // Ensure the ID is positive (as XOR might produce a negative value)
        if (souvenirId < 0) {
            souvenirId = -souvenirId;
        }

        return souvenirId;
    }
}
