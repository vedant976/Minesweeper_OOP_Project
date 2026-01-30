import java.io.*;
import java.util.HashMap;

public class HighScoreManager {
    private static final String FILE_NAME = "highscores.txt";
    private static final HashMap<String, Integer> scores = new HashMap<>();

    static {
        loadScores();
    }

    private static void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    scores.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException ignored) {}
    }

    public static int getHighScore(String level) {
        return scores.getOrDefault(level, Integer.MAX_VALUE);
    }

    public static void updateHighScore(String level, int time) {
        if (time < getHighScore(level)) {
            scores.put(level, time);
            saveScores();
        }
    }

    private static void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String level : scores.keySet()) {
                writer.write(level + "=" + scores.get(level));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
