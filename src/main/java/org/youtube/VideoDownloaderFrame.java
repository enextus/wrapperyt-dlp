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

    private static final String OUTPUT_PATH = "C:/Users/Administrator/Desktop/downloadVideo/";
    private final JTextField urlField;
    private final JButton downloadButton;

    private final JButton clearButton; // Добавляем кнопку очистки
    private final JLabel infoLabel;

    public VideoDownloaderFrame() {
        setTitle("Video Downloader");
        setSize(500, 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        urlField = new JTextField();

        // Добавление контекстного меню для поля ввода
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

        // Установка фокуса на поле ввода после отображения
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                urlField.requestFocusInWindow();
            }
        });
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
        System.out.println("url: " + url); // Используйте println вместо printf

        // Проверка на пустой ввод
        if (url.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(VideoDownloaderFrame.this,
                        "Please enter a URL.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }

        System.out.println("url: " + url);

        if (!isValidURL(url)) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(VideoDownloaderFrame.this,
                        "Invalid URL. Please check and try again.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }

        List<String> command = new ArrayList<>();
        command.add("yt-dlp");
        command.add(url);
        command.add("-o");
        command.add(OUTPUT_PATH + "%(title)s.%(ext)s"); // Используйте OUTPUT_PATH напрямую

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
        } catch (Exception ex) {
            infoLabel.setText("Error: " + ex.getMessage());
        }
    }

}
