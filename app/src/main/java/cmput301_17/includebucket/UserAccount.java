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
    private String uid;

    private String uniqueUserName, email, phoneNumber;
    private Boolean isLoggedIn;
    private Collection<Request> riderRequests;
    private Collection<Request> driverRequests;



    private UserCategory userCategory;


    /**
     * User can either be a rider or a driver.
     */
    public enum UserCategory {
        rider, driver
    }

    /**
     * The initialization constructor (creates an empty user). Will later be filled with values.
     */
    public UserAccount() {}

    /**
     * This is the constructor that creates a user with an id and sets values its values.
     * @param
     */
    public UserAccount(String userLogin, String userEmail, String userPhone) {
        this.uniqueUserName = userLogin;
        this.email = userEmail;
        this.phoneNumber = userPhone;
        this.isLoggedIn = true;
        this.userCategory = getUserCategory();
    }

    /**
     * This returns the user id
     * @return
     */
    public String getUid() {
        return uid;
    }

    /**
     * this sets the user id.
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /*
     * Every user account has a request list
     */
    public RequestList getRequestList() {
        return null;
    }

    public void setRequestList(RequestList requestList) {

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
    public Collection<Request> getRiderRequests() {
        return riderRequests;
    }

    /**
     * This sets the sets the list of requests.
     * @param riderRequests
     */
    public void setRiderRequests(ArrayList<Request> riderRequests) {
        this.riderRequests = riderRequests;
    }

    /**
     * This adds a new request for a rider to the list
     * @param request
     */
    public void addRiderRequest(Request request){
        this.riderRequests.add(request);
    }

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
    public UserCategory getUserCategory() {
        return userCategory;
    }

    /**
     * Sets the user category
     * @param userCategory
     */
    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }
}