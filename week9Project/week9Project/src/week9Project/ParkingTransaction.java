package week9Project;

import java.time.LocalDateTime;

public class ParkingTransaction {
	
    // Attributes 
    private Car car;                    // The car involved in the transaction
    private LocalDateTime entryTime;     // Time when the car enters the parking lot
    private LocalDateTime exitTime;      // Time when the car exits the parking lot (nullable until exit)
    private double charge;               // Parking fee for the transaction
    private ParkingLot parkingLot;       // The parking lot where the car is parked


    // Constructor to initialize a new parking transaction.
    public ParkingTransaction(Car car, LocalDateTime entryTime, ParkingLot parkingLot) {
        this.car = car;                  // Associate a car with the transaction
        this.entryTime = entryTime;      // Record the entry time
        this.exitTime = null;            // Exit time is null until the car leaves
        this.charge = 0.0;               // Initialize charge to zero
        this.parkingLot = parkingLot;    // Associate a parking lot with the transaction

    }

    // Exit method: Sets the exit time and applies the charge
    public void exit(LocalDateTime exitTime, double charge) {
        this.exitTime = exitTime;        // Set the exit time
        setCharge(charge);               // Validate and set the charge using the setter
    }
    
    // Setter method for charge for the parking transaction
    public void setCharge(double charge) {
        if (charge < 0) {
            throw new IllegalArgumentException("Charge cannot be negative");
        }
        this.charge = charge; // Update the charge method
    }
    
    // Getter: car type
    public Car getCar() {
        return car;
    }

    // Getter: entry time of the car
    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    // Getter: exit time of the car, if it exits
    public LocalDateTime getExitTime() {
        return exitTime;
    }

    // Getter: retrieves the parking fee for this transaction
    public double getCharge() {
        return charge;
    }
    
    // Method to get the associated parking lot for the transaction
    public ParkingLot getParkingLot() {
        return parkingLot;
    }
    
    // Provides a string representation of the parking transaction
    @Override
    public String toString() {
        return "ParkingTransaction[Car: " + car.getLicense() + 
               ", Entry: " + entryTime + 
               ", Exit: " + (exitTime != null ? exitTime : "Still Parked") + 
               ", Charge: " + charge + "]";
    }
}
