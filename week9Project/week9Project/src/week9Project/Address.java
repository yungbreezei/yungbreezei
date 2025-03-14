package week9Project;

import java.util.Objects;
import java.util.regex.Pattern;

public class Address {

    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String state;
    private String zipCode;
    
    // Regex pattern for ZIP codes (5-digit or ZIP+4 format)
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5}(-\\d{4})?$");

    // Regex pattern for 2-letter state abbreviations (basic validation)
    private static final Pattern STATE_PATTERN = Pattern.compile("^[A-Z]{2}$");

    /**
     * Constructor for an address with primary address, city, state, and ZIP code.
     */
    public Address(String streetAddress1, String city, String state, String zipCode) {
        this(streetAddress1, "", city, state, zipCode); // Delegate to full constructor
    }

    /**
     * Full constructor with both address lines, city, state, and ZIP code.
     */
    public Address(String streetAddress1, String streetAddress2, String city, String state, String zipCode) {
        this.streetAddress1 = streetAddress1;
        this.streetAddress2 = streetAddress2;
        this.city = city;
        setState(state);  // Validate state
        setZipCode(zipCode); // Validate ZIP code
    }
    
    /** Getters */
    public String getStreetAddress1() {
        return streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }
    
    /** Setters with validation */
    public void setState(String state) {
        if (state != null && STATE_PATTERN.matcher(state.trim()).matches()) {
            this.state = state.trim();  // Store the trimmed state
        } else {
            throw new IllegalArgumentException("Invalid state abbreviation. Must be 2 uppercase letters.");
        }
    }

    public void setZipCode(String zipCode) {
        if (zipCode != null && ZIP_PATTERN.matcher(zipCode).matches()) {
            this.zipCode = zipCode;
        } else {
            throw new IllegalArgumentException("Invalid ZIP code format. Must be 5 or 9 digits.");
        }
    }


    /**
     * Returns the full address as a formatted string.
     */
    public String getAddressInfo() {
        // Use a StringBuilder for better performance with string concatenation
        StringBuilder info = new StringBuilder(streetAddress1);
        
        // Include the secondary address line if it's provided
        if (streetAddress2 != null && !streetAddress2.trim().isEmpty()) {
            info.append(", ").append(streetAddress2);
        }
        
        // Append the city, state, and ZIP code
        info.append(", ").append(city)
            .append(", ").append(state)
            .append(" ").append(zipCode);
        
        return info.toString();
    }
    
    /*
     *  equals() method should compare all relevant fields in
     *   Address class to determine equality.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
        	return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
        	return false;
        }
        
        Address address = (Address) obj;
        
        return streetAddress1.equals(address.streetAddress1) &&
               (streetAddress2 != null ? streetAddress2.equals(address.streetAddress2) : address.streetAddress2 == null) &&
               city.equals(address.city) &&
               state.equals(address.state) &&
               zipCode.equals(address.zipCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(streetAddress1, streetAddress2, city, state, zipCode);
    }

    /** String representation of the address */
    @Override
    public String toString() {
        return getAddressInfo(); // Use getAddressInfo() to format the output
    }
}
