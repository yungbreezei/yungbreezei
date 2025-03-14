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
        // Verifying the initial values after creating the car
        assertEquals("ABC-123", car.getLicensePlate(), "License should match");
        assertEquals(CarType.SUV, car.getType(), "Car type should match");
        assertEquals("C001", car.getOwner(), "Owner ID should match");
        assertTrue(car.hasValidParkingPass(), "Car should be active (valid parking pass).");
    }
 
    // Test case to verify the string representation of the car
    @Test
    void testToString() {
        // Verifying the expected string representation of the car
        String expected = "Car[License: ABC-123, Type: SUV, Owner ID: C001, Valid Pass: true, Permit Expiration: 2025-12-31]";
        assertEquals(expected, car.toString(), "Car string representation should match.");
    }

 // Test activation and de-activation of parking pass
    @Test
    void testSetActive() {
        // Set to false (no null value)
        car.setHasValidParkingPass(false);
        assertFalse(car.hasValidParkingPass(), "Parking pass should be inactive.");

        // Set to true
        car.setHasValidParkingPass(true);
        assertTrue(car.hasValidParkingPass(), "Parking pass should be active.");
    }

    // Test case to verify updating the license plate
    @Test
    void testSetLicense() {
        // Update license plate and verify the change
        car.setLicense("XYZ-789");
        assertEquals("XYZ-789", car.getLicensePlate(), "License plate should be updated correctly.");
    }

    // Test case to verify updating the owner ID
    @Test
    void testSetOwnerId() {
        // Update owner ID and verify the change
        car.setOwner("C002");
        assertEquals("C002", car.getOwner(), "Owner ID should be updated correctly.");
    }
    
    // Test setting a permit and expiration
    @Test
    void testSetPermit() {
        // Set a valid permit with a future expiration date
        LocalDate newExpiration = LocalDate.of(2025, 12, 31);
        car.setPermit("P123", newExpiration);

        // Verify that the permit and expiration date are set correctly
        assertEquals("P123", car.getPermit(), "Permit should be updated.");
        assertEquals(newExpiration, car.getPermitExpiration(), "Permit expiration should match.");
        assertTrue(car.hasValidParkingPass(), "Parking pass should be valid after setting the permit.");

        // Now test for an expired permit and ensure it throws an exception
        LocalDate expiredDate = LocalDate.of(2023, 1, 1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            car.setPermit("P456", expiredDate); // This should throw an exception
        }, "An expired permit should throw an exception.");
    }

    // Test clearing the permit
    @Test
    void testClearPermit() {
        // Initially set a permit
        car.setPermit("P123", LocalDate.of(2025, 12, 31));

        // Verify that the permit is set and the parking pass is valid
        assertEquals("P123", car.getPermit(), "Permit should be set correctly.");
        assertTrue(car.hasValidParkingPass(), "Parking pass should be valid.");

        // Clear the permit and verify the changes
        car.clearPermit();
        assertNull(car.getPermit(), "Permit should be cleared.");
        assertFalse(car.hasValidParkingPass(), "Parking pass should be invalid after clearing the permit.");
    }

    // Test when the permit has just expired (not valid anymore)
    @Test
    void testExpiredPermit() {
        // Set an expiration date to a future date
        LocalDate futureDate = LocalDate.now().plusDays(1);  // Just one day in the future
        car.setPermit("P789", futureDate);

        // Verify the permit and expiration date are set correctly
        assertEquals("P789", car.getPermit(), "Permit should be set correctly.");
        assertTrue(car.hasValidParkingPass(), "Parking pass should be valid due to future expiration date.");

        // Now set an expired permit (for further tests)
        LocalDate expiredDate = LocalDate.now().minusDays(1);  // One day in the past
        assertThrows(IllegalArgumentException.class, () -> {
            car.setPermit("P789", expiredDate); // This should throw an exception
        }, "Expiration date must be in the future.");
    }

    // Test that the car's parking pass validity is updated correctly when expiration date changes
    @Test
    void testPermitExpirationUpdate() {
        // Set a permit with a future expiration date and verify validity
        car.setPermit("P101", LocalDate.of(2025, 12, 31));
        assertTrue(car.hasValidParkingPass(), "Parking pass should be valid with a future expiration.");

        // Update the permit to a new future expiration date and verify validity
        car.setPermit("P101", LocalDate.of(2026, 1, 1)); // Ensure this date is also in the future
        assertTrue(car.hasValidParkingPass(), "Parking pass should still be valid after expiration update.");

        // Set an expired permit
        // The `setPermit` method should throw an IllegalArgumentException for expired dates
        assertThrows(IllegalArgumentException.class, () -> {
            car.setPermit("P101", LocalDate.of(2023, 1, 1)); // This date is in the past, should throw exception
        }, "Setting a permit with an expired date should throw an IllegalArgumentException.");

        // After exception, the car should still have a valid parking pass as the permit was not updated
        assertTrue(car.hasValidParkingPass(), "Parking pass should remain valid after trying to set an expired date.");
    }

    /*
     * Test: Ensure invalid license plate throws an exception
     */
    @Test
    void testInvalidLicensePlate() {
        assertThrows(IllegalArgumentException.class, () -> {
            car.setLicense("xyz-7819"); // Invalid license plate format
        }, "An invalid license plate should throw an exception.");
    }

    /*
     * Test: Ensure invalid state changes when parking pass is set
     */
    @Test
    void testValidParkingPassState() {
        // Test setting valid parking pass state (true or false)
        car.setHasValidParkingPass(true);
        assertTrue(car.hasValidParkingPass(), "Parking pass should be active when set to true.");

        car.setHasValidParkingPass(false);
        assertFalse(car.hasValidParkingPass(), "Parking pass should be inactive when set to false.");
    }
}
