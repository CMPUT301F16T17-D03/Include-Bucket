package cmput301_17.includebucket;

import java.io.Serializable;
import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Created by Owner on 10/20/2016.
 *
 * This is the Request class. It holds the data that is attached to each request made by a specific
 * UserAccount.
 */
public class Request implements Serializable {

    @JestId
    String requestId;

    private String startLocation;
    private String endLocation;
    private String userName;
    private String riderStory = null;
    private double fare;
    private ArrayList<String> keywords;
    private ArrayList<UserAccount> drivers;
    private boolean driverAccepted;
    private boolean riderAccepted;
    private boolean isCompleted, isPaid;

    /**
     * Enum class that specifies the state of the status.
     * A request can be:
     *     Open (Rider just made a ride request, no driver has accepted it)
     *     Accepted by driver (A driver accepted the open request)
     *     Pending (A request that was accepted by a driver is waiting to be confirmed by the rider)
     *     Confirmed and completed (The rider confirmed that driver's acceptance and payment is completed)
     */
    public enum RequestState {
        requestOpen, acceptedByDriver, requestPending, requestCompleted
    }

    private RequestState requestState;

    /**
     * The empty constructor.

     */
    public Request() {}

    /**
     * Intantiates a new Request.
     * @param loc1  The start location
     * @param loc2  The end location
     * @param userName The rider making a request
     * @param story The rider's story (where is the rider going?)
     */
    public Request(String loc1, String loc2, String userName, String story, ArrayList<UserAccount> drivers) {
        this.requestId = null;
        this.userName = userName;
        this.startLocation = loc1;
        this.endLocation = loc2;
        this.riderStory = story;
        this.drivers = drivers;
    }

    public String getRequestID() {return requestId; }

    public void setRequestID(String requestID) { this.requestId = requestID; }

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

    public String getRiderStory() {
        return riderStory;
    }

    public void setRiderStory(String riderStory) {
        this.riderStory = riderStory;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<UserAccount> getDrivers() {
        return drivers;
    }

    public void setDrivers(ArrayList<UserAccount> drivers) {
        this.drivers = drivers;
    }

    public void addDriver(UserAccount driver){
        this.drivers.add(driver);
    }

    public void removeDriver(UserAccount driver){
        this.drivers.remove(driver);
    }

    public boolean isDriverAccepted() {
        return driverAccepted;
    }

    public void setDriverAccepted(boolean driverAccepted) {
        this.driverAccepted = driverAccepted;
    }

    public boolean hasRiderAccepted() {
        return riderAccepted;
    }

    public void setRiderAccepted(boolean riderAccepted) {
        this.riderAccepted = riderAccepted;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }

    public String getUserId() {
        return userName;
    }

    public boolean isRiderAccepted() {
        return Boolean.FALSE;
    }

    @Override
    public String toString() {
        String status = "Open";

        if (hasRiderAccepted()){
            status = "Driver Accepted";
        }
        else if (isDriverAccepted()) {
            if(getDrivers().size()== 1) {
                 status = "1 Pending Driver";
            }else{
                 status = getDrivers().size() +"Pending Drivers";
            }
        }

        return getRiderStory() + "\n\n" + "Price: " + getFare() + "\nStatus: " + status;
    }
}