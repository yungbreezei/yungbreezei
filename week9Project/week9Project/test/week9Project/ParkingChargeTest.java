package week9Project;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class ParkingChargeTest {
	
    /**
     * Tests the constructor and getters to ensure proper initialization of the ParkingCharge object.
     */
    @Test
    void testConstructorAndGetters() {
    	// Initialize test values
        Money amount = new Money(500); // Amount in cents ($5.00)
        String permitId = "P123";
        String lotId = "L001";
        Instant incurred = Instant.now(); // Time charge was incurred
        Instant expirationDate = incurred.plus(1, ChronoUnit.DAYS); // Expiration set to 1 day later

        // Create ParkingCharge instance
        ParkingCharge charge = new ParkingCharge(amount, permitId, lotId, incurred, expirationDate);
        
        // Verify all getters return expected values
        assertEquals(amount, charge.getAmount(), "Amount should match the initialized value.");
        assertEquals(permitId, charge.getPermitId(), "Permit ID should match.");
        assertEquals(lotId, charge.getLotId(), "Lot ID should match.");
        assertEquals(incurred, charge.getIncurred(), "Incurred timestamp should match.");
    }

    /**
     * Tests if a permit is correctly identified as expired when the expiration time is in the past.
     */
    @Test
    void testIsPermitExpired_Expired() {
        // Set timestamps to simulate an expired permit
        Instant expiredTime = Instant.now().minus(1, ChronoUnit.DAYS); // Charge incurred yesterday
        Instant validExpiration = expiredTime.plus(1, ChronoUnit.MINUTES); // Ensure it expires after incurred

        // Create ParkingCharge instance with a valid expiration date
        Money amount = new Money(500);
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", expiredTime, validExpiration);

        // Verify that the permit is marked as expired
        assertTrue(charge.isPermitExpired(), "Permit should be expired.");
    }

    /**
     * Tests if a permit is correctly identified as valid when the expiration time is in the future.
     */
    @Test
    void testIsPermitExpired_NotExpired() {
        // Set timestamps to simulate a valid permit
        Instant validTime = Instant.now(); // Current time as the incurred time
        Instant validExpiration = validTime.plus(1, ChronoUnit.DAYS); // Expiration set to 1 day later

        // Create ParkingCharge instance
        Money amount = new Money(500);
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", validTime, validExpiration);
        
        // Verify that the permit is NOT marked as expired
        assertFalse(charge.isPermitExpired(), "Permit should not be expired.");
    }

    /**
     * Tests the toString() method to ensure the string representation is formatted correctly.
     */
    @Test
    void testToString() {
        // Initialize values for testing
        Money amount = new Money(500);
        String permitId = "P123";
        String lotId = "L001";
        Instant incurred = Instant.now();
        Instant expirationDate = incurred.plus(1, ChronoUnit.DAYS);
        
        // Create ParkingCharge instance
        ParkingCharge charge = new ParkingCharge(amount, permitId, lotId, incurred, expirationDate);
        
        // Expected string format
        String expectedString = "ParkingCharge{permitId='P123', amount=$5.00, lotId='L001'}";
        
        // Verify the string output
        assertEquals(expectedString, charge.toString(), "toString() output does not match expected format.");
    }
    
    /**
     * Tests if a permit with an already expired timestamp is correctly identified as expired.
     */
    @Test
    void testInvalidPermitExpiration() {
        // Set timestamps where expiration is before incurred time
        Instant incurredTime = Instant.now();
        Instant invalidExpiration = incurredTime.minus(1, ChronoUnit.HOURS); // Expiration before incurred

        Money amount = new Money(500);

        // Expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new ParkingCharge(amount, "P123", "L001", incurredTime, invalidExpiration);
        });
    }
    
    @Test
    void testInvalidPermitExpirationThrowsException() {
        // Set timestamps where expiration happens before incurred time
        Instant incurredTime = Instant.now();
        Instant invalidExpiration = incurredTime.minus(1, ChronoUnit.HOURS); // Expiration before incurred

        Money amount = new Money(500);

        // Assert that the constructor throws an IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ParkingCharge(amount, "P123", "L001", incurredTime, invalidExpiration);
        });

        // Ensure the exception message matches exactly
        assertEquals("Expiration date cannot be before the incurred date.", exception.getMessage());
    }
    /*
     * Tests Ensure free parking charges are valid
     */
    @Test
    void testZeroChargeAmount() {
        // Create a charge amount of zero (free parking scenario)
        Money zeroAmount = new Money(0);
        Instant incurred = Instant.now(); // Charge incurred at current time
        Instant expiration = incurred.plus(1, ChronoUnit.DAYS); // Expiration set 1 day later
        
        // Create a ParkingCharge instance with zero amount
        ParkingCharge charge = new ParkingCharge(zeroAmount, "P123", "L001", incurred, expiration);
        
        // Verify that the stored amount is zero as expected
        assertEquals(zeroAmount, charge.getAmount(), "Charge amount should be zero.");
    }

    /*
     * Test expiration logic for long-term permits
     */
    @Test
    void testLongTermParkingExpiration() {
        // Create a standard charge amount
        Money amount = new Money(500);
        Instant incurred = Instant.now(); // Charge incurred at current time
        Instant expiration = incurred.plus(365, ChronoUnit.DAYS); // Expiration set 1 year later
        
        // Create a ParkingCharge instance for long-term parking
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", incurred, expiration);
        
        // Verify that the permit is still valid (not expired)
        assertFalse(charge.isPermitExpired(), "Permit should still be valid for long-term parking.");
    }
}
