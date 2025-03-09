package week9Project;


import java.util.*;

import java.util.HashMap;
import java.util.Map;


public class Customer {
	
    private String customerId;            // Unique identifier for the customer
    private String name;                  // Customer's name
    private Address address;              // Address object representing customer's address
    private String phoneNumber;           // Customer's phone number (formatted)
    private Map<String, Car> cars;        // Maps license plates to Car objects
    private List<ParkingPermit> permits;  // Stores customer's parking permits
    private double balance = 0.0;         // Customer's outstanding balance
    
    public Customer(String customerId, String name, Address address, String phoneNumber) {
        this.customerId = customerId;     // Set customer ID
        this.name = name;                 // Set name
        this.address = address;           // Set address
        this.phoneNumber = phoneNumber;   // Set phone number
        this.cars = new HashMap<>();      // Initialize car storage
        this.permits = new ArrayList<>(); // Initialize permit storage
        this.balance = 0.0;               // Initialize balance
    }
    
    // Registers a new car for this customer
    public Car registerCar(String licensePlate, CarType type) {
        
    	// Create a new car object linked to this customer
        Car car = new Car(licensePlate, type, customerId, true, null);
        
        // Store the car in the map using the license plate as the key
        cars.put(licensePlate, car);
        return car; // Return the created car object
    }
    
    // Retrieves all cars registered to the customer
    public Collection<Car> getCars() {
        return cars.values(); // Return all registered cars
    }
    
    // Registers a new parking permit for this customer
    public void addPermit(ParkingPermit permit) {
        permits.add(permit);
    }

    // Retrieves all parking permits assigned to this customer
    public List<ParkingPermit> getPermits() {
        return permits;
    }
    
    // Get a specific car by license plate
    public Car getCar(String license) {
        return cars.get(license); // Get the car by license plate
    }
    
    // Ensure the phone number is in a valid format & Sets a new phone number for the customer.
    public void setPhoneNumber(String phoneNumber) {
        // Validate phone number format using a regular expression
        if (!phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
            throw new IllegalArgumentException("Invalid phone number format. Expected: XXX-XXX-XXXX");
        }
        this.phoneNumber = phoneNumber; // Update phone number if valid
    }
    
    // Adds a charge to the customer’s balance
    public void addCharge(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("Invalid charge amount: " + amount);
        }
    }
    
    // Getter: for customer ID
    public String getCustomerId() {
        return customerId;
    }
    
    // Checks if the customer has a car with the given license plate
    public boolean hasCar(String licensePlate) {
        return cars.containsKey(licensePlate);
    }
    
    // Getter: Customer's name
    public String getName() {
        return name;
    }
    
    // Getter: Customer's address
    public Address getAddress() {
        return address;
    }

    // Getter: Customer's phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    //Getter: Retrieves current balance for the customer
    public double getBalance() {
        return balance; // Ensure there's a getter for balance
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    
    // Custom string representation of the customer
    @Override
    public String toString() {
        return "Customer[ID: " + customerId + ", Name: " + name + ", Balance: " + balance + "]";
    }
}
