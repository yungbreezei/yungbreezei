package week9Project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import week9Project.exceptions.*;

import java.util.Calendar;
import java.util.Collection;

public class PermitManagerTest {

    private PermitManager permitManager;
    private Car car1, car2;
    private ParkingPermit permit1, permit2;

    @BeforeEach
    void setUp() {
        permitManager = new PermitManager();
        car1 = new Car("XYZ123", CarType.SUV, "SUV", false, null);
        car2 = new Car("ABC456", CarType.COMPACT, "Sedan", false, null);
    }

    @Test
    void testRegisterPermit() throws DuplicatePermitException {
        // Register a permit for car1
        permit1 = permitManager.register(car1);
        
        // Verify the permit is created
        assertNotNull(permit1, "Permit should be created.");
        assertEquals(car1, permit1.getCar(), "The permit should be for car1.");
    }

    @Test
    void testDuplicatePermitException() {
        // Register a permit for car1
        try {
            permit1 = permitManager.register(car1);
        } catch (DuplicatePermitException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Attempt to register the same car again, should throw DuplicatePermitException
        assertThrows(DuplicatePermitException.class, () -> permitManager.register(car1), 
            "DuplicatePermitException should be thrown for a car that already has a permit.");
    }

    @Test
    void testGetPermitIds() throws DuplicatePermitException {
        // Register permits for car1 and car2
        permit1 = permitManager.register(car1);
        permit2 = permitManager.register(car2);
        
        // Verify the permit IDs are returned correctly
        assertTrue(permitManager.getPermitIds().contains(permit1.getId()), "Permit ID for car1 should be in the list.");
        assertTrue(permitManager.getPermitIds().contains(permit2.getId()), "Permit ID for car2 should be in the list.");
    }

    @Test
    void testGetPermitById() throws DuplicatePermitException, PermitNotFoundException, PermitExpiredException {
        // Register permit for car1
        permit1 = permitManager.register(car1);
        
        // Retrieve the permit by ID and verify it's correct
        ParkingPermit retrievedPermit = permitManager.getPermitById(permit1.getId());
        assertEquals(permit1, retrievedPermit, "The retrieved permit should match the registered permit.");
    }

    @Test
    void testGetPermitByIdNotFound() {
        // Test retrieving a permit that doesn't exist
        assertThrows(PermitNotFoundException.class, () -> permitManager.getPermitById("P9999"),
            "PermitNotFoundException should be thrown for a non-existent permit ID.");
    }

    @Test
    void testGetPermitByIdExpired() throws DuplicatePermitException {
        // Register a permit for car1
        permit1 = permitManager.register(car1);
        
        // Expire the permit manually for testing (assuming there's a method to expire it)
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.YEAR, -1); // Set to the past
        permit1.setExpirationDate(expirationDate);
        
        // Test retrieving the expired permit
        assertThrows(PermitExpiredException.class, () -> permitManager.getPermitById(permit1.getId()),
            "PermitExpiredException should be thrown for an expired permit.");
    }

    @Test
    void testGetPermitIdsByCustomer() throws DuplicatePermitException {
        // Initialize customer object
        Customer customer = new Customer("C001", "John Doe", new Address
        		("123 Main St", "Springfield", "IL", "62701"), "123-456-7890");

        // Register permits for car1 and car2
        permit1 = permitManager.register(car1);
        permit2 = permitManager.register(car2);
        
        // Assuming the car has the customer set correctly
        permit1.getCar().setOwner(customer.getCustomerId());  // Set customer ID for car1
        permit2.getCar().setOwner(customer.getCustomerId());  // Set customer ID for car2

        // Retrieve permit IDs by customer
        assertTrue(permitManager.getPermitIds(customer).contains(permit1.getId()), 
            "Permit ID for car1 should be associated with the customer.");
        assertTrue(permitManager.getPermitIds(customer).contains(permit2.getId()), 
            "Permit ID for car2 should be associated with the customer.");
    }
    
    /*
     *  test checks if the PermitManager correctly throws a DuplicatePermitException 
     *  when trying to register a permit for the same car.
     */
    @Test
    void testRegisterDuplicatePermit() {
        // Register a permit for car1
        try {
            permitManager.register(car1); // First registration
        } catch (DuplicatePermitException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Try registering the same car again, should throw exception
        assertThrows(DuplicatePermitException.class, () -> permitManager.register(car1), 
            "Duplicate permit should throw DuplicatePermitException.");
    }
    /*
     * test checks if the getPermitById() method correctly throws a PermitExpiredException
     *  when trying to get an expired permit
     */
    @Test
    void testGetExpiredPermit() throws DuplicatePermitException {
        // Register a permit for car1
        ParkingPermit permit = permitManager.register(car1);
        
        // Manually set the expiration date to the past (simulate an expired permit)
        Calendar pastDate = Calendar.getInstance();
        pastDate.add(Calendar.DAY_OF_YEAR, -1);  // Set to 1 day in the past
        permit.setExpirationDate(pastDate);
        
        // Try to get the expired permit and assert that it throws PermitExpiredException
        assertThrows(PermitExpiredException.class, () -> permitManager.getPermitById(permit.getId()),
            "Expired permit should throw PermitExpiredException.");
    }
    /*
     * test checks if the getPermitIds() method correctly 
     * retrieves all permit IDs in the system.
     */
    @Test
    void testGetAllPermitIds() throws DuplicatePermitException {
        // Register permits for car1 and car2
        ParkingPermit permit1 = permitManager.register(car1);
        ParkingPermit permit2 = permitManager.register(car2);

        // Retrieve all permit IDs
        Collection<String> permitIds = permitManager.getPermitIds();

        // Assert that both permit IDs are returned
        assertTrue(permitIds.contains(permit1.getId()), "Permit ID for car1 should be in the list.");
        assertTrue(permitIds.contains(permit2.getId()), "Permit ID for car2 should be in the list.");
    }
	/*
	 * test checks if the getPermitIds(Customer customer) method 
	 * works when the customer has no permits.
	 */
    @Test
    void testGetPermitIdsByCustomerNoPermits() throws DuplicatePermitException {
        // Initialize a new customer
        Customer customer = new Customer("C001", "John Doe", new Address("123 Main St", "Springfield", "IL", "62701"), "123-456-7890");

        // Retrieve permit IDs for the customer (should be empty since no permits are registered)
        Collection<String> permitIds = permitManager.getPermitIds(customer);

        // Assert that no permits are returned
        assertTrue(permitIds.isEmpty(), "No permits should be returned for a customer with no permits.");
    }
    
	/*
	 * Test checks if the expiration date is correctly set when registering a permit.
	 */
    @Test
    void testRegisterPermitExpiration() throws DuplicatePermitException {
        // Register a permit for car1
        ParkingPermit permit = permitManager.register(car1);
        
        // Get the expiration date
        Calendar expirationDate = permit.getExpirationDate();
        
        // Assert that the expiration date is one year from the current date
        Calendar expectedExpirationDate = Calendar.getInstance();
        expectedExpirationDate.add(Calendar.YEAR, 1);
        
        assertEquals(expectedExpirationDate.get(Calendar.YEAR), expirationDate.get(Calendar.YEAR), "The expiration year should be one year from now.");
        assertEquals(expectedExpirationDate.get(Calendar.MONTH), expirationDate.get(Calendar.MONTH), "The expiration month should match.");
        assertEquals(expectedExpirationDate.get(Calendar.DAY_OF_MONTH), expirationDate.get(Calendar.DAY_OF_MONTH), "The expiration day should match.");
    }
	/*
	 * test ensures that the PermitManager can handle a customer 
	 * with multiple permits for different cars.
	 */
    @Test
    void testCustomerWithMultiplePermits() throws DuplicatePermitException {
        // Initialize customer object
        Customer customer = new Customer("C001", "John Doe", new Address("123 Main St", "Springfield", "IL", "62701"), "123-456-7890");

        // Register permits for multiple cars
        ParkingPermit permit1 = permitManager.register(car1);
        ParkingPermit permit2 = permitManager.register(car2);
        
        // Set owner to the same customer
        permit1.getCar().setOwner(customer.getCustomerId());
        permit2.getCar().setOwner(customer.getCustomerId());
        
        // Retrieve permit IDs for the customer
        Collection<String> permitIds = permitManager.getPermitIds(customer);

        // Assert that both permits are associated with the customer
        assertTrue(permitIds.contains(permit1.getId()), "Permit ID for car1 should be associated with the customer.");
        assertTrue(permitIds.contains(permit2.getId()), "Permit ID for car2 should be associated with the customer.");
    }

    
}
