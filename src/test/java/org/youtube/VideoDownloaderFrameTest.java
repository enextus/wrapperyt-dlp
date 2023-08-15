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

    @Test
    public void testDifferentProtocols() {
        assertFalse(VideoDownloaderFrame.isValidURL("ftp://www.example.com")); // FTP is not a supported protocol
    }


    @Test
    public void testDifferentProtocols_others() {
        assertTrue(VideoDownloaderFrame.isValidURL("http://www.example.com"));
        assertFalse(VideoDownloaderFrame.isValidURL("xyz://www.example.com")); // if you expect this to fail
    }

    @Test
    public void testEmptyOrNullUrl() {
        assertFalse(VideoDownloaderFrame.isValidURL(null));
        assertFalse(VideoDownloaderFrame.isValidURL(""));
    }

}
