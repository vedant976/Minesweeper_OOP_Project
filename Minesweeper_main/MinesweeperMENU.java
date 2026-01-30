import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MinesweeperMENU {
    private JFrame menuFrame;
    private boolean soundEnabled = true;

    public MinesweeperMENU() {
        createStartMenu();
    }

    private void createStartMenu() {
        menuFrame = new JFrame("Minesweeper - Start Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(300, 500);
        
        JPanel panel = new JPanel(); // Create a panel for components
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical layout
        panel.setBackground(new Color(46, 46, 46)); // same as before
        menuFrame.setContentPane(panel); // Add panel to frame

        menuFrame.getContentPane().setBackground(new Color(46, 46, 46));
        
        JLabel titleLabel = new JLabel("Minesweeper", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createRigidArea(new Dimension(0, 20))); // spacing
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));   

        JButton easyButton = new JButton("Easy (8x8, 10 Mines)");
            easyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            easyButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            easyButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
            panel.add(easyButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

        JButton mediumButton = new JButton("Medium (10x10, 20 Mines)");
            mediumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            mediumButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            mediumButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
            panel.add(mediumButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

        JButton hardButton = new JButton("Hard (12x12, 30 Mines)");
            hardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            hardButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            hardButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
            panel.add(hardButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

        JButton customButton = new JButton("Custom Grid");
            customButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            customButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            customButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
            panel.add(customButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

        JButton soundButton = new JButton("Sound: ON");
            soundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            soundButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            soundButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
            panel.add(soundButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

        JButton highScoresButton = new JButton("View High Scores");
            highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            highScoresButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            highScoresButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
            panel.add(highScoresButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

        JButton statsButton = new JButton("View Stats");
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        statsButton.addActionListener(e -> {
            String stats = StatsManager.getFormattedStats();
            JOptionPane.showMessageDialog(menuFrame, stats, "Statistics", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(statsButton);
            

        highScoresButton.addActionListener(e -> showHighScores());

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color blue = new Color(74, 144, 226);
        Color text = Color.WHITE;

        JButton[] buttons = {easyButton, mediumButton, hardButton, customButton, soundButton,highScoresButton,statsButton};
        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setBackground(blue);
            button.setForeground(text);
            button.setFocusPainted(false);
        }

        easyButton.addActionListener(e -> {
            menuFrame.dispose();
            SwingUtilities.invokeLater(() -> new Minesweeper(8, 8, 10, soundEnabled,"EASY"));
        });

        mediumButton.addActionListener(e -> {
            menuFrame.dispose();
            SwingUtilities.invokeLater(() -> new Minesweeper(10, 10, 20, soundEnabled,"MEDIUM"));
        });

        hardButton.addActionListener(e -> {
            menuFrame.dispose();
            SwingUtilities.invokeLater(() -> new Minesweeper(12, 12, 30, soundEnabled,"HARD"));
        });

        customButton.addActionListener(e -> showCustomDialog());

        soundButton.addActionListener(e -> {
            soundEnabled = !soundEnabled;
            soundButton.setText("Sound: " + (soundEnabled ? "ON" : "OFF"));
        });

        menuFrame.add(titleLabel);
        menuFrame.add(easyButton);
        menuFrame.add(mediumButton);
        menuFrame.add(hardButton);
        menuFrame.add(customButton);
        menuFrame.add(soundButton);
        menuFrame.add(highScoresButton);
        menuFrame.add(statsButton);

        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    private void showCustomDialog() {
        JTextField rowsField = new JTextField();
        JTextField colsField = new JTextField();
        JTextField minesField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Rows:"));
        panel.add(rowsField);
        panel.add(new JLabel("Columns:"));
        panel.add(colsField);
        panel.add(new JLabel("Mines:"));
        panel.add(minesField);

        int result = JOptionPane.showConfirmDialog(menuFrame, panel, "Custom Grid", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                int mines = Integer.parseInt(minesField.getText());

                if (rows > 0 && cols > 0 && mines >= 0 && mines < rows * cols) {
                    menuFrame.dispose();
                    String difficultyLevel = "CUSTOM";
                    SwingUtilities.invokeLater(() -> new Minesweeper(rows, cols, mines,soundEnabled,difficultyLevel));
                } else {
                    JOptionPane.showMessageDialog(menuFrame, "Invalid grid settings.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(menuFrame, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void showHighScores() {
        String[] difficulties = { "EASY", "MEDIUM", "HARD", "CUSTOM" };
        String scores = readHighScores();
        StringBuilder message = new StringBuilder("🏆 High Scores:\n\n");
    
        for (String level : difficulties) {
            int score = HighScoreManager.getHighScore(level);
            message.append(level)
                   .append(": ")
                   .append(score == Integer.MAX_VALUE ? "—" : score + " seconds")
                   .append("\n");
        }
        JOptionPane.showMessageDialog(menuFrame, scores, "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    private String readHighScores() 
    {
        StringBuilder sb = new StringBuilder();
        File file = new File("C:\\Users\\Hp\\Desktop\\MineSweeper\\src\\highscores.txt");

        if (!file.exists()) {
            return "No high scores yet.";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return "Error reading high scores.";
        }

        return sb.toString();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperMENU::new);
    }
}