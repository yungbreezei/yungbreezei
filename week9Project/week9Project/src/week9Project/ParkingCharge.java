package week9Project;

import java.time.Instant;

public class ParkingCharge {
	
    // Attributes to store parking charge details
    private String permitId; // Unique identifier for the permit
    private String lotId;    // ID of the parking lot where the charge was incurred
    private Instant incurred; // Time when the charge was incurred
    private Money amount;    // Amount of the charge (in Money object)
    private Instant expirationDate; // Expiration date of the parking permit

    /**
     * Constructor to initialize a ParkingCharge with the given details.
     */
    public ParkingCharge(Money amount, String permitId, String lotId, Instant incurred, Instant expirationDate) {
        if (amount == null || permitId == null || lotId == null || incurred == null || expirationDate == null) {
            throw new IllegalArgumentException("None of the fields can be null.");
        }
        if (expirationDate.isBefore(incurred)) {
            throw new IllegalArgumentException("Expiration date cannot be before the incurred date.");
        }     
        
        this.amount = amount;
        this.permitId = permitId;
        this.lotId = lotId;
        this.incurred = incurred;
        this.expirationDate = expirationDate;
    }
    
    /** Getters */
    public Money getAmount() {
        return amount;
    }

    public String getPermitId() {
        return permitId;
    }

    public String getLotId() {
        return lotId;
    }

    public Instant getIncurred() {
        return incurred;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    /**
     * Check if the parking permit is expired.
     */
    public boolean isPermitExpired() {
        return Instant.now().isAfter(expirationDate); // Compares current time with expiration date
    }
    
    /**
     * Override equals method to compare ParkingCharge objects.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
        	return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
        	return false;
        }
        
        ParkingCharge that = (ParkingCharge) obj;
        return permitId.equals(that.permitId) &&
               lotId.equals(that.lotId) &&
               incurred.equals(that.incurred) &&
               amount.equals(that.amount) &&
               expirationDate.equals(that.expirationDate);
    }

    /**
     * Override toString to provide a clear, formatted output.
     */
    @Override
    public String toString() {
        return "ParkingCharge{permitId='" + permitId + "', amount=" + amount + ", lotId='" + lotId + "'}";
    }
}
