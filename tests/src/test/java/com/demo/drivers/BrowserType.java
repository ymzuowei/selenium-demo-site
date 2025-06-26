package com.demo.drivers;

import java.util.Set;

public enum BrowserType {
    CHROME, CHROME_360, FIREFOX, EDGE, SAFARI, CHROME_RESPONSIVE,
    ANDROID_REAL, IOS_REAL;

    private static final Set<String> REAL_DEVICE_NAMES = Set.of("ANDROID_REAL", "IOS_REAL");

    public static BrowserType fromEnvOrDefault() {
        String env = System.getProperty("browser", "CHROME");
        return BrowserType.valueOf(env.toUpperCase());
    }

    public static boolean isRealDevice(String browserName) {
        return REAL_DEVICE_NAMES.contains(browserName.toUpperCase());
    }
}