package week9Project;

import static org.junit.jupiter.api.Assertions.*;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import week9Project.exceptions.InvalidParkingPassException;
import week9Project.exceptions.ParkingLotFullException;

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
    
    @Test
    public void testParkingDurationCutoff() throws InvalidParkingPassException, ParkingLotFullException {
        // Setup a car and parking lot
        Car car = new Car("123ABC", CarType.COMPACT, "customer1", true, null);
        ParkingLot lot = new ParkingLot("Lot 1", null, 10, 0, false);  // Assume hourly rate is 10 cents per minute
        ParkingTransaction transaction = lot.entry(car);

        // Park for 23.9 hours
        transaction.getExitTime(LocalDateTime.now().plusHours(23).plusMinutes(54));
        double expectedChargeBefore24 = 23.9 * lot.getHourlyRate();
        assertEquals(expectedChargeBefore24, transaction.getCharge(), 0.1, "Charge should be for 23.9 hours");

        // Park for 24.1 hours (just after 24 hours)
        transaction.getExitTime(LocalDateTime.now().plusHours(24).plusMinutes(6));
        double expectedChargeAfter24 = 24.1 * lot.getHourlyRate();
        assertEquals(expectedChargeAfter24, transaction.getCharge(), 0.1, "Charge should be for 24.1 hours");
    }
    
    @Test
    void testInvalidPermitExpiration() {
        // Setup an expired parking charge
        Instant expiredTime = Instant.now().minus(1, ChronoUnit.DAYS); // 1 day ago
        Instant invalidExpiration = expiredTime.minus(1, ChronoUnit.HOURS); // Expired permit

        Money amount = new Money(500); // 500 cents = $5.00
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", expiredTime, invalidExpiration);

        assertTrue(charge.isPermitExpired(), "Expired parking permit should return true.");
    }
    
    @Test
    void testDurationBoundary() throws InvalidParkingPassException, ParkingLotFullException {
        // Setup for a ParkingLot with an hourly rate of 10 cents per minute
        ParkingLot lot = new ParkingLot("Lot 1", new Address(null, null, null, null), 10, 10.0, false);
        Car car = new Car("XYZ123", CarType.COMPACT, "C001", true, LocalDate.of(2025, 5, 20));

        // Entry of the car
        ParkingTransaction transaction = lot.entry(car);

        // Park the car for 23.9 hours (23 hours 54 minutes)
        transaction.getExitTime(LocalDateTime.now().plusHours(23).plusMinutes(54));
        double expectedChargeBefore24 = 23.9 * lot.getHourlyRate(); 
        assertEquals(expectedChargeBefore24, transaction.getCharge(), 0.1, "Charge should match for 23.9 hours");

        // Park for 24.1 hours (24 hours 6 minutes)
        transaction.getExitTime(LocalDateTime.now().plusHours(24).plusMinutes(6));
        double expectedChargeAfter24 = 24.1 * lot.getHourlyRate();
        assertEquals(expectedChargeAfter24, transaction.getCharge(), 0.1, "Charge should match for 24.1 hours");
    }
    
    @Test
    void testMultiDayCharge() throws InvalidParkingPassException, ParkingLotFullException {
        // Set up a parking lot with a daily rate
        ParkingLot lot = new ParkingLot("Lot 2", new Address(null, null, null, null), 10, 10.0, true);
        Car car = new Car("LMN123", CarType.COMPACT, "C002", true, LocalDate.of(2025, 5, 20));

        // Entry of the car
        ParkingTransaction transaction = lot.entry(car);

        // Park the car for 2 full days (48 hours)
        transaction.getExitTime(LocalDateTime.now().plusDays(2)); 
        double expectedChargeForTwoDays = 2 * 24 * lot.getHourlyRate();
        assertEquals(expectedChargeForTwoDays, transaction.getCharge(), 0.1, "Charge should match for two full days");
    }

    @Test
    void testLongDurationCharge() throws InvalidParkingPassException, ParkingLotFullException {
        // Set up a parking lot with a daily rate
        ParkingLot lot = new ParkingLot("Lot 3", new Address(null, null, null, null), 10, 10.0, true);
        Car car = new Car("OPQ123", CarType.SUV, "C003", true, LocalDate.of(2025, 6, 20));

        // Entry of the car
        ParkingTransaction transaction = lot.entry(car);

        // Park the car for 5 days
        transaction.getExitTime(LocalDateTime.now().plusDays(5)); 
        double expectedChargeForFiveDays = 5 * 24 * lot.getHourlyRate();
        assertEquals(expectedChargeForFiveDays, transaction.getCharge(), 0.1, "Charge should match for five full days");
    }


}
