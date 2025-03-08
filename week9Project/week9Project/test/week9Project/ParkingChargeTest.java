package week9Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class ParkingChargeTest {

    // Test the constructor and getter methods
    @Test
    void testConstructorAndGetters() {
        // Setup data for testing
        Money amount = new Money(500); // 500 cents = 5.00 dollars
        String permitId = "P123";
        String lotId = "L001";
        Instant incurred = Instant.now();
        Instant expirationDate = incurred.plus(1, ChronoUnit.DAYS); // 1 day later

        // Create a new ParkingCharge object
        ParkingCharge charge = new ParkingCharge(amount, permitId, lotId, incurred, expirationDate);

        // Assert that the getters return the expected values
        assertEquals(amount, charge.getAmount(), "The amount should match the provided amount.");
        assertEquals(permitId, charge.getPermitId(), "The permitId should match the provided permitId.");
        assertEquals(lotId, charge.getLotId(), "The lotId should match the provided lotId.");
        assertEquals(incurred, charge.getIncurred(), "The incurred time should match the provided incurred time.");
    }

    // Test the isPermitExpired method (when the permit is expired)
    @Test
    void testIsPermitExpired_Expired() {
        // Setup data for testing
        Instant expiredTime = Instant.now().minus(1, ChronoUnit.DAYS); // 1 day ago
        Instant validExpiration = expiredTime.minus(1, ChronoUnit.MINUTES); // Already expired
        Money amount = new Money(500); // 500 cents = 5.00 dollars
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", expiredTime, validExpiration);

        // Assert that the permit is expired
        assertTrue(charge.isPermitExpired(), "The permit should be expired.");
    }

    // Test the isPermitExpired method (when the permit is not expired)
    @Test
    void testIsPermitExpired_NotExpired() {
        // Setup data for testing
        Instant validTime = Instant.now();
        Instant validExpiration = validTime.plus(1, ChronoUnit.DAYS); // 1 day later
        Money amount = new Money(500); // 500 cents = 5.00 dollars
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", validTime, validExpiration);

        // Assert that the permit is not expired
        assertFalse(charge.isPermitExpired(), "The permit should not be expired.");
    }

    // Test the toString method for proper formatting
    @Test
    void testToString() {
        Money amount = new Money(500); // 500 cents = 5.00 dollars
        String permitId = "P123";
        String lotId = "L001";
        Instant incurred = Instant.now();
        Instant expirationDate = incurred.plus(1, ChronoUnit.DAYS); // 1 day later

        ParkingCharge charge = new ParkingCharge(amount, permitId, lotId, incurred, expirationDate);

        String expectedString = "ParkingCharge{permitId='P123', amount=$5.00, lotId='L001'}";
        assertEquals(expectedString, charge.toString(), "The toString method should return the correct string representation.");
    }
}
