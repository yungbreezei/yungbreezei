package week9Project;

import java.util.Calendar;
import java.util.Objects;

public class ParkingPermit {
	
	private String permitId; // Unique ID for the parking permit
    private Calendar expirationDate; // Expiration date of the permit
    
    private Car car; // Fields for car
    private Calendar registrationDate; // Fields for registration date


    /**
     * Constructor to initialize a ParkingPermit with an ID, a car, expiration date, and registration date.
     */
    public ParkingPermit(String permitId, Car car, Calendar expirationDate, Calendar registrationDate) {
        if (expirationDate == null || registrationDate == null) {
        	// throws IllegalArgumentException if expiration date or registration date is null.
            throw new IllegalArgumentException("Expiration date and registration date cannot be null");
        }
        
        if (permitId == null || permitId.length() > 20) {
            throw new IllegalArgumentException("Permit ID should not exceed 20 characters.");
        }
        
        this.permitId = permitId; // The unique ID of the parking permit.
        this.car = car; // The car associated with the permit
        this.expirationDate = expirationDate; // The expiration date of the permit.
        this.registrationDate = registrationDate; //The registration date of the permit.
    }
    
    /**
     * Retrieves the permit ID.
     */
    public String getId() {
    	// The unique permit ID
        return permitId; // return The unique ID of the parking permit.
    }
    
    /**
     * Retrieves the expiration date of the permit.
     */
    public Calendar getExpirationDate() {
		return expirationDate;  // return The expiration date of the parking permit.	
    }
    
    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    /**
     * Retrieves the registration date of the permit.
     */
    public Calendar getRegistrationDate() {
        return registrationDate; // return The registration date of the parking permit.
    }
    
    /**
     * Sets the registration date of the permit.
     */
    public void setRegistrationDate(Calendar registrationDate) {
        if (registrationDate == null) {
        	// throws IllegalArgumentException if the registration date is null.
            throw new IllegalArgumentException("Registration date cannot be null");
        }
        this.registrationDate = registrationDate; // registrationDate The registration date to be set.
    }
   
    /**
     * Retrieves the car associated with the permit.
     */
    public Car getCar() {
		return car; // return The car associated with the parking permit.
	}
	
    /**
     * Checks if the parking permit has expired.
     * @return True if the permit has expired, false if it is still valid.
     */
    public boolean isExpired() {
        Calendar today = Calendar.getInstance(); // Gets the current date
        return expirationDate.before(today); // Returns true if expirationDate is before today
    }
    
    /**
     * Checks whether this permit is equal to another object.
     * Two ParkingPermit objects are considered equal if their permit IDs are the same.
     * @param obj The object to compare with this ParkingPermit.
     * @return True if both objects have the same permitId, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
    	// Check if both references are the same
    	if (this == obj) {
        	return true;
        }
    	// Ensure the object is a ParkingPermit
        if (obj == null || getClass() != obj.getClass()) {
        	return false; 
        }
        ParkingPermit permit = (ParkingPermit) obj; // obj The object to compare with this ParkingPermit.
        return Objects.equals(permitId, permit.permitId);  // Compare permit IDs True if both objects have the same permitId, otherwise false.
    }

    /**
     * Generates a hash code for this ParkingPermit based on the permitId.
     */
    @Override
    public int hashCode() {
    	
        return Objects.hash(permitId); // return The hash code for this ParkingPermit.
    }

    /**
     * Provides a string representation of the ParkingPermit object.
     */
    @Override
    public String toString() {
    	// return A string representation of the parking permit.
    	return "ParkingPermit[ID: " + permitId + ", License Plate: " + (car != null ? car.getLicensePlate() : "null") + 
                ", Expiration Date: " + expirationDate.getTime() + 
                ", Registration Date: " + registrationDate.getTime() + "]";
     }
}
