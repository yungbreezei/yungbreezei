package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

class ParkingPermitTest {

    private ParkingPermit permit1;
    private ParkingPermit permit2;
    private ParkingPermit permit3;
    private ParkingPermit permitWithEmptyValues;
    private ParkingPermit activePermit;
    private ParkingPermit expiredPermit;
    
    private Car car;  // Assumed you have a Car class for this
    private Calendar expirationDate;
    private Calendar registrationDate;


    @BeforeEach
    void setUp() {
        // Set up a car object for testing
        car = new Car("ABC-123", CarType.COMPACT, "C001", false, null);

        // Set up expiration and registration dates
        expirationDate = Calendar.getInstance();
        expirationDate.set(2025, Calendar.DECEMBER, 31, 23, 59, 59);  // Set to specific expiration date: December 31, 2025

        registrationDate = Calendar.getInstance();
        registrationDate.set(2025, Calendar.MARCH, 14, 0, 0, 0);  // Set to specific date: March 14, 2025
        
        activePermit = new ParkingPermit("P123", null, registrationDate, expirationDate);

        // Initialize ParkingPermits
        permit1 = new ParkingPermit("P123", car, expirationDate, registrationDate);
        permit2 = new ParkingPermit("P123", car, expirationDate, registrationDate); // Same details as permit1
        permit3 = new ParkingPermit("P124", car, expirationDate, registrationDate); // Different permitId
        permitWithEmptyValues = new ParkingPermit("", car, expirationDate, registrationDate); // Empty permitId
        activePermit = new ParkingPermit("P125", car, expirationDate, registrationDate); // Active permit
        expiredPermit = new ParkingPermit("P124", null, registrationDate, expirationDate); // Expired permit (current date)
    }

    // Test the constructor and getter methods
    @Test
    void testConstructorAndGetters() {
        assertEquals("P123", permit1.getId(), "The permit ID should match the provided ID.");
        assertEquals(car, permit1.getCar(), "The car should match the provided car.");
        assertEquals(expirationDate.getTime(), permit1.getExpirationDate().getTime(), "The expiration date should match.");
        assertEquals(registrationDate.getTime(), permit1.getRegistrationDate().getTime(), "The registration date should match.");
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
        ParkingPermit anotherEmptyPermit = new ParkingPermit("", car, expirationDate, registrationDate);
        assertTrue(permitWithEmptyValues.equals(anotherEmptyPermit), "Permits with empty IDs should be equal.");
    }
    // Test equals method case sensitivity
    @Test
    void testEqualsCaseSensitivity() {
        ParkingPermit caseSensitivePermit = new ParkingPermit("p123", car, expirationDate, registrationDate);
        assertFalse(permit1.equals(caseSensitivePermit), "Permit ID comparison should be case-sensitive.");
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
        String expected = "ParkingPermit[ID: P123, License Plate: ABC-123, Expiration Date: " 
        		+ expirationDate.getTime() + ", Registration Date: " + registrationDate.getTime() + "]";
        assertEquals(expected, permit1.toString(), "toString should return correct string format.");
    }

    
    // Test permit ID mutation does not affect hash consistency (Important for HashMap usage)
    @Test
    void testPermitIdMutationDoesNotAffectHash() {
        ParkingPermit originalPermit = new ParkingPermit("ORIGINAL123", car, expirationDate, registrationDate);
        int originalHash = originalPermit.hashCode();

        ParkingPermit modifiedPermit = new ParkingPermit("ORIGINAL123", car, expirationDate, registrationDate);
        assertEquals(originalHash, modifiedPermit.hashCode(), "Changing license plate should not change hash code.");
    }
    
    // Test getExpirationDate method
    @Test
    void testGetExpirationDate() {
        // Extract only the date part (year, month, day) for comparison
        Date expectedDate = expirationDate.getTime();
        Date actualDate = activePermit.getExpirationDate().getTime();

        assertEquals(expectedDate, actualDate, "Expiration date should match expected value.");
    }

    // Test getRegistrationDate method
    @Test
    void testGetRegistrationDate() {
        Date expectedDate = registrationDate.getTime();
        Date actualDate = activePermit.getRegistrationDate().getTime();

        assertEquals(expectedDate, actualDate, "Registration date should match expected value.");
    }

    // Test isExpired method
    @Test
    void testIsExpired() {
        Calendar today = Calendar.getInstance();
        boolean isExpired = expirationDate.before(today);  // Compare expirationDate with today's date
        assertEquals(isExpired, activePermit.isExpired(), "Expiration status should be correct.");
    }

    // Test isExpired method for an active permit
    @Test
    void testIsExpiredActivePermit() {
        assertFalse(activePermit.isExpired(), "Active permit should not be expired.");
    }

    // Test isExpired method for an expired permit
    @Test
    void testIsExpiredExpiredPermit() {
        assertTrue(expiredPermit.isExpired(), "Expired permit should be expired.");
    }
    /*
     * ensures the permit ID has a maximum length if there is a business rule restricting it 
     * (ex, no more than 20 characters).
     */
    @Test
    void testPermitIdMaxLength() {
        String longId = "P123456789012345678901";  // 21 characters
        assertThrows(IllegalArgumentException.class, () -> new ParkingPermit(longId, car, expirationDate, registrationDate), "Permit ID should not exceed 20 characters.");
    }
    
    // Test setRegistrationDate method
    @Test
    void testSetRegistrationDate() {
        Calendar newDate = Calendar.getInstance();
        newDate.add(Calendar.DAY_OF_YEAR, 5); // New registration date set to 5 days from now
        permit1.setRegistrationDate(newDate);
        assertEquals(newDate.getTime(), permit1.getRegistrationDate().getTime(), "Registration date should be updated.");
    }

    // Test IllegalArgumentException for null registration date
    @Test
    void testSetRegistrationDateNull() {
        assertThrows(IllegalArgumentException.class, () -> permit1.setRegistrationDate(null), "Registration date cannot be null.");
    }
}
