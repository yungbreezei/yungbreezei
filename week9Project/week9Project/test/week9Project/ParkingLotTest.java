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

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    private ParkingLot parkingLot;
    private Car car;
    private ParkingPermit activePermit;
    private ParkingPermit expiredPermit;

    
    // Setup method to initialize the parkingLot and car objects before each test
    @BeforeEach
    void setUp() {
        Address address = new Address(null, null, null, null);
        parkingLot = new ParkingLot("P1", address, 100, 10.0, true);
        car = new Car("DEF-456", CarType.COMPACT, "C002", true, LocalDate.of(2025, 5, 20));
        activePermit = new ParkingPermit("P123", "DEF-456", LocalDate.now().plusDays(10)); // Ensure it's the same car
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
        assertTrue(transaction.getCharge() > 0);
    }

    // Test: Verify that the charge is calculated correctly based on the parking duration and rate
    @Test
    void testChargeCalculation() throws InvalidParkingPassException, ParkingLotFullException, CarAlreadyParkedException, CarNotParkedException, ParkingDurationTooLongException {
        ParkingLot parkingLot = new ParkingLot("P6", new Address(null, null, null, null), 2, 10.0, true);
        Car compactCar = new Car("XYZ-123", CarType.COMPACT, "C004", true, LocalDate.of(2025, 5, 20));

        // Entry of the car
        ParkingTransaction transaction = parkingLot.entry(compactCar, activePermit);

        // Assume the car was parked for 3 hours (or more), calculate expected charge
        double expectedCharge = 24.0; // 20% discount on the base rate (10.0 * 0.8 = 8.0), then 8.0 * 3 hours = 24.0

        // Exit the car
        parkingLot.exit(transaction);
        assertNotNull(transaction.getExitTime(), "Exit time should not be null after exit.");


        // Verify the charge is correctly calculated
        assertEquals(expectedCharge, transaction.getCharge(), 0.001, "Charge should be correctly calculated.");
    }

    
    // Test: Ensure the parking pass is valid and invalid scenarios are checked
    @Test
    void testInvalidParkingPass() {
        ParkingTransaction fakeTransaction = new ParkingTransaction(new Car("XYZ-999", CarType.COMPACT, "C999", true, LocalDate.now().plusDays(5)), LocalDateTime.now(), parkingLot);

        assertThrows(InvalidParkingPassException.class, () -> parkingLot.exit(fakeTransaction)); // Pass a fake transaction
    }

    
    // Test: After exiting a car, verify that the lot should become available again (especially if using a limited-capacity lot)
    @Test
    void testLotAvailabilityAfterExit() throws Exception {
        parkingLot.entry(car, activePermit);
        parkingLot.exit(parkingLot.getTransaction(car));

        Car newCar = new Car("NEW-111", CarType.SUV, "C012", true, LocalDate.of(2025, 12, 20));
        ParkingTransaction newTransaction = parkingLot.entry(newCar, activePermit);

        assertNotNull(newTransaction, "New car should be able to park after another car exits.");
    }




    
    // Test: If a Parking lot is full of cars
    @Test
    void testParkingLotFullException() {
        Address address = new Address(null, null, null, null);
        ParkingLot smallLot = new ParkingLot("P2", address, 1, 10.0, true);
        Car car1 = new Car("XYZ-123", CarType.COMPACT, "C004", true, LocalDate.of(2025, 5, 20));
        Car car2 = new Car("XYZ-456", CarType.SUV, "C005", true, LocalDate.of(2025, 2, 20));

        assertDoesNotThrow(() -> smallLot.entry(car1, activePermit));

        // Expect ParkingLotFullException when trying to park the second car
        assertThrows(ParkingLotFullException.class, () -> smallLot.entry(car2, activePermit));
    }

    
    @Test
    void testLotFullWhenCapacityIsReached() throws InvalidParkingPassException, ParkingLotFullException, CarAlreadyParkedException {
        ParkingLot fullLot = new ParkingLot("P3", new Address(null, null, null, null), 1, 10.0, true);
        Car car1 = new Car("XYZ-123", CarType.COMPACT, "C006", true, LocalDate.of(2025, 5, 20));
        Car car2 = new Car("XYZ-789", CarType.SUV, "C007", true, LocalDate.of(2025, 6, 20));

        // Park first car
        fullLot.entry(car1, activePermit);

        // Check that it is full when trying to add the second
        assertThrows(ParkingLotFullException.class, () -> fullLot.entry(car2, activePermit));
    }
    
    // Test: To see how system behaves when the parking lot is empty
    @Test
    void testParkingLotEmpty() {
        ParkingLot parkingLot = new ParkingLot("P6", new Address(null, null, null, null), 2, 10.0, true);
        
        // Try to exit a car that hasn't entered
        assertThrows(InvalidParkingPassException.class, () -> {
            parkingLot.exit(null); // Passing null or a non-existent transaction
        });
    }
    
    @Test
    void testExpiredPermit() {
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
}
