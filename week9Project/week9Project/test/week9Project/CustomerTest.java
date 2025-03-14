package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        Address address = new Address("123 Main St", "New York", "NY", "10001");
        customer = new Customer("C001", "John Doe", address, "123-456-7890"); // Correct initialization
    }
    
    // Verify that a car is successfully registered to the customer
    @Test
    void testRegisterCar() {
        Car car = customer.registerCar("XYZ-123", CarType.COMPACT);

        assertNotNull(car, "Car should not be null after registration");
        assertEquals(1, customer.getCars().size(), "Customer should have one registered car");
        assertEquals("XYZ-123", car.getLicensePlate(), "License plate should match");
        assertEquals(CarType.COMPACT, car.getType(), "Car type should match");
        assertTrue(customer.getCars().contains(car), "Car should be linked to the customer");
    }

    @Test
    void testAddCharge() {
        customer.addCharge(50.0);
        assertEquals(50.0, customer.getBalance(), 0.001, "Balance should be updated correctly");

        customer.addCharge(25.0);
        assertEquals(75.0, customer.getBalance(), 0.001, "Balance should accumulate correctly");
    }

    // Ensure that registering multiple cars works as expected
    @Test
    void testRegisterMultipleCars() {
        customer.registerCar("XYZ-123", CarType.COMPACT);
        customer.registerCar("ABC-456", CarType.SUV);

        assertEquals(2, customer.getCars().size(), "Customer should have two cars registered");
    }

    // Verify that the toString method outputs the correct customer information
    @Test
    void testToString() {
        String output = customer.toString();
        assertTrue(output.contains("John Doe"), "Output should contain the customer name");
        assertTrue(output.contains("C001"), "Output should contain the customer ID");
    }
    /*
     *  test what happens if a customer tries to register a car with an invalid license plate 
     *  (ex, null or empty).
     */
    @Test
    void testRegisterCarWithInvalidLicensePlate() {
        assertThrows(IllegalArgumentException.class, () -> customer.registerCar("", CarType.COMPACT), 
                     "An empty license plate should throw an IllegalArgumentException.");
        assertThrows(IllegalArgumentException.class, () -> customer.registerCar(null, CarType.COMPACT), 
                     "A null license plate should throw an IllegalArgumentException.");
    }
    
    /*
     * test that a customer with no cars does not break any methods and 
     * ensure the getCars() method returns an empty list.
     */
    @Test
    void testCustomerWithNoCars() {
        Customer newCustomer = new Customer("C002", "Jane Smith", new Address("456 Elm St", "Los Angeles", "CA", "90001"), "987-654-3210");
        
        assertTrue(newCustomer.getCars().isEmpty(), "New customer should have no registered cars.");
    }
    /*/
     * Add multiple charges and check if the balance accumulates as expected. 
     * This will ensure the logic for adding charges works properly.
     */
    @Test
    void testBalanceAfterMultipleCharges() {
        customer.addCharge(20.0);
        customer.addCharge(50.0);
        customer.addCharge(30.0);
        
        assertEquals(100.0, customer.getBalance(), 0.001, "Balance should correctly accumulate after multiple charges.");
    }
    
    /*
     * Test the limit to how many cars a customer can register 
     *
     */
    @Test
    void testCarRegistrationLimit() {
        // Assuming the customer is allowed to register up to 3 cars
        customer.registerCar("XYZ-123", CarType.COMPACT);
        customer.registerCar("ABC-456", CarType.SUV);
        customer.registerCar("LMN-789", CarType.COMPACT);

        assertThrows(IllegalStateException.class, () -> customer.registerCar("DEF-101", CarType.SUV), 
                     "Customer should not be able to register more than 3 cars.");
    }

}
