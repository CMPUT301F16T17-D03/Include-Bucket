package cmput301_17.includebucket;

import java.util.ArrayList;

/**
 * Created by Owner on 10/20/2016.
 */

public class UserAccount {
    //User
    // -newuser
    // -user id
    // -password
    // -get unique username
    // -get Riderrequests
    // -get Driverrequests
    // -get contact info
    // -change contact info
    // -stays logged in
    // - save in file for offline
    private String uniqueUserName;
    private String email;
    private String phoneNumber;
    private ArrayList<UberRequest> riderRequests;
    private ArrayList<UberRequest> driverRequests;
    private long uid;

    public UserAccount(long id){
        this.uid = id;
    }

    public String getUniqueUserName() {
        return uniqueUserName;
    }

    public boolean setUniqueUserName(String name){ //, Database database){
        if (
            //isUnique(name, database)
                true) {
            this.uniqueUserName = name;
            return true;
        }
        return false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<UberRequest> getRiderRequests() {
        return riderRequests;
    }

    public void setRiderRequests(ArrayList<UberRequest> riderRequests) {
        this.riderRequests = riderRequests;
    }

    public void addRiderRequest(UberRequest request){
        this.riderRequests.add(request);
    }

    public ArrayList<UberRequest> getDriverRequests() {
        return driverRequests;
    }

    public void setDriverRequests(ArrayList<UberRequest> driverRequests) {
        this.driverRequests = driverRequests;
    }

    public void addDriverRequest(UberRequest request){
        if (this.driverRequests.contains(request)){
            return;
        }
        this.driverRequests.add(request);
        request.addDriver(this.getUniqueUserName());
    }

    public void cancelDriverRequest(UberRequest request){
        if (this.driverRequests.contains(request)){
            this.driverRequests.remove(request);
            request.removeDriver(this.getUniqueUserName());
        }
        return;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }






}