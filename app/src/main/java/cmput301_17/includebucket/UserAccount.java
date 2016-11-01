package cmput301_17.includebucket;

import java.util.ArrayList;

/**
 * This is the UserAccount clas
 *
 * It holds and controls the information that is attached to the user's profile.
 */

public class UserAccount {
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

    private String uniqueUserName;
    private String email;
    private String phoneNumber;
    private ArrayList<UberRequest> riderRequests;
    private ArrayList<UberRequest> driverRequests;
    private long uid;

    /**
     * This is the constructor that creates a user with an id.
     * @param id
     */
    public UserAccount(long id){
        this.uid = id;
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
    public ArrayList<UberRequest> getRiderRequests() {
        return riderRequests;
    }

    /**
     * This sets the sets the list of requests.
     * @param riderRequests
     */
    public void setRiderRequests(ArrayList<UberRequest> riderRequests) {
        this.riderRequests = riderRequests;
    }

    /**
     * This adds a new request for a rider to the list
     * @param request
     */
    public void addRiderRequest(UberRequest request){
        this.riderRequests.add(request);
    }

    /**
     * Tgis returns the list of requests
     * @return
     */
    public ArrayList<UberRequest> getDriverRequests() {
        return driverRequests;
    }

    /**
     * This sets the driver requests list.
     * @param driverRequests
     */
    public void setDriverRequests(ArrayList<UberRequest> driverRequests) {
        this.driverRequests = driverRequests;
    }

    /**
     * This adds a new request for the driver to the list.
     * @param request
     */

    public void addDriverRequest(UberRequest request){
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
    public void cancelDriverRequest(UberRequest request){
        if (this.driverRequests.contains(request)){
            this.driverRequests.remove(request);
            request.removeDriver(this.getUniqueUserName());
        }
        return;
    }

    /**
     * This returns the user id
     * @return
     */
    public long getUid() {
        return uid;
    }

    /**
     * this sets the user id.
     * @param uid
     */

    public void setUid(long uid) {
        this.uid = uid;
    }


}