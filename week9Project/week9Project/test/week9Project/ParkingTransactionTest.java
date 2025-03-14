package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingTransactionTest {

    private ParkingTransaction transaction;
    private Car car;
    private ParkingLot parkingLot;
    private ParkingPermit permit;

    @BeforeEach
    void setUp() {
        // Setup the car and parking lot
        car = new Car("GHI-789", CarType.COMPACT, "C003", true, LocalDate.of(2025, 2, 28));
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.set(2025, Calendar.DECEMBER, 31); // Set expiration date to December 31, 2025
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(2025, Calendar.FEBRUARY, 28); // Set registration date to February 28, 2025
        
        // Create the ParkingPermit using the constructor
        permit = new ParkingPermit("P12345", car, expirationDate, registrationDate); 

        Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        parkingLot = new ParkingLot("Lot A", address, 100, 5.0, true); // Use the correct constructor
    
        // Initialize the parking transaction
        transaction = new ParkingTransaction(permit, parkingLot);
    }

    @Test
    void testExit() {
        // Simulate the car exiting after 2 hours
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2);
        transaction.exit(exitTime, 8.0); // Set exit time and charge

        assertNotNull(transaction.getExitTime(), "Exit time should be set.");
        assertEquals(8.0, transaction.getCharge().getDollars(), 0.01, 
        		"Charge should match the expected value.");
    }

    @Test
    void testToString() {
        // Test the toString method after exiting the transaction
        transaction.exit(LocalDateTime.now(), 8.0);
        assertTrue(transaction.toString().contains("Charge: 8.0"), 
        		"toString should contain the charge.");
    }

    @Test
    void testSetCharge() {
        // Test setting a valid charge
        transaction.setCharge(15.0);
        assertEquals(15.0, transaction.getCharge().getDollars(), "Charge should be updated to the new value.");
    }

    @Test
    void testInvalidCharge() {
        // Test setting a negative charge, expecting an exception
        assertThrows(IllegalArgumentException.class, () -> transaction.setCharge(-5.0), "Charge cannot be negative.");
    }

    @Test
    void testEntryTime() {
        // Check if the entry time is correctly set (should be within a small margin)
        LocalDateTime entryTime = LocalDateTime.now();
        ParkingTransaction transaction = new ParkingTransaction(permit, parkingLot);
        assertTrue(Duration.between(entryTime, transaction.getEntryTime()).toMillis() < 10, "Entry time should be set immediately.");
    }

    @Test
    void testChargeCalculation() {
        // Test if the charge is calculated correctly when exiting
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2); // 2 hours later
        transaction.exit(exitTime, 10.0); // Set a charge of 10.0 for 2 hours
        assertEquals(10.0, transaction.getCharge().getDollars(), 0.01, "Charge should match the calculated value.");
    }

    @Test
    public void testInvalidTransactionFee() {
        // Test if an exception is thrown when trying to set a negative charge
        LocalDateTime entryTime = LocalDateTime.now();
        ParkingTransaction transaction = new ParkingTransaction(permit, parkingLot);

        // Test for a negative charge
        assertThrows(IllegalArgumentException.class, () -> transaction.setCharge(-5.0), "Charge cannot be negative.");
    }

    @Test
    public void testDiscountApplication() {
        // Apply a 20% discount for compact cars and check the fee calculation
        double discount = 0.2;
        double originalCharge = 5.0 * 2; // 2 hours at $5/hour
        double discountedCharge = originalCharge * (1 - discount);

        // Create the parking transaction
        ParkingTransaction transaction = new ParkingTransaction(permit, parkingLot);

        // Exit the transaction and apply the discounted charge
        transaction.exit(LocalDateTime.now(), discountedCharge);

        // Assert that the charge after discount matches the expected discounted value
        assertEquals(discountedCharge, transaction.getCharge().getDollars(), 0.01, "The charge should reflect the discount for compact cars.");
    }

    @Test
    void testTransactionAlreadyCompleted() {
        // Test completing a transaction twice (should throw exception)
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2);
        Money feeCharged = new Money(10.0);
        transaction.complete(exitTime, feeCharged);

        // Assert that completing again throws an exception
        assertThrows(IllegalStateException.class, () -> transaction.complete(exitTime, feeCharged), "Should throw IllegalStateException as transaction is already completed.");
    }

    @Test
    void testCompleteTransaction() {
        // Test if completing the transaction works correctly
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2);
        Money feeCharged = new Money(10.0);
        transaction.complete(exitTime, feeCharged);

        assertNotNull(transaction.getExitTime(), "Exit time should be set.");
        assertEquals(feeCharged.getDollars(), transaction.getFeeCharged().getDollars(), "Fee charged should be set correctly.");
    }
}
