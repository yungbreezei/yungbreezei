import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Main class for the Weather App, fetching data from OpenWeatherMap and providing user interaction.
 */

public class Week10 {
    //Get API data from OpenWeatherMap with APIKEY
    private static final String API_KEY = "217141b6ee7452856ea647dfeda27f59";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=london&appid=217141b6ee7452856ea647dfeda27f59&units=metric";

    public static void main(String[] args) {
        // Prompt the user to access Weather Map in London, England
        String city = getUserInput.getUserInput("Press Enter to Access Weather Map in London, England: ");
        String apiUrl = BASE_URL + "q=" + city + "&appid=" + API_KEY;

        try {
             JsonObject weatherData = getWeatherData.getWeatherData(apiUrl);

            while (true) {
                City.displayMenu();
                int choice = getUserInput.getUserInputAsInt("Enter your choice (1-6): ");

                switch (choice) {
                    case 1:
                        // Display current weather information and check if the current month is the coldest or wettest
                        System.out.println("\nCurrent Weather:");
                        System.out.println(weatherData.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString());
                                             
                        // Add the if statement to check if the current month is the coldest or wettest
                        Calendar calendar = Calendar.getInstance();
                        int currentMonth = calendar.get(Calendar.MONTH);
                        String currentMonthName = new SimpleDateFormat("MMMM").format(calendar.getTime());

                        if (ClimateInfo.isColdestMonth(currentMonth)) {
                            System.out.println(currentMonthName + " is the coldest month in London!");
                        }else if (ClimateInfo.isWettestMonth(currentMonth)) {
                            System.out.println(currentMonthName + " is the wettest month in London!");
                        }else {
                            System.out.println(currentMonthName + " is not the coldest in London.");
                            System.out.println(currentMonthName + " is not the wettest in London.");
                        }
                        break;
                    case 2:
                        // Weather map prints Kelvin
                        System.out.println("\nTemperature in Kelvin:");
                        System.out.println("Current Temperature: " + weatherData.getAsJsonObject("main").get("temp").getAsDouble() + " Kelvin");
                        double temperatureInKelvin = weatherData.getAsJsonObject("main").get("temp").getAsDouble();
                        
                        // Conversion from Kelvin to Fahrenheit
                        double temperatureInFahrenheit = TemperatureConversion.convertKelvinToFahrenheit(temperatureInKelvin);
                        System.out.println("\nTemperature in Fahrenheit:");
                        System.out.println("Current Temperature: " + temperatureInFahrenheit + " °F");

                        if (temperatureInFahrenheit <= 80) {
                            FoodOption soupOption = FoodOption.createSoup();
                            PopularSpot bigBenSpot = PopularSpot.showBigBen();                           
                            PopularSpot coffeeShop = PopularSpot.showCoffeeShop();

                            // Print information about the FoodOption & PopularSpots
                            System.out.println("\nHere is a Food Suggestion for this weather: " + soupOption.getFoodName());
                            System.out.println("Price: $" + soupOption.getPrice());
                            System.out.println("\nA Popular spot for this weather: " + bigBenSpot.toString());
                            System.out.println("\nA Popular spot for this weather: " + coffeeShop.toString());
                        }else{
                            FoodOption icePopOption = FoodOption.createIcePop();
                            PopularSpot bridgeSpot = PopularSpot.showBridge();
                            PopularSpot conservatory = PopularSpot.showConservatory();

                            // Print information about the FoodOption & PopularSpots
                            System.out.println("\nHere is a Food Suggestion for this weather: " + icePopOption.getFoodName());
                            System.out.println("Price: $" + icePopOption.getPrice());
                            System.out.println("\nPopular spot for this weather: " + bridgeSpot.toString());
                            System.out.println("\nPopular spot for this weather: " + conservatory.toString());
                        }
                        break;
                    case 3:
                        // Weather map prints Kelvin
                        System.out.println("\nReal Feel in Kelvin:");
                        System.out.println(weatherData.getAsJsonObject("main").get("feels_like").getAsDouble() + " Kelvin");
                        double realFeelInKelvin = weatherData.getAsJsonObject("main").get("feels_like").getAsDouble();
                        
                        // Conversion from Kelvin to Fahrenheit
                        double realFeelInFahrenheit = TemperatureConversion.convertKelvinToFahrenheit(realFeelInKelvin);
                        System.out.println("\nReal Feel in Fahrenheit:");
                        System.out.println(realFeelInFahrenheit + " °F");
                        break;
                    case 4:
                        // Display humidity information along with a fun fact
                        System.out.println("\nHumidity:");
                        System.out.println("Current Humidity: " + weatherData.getAsJsonObject("main").get("humidity").getAsDouble() + "%");
                        System.out.println("Fun fact: The driest year in UK history was 1855.");
                        break;
                    case 5:
                        // Display wind speed information along with a fun fact
                        System.out.println("\nWind Speed:");
                        System.out.println("Current Wind Speed: " + weatherData.getAsJsonObject("wind").get("speed").getAsDouble() + " m/s");
                        System.out.println("Fun fact: The windiest day in UK history was December 19, 2008. Wind gust 194 mph(312 km/h).");
                        break;
                    case 6:
                        // Exit the program
                        System.out.println("Exiting the program.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }
}
