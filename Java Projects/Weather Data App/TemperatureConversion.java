//Class for temperature conversion

public class TemperatureConversion {
 
    public static double convertKelvinToFahrenheit(double temperatureInKelvin) {
        // The formula to convert temperature from Kelvin to Fahrenheit
        return (temperatureInKelvin - 273.15) * 9/5 + 32;
    }
}
