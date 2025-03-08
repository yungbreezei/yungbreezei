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
}
