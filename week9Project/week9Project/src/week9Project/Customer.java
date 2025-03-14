package week9Project;


import java.util.*;

/**
 * Represents a customer who can own cars and parking permits within the system.
 */
public class Customer {
	
    private String customerId;            // Unique identifier for the customer
    private String name;                  // Customer's name
    private Address address;              // Address object representing customer's address
    private String phoneNumber;           // Customer's phone number (formatted)
    private Map<String, Car> cars;        // Maps license plates to Car objects
    private List<ParkingPermit> permits;  // Stores customer's parking permits
    private double balance;         	  // Customer's outstanding balance
    private static final int MAX_CARS = 3; // Maximum cars a customer can register

    /*
     * Constructs a new Customer.
     */
    public Customer(String customerId, String name, Address address, String phoneNumber) {
        this.customerId = customerId;     // Set customer ID
        this.name = name;                 // Set name
        this.address = address;           // Set address
        setPhoneNumber(phoneNumber); 	  // Ensures phone format validation
        this.cars = new HashMap<>();      // Initialize car storage
        this.permits = new ArrayList<>(); // Initialize permit storage
        this.balance = 0.0;               // Initialize balance
    }
    
    /**
     * Registers a new car for this customer.
     * registerCar(String, CarType) now prevents duplicate cars.
     */
    public Car registerCar(String licensePlate, CarType type) {
        if (cars.size() >= MAX_CARS) {
            throw new IllegalStateException("Customer cannot register more than " + MAX_CARS + " cars.");
        }
        if (cars.containsKey(licensePlate)) {
            throw new IllegalArgumentException("Car with this license plate is already registered.");
        }
        
    	// Create a new car object linked to this customer
        Car car = new Car(licensePlate, type, customerId, true, null);
        
        cars.put(licensePlate, car);  // Store the car in the map using the license plate as the key
        return car; // Return The newly registered Car object
    }
    
    // Retrieves all cars registered to the customer
    public Collection<Car> getCars() {
        return cars.values(); // Return a collection of all the customer's cars
    }
    
    // Assigns a parking permit to this customer.
    public void addPermit(ParkingPermit permit) {
        permits.add(permit); // param permit The ParkingPermit to be added
    }

    // Retrieves all parking permits assigned to this customer
    public List<ParkingPermit> getPermits() {
        return permits; // return a list of the customer's parking permits
    }
    
    /**
     * Retrieves a specific car owned by this customer.
     */
    public Car getCar(String licensePlate) {
        return cars.get(licensePlate); // return the Car object if found, otherwise null
    }
    
    /**
     * Updates the customer's phone number after validating its format.
     * Sets a new phone number for the customer.
     */
    public void setPhoneNumber(String phoneNumber) {
        // param phoneNumber The new phone number (format: XXX-XXX-XXXX)
        if (!phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
        	//
            throw new IllegalArgumentException("Invalid phone number format. Expected: XXX-XXX-XXXX");
        }
        this.phoneNumber = phoneNumber; // Update phone number if valid
    }
    
    /**
     * Adds a charge to the customer’s balance.
     * addCharge(double) throws an exception for non-positive amounts.
     */
    public void addCharge(double amount) {
    	// param amount The charge amount (must be positive)
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("Invalid charge amount: " + amount);
        }
    }
    
    /**
     * Retrieves the customer's ID.
     */
    public String getCustomerId() {
        return customerId;
    }
    
    /**
     * Checks if the customer owns a car with the given license plate.
     * @param licensePlate The license plate to check
     */
    public boolean hasCar(String licensePlate) {
        return cars.containsKey(licensePlate); // return true if the car exists, false otherwise
    }
    
    /**
     * Retrieves the customer's name.
     */
    public String getName() {
        return name; // return Customer's name
    }
    
    /**
     * Retrieves the customer's address.
     */    
    public Address getAddress() {
        return address; // @return Customer's address
    }

    /**
     * Retrieves the customer's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber; // return Customer's phone number

    }
    
    /**
     * Retrieves the customer's current balance.
     */
    public double getBalance() {
        return balance; // return Outstanding balance
    }
    
    /**
     * Compares two Customer objects based on their unique ID.
     * @return True if the customer IDs match, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // param obj The object to compare with
    	if (this == obj) {
        	return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
        	return false;
        }
        Customer customer = (Customer) obj;
        return customerId.equals(customer.customerId); // Compare by unique ID
    }

    
    /**
     * Generates a hash code for the customer using the unique ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(customerId); // return The hash code value: unique ID
    }
    
    /**
     * Returns a formatted string representation of the Customer object.
     */
    @Override
    public String toString() {
    	// return String representation of the customer
        return "Customer[ID: " + customerId + ", Name: " + name + ", Balance: " + balance + "]";
    }
}
