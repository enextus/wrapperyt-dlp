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

    public static final String OUTPUT_PATH = "C:/Users/Administrator/Desktop/downloadVideo/";
    private final JTextField urlField;
    private final JButton downloadButton;

    private final JButton clearButton; // Добавляем кнопку очистки
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
        downloadButton.addActionListener(e -> downloadVideo());

        clearButton = new JButton("Clear"); // Создаем кнопку
        clearButton.addActionListener(e -> urlField.setText("")); // Устанавливаем действие для очистки поля

        infoLabel = new JLabel("Enter the video URL");

        JPanel eastPanel = new JPanel(); // Создаем новую панель для кнопок
        eastPanel.setLayout(new GridLayout(2, 1)); // Располагаем кнопки друг над другом
        eastPanel.add(downloadButton);
        eastPanel.add(clearButton);

        panel.add(urlField, BorderLayout.CENTER);
        panel.add(eastPanel, BorderLayout.EAST); // Добавляем панель вместо прямого добавления кнопки
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
                JOptionPane.showMessageDialog(VideoDownloaderFrame.this,
                        "Invalid URL. Please check and try again.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }

        String outputPath = OUTPUT_PATH; // Путь для сохранения видео

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
