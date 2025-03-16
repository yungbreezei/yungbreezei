package week9Project;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

/**
 * This is the class that manages the parking transactions. It stores the permit
 * and which lot the vehicle is using.
 */
public class ParkingTransaction {
    
    // Attributes 
    private Car car;                    // The car involved in the transaction
    private LocalDateTime entryTime;     // Time when the car enters the parking lot
    private LocalDateTime exitTime;      // Time when the car exits the parking lot (nullable until exit)
    private Money charge;               // Parking fee for the transaction
    private ParkingLot parkingLot;       // The parking lot where the car is parked
    
    // Week 10 improvements
    private Calendar transactionDate;    // Date when the transaction was created
    private Money feeCharged;            // The amount charged for this transaction
    private ParkingPermit permit;        // Parking permit used for this transaction

    /**
     * Constructor to initialize a new parking transaction.
     * @param permit The parking permit associated with this transaction.
     * @param parkingLot The parking lot where the car is parked.
     */
    public ParkingTransaction(ParkingPermit permit, ParkingLot parkingLot) {
        this.car = permit.getCar(); // Associate the car from the permit with the transaction
        this.entryTime = LocalDateTime.now();  // Record the entry time as the current time
        this.exitTime = null;  // Exit time is null until the car leaves
        this.parkingLot = Objects.requireNonNull(parkingLot, "Parking lot cannot be null");
        this.transactionDate = Calendar.getInstance();  // Set transaction date to current time
        this.feeCharged = new Money(0.0);  // Initialize feeCharged with a default value
        this.charge = new Money(0.0);  // Ensure charge is initialized
        this.permit = permit;  // Use the permit passed to the constructor
    }
    
    /**
     * Sets the exit time and applies the charge for the parking transaction.
     * @param exitTime The time when the car exits the parking lot.
     * @param chargeAmount The charge for the parking transaction.
     */
    public void exit(LocalDateTime exitTime, double chargeAmount) {
        this.exitTime = exitTime;
        this.charge = new Money(chargeAmount); // Store the charge using the Money class
    }
    
    /**
     * Sets the charge for the parking transaction.
     * @param chargeAmount The charge for the parking transaction.
     */
    public void setCharge(double chargeAmount) {
        this.charge = new Money(chargeAmount); // Create a new Money object with the charge amount
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
    
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = Objects.requireNonNull(exitTime, "Exit time cannot be null");
    }
    
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = Objects.requireNonNull(entryTime, "Entry time cannot be null");
    }

    // Getter: retrieves the parking fee for this transaction
    public Money getCharge() {
        return charge != null ? charge : new Money(0.0);
    }
    
    // Method to get the associated parking lot for the transaction
    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    // Getter for the transaction date
    public Calendar getTransactionDate() {
        return transactionDate;
    }

    // Getter for the fee charged
    public Money getFeeCharged() {
        return feeCharged;
    }

    // Getter for the parking permit used in this transaction
    public ParkingPermit getPermit() {
        return permit;
    }
    
 /* Method to check if the parking transaction is completed
  *    checks if the transaction is finished. 
  *    In this case, it checks whether the exitTime is not null (meaning the car has exited) and 
  *    whether a charge has been applied (i.e., charge > 0).
  *    If both conditions are met, the transaction is considered 
  *    complete and isCompleted() returns true; otherwise, it returns false
  */
    public boolean isCompleted() {
        return exitTime != null && feeCharged.compareTo(new Money(0)) > 0; // Check if exitTime is set and a charge has been applied
    }  
    
 /* Method to complete the parking transaction
  * It checks if exitTime is already set. 
  * If so, it throws an exception to prevent completing an already completed transaction.
	It ensures that both exitTime and feeCharged are non-null.
	It sets exitTime and assigns the feeCharged amount.
  */
    public void complete(LocalDateTime exitTime, Money feeCharged) {
        if (this.exitTime != null) {
        	// throws IllegalStateException If the transaction has already been completed.
            throw new IllegalStateException("Transaction is already completed.");
        }
        if (feeCharged.compareTo(new Money(0)) <= 0) {  
        	// throws IllegalArgumentException If the fee is less than or equal to zero.
            throw new IllegalArgumentException("Fee must be greater than zero.");
        }
        // Set exitTime and both charge and feeCharged fields
        this.exitTime = Objects.requireNonNull(exitTime, "Exit time cannot be null");
        this.charge = Objects.requireNonNull(feeCharged, "Fee cannot be null");
        this.feeCharged = feeCharged;  // Update the feeCharged field
    }

    /**
     * Compares two ParkingTransactions based on their entry times and cars
     */
    @Override
    public boolean equals(Object obj) {
    	//obj The object to compare with 
        if (this == obj) {
        	return true;
        }
        if (!(obj instanceof ParkingTransaction)) {
        	return false;
        }
        ParkingTransaction that = (ParkingTransaction) obj;
        return Objects.equals(car, that.car) && Objects.equals(entryTime, that.entryTime);
    }

    /* 
     * Override hashCode to ensure consistent hashing based on car and entry time
     */
    @Override
    public int hashCode() {
        return Objects.hash(car, entryTime);
    }
    
    /* 
     * Provides a string representation of the parking transaction
     */
    @Override
    public String toString() {
        return "ParkingTransaction[Car: " + car.getLicensePlate() + 
               ", Entry: " + entryTime + 
               ", Exit: " + (exitTime != null ? exitTime : "Still Parked") + 
               ", Charge: " + (charge != null ? charge.getDollars() + "." + charge.getCents() : "No Charge") + 
               ", Transaction Date: " + transactionDate.getTime() + 
               ", Fee Charged: " + feeCharged.getDollars() + "." + feeCharged.getCents() + "]";
    }


}
