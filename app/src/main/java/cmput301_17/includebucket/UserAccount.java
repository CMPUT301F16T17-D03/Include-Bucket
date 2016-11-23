package cmput301_17.includebucket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import io.searchbox.annotations.JestId;

/**
 * This is the UserAccount class
 *
 * It holds and controls the information that is attached to the user's profile.
 */

public class UserAccount implements Serializable {

    @JestId
    private String userId;

    private String uniqueUserName, email, phoneNumber;
    private Boolean isLoggedIn;
    private ArrayList<String> riderRequestIds  = new ArrayList<>();
    private Collection<Request> driverRequests = new ArrayList<>();

    private UserState userState;

    /**
     * User can either be a rider or a driver.
     */
    public enum UserState {
        rider, driver
    }

    public UserAccount() {}

    /**
     * This is the constructor that creates a user with an id and sets its values.
     * @param
     */
    public UserAccount(String userLogin, String userEmail, String userPhone) {
        this.uniqueUserName = userLogin;
        this.email = userEmail;
        this.phoneNumber = userPhone;
        this.isLoggedIn = true;
        this.userState = null;
    }

    /**
     * Constructor to create a copy of an existing user with all of information
     * @param user
     */
    public UserAccount(UserAccount user) {
        this.uniqueUserName = user.getUniqueUserName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.isLoggedIn = true;
        this.userState = user.getUserState();
    }

    /**
     * This returns the user id
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * this sets the user id.
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUniqueUserName() {
        return uniqueUserName;
    }

    /**
     * This sets the unique username
     * @param name
     * @return
     */
    public boolean setUniqueUserName(String name){ //, Database database){
        if (
            //isUnique(name, database)
                true) {
            this.uniqueUserName = name;
            return true;
        }
        return false;
    }

    /**
     * This returrns the email address.
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * This sets the email address.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This returns the phone number.
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * This sets the phone number.
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * This returns the rider request list.
     * @return
     */
    public ArrayList<String> getRiderRequestIds() {
        return riderRequestIds;
    }

    /**
     * This sets the sets the list of requests.
     * @param riderRequestIds
     */
    public void setRiderRequestIds(ArrayList<String> riderRequestIds) {
        this.riderRequestIds = riderRequestIds;
    }

    /**
     * This adds a new request for a rider to the list
     * @param requestId
     */
    public void addRiderRequestId(String requestId){
        this.riderRequestIds.remove(requestId);
    }

    public void clearRiderRequestIds() { this.riderRequestIds.clear(); }
    /**
     * This returns the list of requests
     * @return
     */
    public Collection<Request> getDriverRequests() {
        return driverRequests;
    }

    /**
     * This sets the driver requests list.
     * @param driverRequests
     */
    public void setDriverRequests(ArrayList<Request> driverRequests) {
        this.driverRequests = driverRequests;
    }

    /**
     * This adds a new request for the driver to the list.
     * @param request
     */

    public void addDriverRequest(Request request){
        if (this.driverRequests.contains(request)){
            return;
        }
        this.driverRequests.add(request);
        request.addDriver(this);
    }

    /**
     * This cancels the request.
     * @param request
     */
    public void cancelDriverRequest(Request request){
        if (this.driverRequests.contains(request)){
            this.driverRequests.remove(request);
            request.removeDriver(this);
        }
        return;
    }

    /**
     * This returns the login status of the user
     * @return isLoggedIn
     */
    public boolean getLoginStatus() { return this.isLoggedIn; }

    /**
     * This changes the user's login status
     * @param status
     */
    public void setLoginStatus(Boolean status) { this.isLoggedIn = status; }

    /**
     * Gets the user category
     * @return
     */
    public UserState getUserState() {
        return userState;
    }

    /**
     * Sets the user category
     * @param userState
     */
    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}