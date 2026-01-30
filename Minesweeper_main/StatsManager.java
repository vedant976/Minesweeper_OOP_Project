import java.io.*;
import java.util.HashMap;

public class StatsManager {
    private static final String FILE_NAME = "stats.txt";
    private static HashMap<String, Integer> stats = new HashMap<>();

    static {
        loadStats();
    }

    private static void loadStats() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    stats.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException ignored) {
            stats.put("GamesPlayed", 0);
            stats.put("GamesWon", 0);
        }
    }

    public static void incrementGamesPlayed() {
        stats.put("GamesPlayed", getGamesPlayed() + 1);
        saveStats();
    }

    public static void incrementGamesWon() {
        stats.put("GamesWon", getGamesWon() + 1);
        saveStats();
    }

    public static int getGamesPlayed() {
        return stats.getOrDefault("GamesPlayed", 0);
    }

    public static int getGamesWon() {
        return stats.getOrDefault("GamesWon", 0);
    }

    private static void saveStats() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String key : stats.keySet()) {
                writer.write(key + "=" + stats.get(key));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFormattedStats() {
        int played = getGamesPlayed();
        int won = getGamesWon();
        double winRate = (played > 0) ? (100.0 * won / played) : 0.0;
        return "Games Played: " + played + "\nGames Won: " + won + "\nWin Rate: " + String.format("%.2f", winRate) + "%";
    }
}
