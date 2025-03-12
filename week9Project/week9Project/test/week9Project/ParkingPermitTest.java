package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class ParkingPermitTest {

    private ParkingPermit permit1;
    private ParkingPermit permit2;
    private ParkingPermit permit3;
    private ParkingPermit permitWithEmptyValues;
    private ParkingPermit permitWithLongId;
    private ParkingPermit activePermit;
    private ParkingPermit expiredPermit;

    @BeforeEach
    void setUp() {
        // Set up test permits with a specific expiration date
        LocalDate specificExpirationDate = LocalDate.of(2025, 12, 31);  // Specific expiration date: December 31, 2025

        permit1 = new ParkingPermit("P123", "ABC-123", specificExpirationDate); // Use specific expiration date
        permit2 = new ParkingPermit("P123", "ABC-123", specificExpirationDate); // Same expiration date as permit1
        permit3 = new ParkingPermit("P124", "XYZ-456", specificExpirationDate); // Same expiration date
        permitWithEmptyValues = new ParkingPermit("", "", specificExpirationDate); // Same expiration date
        permitWithLongId = new ParkingPermit("P12345678901234567890", "LONG-123", specificExpirationDate); // Same expiration date
        activePermit = new ParkingPermit("P123", "ABC-123", specificExpirationDate); // Active permit
        expiredPermit = new ParkingPermit("P124", "XYZ-456", LocalDate.now().minusDays(1));
    }


    // Test the constructor and getter methods
    @Test
    void testConstructorAndGetters() {
        assertEquals("P123", permit1.getId(), "The permit ID should match the provided ID.");
        assertEquals("ABC-123", permit1.getLicensePlate(), "The license plate should match the provided license plate.");
    }

    // Test equals method (two permits with the same permitId should be equal)
    @Test
    void testEqualsSamePermitId() {
        assertTrue(permit1.equals(permit2), "Permits with the same ID should be considered equal.");
    }

    // Test equals method (two permits with different permitId should not be equal)
    @Test
    void testEqualsDifferentPermitId() {
        assertFalse(permit1.equals(permit3), "Permits with different IDs should not be considered equal.");
    }

    // Test equals method with null
    @Test
    void testEqualsNull() {
        assertFalse(permit1.equals(null), "Permit should not be equal to null.");
    }

    // Test equals method with different object type
    @Test
    void testEqualsDifferentObjectType() {
        assertFalse(permit1.equals("Some String"), "Permit should not be equal to a different object type.");
    }

    // Test equals method with empty permit ID
    @Test
    void testEqualsEmptyPermitId() {
        ParkingPermit anotherEmptyPermit = new ParkingPermit("", "", LocalDate.now().plusDays(10));
        assertTrue(permitWithEmptyValues.equals(anotherEmptyPermit), "Permits with empty IDs should be equal.");
    }

    // Test equals method case sensitivity
    @Test
    void testEqualsCaseSensitivity() {
        ParkingPermit caseSensitivePermit = new ParkingPermit("p123", "ABC-123", LocalDate.now().plusDays(10));
        assertFalse(permit1.equals(caseSensitivePermit), "Permit ID comparison should be case-sensitive.");
    }

    // Test extremely long permit ID
    @Test
    void testLongPermitIdHandling() {
        assertEquals("P12345678901234567890", permitWithLongId.getId(), "Permit should handle long IDs correctly.");
    }

    // Test hashCode method (two equal permits should have the same hash code)
    @Test
    void testHashCode() {
        assertEquals(permit1.hashCode(), permit2.hashCode(), "Equal permits should have the same hash code.");
        assertNotEquals(permit1.hashCode(), permit3.hashCode(), "Different permits should have different hash codes.");
    }

    // Test toString method
    @Test
    void testToString() {
        String expected = "ParkingPermit[ID: P123, License Plate: ABC-123, Expiration Date: 2025-12-31]";
        assertEquals(expected, permit1.toString(), "toString should return correct string format.");
    }

    // Test permit ID mutation does not affect hash consistency (Important for HashMap usage)
    @Test
    void testPermitIdMutationDoesNotAffectHash() {
        ParkingPermit originalPermit = new ParkingPermit("ORIGINAL123", "DEF-456", LocalDate.now().plusDays(10));
        int originalHash = originalPermit.hashCode();

        ParkingPermit modifiedPermit = new ParkingPermit("ORIGINAL123", "NEW-PLATE", LocalDate.now().plusDays(10));
        assertEquals(originalHash, modifiedPermit.hashCode(), "Changing license plate should not change hash code.");
    }
    
    // Test getExpirationDate method
    @Test
    void testGetExpirationDate() {
        assertEquals(LocalDate.of(2025, 12, 31), activePermit.getExpirationDate(), "Expiration date should match expected value.");
    }

    // Test isExpired method for an active permit
    @Test
    void testIsExpiredActivePermit() {
        assertFalse(activePermit.isExpired(), "Permit should not be expired.");
    }

    // Test isExpired method for an expired permit
    @Test
    void testIsExpiredExpiredPermit() {
        assertTrue(expiredPermit.isExpired(), "Permit should be expired.");
    }
}
