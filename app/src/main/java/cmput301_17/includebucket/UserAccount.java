package cmput301_17.includebucket;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * This is the UserAccount class
 *
 * It holds and controls the information that is attached to the user's profile.
 */

public class UserAccount implements UserAccountInterface {

    /**
     * User
     * -newuser
     * user id
     * -password
     * get unique username
     * get Riderrequests
     * get Driverrequests
     * get contact info
     * change contact info
     * stays logged in
     * save in file for offline
     *
     */

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

    @JestId
    private String uid, uniqueUserName, name, email, phoneNumber;
    private ArrayList<UserRequest> riderRequests;
    private ArrayList<UserRequest> driverRequests;

    /**
     * This is the constructor that creates a user with an id.
     * @param
     */
    public UserAccount(String userLogin, String userName, String userEmail, String userPhone) {
        this.uniqueUserName = userLogin;
        this.name = userName;
        this.email = userEmail;
        this.phoneNumber = userPhone;
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
     * This sets the emila address.
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
    public ArrayList<UserRequest> getRiderRequests() {
        return riderRequests;
    }

    /**
     * This sets the sets the list of requests.
     * @param riderRequests
     */
    public void setRiderRequests(ArrayList<UserRequest> riderRequests) {
        this.riderRequests = riderRequests;
    }

    /**
     * This adds a new request for a rider to the list
     * @param request
     */
    public void addRiderRequest(UserRequest request){
        this.riderRequests.add(request);
    }

    /**
     * Tgis returns the list of requests
     * @return
     */
    public ArrayList<UserRequest> getDriverRequests() {
        return driverRequests;
    }

    /**
     * This sets the driver requests list.
     * @param driverRequests
     */
    public void setDriverRequests(ArrayList<UserRequest> driverRequests) {
        this.driverRequests = driverRequests;
    }

    /**
     * This adds a new request for the driver to the list.
     * @param request
     */

    public void addDriverRequest(UserRequest request){
        if (this.driverRequests.contains(request)){
            return;
        }
        this.driverRequests.add(request);
        request.addDriver(this.getUniqueUserName());
    }

    /**
     * This cancels the request.
     * @param request
     */
    public void cancelDriverRequest(UserRequest request){
        if (this.driverRequests.contains(request)){
            this.driverRequests.remove(request);
            request.removeDriver(this.getUniqueUserName());
        }
        return;
    }

    public boolean isLoggedIn() {
        return Boolean.FALSE;
    }
}