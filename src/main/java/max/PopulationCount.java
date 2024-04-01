package max;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class PopulationCount {
    public static void main(String[] args) {
        try {
            String population = getCurrentWorldPopulation();
            System.out.println("Current World Population Clock:");
            System.out.println(population);
        } catch (IOException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }

    public static String getCurrentWorldPopulation() throws IOException {
        String apiKey = "a8e1cd97c4msh064d3075d8aa236p17d767jsn10c62c5af329"; // Replace "YOUR_API_KEY" with actual key
        String apiUrl = "https://real-time-statistics.p.rapidapi.com/counters/current_population";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-RapidAPI-Key", apiKey);
        connection.setRequestProperty("X-RapidAPI-Host", "real-time-statistics.p.rapidapi.com");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        long population = jsonResponse.getLong("response");

        return formatPopulation(population);
    }

    public static Integer getBirths() throws IOException {
        String apiKey = "a8e1cd97c4msh064d3075d8aa236p17d767jsn10c62c5af329"; // Replace "YOUR_API_KEY" with actual key
        String apiUrl = "https://real-time-statistics.p.rapidapi.com/counters/births_today";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-RapidAPI-Key", apiKey);
        connection.setRequestProperty("X-RapidAPI-Host", "real-time-statistics.p.rapidapi.com");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        long res = jsonResponse.getLong("response");

        return (int) res;
    }

    public static Integer getDeaths() throws IOException {
        String apiKey = "a8e1cd97c4msh064d3075d8aa236p17d767jsn10c62c5af329"; // Replace "YOUR_API_KEY" with actual key
        String apiUrl = "https://real-time-statistics.p.rapidapi.com/counters/dth1s_today";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-RapidAPI-Key", apiKey);
        connection.setRequestProperty("X-RapidAPI-Host", "real-time-statistics.p.rapidapi.com");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        long res = jsonResponse.getLong("response");

        return (int) res;
    }

    private static String formatPopulation(long population) {
        return String.format("%,d", population);
    }
}
