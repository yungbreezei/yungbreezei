package week9Project;

import week9Project.exceptions.InvalidParkingPassException;
import week9Project.exceptions.ParkingLotFullException;

public class Main {
    public static void main(String[] args) {
        // Create an address for the parking lot
        Address lotAddress = new Address("123 Main St", "Cityville", "12345", null);

        // Initialize a ParkingLot with a capacity of 2 and a base rate of $5.00
        ParkingLot parkingLot = new ParkingLot("LotA", lotAddress, 2, 5.0, true);

        // Create some car objects
        Car car1 = new Car("ABC123", CarType.COMPACT, null, false, null);
        Car car2 = new Car("XYZ789", CarType.SUV, null, false, null);

        try {
            // Car 1 enters the lot
            ParkingTransaction transaction1 = parkingLot.entry(car1);
            System.out.println("Car 1 entered: " + transaction1);

            // Car 2 enters the lot
            ParkingTransaction transaction2 = parkingLot.entry(car2);
            System.out.println("Car 2 entered: " + transaction2);

            // Simulate Car 1 exiting after 4 hours
            Thread.sleep(1000); // Simulate time passing (for demo purposes)
            parkingLot.exit(transaction1);
            System.out.println("Car 1 exited: " + transaction1);

            // Attempt to add another car to a full lot
            Car car3 = new Car("LMN456", CarType.COMPACT, null, false, null);
            ParkingTransaction transaction3 = parkingLot.entry(car3);
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
} 
