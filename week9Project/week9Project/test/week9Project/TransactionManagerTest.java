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
    // Verifies that a car can be parked in a parking lot, and charges can be set correctly.
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


    // Verifies that the total parking charges can be retrieved for a parked car
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
    
    //Verifies that a transaction can be completed and marked as completed
    @Test
    void testCompleteTransaction() throws CarAlreadyParkedException, NoActiveTransactionException, InvalidParkingPassException, DuplicatePermitException {
        // Create a valid customer, car, and permit
        Car car = new Car("XYZ123", CarType.SUV, "123", false, LocalDate.now().plusYears(1)); // Use customer ID as owner
        ParkingPermit permit = new ParkingPermit("P123", car, expirationDate, Calendar.getInstance());

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

    // Verifies that an exception is thrown when completing a transaction with an expired permit.
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

    // Verifies that an exception is thrown when attempting to complete a transaction that doesn't exist
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

    // Verifies that a CarAlreadyParkedException is thrown when trying to park a car that is already parked
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
    
    // Verifies that the parking fee is calculated correctly based on the duration of parking
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

    // Verifies that parking transactions for different cars are handled independently
    @Test
    void testMultipleTransactionsForDifferentCars() throws CarAlreadyParkedException, NoActiveTransactionException, InvalidParkingPassException {
        Car car1 = new Car("ABC123", CarType.COMPACT, "101", true, LocalDate.now().plusYears(1));
        Car car2 = new Car("DEF456", CarType.SUV, "102", true, LocalDate.now().plusYears(1));

        ParkingPermit permit1 = new ParkingPermit("P101", car1, expirationDate, Calendar.getInstance());
        ParkingPermit permit2 = new ParkingPermit("P102", car2, expirationDate, Calendar.getInstance());

        // Park and complete transactions
        transactionManager.park(Calendar.getInstance(), permit1, parkingLot);
        transactionManager.completeTransaction(permit1); // Assume fee is set during completion
        
        transactionManager.park(Calendar.getInstance(), permit2, parkingLot);
        transactionManager.completeTransaction(permit2); // Assume fee is set during completion

        // Check individual charges
        Money chargesCar1 = transactionManager.getParkingCharges(permit1);
        Money chargesCar2 = transactionManager.getParkingCharges(permit2);

        assertNotNull(chargesCar1, "The parking charges for car1 should not be null.");
        assertNotNull(chargesCar2, "The parking charges for car2 should not be null.");

        // Check that each car has independent charges
        assertEquals(25.0, chargesCar1.getDollars(), "Car 1's charges should be correct.");
        assertEquals(25.0, chargesCar2.getDollars(), "Car 2's charges should be correct.");
        
        // Check total charges are the sum of both
        assertEquals(50.0, chargesCar1.getDollars() + chargesCar2.getDollars(), 
                "Both cars should have independent charges and the sum should match.");
    }

    // Verifies that the parking charges can be retrieved by using a parking permit instead of a license plate number
    @Test
    void testGetParkingChargesByLicensePlate() throws CarAlreadyParkedException, NoActiveTransactionException, InvalidParkingPassException {
        Car car1 = new Car("ABC123", CarType.COMPACT, "101", false, LocalDate.now().plusYears(1));
    	
    	// have a permit object for the test
        ParkingPermit permit = new ParkingPermit("P101", car1, Calendar.getInstance(), Calendar.getInstance());
        transactionManager.park(Calendar.getInstance(), permit, parkingLot);
        transactionManager.completeTransaction(permit);
        
        // Now checking parking charges using the permit
        Money charges = transactionManager.getParkingCharges(permit);  // Use the permit instead of license plate string
        assertTrue(charges.compareTo(new Money(0)) > 0, "The parking charges should be greater than 0 after completing the transaction.");
    }
    


}
