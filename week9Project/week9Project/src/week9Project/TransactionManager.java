package week9Project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import week9Project.exceptions.CarAlreadyParkedException;
import week9Project.exceptions.InvalidParkingPassException;
import week9Project.exceptions.NoActiveTransactionException;


/**
 * This is the class that manages all the parking transactions.
 */
public class TransactionManager {


	    private ArrayList<ParkingTransaction> transactions;
	    private HashMap<String, ArrayList<ParkingTransaction>> vehicleTransaction;

	    
	    // Constructor to initialize the transaction manager
	    public TransactionManager() {
	        this.transactions = new ArrayList<>();
	        this.vehicleTransaction = new HashMap<>();
	    }

	    /**
	     * Creates a parking transaction, adds it to the transaction list, and associates it with the permit.
	     * @param date The date when the vehicle is parked.
	     * @param permit The parking permit for the vehicle.
	     * @param lot The parking lot where the vehicle is parked.
	     */
	    public ParkingTransaction park(Calendar date, ParkingPermit permit, ParkingLot lot) throws CarAlreadyParkedException {
	        // Store transactions by vehicle's license plate
	        String licensePlate = permit.getCar().getLicensePlate();
	        
	        // Check if the car is already parked in the lot
	        if (vehicleTransaction.containsKey(licensePlate)) {
	            throw new CarAlreadyParkedException("Car with license plate " + licensePlate + " is already parked.");
	        }
	        ParkingTransaction transaction = new ParkingTransaction(permit, lot);
	        transactions.add(transaction);
	        
	        vehicleTransaction.computeIfAbsent(licensePlate, k -> new ArrayList<>()).add(transaction);

	        return transaction;  // return The newly created ParkingTransaction.
	    }
	    
	    

	    /**
	     * Calculates total parking charges for a specific parking permit.
	     * @param permit The parking permit to calculate charges for.
	     */
	    public Money getParkingCharges(ParkingPermit permit) {
	    	
	        // Initialize total charges with a default value of 0
	        Money totalCharges = new Money(0);
	        String licensePlate = permit.getCar().getLicensePlate();
	        
	        if (vehicleTransaction.containsKey(licensePlate)) {
	            for (ParkingTransaction transaction : vehicleTransaction.get(licensePlate)) {
	                System.out.println("Adding charge: " + transaction.getCharge().getDollars());
	                totalCharges = totalCharges.add(transaction.getCharge());
	            }
	        }

	        return totalCharges; // return The total parking charges as a Money object.
	    }

	    /**
	     * Calculates total parking charges for a specific vehicle using its license plate.
	     * @param licensePlate The license plate of the vehicle.
	     */
	    public Money getParkingCharges(String licensePlate) {
	        Money totalCharges = new Money(0);

	        if (vehicleTransaction.containsKey(licensePlate)) {
	            for (ParkingTransaction transaction : vehicleTransaction.get(licensePlate))
	            {
	                totalCharges = totalCharges.add(transaction.getCharge());
	            }
	        }

	        return totalCharges; // return The total parking charges as a Money object.
	    }

	    /**
	     * Processes the exit of a car by completing the parking transaction associated
	     *  with the given permit.
	     * @param permit The parking permit of the car exiting the parking lot.
	     */
	    public void completeTransaction(ParkingPermit permit) throws NoActiveTransactionException, InvalidParkingPassException {
	        String licensePlate = permit.getCar().getLicensePlate();
	        boolean transactionCompleted = false;

	        if (vehicleTransaction.containsKey(licensePlate)) {
	            for (ParkingTransaction transaction : vehicleTransaction.get(licensePlate)) {
	                if (!transaction.isCompleted()) {
	                    // Here you can implement the logic to calculate the parking fee based on time, lot, etc.
	                    // For now, using a static fee of 25.0
	                	transaction.complete(LocalDateTime.now(), new Money(25.0));  // Ensure this is setting the charge
	                    transactionCompleted = true;

	                }
	            }
	        }
	        if (!transactionCompleted) {
	            throw new NoActiveTransactionException("No active parking transaction found for permit: " + permit.getId());
	        }
	        
	        // Additional check for permit validity
	        if (permit.isExpired()) {
	            throw new InvalidParkingPassException("The parking permit " + permit.getId() + " is expired.");
	        }
	    }
	    
	}
