package week9Project;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressTest {

    private Address fullAddress;
    private Address addressWithoutStreet2;
    private Address addressWithEmptyStreet2;

    @BeforeEach
    void setUp() {
        // Initialize Address objects for different scenarios
        fullAddress = new Address("123 Main St", "Apt 4B", "New York", "NY", "10001");
        addressWithoutStreet2 = new Address("456 Elm St", null, "Los Angeles", "CA", "90012");
        addressWithEmptyStreet2 = new Address("789 Pine St", "", "Chicago", "IL", "60610");
    }

    @Test
    void testGetAddressInfo() {
        String expected = "123 Main St, Apt 4B, New York, NY 10001";
        assertEquals(expected, fullAddress.getAddressInfo(), "Full address should match expected output.");
    }

    @Test
    void testGetAddressInfoWithoutStreet2() {
        String expected = "456 Elm St, Los Angeles, CA 90012";
        assertEquals(expected, addressWithoutStreet2.getAddressInfo(), "Address without street2 should exclude it.");
    }

    @Test
    void testGetAddressInfoWithEmptyStreet2() {
        String expected = "789 Pine St, Chicago, IL 60610";
        assertEquals(expected, addressWithEmptyStreet2.getAddressInfo(), "Address with empty street2 should exclude it.");
    }

    @Test
    void testToString() {
        assertEquals(fullAddress.getAddressInfo(), fullAddress.toString(), "toString() should match getAddressInfo().");
    }
    
    /*
     * Ensure that the Address class handles null values appropriately, 
     * especially for fields like street2, city, state, and zipcode.
     */
    @Test
    void testAddressWithNullFields() {
        // Attempting to create an address with null fields should trigger validation errors.
        assertThrows(IllegalArgumentException.class, () -> {
            new Address("123 Main St", "Apt 4B", "New York", null, "10001");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Address("123 Main St", "Apt 4B", "New York", "NY", null); // Invalid ZIP code (null)
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Address("123 Main St", "Apt 4B", "New York", null, "10001"); // Invalid state code
        });
    }


    /*
     * test how the Address class handles an invalid one.
     */
    @Test
    void testInvalidZipCode() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Address("123 Main St", "Apt 5A", "New York", "NY", "ABCDE");
        });
        assertEquals("Invalid ZIP code format. Must be 5 or 9 digits.", thrown.getMessage(), "Expected exception message for invalid ZIP code.");
    }

	/*
	 * test equality between two Address objects.
	 */
    @Test
    void testAddressEquality() {
        Address anotherFullAddress = new Address("123 Main St", "Apt 4B", "New York", "NY", "10001");
        assertEquals(fullAddress, anotherFullAddress, "Addresses with the same values should be equal.");
    }

	/*
	 * override the hashCode method. You can add a test to ensure consistency between equals and hashCode.
	 */
    @Test
    void testAddressHashCodeConsistency() {
        Address anotherFullAddress = new Address("123 Main St", "Apt 4B", "New York", "NY", "10001");
        assertEquals(fullAddress.hashCode(), anotherFullAddress.hashCode(), "Equal addresses should have the same hash code.");
    }

    /*
     * Ensure that street2 is correctly handled when it's passed as an empty string or null:
     */
    @Test
    void testBlankStreet2Handling() {
        Address addressWithBlankStreet2 = new Address("123 Oak St", "", "Los Angeles", "CA", "90001");
        String expected = "123 Oak St, Los Angeles, CA 90001";
        assertEquals(expected, addressWithBlankStreet2.getAddressInfo(), "Address with blank street2 should behave correctly.");
    }

}
