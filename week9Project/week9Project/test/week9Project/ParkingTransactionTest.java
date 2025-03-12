package week9Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingTransactionTest {

    private ParkingTransaction transaction;
    Car car;

    @BeforeEach
    void setUp() {
        car = new Car("GHI-789", CarType.COMPACT, "C003", true, LocalDate.of(2025, 2, 28));
        transaction = new ParkingTransaction(car, LocalDateTime.now(), null);
    }
    
    @Test
    void testExit() {
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2);
        transaction.exit(exitTime, 8.0);
        assertNotNull(transaction.getExitTime());
        assertEquals(8.0, transaction.getCharge(), 0.01); // Using delta for double comparison
    }

    @Test
    void testToString() {
        transaction.exit(LocalDateTime.now(), 8.0);
        assertTrue(transaction.toString().contains("Charge: 8.0"));
    }
    

    @Test
    void testSetCharge() {
        transaction.setCharge(15.0);
        assertEquals(15.0, transaction.getCharge());
    }

    @Test
    void testInvalidCharge() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setCharge(-5.0));
    }
    
    @Test
    void testEntryTime() {
        LocalDateTime entryTime = LocalDateTime.now();
        ParkingTransaction transaction = new ParkingTransaction(car, entryTime, null);
        assertTrue(Duration.between(entryTime, transaction.getEntryTime()).toMillis() < 10);
    }

    @Test
    void testChargeCalculation() {
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2);
        transaction.exit(exitTime, 10.0);
        assertEquals(10.0, transaction.getCharge(), 0.01);
    }
    
    @Test
    public void testInvalidTransactionFee() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.now();
        ParkingLot parkingLot = new ParkingLot("Lot A", null, 10, 5.0, false); // $5 per hour
        Car car = new Car("XYZ123", null, "SUV", false, null);
        ParkingTransaction transaction = new ParkingTransaction(car, entryTime, parkingLot);

        // Act & Assert: Try setting a negative charge, should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> transaction.setCharge(-5.0),
                "Charge cannot be negative");
    }
    
    @Test
    public void testDiscountApplication() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.now();
        ParkingLot parkingLot = new ParkingLot("Lot A", null, 10, 5.0, false); // $5 per hour
        Car compactCar = new Car("COMPACT123", null, "Compact", false, null);
        ParkingTransaction transaction = new ParkingTransaction(compactCar, entryTime, parkingLot);

        // Act: Assume a discount of 20% for compact cars (you could add discount logic elsewhere in the system)
        double discount = 0.2;
        double originalCharge = 5.0 * 2; // 2 hours at $5/hour
        double discountedCharge = originalCharge * (1 - discount);

        // Simulate exit and apply discounted charge
        transaction.exit(LocalDateTime.now(), discountedCharge);

        // Assert: The charge should reflect the discounted amount
        assertEquals(discountedCharge, transaction.getCharge(), 0.01,
                "The charge should include the discount for compact cars.");
    }
    
}
