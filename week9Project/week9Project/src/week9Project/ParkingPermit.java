package week9Project;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;

public class ParkingPermit {
	
	private String permitId; // Unique ID for the parking permit
    private String licensePlate; // License plate associated with the permit
    private LocalDate expirationDate; // Expiration date of the permit
    
    //week 10
    private Car car;
    //private Calendar expirationDate;
    private Calendar registrationDate;


    // Constructor to initialize a ParkingPermit with an ID and a license plate.
    public ParkingPermit(String permitId, String licensePlate, LocalDate expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be null");
        }
        this.permitId = permitId;
        this.licensePlate = licensePlate;
        this.expirationDate = expirationDate;
    }
    // Retrieves the permit ID.
    public String getId() {
    	// The unique permit ID
        return permitId;
    }

    // Retrieves the associated license plate.
    public String getLicensePlate() {
    	// The license plate number
        return licensePlate;
    }
    
    // Retrieves the expiration date
    public LocalDate getExpirationDate() {
		return expirationDate;  	
    }
    
    // Checks if the permit is expired
    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }
    
    // Overrides the equals method to compare two ParkingPermit objects based on permitId.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if both references are the same
        if (obj == null || getClass() != obj.getClass()) return false; // Ensure the object is a ParkingPermit
        ParkingPermit permit = (ParkingPermit) obj; // obj The object to compare with this ParkingPermit.
        return Objects.equals(permitId, permit.permitId);  // Compare permit IDs True if both objects have the same permitId, otherwise false.
    }

    // Overrides the hashCode method to generate a hash based on permitId.
    @Override
    public int hashCode() {
    	//  The hash code for this ParkingPermit.
        return Objects.hash(permitId);
    }

    // Provides a string representation of the ParkingPermit object
    @Override
    public String toString() {
        return "ParkingPermit[ID: " + permitId + ", License Plate: " + licensePlate + 
               ", Expiration Date: " + expirationDate + "]";
    }
}
