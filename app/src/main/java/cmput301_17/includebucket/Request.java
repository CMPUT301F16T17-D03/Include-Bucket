package cmput301_17.includebucket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import io.searchbox.annotations.JestId;

/**
 * Created by Owner on 10/20/2016.
 *
 * This is the Request class. It holds the data that is attached to each request made by a specific
 * UserAccount.
 */
public class Request implements Serializable {

    @JestId
    String requestID;

    private String startLocation;
    private String endLocation;
    private UserAccount rider;
    private UserAccount driver = new UserAccount();
    private String riderStory = null;
    private double fare;
    private ArrayList<String> keywords;
    private Collection<UserAccount> pendingDrivers;
    private boolean driverAccepted;
    private boolean riderAccepted;
    private boolean isCompleted, isPaid;


    /**
     * Enums that specify the state of the status.
     * A request can be:
     *     Open (Rider just made a ride request, no driver has accepted it)
     *     Accepted by driver (A driver accepted the open request)
     *     Pending (A request that was accepted by a driver is waiting to be confirmed by the rider)
     *     Confirmed and completed (The rider confirmed that driver's acceptance and payment is completed)
     */
    public enum RequestStatus {
        Open, Accepted, Pending, Closed
    }

    private RequestStatus requestStatus;

    /**
     * The empty constructor.
     */
    public Request() {}

    /**
     * Intantiates a new Request.
     * @param loc1  The start location
     * @param loc2  The end location
     * @param rider The rider making a request
     * @param story The rider's story (where is the rider going?)
     */
    public Request(String loc1, String loc2, UserAccount rider, String story, Collection<UserAccount> pendingDrivers, UserAccount driver) {
        this.requestID = null;
        this.startLocation = loc1;
        this.endLocation = loc2;
        this.rider = rider;
        this.riderStory = story;
        this.pendingDrivers = pendingDrivers;
        this.driver = driver;
        requestStatus = null;
    }

    public String getRequestID() {return requestID; }

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

    public UserAccount getRider() {
        return rider;
    }

    public void setRider(UserAccount rider) {
        this.rider = rider;
    }

    //public String getRiderUserName() { return getRider().getUniqueUserName(); }

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

    public Collection<UserAccount> getDrivers() {
        return pendingDrivers;
    }

    public void setDrivers(Collection<UserAccount> drivers) {
        this.pendingDrivers = pendingDrivers;
    }

    public void addDriver(UserAccount driver){
        this.pendingDrivers.add(driver);
    }

    public void removeDriver(UserAccount driver){
        this.pendingDrivers.remove(driver);
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

    public void setIsCompleted(Boolean isCompleted) {this.isCompleted = isCompleted;}

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }

    public void setUser(UserAccount user){
        this.rider = user;
    }

    public void chooseDriver(UserAccount user){
        this.driver = user;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String toString() {

        String status = null;
        if (hasRiderAccepted()){
            status = "Closed";
        }

        else if (isDriverAccepted()) {
            if(getDrivers().size()== 1) {
                status = "1 Pending Driver";
            } else{
                status = getDrivers().size() +" Pending Drivers";
            }
        }

/*        if (getRequestStatus() == RequestStatus.Pending) {
            status = getDrivers().size() + " Pending Drivers";
        }
        else status = getRequestStatus().toString();
*/
        return "Driver: " + getRiderStory() + "\n" + "Price: " + getFare() + "\nStatus: " + status;
    }
}