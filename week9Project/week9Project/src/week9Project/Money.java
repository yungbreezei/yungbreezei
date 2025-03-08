package week9Project;

import java.text.NumberFormat; // For formatting money as currency
import java.util.Locale;       // For locale-specific currency formatting

public class Money {
    
    // Stores money in cents to avoid floating point precision errors
    public long cents;

    // Default constructor initializes money to zero cents
    public Money() {
        this.cents = 0; // Default value for a new Money object
    }

    // Constructor to initialize money with a specified amount in cents
    public Money(long cents) {
        this.cents = cents; // Set the amount in cents
    }

    // Method to get the amount in dollars (converts cents to dollars)
    public double getDollars() {
        return cents / 100.0; // Divide by 100 to convert cents to dollars
    }

    // Returns a string representation of the money in a localized currency format
    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US); // Format as US currency
        return formatter.format(getDollars()); // Return the formatted string (e.g., $12.34)
    }
}
