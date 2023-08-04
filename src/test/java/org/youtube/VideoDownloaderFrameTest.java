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
        assertFalse(VideoDownloaderFrame.isValidURL("invalidurl"));
        assertFalse(VideoDownloaderFrame.isValidURL("youtube.com/watch?v=dQw4w9WgXcQ"));
        assertFalse(VideoDownloaderFrame.isValidURL("ht:/malformed.com"));
    }

    @Test
    public void testEmptyUrl() {
        assertFalse(VideoDownloaderFrame.isValidURL(""));
    }

    @Test
    public void testInvalidUrl1() {
        assertFalse(VideoDownloaderFrame.isValidURL("invalidurl"), "Failed on: invalidurl");
    }

    @Test
    public void testInvalidUrl2() {
        assertFalse(VideoDownloaderFrame.isValidURL("youtube.com/watch?v=dQw4w9WgXcQ"), "Failed on: youtube.com/watch?v=dQw4w9WgXcQ");
    }

    @Test
    public void testInvalidUrl3() {
        assertFalse(VideoDownloaderFrame.isValidURL("ht:/malformed.com"), "Failed on: ht:/malformed.com");
    }

}
