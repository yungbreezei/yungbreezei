package week9Project;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import week9Project.exceptions.CarAlreadyParkedException;
import week9Project.exceptions.CarNotParkedException;
import week9Project.exceptions.InvalidParkingPassException;
import week9Project.exceptions.ParkingDurationTooLongException;
import week9Project.exceptions.ParkingLotFullException;

public class ParkingLot {
	
	
	// Attributes
    private String lotId;                           // Unique identifier for the parking lot
    public Address address;                        // Physical address of the parking lot
    public int capacity;                           // Maximum number of cars allowed
    private double baseRate;                        // Base parking rate (e.g., per hour)
    private boolean scanOnExit;                     // If true, charge on exit; otherwise, on entry
    public List<ParkingTransaction> transactions;  // Active parking transactions
    public List<Car> parkedCars;                  // Currently parked cars
    private static final long MAX_ALLOWED_PARKING_DURATION = 24;
    
    // Constructor to initialize a parking lot with an ID, address, capacity, rate, and exit scan flag
    public ParkingLot(String lotId, Address address, int capacity, double baseRate, boolean scanOnExit) {
        this.lotId = lotId;  // Unique ID for Parking lot
        this.address = address; // Address of Parking lot
        this.capacity = capacity; // Max # of cars lot can hold
        this.baseRate = baseRate; // Base parking rate for the lot
        this.scanOnExit = scanOnExit; // Whether the lot charges on exit or entry, true of false
        this.parkedCars = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }
   
    // Handles car entry, creating a new ParkingTransaction
    public ParkingTransaction entry(Car car) throws InvalidParkingPassException, ParkingLotFullException, CarAlreadyParkedException {
        // Ensure the parking lot is not full before allowing entry
        if (transactions.size() >= capacity) {
            throw new ParkingLotFullException("Parking lot is full.");
        }
        
        // Check if the car is already parked in the lot
        for (ParkingTransaction transaction : transactions) {
            if (transaction.getCar().equals(car)) {
                throw new CarAlreadyParkedException("This car is already parked in the lot.");
            }
        }

        // Create a new parking transaction and track the car
        ParkingTransaction transaction = new ParkingTransaction(car, LocalDateTime.now(), this);
        transactions.add(transaction);
        parkedCars.add(car); // Keep track of cars currently in the lot

        return transaction;
    }

    // Handles car exit, calculates the charge, and finalizes the transaction.
    public void exit(ParkingTransaction transaction) throws InvalidParkingPassException, CarNotParkedException, ParkingDurationTooLongException {
        // Validate if the transaction exists in the parking lot
        if (transaction == null || !transactions.contains(transaction)) {
            throw new InvalidParkingPassException("Car has not entered the parking lot.");
        }
        
        // Check if the car is in the parking lot (not in the transaction list)
        if (!transactions.contains(transaction)) {
            throw new CarNotParkedException("The car is not parked in this lot.");
        }
        
        // After a car exits, if the parking duration exceeds the allowed time limit (24 hours), this exception can be thrown
        long duration = ChronoUnit.HOURS.between(transaction.getEntryTime(), transaction.getExitTime(null));
        
        if (duration > MAX_ALLOWED_PARKING_DURATION) { 
            throw new ParkingDurationTooLongException("Parking duration exceeds the allowed limit.");
        }

        // Record exit time and calculate charge
        transaction.exit(LocalDateTime.now(), baseRate);
        double charge = calculateCharge(transaction);
        transaction.setCharge(charge);

        // Update internal records to reflect the car's departure
        parkedCars.remove(transaction.getCar());
        transactions.remove(transaction);
    }


    //Calculate charge based on scan policy
    private double calculateCharge(ParkingTransaction transaction) {
        double rate = baseRate;

        // Applies discount for compact cars
        if (transaction.getCar().getType() == CarType.COMPACT) {
            rate *= 0.8; // 20% discount for compact cars
        }

        // Applies scan-on-exit logic
        if (scanOnExit) {
            long hoursParked = java.time.Duration.between(transaction.getEntryTime(), transaction.getExitTime(null)).toHours();
            return rate * Math.max(hoursParked, 3); // At least 3 hours charged
        }

        return rate; // Flat rate on Entry
    }

    
    // Allow retrieve specific ParkingTransaction by the car
    public ParkingTransaction getTransaction(Car car) {
        for (ParkingTransaction transaction : transactions) {
            if (transaction.getCar().equals(car)) {
                return transaction;
            }
        }
        return null; // Return null if no transaction found for the car
    }


	public double getHourlyRate() {
		return 0;
	}
	
    // Override the toString method to return a custom string representation of the parking lot
    @Override
    public String toString() {
        return "ParkingLot[ID: " + lotId + ", Capacity: " + capacity + ", Rate: " + baseRate + "]";
    }

}
