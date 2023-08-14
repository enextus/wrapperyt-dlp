package org.youtube;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VideoDownloaderFrame frame = new VideoDownloaderFrame();
            frame.setVisible(true);
        });
    }

}
