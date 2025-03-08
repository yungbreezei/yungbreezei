package week9Project;

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
}
