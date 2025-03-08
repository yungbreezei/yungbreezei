package week9Project;

public class Address {

    String streetAddress1;
    String streetAddress2;
    String city;
    String state;
    String zipCode;

    // Constructor for an address with only the primary address line.
    public Address(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
        this.streetAddress2 = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
    }

    // Constructor for an address with primary address, city, state, and ZIP code
    public Address(String streetAddress1, String city, String state, String zipCode) {
        this.streetAddress1 = streetAddress1;
        this.streetAddress2 = "";   // Default to an empty string for optional fields
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    //Constructor for a full address with both address lines, city, state, and ZIP code
    public Address(String streetAddress1, String streetAddress2, String city, String state, String zipCode) {
        this.streetAddress1 = streetAddress1;
        this.streetAddress2 = streetAddress2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    // Getters: retrieves the city
    public String getCity() {
        return city;
    }

    // Getters: retrieves the state
    public String getState() {
        return state;
    }

    // Getters: retrieves the zipCode
    public String getZipCode() {
        return zipCode;
    }

    // Method to return the full address as a formatted string
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

    //  Returns the string representation of the address.
    @Override
    public String toString() {
        return getAddressInfo(); // Use getAddressInfo() to format the output
    }
}
