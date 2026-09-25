package com.shxdnw.resonant_weaponry;

import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DebugLog {
    private static final Logger LOGGER = LoggerFactory.getLogger("resonant_weaponry");

    private DebugLog() {
    }

    public static void log(String message, Object... arguments) {
        if (ResonantWeaponryConfig.general != null && ResonantWeaponryConfig.general.debugLogging) {
            LOGGER.info(message, arguments);
        }
    }
}
