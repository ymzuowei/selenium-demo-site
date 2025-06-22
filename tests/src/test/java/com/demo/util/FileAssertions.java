package com.demo.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class FileAssertions {

    public static void assertDownloadable(String fileUrl) {
        try {
            // Convert to URI first, then to URL
            URI uri = URI.create(fileUrl);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            assertTrue(responseCode >= 200 && responseCode < 300,
                    "File not downloadable: " + fileUrl + " (status " + responseCode + ")");
        } catch (IOException | IllegalArgumentException e) {
            fail("Exception while checking file download: " + e.getMessage());
        }
    }
}
