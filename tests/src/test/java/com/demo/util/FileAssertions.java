package com.demo.util;

// import com.demo.config.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class FileAssertions {

    public static void assertDownloadable(String fileUrl) {
        assertDownloadableRetry(fileUrl, 3, 10000, 10000, 1000);
    }

    public static void assertDownloadableRetry(String fileUrl, int maxRetries, int connectTimeout, int readTimeout, int retryIntervalMillis) {
        String absoluteUrl = fileUrl;
        if (!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://")) {
            // String baseUrl = Config.getBaseUrl();
            String baseUrl = "http://localhost:8080/";
            if (fileUrl.startsWith("/")) {
                absoluteUrl = baseUrl + fileUrl.substring(1);
            } else {
                absoluteUrl = baseUrl + fileUrl;
            }
        }

        System.out.println("[FileAssertions] Checking absolute file URL: " + absoluteUrl);

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                URI uri = URI.create(absoluteUrl);
                URL url = uri.toURL();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);

                int responseCode = conn.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    System.out.println("[FileAssertions] File is downloadable. Response code: " + responseCode);
                    return; // success
                } else {
                    fail("File not downloadable: " + absoluteUrl + " (status " + responseCode + ")");
                }

            } catch (IOException | IllegalArgumentException e) {
                System.err.println("[FileAssertions] Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxRetries) {
                    fail("Exception while checking file download: " + e.getMessage());
                }
                try {
                    Thread.sleep(retryIntervalMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    fail("Interrupted during retry sleep");
                }
            }
        }
    }
}
