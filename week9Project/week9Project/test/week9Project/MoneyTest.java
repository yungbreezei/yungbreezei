package week9Project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MoneyTest {

    // Test the default constructor (which sets cents to 0)
    @Test
    void testDefaultConstructor() {
        // Create a new Money object using the default constructor
        Money money = new Money(0);
        
        // Assert that the cents value is 0 as expected from the default constructor
        assertEquals(0, money.getCents(), "Default constructor should set cents to 0");
    }

    // Test the constructor that takes a long (cents)
    @Test
    void testConstructorWithLong() {
        long cents = 500; // Initializing with 500 cents
        
        // Create a new Money object using the constructor that takes cents as a long
        Money money = new Money(cents); 
        
        // Assert that the cents field is correctly set
        assertEquals(cents, money.getCents(), "Constructor with long should correctly set cents");
    }

    // Test the getDollars method which converts cents to dollars
    @Test
    void testGetDollars() {
        Money money = new Money(500); // 500 cents = 5.00 dollars
        
        // Assert that getDollars correctly converts cents to dollars
        assertEquals(5.00, money.getDollars(), 0.001, "getDollars should correctly convert cents to dollars");
        
        // Create another Money object with 999 cents (9.99 dollars)
        money = new Money(999); // 999 cents = 9.99 dollars
        
        // Assert the conversion for 999 cents
        assertEquals(9.99, money.getDollars(), 0.001, "getDollars should correctly convert cents to dollars");
    }

    // Test the toString method which formats the money as a currency string
    @Test
    void testToString() {
    	// 500 cents = 5.00 dollars
    	Money money = new Money(500); 
        
        // Assert that toString gives the correct currency format
        assertEquals("$5.00", money.toString(), "toString should format money as currency");
        
        money = new Money(999); // 999 cents = 9.99 dollars
        
        // Assert for 999 cents
        assertEquals("$9.99", money.toString(), "toString should format money as currency");
    }

    // Test the constructor that takes a double (dollars)
    @Test
    void testConstructorWithDouble() {
        double amount = 10.99; // $10.99
        Money money = new Money(amount);
        assertEquals(10.99, money.getDollars(), 0.001, "Constructor with double should correctly set dollars");
    }

    // Test for negative amount passed to constructor with double
    @Test
    void testNegativeAmountWithDouble() {
    	assertThrows(IllegalArgumentException.class, () -> {
    		new Money(-1.0); // Negative amount should throw exception
        }, "Negative amounts should throw IllegalArgumentException");
    }

    // Test for negative amount passed to constructor with long
    @Test
    void testNegativeAmountWithLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money(-100); // Negative amount in cents should throw exception
        }, "Negative cents should throw IllegalArgumentException");
    }


    // Test adding two Money objects together
    @Test
    void testAdd() {
        Money money1 = new Money(500); // 500 cents = $5.00
        Money money2 = new Money(300); // 300 cents = $3.00
        
        Money sum = money1.add(money2); // Expected sum = $8.00
        assertEquals(800, sum.getCents(), "add method should correctly sum two Money objects");
    }

    // Test the compareTo method for comparing Money objects
    @Test
    void testCompareTo() {
        Money money1 = new Money(500); // 500 cents = $5.00
        Money money2 = new Money(300); // 300 cents = $3.00
        Money money3 = new Money(500); // 500 cents = $5.00
        
        assertTrue(money1.compareTo(money2) > 0, "Money1 should be greater than Money2");
        assertTrue(money2.compareTo(money1) < 0, "Money2 should be less than Money1");
        assertEquals(0, money1.compareTo(money3), "Money1 should be equal to Money3");
    }

    // Test the equals method for comparing Money objects
    @Test
    void testEquals() {
        // Create Money objects with different values
        Money money1 = new Money(500); // 500 cents = $5.00
        Money money2 = new Money(300); // 300 cents = $3.00
        Money money3 = new Money(500); // 500 cents = $5.00

        // Test for equality between money1 and money3 (same value, should be true)
        assertTrue(money1.equals(money3), "Money1 should be equal to Money3");

        // Test for inequality between money1 and money2 (different value, should be false)
        assertFalse(money1.equals(money2), "Money1 should not be equal to Money2");

        // Test for equality with null (should be false, money1 is not null)
        assertFalse(money1.equals(null), "Money1 should not be equal to null");

        // Test for equality between money1 and itself (should be true)
        assertTrue(money1.equals(money1), "Money1 should be equal to itself");

        // Test for equality with an object of a different class (should be false)
        assertFalse(money1.equals("String object"), "Money1 should not be equal to an object of different class");
    }

    // Test the hashCode method for Money objects
    @Test
    void testHashCode() {
        Money money1 = new Money(500); // 500 cents = $5.00
        Money money2 = new Money(500); // 500 cents = $5.00
        
        assertEquals(money1.hashCode(), money2.hashCode(), "Equal Money objects should have the same hash code");
    }

    // Test the edge case where cents is 0
    @Test
    void testZeroCents() {
        Money money = new Money(0); // 0 cents = $0.00
        assertEquals(0.00, money.getDollars(), 0.001, "Zero cents should be equivalent to $0.00");
        assertEquals("$0.00", money.toString(), "Zero cents should format as $0.00");
    }
}
