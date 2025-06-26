package com.demo.config;

import com.demo.drivers.BrowserType;

public class Config {
    public static String getBaseUrl() {
        BrowserType browser = BrowserType.fromEnvOrDefault();

        if (browser == BrowserType.ANDROID_REAL) {
            return "http://10.0.2.2:8080/";
        } else if (browser == BrowserType.IOS_REAL) {
            return "http://host.docker.internal:8080/"; // for ios simulator or container
        } else {
            return "http://localhost:8080/";
        }
    }
}
