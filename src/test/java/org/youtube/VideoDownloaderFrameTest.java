package org.youtube;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VideoDownloaderFrameTest {

    @Test
    public void testValidUrl() {
        assertTrue(VideoDownloaderFrame.isValidURL("https://www.google.com"));
        assertTrue(VideoDownloaderFrame.isValidURL("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
    }

    @Test
    public void testInvalidUrl() {
        assertFalse(VideoDownloaderFrame.isValidURL("htt://www.google.com")); // отсутствует "p" в "http"
        assertFalse(VideoDownloaderFrame.isValidURL("randomtext"));
    }

}
