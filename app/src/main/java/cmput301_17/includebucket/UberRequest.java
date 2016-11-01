package cmput301f16t17_d03.includebucket;

import java.util.ArrayList;

/**
 * Created by Owner on 10/20/2016.
 */

public class UberRequest {
/**
 * Request
 * start location
 * end location
 * rider
 * fare/fare calc 0-1
 * drivers
 * bool driveraccpeted
 *
 */

    private String startLocation;
    private String endLocation;
    private UserAccount rider;
    private float fare;
    private String description;
    private ArrayList<String> keywords;
    private ArrayList<String> drivers;
    private boolean driverAccepted;
    private boolean riderAccepted;

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public UserAccount getRider() {
        return rider;
    }

    public void setRider(UserAccount rider) {
        this.rider = rider;
    }

    public float getFare() {
        return fare;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<String> getDrivers() {
        return drivers;
    }

    public void setDrivers(ArrayList<String> drivers) {
        this.drivers = drivers;
    }

    public void addDriver(String username){
        this.drivers.add(username);
    }

    public void removeDriver(String username){
        this.drivers.remove(username);
    }

    public boolean isDriverAccepted() {
        return driverAccepted;
    }

    public void setDriverAccepted(boolean driverAccepted) {
        this.driverAccepted = driverAccepted;
    }

    public boolean isRiderAccepted() {
        return riderAccepted;
    }

    public void setRiderAccepted(boolean riderAccepted) {
        this.riderAccepted = riderAccepted;
    }
}
