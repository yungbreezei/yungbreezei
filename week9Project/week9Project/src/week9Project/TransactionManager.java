package week9Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TransactionManager {

	/**
	 * This is the class that manages all the parking transactions.
	 */
	    private ArrayList<ParkingTransaction> transactions;
	    private HashMap<String, ArrayList<ParkingTransaction>> vehicleTransaction;


	    /**
	     * This method will create a parking transaction and will add it to the transactions list.
	     */
	    public ParkingTransaction park(Calendar date, ParkingPermit permit, ParkingLot lot) {
			return null;
	    	
	    }

	    public Money getParkingCharges(ParkingPermit permit) {
			return null;
	    	
	    }

	    public Money getParkingCharges(String licensePlate) {
			return null;
	    	
	    }

	}
