package org.youtube;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class VideoDownloaderFrame extends JFrame {

    private final JTextField urlField;
    private final JButton downloadButton;
    private final JLabel infoLabel;

    public VideoDownloaderFrame() {
        setTitle("Video Downloader");
        setSize(500, 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрируем окно

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        urlField = new JTextField();
        downloadButton = new JButton("Download");
        infoLabel = new JLabel("Enter the video URL");

        downloadButton.addActionListener(e -> downloadVideo());

        panel.add(urlField, BorderLayout.CENTER);
        panel.add(downloadButton, BorderLayout.EAST);
        panel.add(infoLabel, BorderLayout.SOUTH);

        add(panel);
    }

    public static boolean isValidURL(String url) {
        try {
            new URI(url).parseServerAuthority();
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private void downloadVideo() {
        String url = urlField.getText();

        System.out.printf("url: " + url);

        if (!isValidURL(url)) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(VideoDownloaderFrame.this, "Invalid URL. Please check and try again.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }



        String outputPath = "C:/Users/Administrator/Desktop/"; // Путь для сохранения видео

        List<String> command = new ArrayList<>();
        command.add("yt-dlp");
        command.add(url);
        command.add("-o");
        command.add(outputPath + "%(title)s.%(ext)s");

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);  // Объединяем стандартный поток вывода и поток ошибок

        try {
            Process process = processBuilder.start();

            // Читаем поток вывода используя try-with-resources
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                process.waitFor();

                // Если в выводе есть сообщение об ошибке, отображаем его
                if (output.toString().contains("ERROR")) {
                    infoLabel.setText("Error: Check output");
                    System.out.println(output);  // Выводим всю информацию в консоль
                } else {
                    infoLabel.setText("Download complete");
                }
            }
        } catch (Exception ex) {
            infoLabel.setText("Error: " + ex.getMessage());
        }
    }
}
