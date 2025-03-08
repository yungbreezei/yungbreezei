package week9Project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MoneyTest {

    // Test the default constructor (which sets cents to 0)
    @Test
    void testDefaultConstructor() {
        // Create a new Money object using the default constructor
        Money money = new Money();
        
        // Assert that the cents value is 0 as expected from the default constructor
        assertEquals(0, money.cents, "Default constructor should set cents to 0");
    }

    // Test the constructor that takes a long (cents)
    @Test
    void testConstructorWithLong() {
        long cents = 500; // Initializing with 500 cents
        Money money = new Money(cents);
        
        // Assert that the cents field is correctly set
        assertEquals(cents, money.cents, "Constructor with long should correctly set cents");
    }

    // Test the getDollars method which converts cents to dollars
    @Test
    void testGetDollars() {
        Money money = new Money(500); // 500 cents = 5.00 dollars
        
        // Assert that getDollars correctly converts cents to dollars
        assertEquals(5.00, money.getDollars(), 0.001, "getDollars should correctly convert cents to dollars");
        
        money = new Money(999); // 999 cents = 9.99 dollars
        
        // Assert the conversion for 999 cents
        assertEquals(9.99, money.getDollars(), 0.001, "getDollars should correctly convert cents to dollars");
    }

    // Test the toString method which formats the money as a currency string
    @Test
    void testToString() {
        Money money = new Money(500); // 500 cents = 5.00 dollars
        
        // Assert that toString gives the correct currency format
        assertEquals("$5.00", money.toString(), "toString should format money as currency");
        
        money = new Money(999); // 999 cents = 9.99 dollars
        
        // Assert for 999 cents
        assertEquals("$9.99", money.toString(), "toString should format money as currency");
    }
}
