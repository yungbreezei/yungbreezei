package week9Project;

import java.util.*;
import java.util.stream.Collectors;

public class ParkingOffice<T extends Car> {

    private String officeName; // Name of the parking office
    private Map<String, Customer> customers; // Stores customers by customer ID
    private List<ParkingLot> parkingLots; // Manages multiple parking lots

    // Constructor initializes office with name, customer map, and parking lot list
    public ParkingOffice(String officeName) {
        this.officeName = officeName;
        this.customers = new HashMap<>();
        this.parkingLots = new ArrayList<>();
    }

    // Register a new customer with their details
    public void registerCustomer(String customerId, String name, Address address, String phoneNumber) {
        customers.put(customerId, new Customer(customerId, name, address, phoneNumber));
    }

    // Register a car for a customer and associate with their account
    public T registerCar(String customerId, String licensePlate, CarType type) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found.");
        }

        // Create a car of type T and associate it with the customer
        T car = (T) customer.registerCar(licensePlate, type);
        return car;
    }

    // Add a parking lot to the office
    public void addParkingLot(ParkingLot lot) {
        parkingLots.add(lot);
    }

    // Process car entry into the first available parking lot
    public ParkingTransaction enterParking(T car) throws Exception {
        for (ParkingLot lot : parkingLots) {
            // Check if the parking lot has space available by comparing size of parkedCars to capacity
            if (lot.parkedCars.size() < lot.capacity) {
                return lot.entry(car);  // If space available, proceed with entry
            }
        }
        throw new Exception("No available parking lot.");  // If no space in any parking lot
    }

    // Process car exit by locating the transaction and completing it
    public void exitParking(T car) throws Exception {
        for (ParkingLot lot : parkingLots) {
            ParkingTransaction transaction = lot.getTransaction(car);
            if (transaction != null) {
                lot.exit(transaction);
                return;
            }
        }
        throw new Exception("Car not found in any lot.");
    }

    // Retrieve all customers
    public Collection<Customer> getCustomers() {
        return customers.values();
    }
    
    // WEEK 9: ADDING NEW METHODS
    
    // New method: Get all customer IDs
    public Set<String> getCustomerIds() {
        return customers.keySet();
    }

    public Set<String> getPermitIds() {
        return customers.values().stream()
                .flatMap(customer -> customer.getPermits().stream())
                .map(ParkingPermit::getId)
                .collect(Collectors.toSet());
    }

    // New method: Get permit IDs for a specific customer
    public Set<String> getPermitIds(Customer customer) {
        return customer.getPermits().stream()
                .map(ParkingPermit::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "ParkingOffice[Name: " + officeName + ", Customers: " + customers.size() + ", Lots: " + parkingLots.size() + "]";
    }
}
