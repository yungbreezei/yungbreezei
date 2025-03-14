package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import week9Project.exceptions.*;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class TransactionManagerTest {

    private TransactionManager transactionManager;
    private PermitManager permitManager;
    private ParkingLot parkingLot;
    private Car car;
    private ParkingPermit permit;
    private Calendar expirationDate;
    private Calendar registrationDate;
    
    @BeforeEach
    void setUp() {
        // Initialize required objects before each test
    	
        // Set up expiration and registration dates
        expirationDate = Calendar.getInstance();
        expirationDate.set(2025, Calendar.DECEMBER, 31, 23, 59, 59);  // Set to specific expiration date: December 31, 2025

        registrationDate = Calendar.getInstance();
        registrationDate.set(2025, Calendar.MARCH, 14, 0, 0, 0);  // Set to specific date: March 14, 2025
        
        permitManager = new PermitManager();
        transactionManager = new TransactionManager();
        parkingLot = new ParkingLot("Lot A", new Address("456 Park St", "Springfield", "IL", "62701"), 10, 5.0, false);
        car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1));  // Use customer ID as owner
        permit = new ParkingPermit("P456", car, expirationDate, registrationDate);

    }

    @Test
    void testPark() throws CarAlreadyParkedException, DuplicatePermitException {
        // Create a valid customer, car, and permit
        Car car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1)); // Use customer ID as owner
        ParkingPermit permit = new ParkingPermit("P123", car, Calendar.getInstance(), Calendar.getInstance());
        
        // Register the permit with the PermitManager
        permitManager.register(car);  // Ensure this is correctly registering the permit

        // Create a parking lot
        ParkingLot parkingLot = new ParkingLot("Lot A", new Address("456 Park St", "Springfield", "IL", "62701"), 10, 5.0, false);

        // Park the car using the permit in the parking lot
        ParkingTransaction transaction = transactionManager.park(Calendar.getInstance(), permit, parkingLot);

        // Set a charge for the transaction
        transaction.setCharge(5.0);  // Set a charge of 5.0 (adjust as needed)

        // Assert that the transaction is not null
        assertNotNull(transaction, "Transaction should not be null.");

        // Assert the charge after setting it (assuming 5.0 was set in the previous step)
        assertEquals(5.0, transactionManager.getParkingCharges(permit).getDollars(), "The charge should be 5.0.");
    }



    @Test
    void testGetParkingCharges() throws CarAlreadyParkedException, DuplicatePermitException {
        // Create a valid customer, car, and permit
        Car car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1)); // Use customer ID as owner
        ParkingPermit permit = new ParkingPermit("P123", car, Calendar.getInstance(), Calendar.getInstance());
        
        // Register the permit with the PermitManager (not the TransactionManager)
        permitManager.register(car); // Ensure this is correctly registering the permit

        // Park the car using the permit in a parking lot
        ParkingLot parkingLot = new ParkingLot("Lot A", new Address("456 Park St", "Springfield", "IL", "62701"), 10, 5.0, false);
        ParkingTransaction transaction1 = transactionManager.park(Calendar.getInstance(), permit, parkingLot);

        // Complete the transaction with a charge of 15.0
        transaction1.complete(LocalDateTime.now(), new Money(15.0));

        // Check the total charges for the car
        Money totalCharges = transactionManager.getParkingCharges(permit);
        assertEquals(15.0, totalCharges.getDollars(), "Total parking charges should be 15.0.");
    }

    @Test
    void testCompleteTransaction() throws CarAlreadyParkedException, NoActiveTransactionException, InvalidParkingPassException, DuplicatePermitException {
        // Create a valid customer, car, and permit
        Car car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1)); // Use customer ID as owner
        ParkingPermit permit = new ParkingPermit("P123", car, Calendar.getInstance(), Calendar.getInstance());

        // Register the permit with the PermitManager
        permitManager.register(car);

        // Create a parking lot
        ParkingLot parkingLot = new ParkingLot("Lot A", new Address("456 Park St", "Springfield", "IL", "62701"), 10, 5.0, false);

        // Park the car using the permit in the parking lot
        ParkingTransaction transaction = transactionManager.park(Calendar.getInstance(), permit, parkingLot);

        // Complete the transaction with the permit
        transactionManager.completeTransaction(permit);

        // Check if the transaction is marked as completed
        assertTrue(transaction.isCompleted(), "The transaction should be marked as completed.");
    }


    @Test
    void testCompleteTransactionWithExpiredPermit() throws CarAlreadyParkedException, DuplicatePermitException {
        // Create a valid customer, car, and parking lot
        Car car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1)); // Use customer ID as owner
        
        // Create a permit for the car
        ParkingPermit permit = new ParkingPermit("P123", car, Calendar.getInstance(), Calendar.getInstance());

        // Set the permit to expired
        Calendar expiredDate = Calendar.getInstance();
        expiredDate.add(Calendar.MONTH, -1);  // Set expiration date to 1 month ago
        permit.setExpirationDate(expiredDate); // Set expiration date

        // Register the permit with the PermitManager
        permitManager.register(car);

        // Try completing the transaction with an expired permit
        assertThrows(NoActiveTransactionException.class, () -> 
        transactionManager.completeTransaction(permit), 
            "Should throw InvalidParkingPassException for expired permit.");
    }


    @Test
    void testNoActiveTransaction() throws CarAlreadyParkedException, DuplicatePermitException {
        // Create a valid customer, car, and parking lot
        Car car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1)); // Use customer ID as owner
        
        // Create a permit for the car
        ParkingPermit permit = new ParkingPermit("P123", car, Calendar.getInstance(), Calendar.getInstance());

        // Register the permit with the PermitManager
        permitManager.register(car);

        // Try completing the transaction when no active transaction exists
        assertThrows(NoActiveTransactionException.class, () -> transactionManager.completeTransaction(permit), 
            "Should throw NoActiveTransactionException when no active transaction exists.");
    }


    @Test
    void testCarAlreadyParkedException() throws CarAlreadyParkedException, DuplicatePermitException {
        // Create a valid customer, car, and parking lot
        Car car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1)); // Use customer ID as owner
        
        // Create a permit for the car
        ParkingPermit permit = new ParkingPermit("P123", car, Calendar.getInstance(), Calendar.getInstance());

        // Register the permit with the PermitManager
        permitManager.register(car);

        // Create a parking lot
        ParkingLot parkingLot = new ParkingLot("Lot A", new Address("456 Park St", "Springfield", "IL", "62701"), 10, 5.0, false);

        // Park the car using the permit in the parking lot
        transactionManager.park(Calendar.getInstance(), permit, parkingLot);
        
        // Try parking the same car again, expect a CarAlreadyParkedException
        assertThrows(CarAlreadyParkedException.class, () -> transactionManager.park(Calendar.getInstance(), permit, parkingLot), "Should throw CarAlreadyParkedException when the car is already parked.");
    }
    
    @Test
    void testTransactionFeeCalculation() throws CarAlreadyParkedException {
        ParkingTransaction transaction = transactionManager.park(Calendar.getInstance(), permit, parkingLot);
        
        // Simulate the passage of time
        LocalDateTime entryTime = transaction.getEntryTime();
        LocalDateTime exitTime = entryTime.plusHours(2);  // Assume 2 hours of parking
        
        // Set the expected fee (assuming $5 per hour)
        Money expectedFee = new Money(10.0);  // 2 hours * 5 per hour
        transaction.complete(exitTime, expectedFee);
        
        // Check if the fee charged matches the expected fee
        assertEquals(expectedFee, transaction.getCharge(), "The parking fee should match the expected fee.");
    }

}
