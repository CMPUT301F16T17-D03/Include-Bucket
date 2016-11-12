package cmput301_17.includebucket;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import cmput301_17.includebucket.UserAccount;
import io.searchbox.annotations.JestId;

/**
 * Created by Owner on 10/20/2016.
 *
 * This is the Request class. It holds the data that is attached to each request made by a specific
 * UserAccount.
 */
public class Request {

    @JestId
    String requestID;

    private String startLocation;
    private String endLocation;
    private UserAccount rider;
    private float fare;
    private String description;
    private ArrayList<String> keywords;
    private ArrayList<String> drivers;
    private boolean driverAccepted;
    private boolean riderAccepted;

    /**
     * The empty constructor.
     */
    public Request() {}

    /**
     * Intantiates a new Request.
     * @param loc1  The start location
     * @param loc2  The end location
     * @param rider The rider making a request
     */
    public Request(String loc1, String loc2, UserAccount rider) {
        this.requestID = null;
        this.startLocation = loc1;
        this.endLocation = loc2;
        this.rider = rider;
    }

    /**
     * Intantiates a new Request with specified keyword(s).
     * @param loc1  The start location
     * @param loc2  The end location
     * @param rider The rider making a request
     * @param keys  The keyword
     */
    public Request(String loc1, String loc2, UserAccount rider, ArrayList<String> keys) {
        this.requestID = null;
        this.startLocation = loc1;
        this.endLocation = loc2;
        this.rider = rider;
        this.keywords = keys;
    }

    public String getRequestID() {return requestID; }

    public void setRequestID(String requestID) { this.requestID = requestID; }

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
