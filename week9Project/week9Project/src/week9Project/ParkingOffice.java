package week9Project;

import java.util.*;
import java.util.stream.Collectors;

import week9Project.exceptions.DuplicatePermitException;
import week9Project.exceptions.PermitExpiredException;
import week9Project.exceptions.PermitNotFoundException;

public class ParkingOffice<T extends Car> {

    private String officeName; // Name of the parking office
    private Map<String, Customer> customers; // Stores customers by customer ID
    private List<ParkingLot> parkingLots; // Manages multiple parking lots
    
    private Address parkingOfficeAddress;
    private TransactionManager transactionManager; // Manages parking transactions
    private PermitManager permitManager; // Handles parking permit operations


    /**
     * Initializes a ParkingOffice with the provided name and address.
     * @param officeName The name of the parking office.
     * @param address The address of the parking office.
     */
    public ParkingOffice(String officeName, Address address) {
        this.officeName = officeName;
        this.customers = new HashMap<>(); // Initializes customer registry
        this.parkingLots = new ArrayList<>();  // Initializes the list of parking lots
        
        this.parkingOfficeAddress = address;
        this.transactionManager = new TransactionManager(); // Initializes the TransactionManager
        this.permitManager = new PermitManager(); // Initializes the PermitManager
    }

    /**
     * Registers a new customer with the provided details.
     * Ensures that the customer ID is unique before adding them.
     * Prevents duplicate customers
     * @param customerId The unique ID of the customer.
     * @param name The name of the customer.
     * @param address The address of the customer.
     * @param phoneNumber The customer's phone number.
     */
    public void registerCustomer(String customerId, String name, Address address, String phoneNumber) {
        if (customers.containsKey(customerId)) {
        	// throws IllegalArgumentException If the customer ID already exists.
            throw new IllegalArgumentException("Customer ID already exists.");
        }
        customers.put(customerId, new Customer(customerId, name, address, phoneNumber));
    }
    
    /**
     * Registers a new car to a customer account by linking it with the provided license plate and type.
     * Prevents duplicate car registrations under the same customer.
     * @param customerId The ID of the customer to register the car under.
     * @param licensePlate The license plate of the car to be registered.
     * @param type The type of the car (e.g., Sedan, SUV).
     */
    public Car registerCar(String customerId, String licensePlate, CarType type) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
        	// throws IllegalArgumentException If the customer is not found 
            throw new IllegalArgumentException("Customer not found.");
        }
        
        if (customer.hasCar(licensePlate)) {
        	// throws IllegalArgumentException the car is already registered
            throw new IllegalArgumentException("Car with this license plate is already registered.");
        }

        // Create a car and associate it with the customer
        return customer.registerCar(licensePlate, type);  // return The registered car.
    }

    /**
     * Adds a parking lot to the parking office for managing parking spaces.
     * @param lot The parking lot to be added.
     */
    public void addParkingLot(ParkingLot lot) {
        parkingLots.add(lot);
    }

    /**
     * Retrieves all customers currently registered at the parking office.
     */
    public Collection<Customer> getCustomers() {
        return customers.values(); // return A collection of all customers.
    }
        
    /**
     * Retrieves all customer IDs in the system.
     */
    public Set<String> getCustomerIds() {
        return customers.keySet(); // return A set containing all customer IDs.
    }
    
    /**
     * Retrieves a set of all permit IDs associated with all customers.
     * @return A set of permit IDs.
     */
    public Set<String> getPermitIds() {
        return customers.values().stream()
                .flatMap(customer -> customer.getPermits().stream()) // Flatten the stream of permits
                .map(ParkingPermit::getId) // Extract the permit IDs
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves a set of permit IDs associated with a specific customer.
     * If the customer has no permits, an empty set is returned.
     * @param customer The customer whose permits to retrieve.
     * @return A set of permit IDs for the given customer.
     */
    public Set<String> getPermitIds(Customer customer) {
        return customer.getPermits() == null ? 
           Collections.emptySet() : 
           customer.getPermits().stream().map(ParkingPermit::getId).collect(Collectors.toSet());
    }
    
    /**
     * Retrieves all parking lots managed by the parking office.     
     **/
    public List<ParkingLot> getParkingLots() {
        return parkingLots; // return A list of all parking lots.
    }
    
    /**
     * Retrieves the name of the parking office.
     */
    public String getOfficeName() {
        return officeName; // return The name of the parking office.
    }

    /**
     * Retrieves the address of the parking office.
     */
    public Address getParkingOfficeAddress() {
        return parkingOfficeAddress; // return The address of the parking office
    }

    /**
     * Registers a car and returns a parking permit for it.
     * @param car The car to be registered.
     * @throws DuplicatePermitException 
     */
    public ParkingPermit register(Car car) throws DuplicatePermitException {
        return permitManager.register(car); // return The parking permit for the registered car.
    }
    
    /**
     * Registers a customer to the parking office.
     * @param customer The customer to be registered.
     */
    public void register(Customer customer) {
        if (!customers.containsKey(customer.getCustomerId())) {
            customers.put(customer.getCustomerId(), customer);
        }
    }
    
    /**
     * Parks a car in a specific parking lot on a given date, generating a parking transaction.
     * @param date The date and time when the car is parked.
     * @param permit The parking permit associated with the car.
     * @param lot The parking lot where the car is parked.
     */
    public ParkingTransaction park(Calendar date, ParkingPermit permit, ParkingLot lot) throws Exception {
        if (!parkingLots.contains(lot)) {
        	// throws Exception If the parking lot is not managed by this office.
            throw new Exception("Parking lot not managed by this office.");
        }
        // Proceed with parking transaction
        return transactionManager.park(date, permit, lot); // return A ParkingTransaction object representing the parking event.
    }
    
    /**
     * Retrieves the parking charges associated with a specific parking permit.
     * @param permit The parking permit for which to retrieve charges.
     */
    public Money getParkingCharges(ParkingPermit permit) {
        return transactionManager.getParkingCharges(permit); // return The parking charges for the permit.
    }

    /**
     * Retrieves the total parking charges for a specific customer.
     * @param customer The customer for whom to retrieve total charges.
     * @throws PermitExpiredException 
     * @throws PermitNotFoundException 
     */
    public Money getParkingCharges(Customer customer) throws PermitNotFoundException, PermitExpiredException {
        Money totalCharges = new Money(0);
        for (String permitId : permitManager.getPermitIds(customer)) {
            ParkingPermit permit = permitManager.getPermitById(permitId);
            totalCharges = totalCharges.add(transactionManager.getParkingCharges(permit));  // Sum up the charges
        }
        return totalCharges; // return The total parking charges for the customer.
    }

    /**
     * Provides a string representation of the parking office, including the name, address, 
     * number of customers, and parking lots.
     */
    @Override
    public String toString() {
    	// return A string representation of the parking office.
        return "ParkingOffice[Name: " + officeName + ", Office Address: " + parkingOfficeAddress + 
        	", Customers: " + customers.size() + ", Lots: " + parkingLots.size() + "]";
    }
}
