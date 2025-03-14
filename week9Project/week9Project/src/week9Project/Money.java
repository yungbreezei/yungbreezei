package week9Project;

import java.text.NumberFormat; // For formatting money as currency
import java.util.Locale;       // For locale-specific currency formatting
import java.util.Objects;

/**
 * Represents monetary values, stored in cents to ensure precision.
 */
public class Money implements Comparable<Money>{
    
    private final long cents; // Stores money in cents to avoid floating point precision errors
    private double amount;  // Store the amount in double

    /**
     * Constructs a Money object from a dollar amount.
     */
    public Money(double amount) {
    	// param amount The monetary amount in dollars (ex, 10.99 for $10.99).
        if (amount < 0) {
        	// throws IllegalArgumentException if the amount is negative.
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.cents = Math.round(amount * 100); // Convert to cents
    }

    /**
     * Constructs a Money object from cents.
     */
    public Money(long cents) {
    	// param cents The monetary amount in cents (e.g., 1099 for $10.99)
        if (cents < 0) {
        	// throws IllegalArgumentException if the cents is negative.
            throw new IllegalArgumentException("Cents cannot be negative.");
        }
        this.cents = cents;
    }

    /**
     * Retrieves the monetary amount in dollars.
     *
     * @return The amount in dollars.
     */
    public double getDollars() {
        return cents / 100.0; // Divide by 100 to convert cents to dollars 
    }
    
    /**
     * Adds two Money values and returns a new Money instance.
     * @param other The Money object to add.
     */
    public Money add(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add null Money object.");
        }
        return new Money(this.cents + other.cents); // return A new Money object representing the sum.
    }
    
    /**
     * Retrieves the monetary amount in cents.
     */
    public long getCents() {
        return cents; // return The amount in cents
    }
    
    /**
     * Compares this Money object with another based on cents.
     *
     * @param other The Money object to compare.
     * @return A negative integer, zero, or a positive integer if this Money
     *         is less than, equal to, or greater than the specified Money.
     */
    @Override
    public int compareTo(Money other) {
        return Long.compare(this.cents, other.cents);
    }
    
    /**
     * Checks if this Money object is equal to another.
     */
    @Override
    public boolean equals(Object obj) {
    	// param obj The object to compare.
        if (this == obj) {
            return true; // If the objects are the same, return true
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // If the object is null or of a different class, return false
        }
        Money money = (Money) obj; // Cast the object to a Money type
        return cents == money.cents; // return True if both Money objects have the same cent value
    }

    /**
     * Generates a hash code for the Money object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount); // return A hash code based on the cents value.
    }

    /**
     * Returns a formatted string representation of the money.
     */
    @Override
    public String toString() {
    	// return A string formatted as currency (e.g., "$12.34")
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US); // Format as US currency
        return formatter.format(getDollars()); // Return the formatted string (e.g., $12.34)
    }
}
