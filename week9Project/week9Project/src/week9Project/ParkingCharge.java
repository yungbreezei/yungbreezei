package week9Project;

import java.time.Instant;

public class ParkingCharge {
	
    // Attributes to store parking charge details
    private String permitId; // Unique identifier for the permit
    private String lotId;    // ID of the parking lot where the charge was incurred
    private Instant incurred; // Time when the charge was incurred
    private Money amount;    // Amount of the charge (in Money object)
    private Instant expirationDate; // Expiration date of the parking permit

    // Constructor to initialize ParkingCharge with the given details
    public ParkingCharge(Money amount, String permitId, String lotId, Instant incurred, Instant expirationDate) {
        this.amount = amount;
        this.permitId = permitId;
        this.lotId = lotId;
        this.incurred = incurred;
        this.expirationDate = expirationDate;
    }
    
    // Getter: Retrieve the amount of the parking charge
    public Money getAmount() {
        return amount;
    }

    // Getter: Retrieve the permit ID associated with the charge
    public String getPermitId() {
        return permitId;
    }

    // Getter: Retrieve the parking lot ID where the charge was incurred
    public String getLotId() {
        return lotId;
    }

    // Getter: Retrieve the time when the charge was incurred
    public Instant getIncurred() {
        return incurred;
    }

    // Method: Check if the parking permit is expired based on the expiration date
    public boolean isPermitExpired() {
        return Instant.now().isAfter(expirationDate); // Compares current time with expiration date
    }

    // Override toString method to provide a readable string representation of ParkingCharge
    @Override
    public String toString() {
        return "ParkingCharge{permitId='" + permitId + "', amount=" + amount + ", lotId='" + lotId + "'}";
    }
}
