package week9Project;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class ParkingChargeTest {
    
    @Test
    void testConstructorAndGetters() {
        Money amount = new Money(500);
        String permitId = "P123";
        String lotId = "L001";
        Instant incurred = Instant.now();
        Instant expirationDate = incurred.plus(1, ChronoUnit.DAYS);
        
        ParkingCharge charge = new ParkingCharge(amount, permitId, lotId, incurred, expirationDate);
        
        assertEquals(amount, charge.getAmount());
        assertEquals(permitId, charge.getPermitId());
        assertEquals(lotId, charge.getLotId());
        assertEquals(incurred, charge.getIncurred());
    }

    @Test
    void testIsPermitExpired_Expired() {
        Instant expiredTime = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant validExpiration = expiredTime.minus(1, ChronoUnit.MINUTES);
        Money amount = new Money(500);
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", expiredTime, validExpiration);
        
        assertTrue(charge.isPermitExpired());
    }

    @Test
    void testIsPermitExpired_NotExpired() {
        Instant validTime = Instant.now();
        Instant validExpiration = validTime.plus(1, ChronoUnit.DAYS);
        Money amount = new Money(500);
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", validTime, validExpiration);
        
        assertFalse(charge.isPermitExpired());
    }

    @Test
    void testToString() {
        Money amount = new Money(500);
        String permitId = "P123";
        String lotId = "L001";
        Instant incurred = Instant.now();
        Instant expirationDate = incurred.plus(1, ChronoUnit.DAYS);
        
        ParkingCharge charge = new ParkingCharge(amount, permitId, lotId, incurred, expirationDate);
        
        String expectedString = "ParkingCharge{permitId='P123', amount=$5.00, lotId='L001'}";
        assertEquals(expectedString, charge.toString());
    }
    
    @Test
    void testInvalidPermitExpiration() {
        Instant expiredTime = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant invalidExpiration = expiredTime.minus(1, ChronoUnit.HOURS);
        Money amount = new Money(500);
        ParkingCharge charge = new ParkingCharge(amount, "P123", "L001", expiredTime, invalidExpiration);
        
        assertTrue(charge.isPermitExpired());
    }
}
