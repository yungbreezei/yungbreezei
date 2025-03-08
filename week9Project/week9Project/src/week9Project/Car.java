package week9Project;

import java.time.LocalDate;

public class Car {

    // Attributes (private to ensure encapsulation)
    private String permit; // Unique permit identifier (optional)
    private LocalDate permitExpiration; // Date when the permit expires
    private String license; // Car's license plate number
    private CarType type; // Enum representing the type of car (e.g., COMPACT, SUV, etc.)
    private String owner; // ID of the customer who owns the car
    private boolean hasValidParkingPass; // Tracks whether the car has a valid parking permit

    // Constructor to initialize a car with license plate, type, and owner ID
    public Car(String license, CarType type, String owner, boolean hasValidParkingPass, LocalDate permitExpiration) {
        this.license = license; // Assign license plate
        this.type = type; // Assign car type
        this.owner = owner; // Assign customer ID
        this.hasValidParkingPass = hasValidParkingPass; // Set the initial parking pass validity
        this.permitExpiration = permitExpiration; // Set the permit expiration date

        // Ensure pass validity is updated based on expiration
        if (permitExpiration != null) {
            this.hasValidParkingPass = !permitExpiration.isBefore(LocalDate.now());
        }
    }

    // Retrieves the car's license plate
    public String getLicense() {
        return license;
    }

    // Updates the car's license plate
    public void setLicense(String license) {
        this.license = license;
    }

    // Retrieves the car's type (CarType enum)
    public CarType getType() {
        return type;
    }

    // Retrieves the ID of the car's owner (customer)
    public String getOwner() {
        return owner;
    }

    // Updates the car's owner
    public void setOwner(String owner) {
        this.owner = owner;
    }

	// Checks whether the car has a valid parking pass
    public boolean hasValidParkingPass() {
        return hasValidParkingPass && (permitExpiration == null || !permitExpiration.isBefore(LocalDate.now()));
    }

    // Updates the validity of the car's parking pass
    public void setHasValidParkingPass(boolean hasValidParkingPass) {
        this.hasValidParkingPass = hasValidParkingPass;
    }

    // Retrieves the permit associated with the car
    public String getPermit() {
        return permit;
    }

    // Retrieves the expiration date of the permit
    public LocalDate getPermitExpiration() {
        return permitExpiration;
    }

    // Sets a new permit for the car with an expiration date
    public void setPermit(String permit, LocalDate expiration) {
        this.permit = permit;
        this.permitExpiration = expiration;
        // Ensure parking pass is valid only if the expiration date is in the future
        this.hasValidParkingPass = (expiration != null && !expiration.isBefore(LocalDate.now()));
    }

    // Clears the permit information from the car (revokes the parking pass)
    public void clearPermit() {
        this.permit = null;
        this.permitExpiration = null;
        this.hasValidParkingPass = false;
    }

    // Override the toString method for debugging, string representation of the car object
    @Override
    public String toString() {
        return String.format(
                "Car[License: %s, Type: %s, Owner ID: %s, Valid Pass: %b, Permit Expiration: %s]",
                license, type, owner, hasValidParkingPass(), permitExpiration != null ? permitExpiration : "None"
        );
    }
}
