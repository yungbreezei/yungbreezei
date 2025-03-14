package week9Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import week9Project.exceptions.DuplicatePermitException;
import week9Project.exceptions.PermitExpiredException;
import week9Project.exceptions.PermitNotFoundException;

/**
 * This is the manager class which manages all the parking permits.
 */
public class PermitManager {

    private HashMap<String, ParkingPermit> permits = new HashMap<>();

  /**
  * This method will create an object of ParkingPermit class and will add it
  * to the permits collection.
  * Note: Assume that the expiration date will be one year from the current date.
  */  
    public ParkingPermit register(Car car) throws DuplicatePermitException {
        // Check if the car already has a permit
        for (ParkingPermit permit : permits.values()) {
            if (permit.getCar().equals(car)) {
                throw new DuplicatePermitException("A permit already exists for car with license plate: " + car.getLicensePlate());
            }
        }
        String permitId = generatePermitId(); // Generate a unique permit ID
        
        Calendar expirationDate = Calendar.getInstance(); // Set expiration date to one year from now
        expirationDate.add(Calendar.YEAR, 1); // Set expiration to one year from now
        
        Calendar registrationDate = Calendar.getInstance();// Set registration date to the current date
        
        // Create a new ParkingPermit with all required fields
        ParkingPermit permit = new ParkingPermit(permitId, car, expirationDate, registrationDate);
        
        permits.put(permitId, permit);// Store the permit in the map
        return permit; // Return the created permit to confirm registration
    }
    
     // Retrieves all permit IDs.
	  public Collection<String> getPermitIds() {
	      return permits.keySet(); // return a collection of all registered permit IDs.
	  }
	  
	  /* Retrieves all permit IDs associated with a given customer.
	   * parameter customer The customer whose permits are being retrieved.
	   */
	    public Collection<String> getPermitIds(Customer customer) {
	      List<String> customerPermitIds = new ArrayList<>();
	      
	      for (ParkingPermit permit : permits.values()) {
	          if (permit.getCar().getOwner().equals(customer.getCustomerId())) {
	              customerPermitIds.add(permit.getId());
	          }
	      }
	      return customerPermitIds; // return a collection of permit IDs associated with the customer.
	  }
	  
	  //  Retrieves a ParkingPermit by its ID.
	  public ParkingPermit getPermitById(String permitId) throws PermitNotFoundException, PermitExpiredException {
		    ParkingPermit permit = permits.get(permitId);
		    
		    if (permit == null) {
		    	// throws permit if No parking permit found with ID
		        throw new PermitNotFoundException("No parking permit found with ID: " + permitId);
		    }
		    
		    if (permit.isExpired()) {
		        throw new PermitExpiredException("The parking permit " + permitId + " has expired.");
		    }
		    
		    return permit; // return the corresponding Permit 
	  }
	
	  // Generates a unique permit ID.
	  private String generatePermitId() {
	      return "P" + (permits.size() + 1); // return a new permit ID as a String.
	  }
}
