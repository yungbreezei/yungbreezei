package week9Project;

import java.time.LocalDate;
import java.util.Objects;

public class Car {

    // Attributes (private to ensure encapsulation)
    private String permit; // Unique permit identifier (optional)
    private LocalDate permitExpiration; // Date when the permit expires
    private String licensePlate; // Car's license plate (final to prevent accidental changes)
    private final CarType type; // Enum representing the car type (e.g., COMPACT, SUV)
    private String owner; // ID of the customer who owns the car
    private boolean hasValidParkingPass; // Tracks whether the car has a valid parking permit

    // Constructor to initialize a new Car with license plate, type, and owner ID
    public Car(String licensePlate, CarType type, String owner, boolean hasValidParkingPass, LocalDate permitExpiration) {
        if (licensePlate == null || licensePlate.isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty.");
        }
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Owner ID cannot be null or empty.");
        }  	
    	this.licensePlate = licensePlate; // Assign license plate
        this.type = Objects.requireNonNull(type, "Car type cannot be null."); // Assign car type
        this.owner = owner; // Assign customer ID
        this.hasValidParkingPass = hasValidParkingPass; // Set the initial parking pass validity
        this.permitExpiration = permitExpiration; // Set the permit expiration date

        // Ensure pass validity is updated based on expiration
        if (permitExpiration != null) {
            this.hasValidParkingPass = hasValidParkingPass && 
            		(permitExpiration == null || !permitExpiration.isBefore(LocalDate.now()));;
        }
    }

    /**
     * Retrieves the car's license plate.
     */
    public String getLicensePlate() {
        return licensePlate; // return The license plate
    }
    
 // Setter for license plate with validation
    public void setLicense(String licensePlate) {
        if (licensePlate == null || licensePlate.isEmpty() || !licensePlate.matches("[A-Z0-9-]+")) {
            throw new IllegalArgumentException("Invalid license plate format.");
        }
        this.licensePlate = licensePlate;
    }

    /**
     * Retrieves the car's type.
     */
    public CarType getType() {
        return type; // return The CarType of the car
    }

    /**
     * Retrieves the owner's ID.
     */
    public String getOwner() {
        return owner; // return The owner's ID
    }

    /**
     * Updates the car's owner.
     * add exception Owner ID cannot be null or empty.
     */
    public void setOwner(String owner) {
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Owner ID cannot be null or empty.");
        }
        this.owner = owner; // param owner The new owner ID
    }

    /**
     * Checks whether the car has a valid parking pass.
     */
    public boolean hasValidParkingPass() {
    	// return True if the car has a valid permit, false otherwise
        return hasValidParkingPass && (permitExpiration == null || !permitExpiration.isBefore(LocalDate.now()));
    }
    
    // Setter for parking pass status
    public void setHasValidParkingPass(boolean hasValidParkingPass) {
        this.hasValidParkingPass = hasValidParkingPass; // Updates the parking pass validity
    }

    /**
     * Retrieves the permit associated with the car.
     */
    public String getPermit() {
        return permit; // return The permit ID or null if no permit exists
    }

    /**
     * Retrieves the expiration date of the permit.
     */
    public LocalDate getPermitExpiration() {
        return permitExpiration; // return The permit expiration date or null if no permit exists
    }

    /**
     * Assigns a permit to the car with an expiration date.
     *
     *  permit Exception Permit ID cannot be null or empty.
     *  expiration  Exception Expiration date must be in the future.
     */
    public void setPermit(String permit, LocalDate expiration) {
    	
        if (permit == null || permit.isEmpty()) {
            throw new IllegalArgumentException("Permit ID cannot be null or empty.");
        }
        if (expiration == null || expiration.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiration date must be in the future.");
        }
        
        this.permit = permit; // The permit ID
        this.permitExpiration = expiration; // The expiration date of the permit
        this.hasValidParkingPass = true;
    }


    /**
     * Clears the car's permit, revoking the parking pass.
     */
    public void clearPermit() {
        this.permit = null;
        this.permitExpiration = null;
        this.hasValidParkingPass = false;
    }
    
    /**
     * Compares two cars based on their license plate.
     */
    @Override
    public boolean equals(Object obj) {
        
    	//obj The object to compare with  	
    	if (this == obj) {
        	return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
        	return false;
        }
        
        Car car = (Car) obj;
        return licensePlate.equals(car.licensePlate); // return True if the cars have the same license plate, false otherwise
    }

    /**
     * Generates a hash code for the car using the license plate.
     */
    @Override
    public int hashCode() {
        return Objects.hash(licensePlate); // return The hash code value
    }

    /**
     * Returns a string representation of the car.
     */
    @Override
    public String toString() {
        return String.format(
        		// return A formatted string describing the car
                "Car[License: %s, Type: %s, Owner ID: %s, Valid Pass: %b, Permit Expiration: %s]",
                licensePlate, type, owner, hasValidParkingPass(), permitExpiration != null ? permitExpiration : "None"
        );
    }
}
