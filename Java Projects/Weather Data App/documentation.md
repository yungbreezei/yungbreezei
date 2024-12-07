# ❄️ Project Weather Data App
<img src="https://static.vecteezy.com/system/resources/previews/024/825/182/original/3d-weather-icon-day-with-rain-free-png.png" alt="Image" width="300" height="320">

## 📚 Table of Contents
- [Objective](#Objective)
- [Weather API](#Weather-API)
- [Question and Solution](#question-and-solution)

***

## Objective
Go to [Open Weather Map API](https://openweathermap.org/api) decide which forecast you want to grab. Use the documentation and sample calls to determine what the data looks like and what kinds of objects you’ll need to create. You can choose to base your calls to the API on user inputs (location, time, etc.), or just make the same call on each run based on the current date and time and a fixed location. Once you’ve stored your weather data, display a menu to the user that lets them repeatedly elect to see different parts of that data, or exit the program. Make sure your menu is sensible and includes navigation you’d want to use. 

***`

## Weather API
Get json from the weather API and store it in objects with appropriate attributes.
Create a sensible interface for a user to garner useful information from your stored weather data.

***

````java
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Week10 {
    private static final String API_KEY = "217141b6ee7452856ea647dfeda27f59";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";

    public static void main(String[] args) {
        String city = getUserInput("Enter city name: ");
        String apiUrl = BASE_URL + "q=" + city + "&appid=" + API_KEY;

        try {
            JsonObject weatherData = getWeatherData(apiUrl);

            while (true) {
                displayMenu();
                int choice = getUserInputAsInt("Enter your choice (1-5): ");

                switch (choice) {
                    case 1:
                        System.out.println("\nCurrent Weather:");
                        System.out.println(weatherData.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString());
                        break;
                    case 2:
                        System.out.println("\nTemperature:");
                        System.out.println("Current Temperature: " + weatherData.getAsJsonObject("main").get("temp").getAsDouble() + " Kelvin");
                        break;
                    case 3:
                        System.out.println("\nHumidity:");
                        System.out.println("Current Humidity: " + weatherData.getAsJsonObject("main").get("humidity").getAsDouble() + "%");
                        break;
                    case 4:
                        System.out.println("\nWind Speed:");
                        System.out.println("Current Wind Speed: " + weatherData.getAsJsonObject("wind").get("speed").getAsDouble() + " m/s");
                        break;
                    case 5:
                        System.out.println("Exiting the program.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Current Weather");
        System.out.println("2. Temperature");
        System.out.println("3. Humidity");
        System.out.println("4. Wind Speed");
        System.out.println("5. Exit");
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading user input", e);
        }
    }

    private static int getUserInputAsInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(getUserInput(prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static JsonObject getWeatherData(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return new com.google.gson.JsonParser().parse(response.toString()).getAsJsonObject();
        } finally {
            connection.disconnect();
        }
    }
}

````
