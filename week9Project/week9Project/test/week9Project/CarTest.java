package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class CarTest {

    private Car car;

    // Setup method to initialize the car object before each test
    @BeforeEach
    void setUp() {
        car = new Car("ABC-123", CarType.SUV, "C001", true, LocalDate.of(2025, 12, 31));
    }

    // Test case to verify the initialization of a car
    @Test
    void testCarInitialization() {
        assertEquals("ABC-123", car.getLicense(), "License should match");
        assertEquals(CarType.SUV, car.getType(), "Car type should match");
        assertEquals("C001", car.getOwner(), "Owner ID should match");
        assertTrue(car.hasValidParkingPass(), "Car should be active");
    }
 
    // Test case to verify the string representation of the car
    @Test
    void testToString() {
        String expected = "Car[License: ABC-123, Type: SUV, Owner ID: C001, Valid Pass: true, Permit Expiration: 2025-12-31]";
        assertEquals(expected, car.toString(), "Car string representation should match.");
    }

    // Test activation and de-activation of parking pass
    @Test
    void testSetActive() {
        car.setHasValidParkingPass(false);
        assertFalse(car.hasValidParkingPass(), "Parking pass should be inactive.");

        car.setHasValidParkingPass(true);
        assertTrue(car.hasValidParkingPass(), "Parking pass should be active.");
    }

    // Test case to verify updating license
    @Test
    void testSetLicense() {
        car.setLicense("XYZ-789");
        assertEquals("XYZ-789", car.getLicense(), "License should be updated correctly");
    }

    // Test case to verify updating owner ID
    @Test
    void testSetOwnerId() {
        car.setOwner("C002");
        assertEquals("C002", car.getOwner(), "Owner ID should be updated correctly");
    }
    
    // Test setting a permit and expiration
    @Test
    void testSetPermit() {
        LocalDate newExpiration = LocalDate.of(2025, 12, 31);
        car.setPermit("P123", newExpiration);

        assertEquals("P123", car.getPermit(), "Permit should be updated.");
        assertEquals(newExpiration, car.getPermitExpiration(), "Permit expiration should match.");
        assertTrue(car.hasValidParkingPass(), "Parking pass should be valid.");

        // Expired permit case
        LocalDate expiredDate = LocalDate.of(2023, 1, 1);
        car.setPermit("P456", expiredDate);
        assertFalse(car.hasValidParkingPass(), "Expired permit should invalidate parking pass.");
    }
    
    // Test clearing the permit
    @Test
    void testClearPermit() {
        // Initially set a permit
        car.setPermit("P123", LocalDate.of(2025, 12, 31));

        assertEquals("P123", car.getPermit(), "Permit should be set correctly.");
        assertTrue(car.hasValidParkingPass(), "Parking pass should be valid.");

        // Clear the permit
        car.clearPermit();

        assertNull(car.getPermit(), "Permit should be cleared.");
        assertFalse(car.hasValidParkingPass(), "Parking pass should be invalid after clearing permit.");
    }

    // Test when the permit has just expired (not valid anymore)
    @Test
    void testExpiredPermit() {
        LocalDate expiredDate = LocalDate.of(2023, 1, 1);
        car.setPermit("P789", expiredDate);

        assertEquals("P789", car.getPermit(), "Permit should be set correctly.");
        assertFalse(car.hasValidParkingPass(), "Parking pass should be invalid due to expired permit.");
    }

    // Test that the car's parking pass validity is updated correctly when expiration date changes
    @Test
    void testPermitExpirationUpdate() {
        // Set a permit with a future expiration date
        car.setPermit("P101", LocalDate.of(2025, 12, 31));
        assertTrue(car.hasValidParkingPass(), "Parking pass should be valid.");

        // Update the permit to an expired one
        car.setPermit("P101", LocalDate.of(2023, 1, 1));
        assertFalse(car.hasValidParkingPass(), "Parking pass should be invalid after expiration update.");
    }
}
