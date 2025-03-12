package week9Project;

import java.time.LocalDate;

import week9Project.exceptions.CarAlreadyParkedException;
import week9Project.exceptions.CarNotParkedException;
import week9Project.exceptions.InvalidParkingPassException;
import week9Project.exceptions.ParkingDurationTooLongException;
import week9Project.exceptions.ParkingLotFullException;

public class Main {
    public static void main(String[] args) throws CarNotParkedException, ParkingDurationTooLongException, CarAlreadyParkedException {
        // Create some car objects with appropriate attributes
        Car car1 = createCar("ABC123", CarType.COMPACT, "customer1", LocalDate.now().plusDays(30));
        Car car2 = createCar("XYZ789", CarType.SUV, "customer2", LocalDate.now().plusDays(7));

        // Display car details
        System.out.println("Car 1 Details: " + car1);
        System.out.println("Car 2 Details: " + car2);

        // Try registering the cars in a parking lot
        ParkingLot parkingLot = new ParkingLot("LotA", new Address("123 Main St", "Cityville", "12345", null), 2, 5.0, true);

        try {
            // Car 1 enters the parking lot
            ParkingTransaction transaction1 = parkingLot.entry(car1, null);
            System.out.println("Car 1 entered: " + transaction1);

            // Car 2 enters the parking lot
            ParkingTransaction transaction2 = parkingLot.entry(car2, null);
            System.out.println("Car 2 entered: " + transaction2);

            // Simulate Car 1 exiting after 4 hours
            Thread.sleep(1000); // Simulate time passing (for demo purposes)
            parkingLot.exit(transaction1);
            System.out.println("Car 1 exited: " + transaction1);

            // Try to add another car to the full lot
            Car car3 = createCar("LMN456", CarType.COMPACT, "customer3", LocalDate.now().plusDays(14));
            ParkingTransaction transaction3 = parkingLot.entry(car3, null);
            System.out.println("Car 3 entered: " + transaction3);

        } catch (ParkingLotFullException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InvalidParkingPassException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error: Simulation interrupted.");
        }

        // Print the final state of the parking lot
        System.out.println("\nFinal Parking Lot State: " + parkingLot);
    }

    // Method to create a Car object with a license plate, type, owner ID, and permit expiration date
    private static Car createCar(String licensePlate, CarType carType, String owner, LocalDate expirationDate) {
        // Return a new Car instance
        return new Car(licensePlate, carType, owner, true, expirationDate);  // Assuming the car has a valid parking pass by default
    }
}
