package week9Project;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import week9Project.exceptions.*;

public class ParkingLot {
	
	
	// Attributes
    private String lotId;                           // Unique identifier for the parking lot
    public Address address;                        // Physical address of the parking lot
    public int capacity;                           // Maximum number of cars allowed
    private double baseRate;                        // Base parking rate (e.g., per hour)
    private boolean scanOnExit;                     // If true, charge on exit; otherwise, on entry
    public List<ParkingTransaction> transactions;  // Active parking transactions
    public List<Car> parkedCars;                  // Currently parked cars
    private static final long MAX_ALLOWED_PARKING_DURATION = 24; // 24-hour max parking rule
    
    /**
     * Constructor to initialize a ParkingLot.
     */
    public ParkingLot(String lotId, Address address, int capacity, double baseRate, boolean scanOnExit) {
        this.lotId = Objects.requireNonNull(lotId, "lotId cannot be null.");
        this.address = Objects.requireNonNull(address, "Address cannot be null.");
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero.");
        if (baseRate < 0) throw new IllegalArgumentException("Base rate cannot be negative.");
        
        this.capacity = capacity; // Max # of cars lot can hold
        this.baseRate = baseRate; // Base parking rate for the lot
        this.scanOnExit = scanOnExit; // Whether the lot charges on exit or entry, true of false
        this.parkedCars = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }
   
    /**
     * Handles car entry by creating a new ParkingTransaction.
     */
    public ParkingTransaction entry(Car car, ParkingPermit permit) 
    		throws InvalidParkingPassException, ParkingLotFullException, CarAlreadyParkedException {
    	
    	// Check if permit is active
        if (permit == null || permit.isExpired()) {
            throw new InvalidParkingPassException("Permit is expired or invalid.");
        }
        if (!car.getLicensePlate().equals(permit.getCar().getLicensePlate())) {
            throw new InvalidParkingPassException("The permit is not valid for this car.");
        }
    	
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
        
        // Create and store new ParkingTransaction
        ParkingTransaction transaction = new ParkingTransaction(permit, this);
        transactions.add(transaction);
        parkedCars.add(car);

        return transaction;
    }

    /**
     * Handles car exit, calculates the charge, and finalizes the transaction.
     */
    public void exit(ParkingTransaction transaction) 
    		throws InvalidParkingPassException, CarNotParkedException, ParkingDurationTooLongException {
    	
        // Validate if the transaction exists in the parking lot
        if (transaction == null || !transactions.contains(transaction)) {
            throw new CarNotParkedException("The car is not parked in this lot.");
        }
        
        // Check if the permit is expired
        Calendar expirationDate = transaction.getPermit().getExpirationDate();
        Calendar currentDate = Calendar.getInstance();
        
        // Compare the expiration date with the current date
        if (expirationDate.before(currentDate)) {
            throw new InvalidParkingPassException("The permit is expired or invalid.");
        }
        
        // Set exit time before calculations
        LocalDateTime exitTime = LocalDateTime.now();
        transaction.setExitTime(LocalDateTime.now());
        
        // Ensure exit time is set before using it in calculations
        if (transaction.getEntryTime() == null || transaction.getExitTime() == null) {
            throw new IllegalStateException("Entry or Exit time cannot be null.");
        }
        
        long duration = ChronoUnit.HOURS.between(transaction.getEntryTime(), exitTime);
        
        /* After a car exits, if the parking duration exceeds the allowed time limit (24 hours), 
        this exception can be thrown */       
        if (duration > MAX_ALLOWED_PARKING_DURATION) { 
            throw new ParkingDurationTooLongException("Parking duration exceeds the allowed limit.");
        }

        // Calculate charge based on policy
        double charge = calculateCharge(transaction);
        transaction.setCharge(charge);

        // Finalize the transaction
        parkedCars.remove(transaction.getCar());
        transactions.remove(transaction);
    }

    /**
     * Calculate the charge for a given transaction.
     */
    private double calculateCharge(ParkingTransaction transaction) {
        double rate = baseRate;

        // Applies discount for compact cars
        if (transaction.getCar().getType() == CarType.COMPACT) {
            rate *= 0.8; // 20% discount for compact cars
        }

        // Applies scan-on-exit logic
        if (scanOnExit) {
            long hoursParked = Math.max(ChronoUnit.HOURS.between(transaction.getEntryTime(), transaction.getExitTime()), 3);
            return rate * hoursParked;
        }

        return rate; // Flat rate on Entry
    }

    /**
     * Retrieve a ParkingTransaction by car.
     */
    public ParkingTransaction getTransaction(Car car) {
        return transactions.stream()
                .filter(transaction -> transaction.getCar().equals(car))
                .findFirst()
                .orElse(null); // Return null if no transaction found for the car
    }

    /**
     * Returns the base hourly rate for parking.
     */
    public double getHourlyRate() {
        return baseRate;
    }
	
    /**
     * Compares two lotIds based on the parking lot.
     */
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
        	return true;  // Check if the objects are the same instance
        }
        if (obj == null || getClass() != obj.getClass()) {
        	return false; // Check for null and class type
        }
        ParkingLot other = (ParkingLot) obj;
        return Objects.equals(lotId, other.lotId); // Compare using lotId
    }

    /**
     * Generates a hash code for the parking lot using the parking lot id.
     */
    @Override
    public int hashCode() {
        return Objects.hash(lotId); // Generate hash based on lotId
    }
	
    /**
     * Returns a string representation of the parking lot
     */
    @Override
    public String toString() {
		// return A formatted string describing the car
        return "ParkingLot[ID: " + lotId + ", Capacity: " + capacity + ", Rate: " + baseRate + "]";
    }
}
