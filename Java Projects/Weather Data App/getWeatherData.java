import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Weather data retrieval class
public class getWeatherData{
    
        /**
     * Retrieves weather data from the specified API URL.
     *
     * @param apiUrl The API URL for fetching weather data.
     * @return A JsonObject containing the weather data.
     * @throws IOException If an error occurs while fetching the data.
     */
    
public static JsonObject getWeatherData(String apiUrl) throws IOException {
       // Create a URL object based on the provided API URL
        URL url = new URL(apiUrl);
        
       // Open a connection to the URL using HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        // Read the response from the API and store it in a StringBuilder
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            // Parse the response as a JsonObject using Gson library
            return new com.google.gson.JsonParser().parse(response.toString()).getAsJsonObject();
        } finally {
        // Disconnect the HttpURLConnection to release resources
            connection.disconnect();
        }
    }
}