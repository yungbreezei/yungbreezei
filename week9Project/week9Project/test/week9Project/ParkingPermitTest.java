package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkingPermitTest {

    private ParkingPermit permit1;
    private ParkingPermit permit2;
    private ParkingPermit permit3;

    @BeforeEach
    void setUp() {
        // Set up test permits
        permit1 = new ParkingPermit("P123", "ABC-123");
        permit2 = new ParkingPermit("P123", "ABC-123"); // Same permitId and license plate as permit1
        permit3 = new ParkingPermit("P124", "XYZ-456"); // Different permitId and license plate
    }

    // Test the constructor and getter methods
    @Test
    void testConstructorAndGetters() {
        assertEquals("P123", permit1.getId(), "The permit ID should match the provided ID.");
        assertEquals("ABC-123", permit1.getLicensePlate(), "The license plate should match the provided license plate.");
    }

    // Test equals method (two permits with the same permitId should be equal)
    @Test
    void testEquals_SamePermitId() {
        assertTrue(permit1.equals(permit2), "The permits with the same ID should be considered equal.");
    }

    // Test equals method (two permits with different permitId should not be equal)
    @Test
    void testEquals_DifferentPermitId() {
        assertFalse(permit1.equals(permit3), "The permits with different IDs should not be considered equal.");
    }

    // Test hashCode method (two equal permits should have the same hash code)
    @Test
    void testHashCode() {
        assertEquals(permit1.hashCode(), permit2.hashCode(), "The hash codes for equal permits should be the same.");
        assertNotEquals(permit1.hashCode(), permit3.hashCode(), "The hash codes for different permits should be different.");
    }

    // Test toString method
    @Test
    void testToString() {
        String expected = "ParkingPermit[ID: P123, License Plate: ABC-123]";
        assertEquals(expected, permit1.toString(), "The toString method should return the correct string representation.");
    }
}
