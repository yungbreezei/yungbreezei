package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

class ParkingOfficeTest {

    private ParkingOffice<Car> office;  // Parking office instance
    private Customer customer;          // Customer instance for testing
    private Address address;            // Customer's address for testing
    private Car car;                    // Car instance for testing
    private ParkingPermit activePermit;
    private ParkingPermit expiredPermit;

    // This method runs before each test to set up the test data
    @BeforeEach
    void setUp() {
        office = new ParkingOffice<>("Downtown Office");  // Initialize parking office
        address = new Address("123 Main St", "City", "State", "12345");  // Create a sample address
        customer = new Customer("C123", "John Doe", address, "123-456-7890");  // Create a sample customer
        // Register the customer in the parking office
        office.registerCustomer(customer.getCustomerId(), customer.getName(), customer.getAddress(), customer.getPhoneNumber());
        activePermit = new ParkingPermit("P123", "ABC-123", LocalDate.now().plusDays(10)); // Valid for 10 more days
        expiredPermit = new ParkingPermit("P124", "XYZ-456", LocalDate.now().minusDays(1)); // Expired yesterday
    }

    // Test case for registering a car for a customer
    @Test
    void testRegisterCar() {
        // Register a car for the customer
        Car car = office.registerCar(customer.getCustomerId(), "XYZ123", CarType.COMPACT);
        
        // Verify that the car was registered successfully
        assertNotNull(car, "Car should be successfully registered.");
        assertEquals("XYZ123", car.getLicense(), "License plate should match.");
        assertEquals(CarType.COMPACT, car.getType(), "Car type should be SEDAN.");
    }

    // Test case for entering a car into parking
    @Test
    void testEnterParking() throws Exception {
        // Create a parking lot with space for 10 cars
        ParkingLot lot = new ParkingLot("Lot1", address, 10, 0, false);
        office.addParkingLot(lot);  // Add the parking lot to the office

        // Register a new car for the customer
        car = office.registerCar(customer.getCustomerId(), "ABC987", CarType.SUV);

        // Simulate the car entering the parking lot without a permit in the test (adjust as needed)
        ParkingTransaction transaction = office.enterParking(car, activePermit);

        // Verify that a parking transaction was created
        assertNotNull(transaction, "Transaction should be created when car enters parking.");
        
        // Assert that the parking lot has the car
        assertTrue(transaction.getParkingLot().parkedCars.size() > 0, "Parking lot should have a car now.");
        assertTrue(transaction.getParkingLot().parkedCars.contains(car), "Car should be in the parking lot.");
    }

    // Test case for exiting a car from parking
    @Test
    void testExitParking() throws Exception {
        // Create a parking lot with space for 10 cars
        ParkingLot lot = new ParkingLot("Lot1", address, 10, 0, false);
        office.addParkingLot(lot);  // Add the parking lot to the office

        // Register a new car for the customer
        car = office.registerCar(customer.getCustomerId(), "DEF456", CarType.COMPACT);

        // Simulate the car entering the parking lot
        ParkingTransaction transaction = office.enterParking(car, activePermit);

        // Simulate the car exiting the parking lot
        office.exitParking(car);

        // Verify that the parking transaction for this car is no longer present in the lot
        assertNull(lot.getTransaction(car), "Transaction should be null after car exits.");
    }

    // Test case for attempting to register a car for a non-existing customer
    @Test
    void testRegisterCarForNonExistingCustomer() {
        // Attempting to register a car for a non-existing customer should throw an exception
        assertThrows(IllegalArgumentException.class, () -> {
            office.registerCar("NonExistentCustomer", "ZZZ999", CarType.SUV);
        }, "Should throw IllegalArgumentException for non-existing customer.");
    }

    // Test case for when there is no parking space available
    @Test
    void testNoParkingSpaceAvailable() {
        // Add a parking lot with 0 available spaces (or simulate full capacity)
        ParkingLot lot = new ParkingLot("Lot1", address, 0, 0, false); // Adjust this to simulate a full lot
        office.addParkingLot(lot);
        
        // Register a new car
        car = office.registerCar(customer.getCustomerId(), "ABC987", CarType.SUV);

        // Test that the exception is thrown when trying to enter parking
        Exception exception = assertThrows(Exception.class, () -> {
            office.enterParking(car, activePermit);
        });
        
        // Verify the exception message is what you expect
        assertEquals("No available parking lot.", exception.getMessage());
    }
    
    @Test
    void testGetPermitIdsNoPermits() {
        // Customer has no permits
        Set<String> permitIds = office.getPermitIds(customer);
        assertTrue(permitIds.isEmpty(), "Permit IDs should be empty if the customer has no permits.");
    }
    @Test
    void testGetPermitIdsMultiplePermits() {
        // Customer has multiple permits
        car = office.registerCar(customer.getCustomerId(), "XYZ123", CarType.COMPACT);
        LocalDate permitExpiration = LocalDate.of(2025, 12, 31);
        customer.addPermit(new ParkingPermit("P123", null, permitExpiration));
        customer.addPermit(new ParkingPermit("P124", null, permitExpiration));
        
        Set<String> permitIds = office.getPermitIds(customer);
        assertEquals(2, permitIds.size(), "Customer should have two permits.");
        assertTrue(permitIds.contains("P123"), "Permit ID P123 should be included.");
        assertTrue(permitIds.contains("P124"), "Permit ID P124 should be included.");
    }
    
    @Test
    void testGetPermitIdsExpiredPermits() {
        // Customer has expired permits
        car = office.registerCar(customer.getCustomerId(), "XYZ123", CarType.COMPACT);
        LocalDate expiredDate = LocalDate.of(2020, 12, 31);
        customer.addPermit(new ParkingPermit("P125", null, expiredDate));

        Set<String> permitIds = office.getPermitIds(customer);
        assertEquals(1, permitIds.size(), "Customer should have one permit.");
        assertTrue(permitIds.contains("P125"), "Expired permit ID P125 should still be included.");
    }
}
