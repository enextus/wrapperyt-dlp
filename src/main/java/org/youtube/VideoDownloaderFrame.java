package org.youtube;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.net.URL;

/**
 * VideoDownloaderFrame is a GUI class for downloading videos from YouTube.
 * It provides a simple interface for entering a video URL, and buttons to download the video
 * and clear the entered URL.
 */
public class VideoDownloaderFrame extends JFrame {

    private static final String OUTPUT_PATH = "C:/Users/Administrator/Desktop/downloadVideo/";
    private final JTextField urlField;
    private final JLabel infoLabel;

    /**
     * Constructor initializes the frame and components within it.
     */
    public VideoDownloaderFrame() {
        JButton downloadButton;
        JButton clearButton;
        setTitle("Video Downloader");
        setSize(500, 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        urlField = new JTextField();

        createAndSetPopupMenu();

        downloadButton = new JButton("Download");
        downloadButton.addActionListener(e -> downloadVideo());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> urlField.setText(""));

        infoLabel = new JLabel("Enter the video URL");

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(2, 1));
        eastPanel.add(downloadButton);
        eastPanel.add(clearButton);

        panel.add(urlField, BorderLayout.CENTER);
        panel.add(eastPanel, BorderLayout.EAST);
        panel.add(infoLabel, BorderLayout.SOUTH);

        add(panel);

        // Set focus on the input field after display
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                urlField.requestFocusInWindow();
            }
        });
    }

    /**
     * Creates a popup menu specifically for the URL field.
     *
     * @return the popup menu with the "Paste" option
     */
    private JPopupMenu createPopupMenuForUrlField() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataFlavor flavor = DataFlavor.stringFlavor;
            if (clipboard.isDataFlavorAvailable(flavor)) {
                try {
                    String clipboardText = (String) clipboard.getData(flavor);
                    urlField.setText(clipboardText);
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        popupMenu.add(pasteMenuItem);

        // ... add more items if necessary

        return popupMenu;
    }

    /**
     * Checks if a given URL is valid.
     *
     * @param url the URL to be checked
     * @return true if the URL is valid, false otherwise
     */
/*    public static boolean isValidURL(String url) {
        try {
            // The result is intentionally ignored; the method will throw an exception if it's an invalid URI.
            new URI(url).parseServerAuthority();
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
    */
    public static boolean isValidURL(String url) {
        try {
            URL u = new URL(url); // Проверяет синтаксис URL

            // Проверка схемы
            if (!("http".equals(u.getProtocol()) || "https".equals(u.getProtocol()))) {
                return false;
            }

            // Проверка хоста
            if (u.getHost() == null || u.getHost().isEmpty()) {
                return false;
            }

            // Если требуется, можно добавить дополнительные проверки (например, специфические пути)

            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    /**
     * Creates and sets the popup menu for the URL field.
     */
    private void createAndSetPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copy = new JMenuItem("Копировать");
        JMenuItem paste = new JMenuItem("Вставить");
        JMenuItem cut = new JMenuItem("Вырезать");

        copy.addActionListener(e -> urlField.copy());
        paste.addActionListener(e -> urlField.paste());
        cut.addActionListener(e -> urlField.cut());

        popupMenu.add(copy);
        popupMenu.add(paste);
        popupMenu.add(cut);

        urlField.setComponentPopupMenu(popupMenu);
    }

    /**
     * Downloads the video from the given URL.
     * Handles all the logic and error cases for downloading a video.
     */
    private void downloadVideo() {

        Path outputPath = Paths.get(OUTPUT_PATH);
        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                infoLabel.setText("Error creating directory: " + e.getMessage());
                return; // Terminate execution if the directory cannot be created
            }
        }

        String url = urlField.getText();
        System.out.println("url: " + url);
        // Check for empty input
        if (url.isEmpty()) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(VideoDownloaderFrame.this,
                    "Please enter a URL.", "Error", JOptionPane.ERROR_MESSAGE));
            return;
        }

        System.out.println("url: " + url);

        if (!isValidURL(url)) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(VideoDownloaderFrame.this,
                    "Invalid URL. Please check and try again.", "Error", JOptionPane.ERROR_MESSAGE));
            return;
        }

        List<String> command = new ArrayList<>();
        command.add("yt-dlp");
        command.add(url);
        command.add("-o");
        command.add(OUTPUT_PATH + "%(title)s.%(ext)s");

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                process.waitFor();
                if (output.toString().contains("ERROR")) {
                    infoLabel.setText("Error: Check output");
                    System.out.println(output);
                } else {
                    infoLabel.setText("Download complete");
                }
            }
        } catch (IOException ex) {
            infoLabel.setText("I/O Error: " + ex.getMessage());
            // Additional handling or logging of IOException
        } catch (InterruptedException ex) {
            infoLabel.setText("Interrupted: " + ex.getMessage());
            // Additional handling or logging of InterruptedException
        } catch (Exception ex) {
            infoLabel.setText("General Error: " + ex.getMessage());
            // Additional handling or logging of the general exception
        }

    }

}
