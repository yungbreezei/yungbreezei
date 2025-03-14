package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import week9Project.exceptions.*;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

class ParkingOfficeTest {

    private ParkingOffice<Car> parkingOffice;
    private Customer customer;
    private Car car;
    private ParkingLot parkingLot;
    private ParkingPermit permit;

    // This method runs before each test to set up the test data
    @BeforeEach
    public void setUp() {
        // Setting up a mock parking office
        Address officeAddress = new Address("123 Main St", "City", "ST", "12345");
        parkingOffice = new ParkingOffice<>("Main Office", officeAddress);

        // Setting up a customer with address
        Address customerAddress = new Address("456 Elm St", "City", "ST", "67890");
        parkingOffice.registerCustomer("C001", "John Doe", customerAddress, "123-456-7890");
        customer = parkingOffice.getCustomers().iterator().next();

        // Registering a car for the customer
        car = new Car("ABC123", CarType.COMPACT, "C001", false, null);
        parkingOffice.registerCar(customer.getCustomerId(), car.getLicensePlate(), car.getType());

        // Creating a parking lot
        parkingLot = new ParkingLot("Lot 1", customerAddress, 10, 0, false);
        parkingOffice.addParkingLot(parkingLot);
    }

    // Test case for registering a car for a customer
    @Test
    public void testRegisterCustomer() {
        // Test that the customer is registered
        assertEquals(1, parkingOffice.getCustomers().size());
        assertTrue(parkingOffice.getCustomers().contains(customer));
    }
    
    @Test
    public void testRegisterCar() {
        // Test that the car is correctly registered
        assertNotNull(customer.getCar(car.getLicensePlate()));
    }
    
    @Test
    public void testAddParkingLot() {
        // Test that the parking lot is added to the office
        assertTrue(parkingOffice.getParkingLots().contains(parkingLot));
    }
    
    @Test
    public void testRegisterPermit() throws DuplicatePermitException {
        // Test registering a permit
        permit = parkingOffice.register(car);
        assertNotNull(permit);
        assertEquals(car.getLicensePlate(), permit.getCar().getLicensePlate());
    }
    
    @Test
    public void testRegisterDuplicateCarThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> parkingOffice.registerCar(customer.getCustomerId(), car.getLicensePlate(), car.getType()));
    }
    
    @Test
    public void testGetParkingChargesByPermit() {
        // Test getting parking charges by permit
        try {
            permit = parkingOffice.register(car);
            Money charges = parkingOffice.getParkingCharges(permit);
            assertNotNull(charges);
        } catch (DuplicatePermitException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCustomerIds() {
        Set<String> customerIds = parkingOffice.getCustomerIds();
        assertTrue(customerIds.contains("C001"));
    }
    
    @Test
    public void testGetParkingChargesByCustomer() throws PermitNotFoundException, PermitExpiredException {
        // Test getting parking charges for a customer
        try {
            permit = parkingOffice.register(car);
            Money charges = parkingOffice.getParkingCharges(customer);
            assertNotNull(charges);
        } catch (DuplicatePermitException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDuplicateCustomerRegistration() {
        // Test that an exception is thrown when trying to register a duplicate customer
        assertThrows(IllegalArgumentException.class, () -> {
            parkingOffice.registerCustomer("C001", "Jane Doe", new Address("789 Pine St", "City", "ST", "98765"), "321-654-0987");
        });
    }

    @Test
    public void testDuplicateCarRegistration() {
        // Test that an exception is thrown when trying to register a duplicate car for the same customer
        assertThrows(IllegalArgumentException.class, () -> {
            parkingOffice.registerCar(customer.getCustomerId(), car.getLicensePlate(), car.getType());
        });
    }

    @Test
    public void testParkCarInUnmanagedLot() {
        // Test that an exception is thrown when trying to park in a non-managed lot
        Address officeAddress = new Address("123 Main St", "City", "ST", "12345");
        ParkingLot unmanagedLot = new ParkingLot("Unmanaged Lot", officeAddress, 5, 0, false);
        assertThrows(Exception.class, () -> {
            parkingOffice.park(Calendar.getInstance(), permit, unmanagedLot);
        });
    }

    @Test
    public void testRegisterParkingPermit() throws DuplicatePermitException {
        ParkingPermit permit = parkingOffice.register(car);
        assertNotNull(permit);
        assertEquals(car.getLicensePlate(), permit.getCar().getLicensePlate());
    }

    @Test
    public void testRegisterDuplicatePermitThrowsException() throws DuplicatePermitException {
        parkingOffice.register(car); // First registration
        assertThrows(DuplicatePermitException.class, () -> parkingOffice.register(car));
    }


    @Test
    public void testParkCar() throws Exception {
        ParkingPermit permit = parkingOffice.register(car);
        Calendar now = Calendar.getInstance();
        ParkingTransaction transaction = parkingOffice.park(now, permit, parkingLot);
        assertNotNull(transaction);
    }

    @Test
    public void testGetParkingLot() {
        List<ParkingLot> lots = parkingOffice.getParkingLots();
        assertTrue(lots.contains(parkingLot));
    }

}
