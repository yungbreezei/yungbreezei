package week9Project;

import java.util.Objects;

public class ParkingPermit {
    private String permitId;
    private String licensePlate;

    public ParkingPermit(String permitId, String licensePlate) {
        this.permitId = permitId;
        this.licensePlate = licensePlate;
    }

    public String getId() {
        return permitId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ParkingPermit permit = (ParkingPermit) obj;
        return Objects.equals(permitId, permit.permitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permitId);
    }


    @Override
    public String toString() {
        return "ParkingPermit[ID: " + permitId + ", License Plate: " + licensePlate + "]";
    }
}
