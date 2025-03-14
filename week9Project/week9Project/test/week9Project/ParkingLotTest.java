package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import week9Project.exceptions.CarAlreadyParkedException;
import week9Project.exceptions.CarNotParkedException;
import week9Project.exceptions.InvalidParkingPassException;
import week9Project.exceptions.ParkingDurationTooLongException;
import week9Project.exceptions.ParkingLotFullException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    private ParkingLot parkingLot;
    private Car car;
    private ParkingPermit activePermit;
    
    // Setup method to initialize the parkingLot and car objects before each test
    @BeforeEach
    void setUp() {
    	Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        parkingLot = new ParkingLot("P1", address, 100, 10.0, true);
        car = new Car("DEF-456", CarType.COMPACT, "C002", true, LocalDate.of(2025, 5, 20));
        
        // Create the Calendar objects for expirationDate and registrationDate
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.DAY_OF_YEAR, 10); // Set expiration 10 days from now
        
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.add(Calendar.DAY_OF_YEAR, -5); // Set registration 5 days ago

        // Create active permit using the correct constructor
        activePermit = new ParkingPermit("P123", car, expirationDate, registrationDate); 
    }

    // Test case to verify the entry of a car into the parking lot
    @Test
    void testEntry() throws InvalidParkingPassException, ParkingLotFullException, CarAlreadyParkedException {
        ParkingTransaction transaction = parkingLot.entry(car, activePermit);
        assertNotNull(transaction);
        assertEquals(1, parkingLot.transactions.size());
        assertEquals(car, transaction.getCar());
    }
    
    // Test case to verify the exit of a car from the parking lot
    @Test
    void testExit() throws InvalidParkingPassException, ParkingLotFullException, CarAlreadyParkedException, CarNotParkedException, ParkingDurationTooLongException {
        ParkingTransaction transaction = parkingLot.entry(car, activePermit);
        parkingLot.exit(transaction);

        assertNotNull(transaction.getExitTime());
        assertTrue(transaction.getCharge().compareTo(new Money(0)) > 0, "Charge should be greater than 0.");
    }

 
    // Test: Ensure the parking pass is valid and invalid scenarios are checked
    @Test
    void testInvalidParkingPass() {
        // Create a Calendar instance and set it to yesterday (expired)
        Calendar expiredDate = Calendar.getInstance();
        expiredDate.add(Calendar.DAY_OF_YEAR, -1); // Set to 1 day before the current date

        // Create a Car instance with an expired permit
        Car expiredCar = new Car("XYZ-999", CarType.COMPACT, "C999", true, expiredDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());

        // Create a ParkingPermit for the expired car
        ParkingPermit expiredPermit = new ParkingPermit("P001", expiredCar, expiredDate, expiredDate);

        // Create a ParkingTransaction with this expired permit
        ParkingTransaction fakeTransaction = new ParkingTransaction(expiredPermit, parkingLot);

        // Assert that an InvalidParkingPassException is thrown when trying to exit with an expired permit
        assertThrows(CarNotParkedException.class, () -> parkingLot.exit(fakeTransaction));
    }

    // Test: After exiting a car, verify that the lot should become available again (especially if using a limited-capacity lot)
    @Test
    void testLotAvailabilityAfterExit() {
        Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        ParkingLot smallLot = new ParkingLot("P2", address, 1, 10.0, true); // Small lot with capacity 1

        Car car1 = new Car("XYZ-123", CarType.COMPACT, "C004", true, LocalDate.of(2025, 5, 20));
        Car car2 = new Car("XYZ-456", CarType.SUV, "C005", true, LocalDate.of(2025, 2, 20));

        // Create Calendar instances for registration and expiration dates
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(2025, Calendar.JANUARY, 1);  // Set registration date to Jan 1, 2025
        
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.set(2026, Calendar.JANUARY, 1);  // Set expiration date to Jan 1, 2026, in the future

        // Create valid ParkingPermits for each car
        ParkingPermit permitForCar1 = new ParkingPermit("C004", car1, expirationDate, registrationDate);
        ParkingPermit permitForCar2 = new ParkingPermit("C005", car2, expirationDate, registrationDate);

        // Park the first car (valid permit)
        assertDoesNotThrow(() -> smallLot.entry(car1, permitForCar1));

        // Try parking the second car, which should trigger ParkingLotFullException
        assertThrows(ParkingLotFullException.class, () -> smallLot.entry(car2, permitForCar2));
    }
    
    // Test: If a Parking lot is full of cars
    @Test
    void testParkingLotFullException() {
        Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        ParkingLot smallLot = new ParkingLot("P2", address, 1, 10.0, true); // Small lot with capacity 1
        Car car1 = new Car("XYZ-123", CarType.COMPACT, "C004", true, LocalDate.of(2025, 5, 20));
        Car car2 = new Car("XYZ-456", CarType.SUV, "C005", true, LocalDate.of(2025, 2, 20));
        
        // Create Calendar instances for registration and expiration dates
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(2025, Calendar.JANUARY, 1); // Set registration date to Jan 1, 2025
        
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.set(2026, Calendar.JANUARY, 1); // Set expiration date to Jan 1, 2026
        
        // Create separate permits for each car
        ParkingPermit permitForCar1 = new ParkingPermit("C004", car1, expirationDate, registrationDate);
        ParkingPermit permitForCar2 = new ParkingPermit("C005", car2, expirationDate, registrationDate);

        // Park the first car, this should not throw any exception
        assertDoesNotThrow(() -> smallLot.entry(car1, permitForCar1));

        // Try parking the second car, expecting ParkingLotFullException
        assertThrows(ParkingLotFullException.class, () -> smallLot.entry(car2, permitForCar2));
    }

    @Test
    void testLotFullWhenCapacityIsReached() throws InvalidParkingPassException, ParkingLotFullException, CarAlreadyParkedException {
        ParkingLot fullLot = new ParkingLot("P3", new Address("123 Main St", "Springfield", "IL", "62701"), 1, 10.0, true);
        Car car1 = new Car("XYZ-123", CarType.COMPACT, "C006", true, LocalDate.of(2025, 5, 20));
        Car car2 = new Car("XYZ-789", CarType.SUV, "C007", true, LocalDate.of(2025, 6, 20));

        // Create a Calendar instance for the expiration date (December 31, 2025)
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.set(2025, Calendar.DECEMBER, 31);  // Set the expiration date to December 31, 2025

        // Create a valid ParkingPermit for car1
        ParkingPermit permitForCar1 = new ParkingPermit("P001", car1, expirationDate, Calendar.getInstance());

        // Create a valid ParkingPermit for car2
        ParkingPermit permitForCar2 = new ParkingPermit("P002", car2, expirationDate, Calendar.getInstance());

        // Park the first car
        fullLot.entry(car1, permitForCar1);

        // Check that it is full when trying to add the second car
        assertThrows(ParkingLotFullException.class, () -> fullLot.entry(car2, permitForCar2));
    }

    // Test: To see how system behaves when the parking lot is empty
    @Test
    void testParkingLotEmpty() {
        ParkingLot parkingLot = new ParkingLot("P6", new Address
        		("123 Main St", "Springfield", "IL", "62701"), 1, 1, false);
        
        // Try to exit a car that hasn't entered
        assertThrows(CarNotParkedException.class, () -> {
            parkingLot.exit(null); // Passing null or a non-existent transaction
        });
    }
    
    @Test
    void testExpiredPermit() {
        // Create a car and an expired permit with the correct constructor
        Car car = new Car("DEF-456", CarType.COMPACT, "C002", true, LocalDate.of(2025, 5, 20));
        
        // Create Calendar objects for expired permit dates
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.DAY_OF_YEAR, -1); // Set expiration date to 1 day ago
        
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.add(Calendar.DAY_OF_YEAR, -10); // Set registration date to 10 days ago
        
        // Create the expired permit
        ParkingPermit expiredPermit = new ParkingPermit("P124", car, expirationDate, registrationDate);

        // Check that trying to use the expired permit throws an exception
        assertThrows(InvalidParkingPassException.class, () -> {
            parkingLot.entry(car, expiredPermit); // Pass the expired permit!
        });
    }


    // Test case to verify the string representation of the parking lot
    @Test
    void testToString() {
        String expected = "ParkingLot[ID: P1, Capacity: 100, Rate: 10.0]";
        assertEquals(expected, parkingLot.toString());
    }
    
    /*
     * Test: If there is a maximum allowed parking duration, test the exact limit.
     */
    @Test
    void testMaxParkingDurationLimit() throws Exception {
        ParkingLot parkingLot = new ParkingLot("P7", new Address("123 Main St", "Springfield", "IL", "62701"), 5, 10.0, true);
        Car car = new Car("TST-001", CarType.COMPACT, "C101", true, LocalDate.of(2026, 1, 10));

        // Create Calendar objects for the permit dates
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.DAY_OF_YEAR, 5); // Set expiration date 5 days ahead
        
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.add(Calendar.DAY_OF_YEAR, -5); // Set registration date 5 days ago

        // Create the permit
        ParkingPermit permit = new ParkingPermit("P456", car, expirationDate, registrationDate);

        // Simulate the car's entry into the parking lot
        ParkingTransaction transaction = parkingLot.entry(car, permit);

        // Simulate max parking time
        transaction.setExitTime(transaction.getEntryTime().plusHours(24)); // Assuming 24-hour max limit

        // Ensure no exception is thrown for max limit
        assertDoesNotThrow(() -> parkingLot.exit(transaction), "Exiting at the max limit should be allowed.");
    }
    
    /*
     * Test: If the system enforces a maximum parking time, verify that exceeding it throws an exception
     */
    @Test
    void testParkingDurationTooLongException() throws Exception {
        ParkingLot parkingLot = new ParkingLot("P8", new Address("123 Main St", "Springfield", "IL", "62701"), 10, 10.0, true);
        
        // Set expiration date (though expiration may not be directly relevant to this test, it's important for parking permit validity)
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.set(2026, Calendar.JUNE, 15);  // Set expiration date
        
        // Create a car with a valid parking pass (has expiration date set)
        Car car = new Car("TST-002", CarType.COMPACT, "C102", true, LocalDate.of(2026, 6, 15));
        ParkingPermit permit = new ParkingPermit("P001", car, expirationDate, Calendar.getInstance());
        
        // Simulate parking the car
        ParkingTransaction transaction = parkingLot.entry(car, permit);
        
        // Set the entry time for the transaction
        transaction.setEntryTime(LocalDateTime.now().minusHours(25));  // Entry time 25 hours ago
        
        // Simulate overstaying by setting the exit time 25 hours after the entry
        transaction.setExitTime(transaction.getEntryTime().plusHours(25));  // Exit 25 hours later

        // Assert that ParkingDurationTooLongException is thrown when the car tries to exit after 25 hours
        assertThrows(ParkingDurationTooLongException.class, () -> parkingLot.exit(transaction),
                "Exiting after 25 hours should throw ParkingDurationTooLongException.");
    }


    /*
     * Test: A car that never entered shouldn't be able to exit.
     */
    @Test
    void testCarNotParkedException() {
        Car carNotParked = new Car("ABC-999", CarType.SUV, "C300", true, LocalDate.of(2027, 3, 12));

        // Try to exit a car that was never parked
        assertThrows(CarNotParkedException.class, () -> parkingLot.exit(parkingLot.getTransaction(carNotParked)),
                "Car that was never parked should not be able to exit.");
    }
  
    /*
     * Test: Verify that a permit cannot be used for a different car.
     */
    @Test
    void testPermitMismatch() {
        // Create a car with a different license plate than the one associated with the active permit
        Car anotherCar = new Car("XYZ-999", CarType.SUV, "C500", true, LocalDate.of(2025, 8, 15));

        // Try to enter the parking lot with a permit that belongs to a different car
        assertThrows(InvalidParkingPassException.class, () -> parkingLot.entry(anotherCar, activePermit),
                "Permit should not be valid for a different car.");
    }
}
