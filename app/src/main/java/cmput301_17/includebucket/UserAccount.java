package cmput301_17.includebucket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import io.searchbox.annotations.JestId;

/**
 * UserAccount
 *
 * This is the UserAccount class
 *
 * It holds and controls the information that is attached to the user's profile.
 */

public class UserAccount implements Serializable {

    @JestId
    private String userId;
    private String uniqueUserName, email, phoneNumber;
    private String vehicleMake, vehicleModel, vehicleYear;
    private Boolean isLoggedIn;

    private UserState userState;

    protected ArrayList<Listener> listeners;

    /**
     * User can either be a rider or a driver.
     */
    public enum UserState {
        rider, driver
    }

    public UserAccount(){}

    public UserAccount(String userLogin, String userEmail, String userPhone) {
        this.uniqueUserName = userLogin;
        this.email = userEmail;
        this.phoneNumber = userPhone;
        this.isLoggedIn = Boolean.FALSE;
        listeners = new ArrayList<>();
    }

    /**
     * This is the constructor that creates a user with an id and sets its values.
     * @param
     */
    public UserAccount(String userLogin, String userEmail, String userPhone, String make, String model, String year) {
        this.uniqueUserName = userLogin;
        this.email = userEmail;
        this.phoneNumber = userPhone;
        this.isLoggedIn = Boolean.FALSE;
        this.userState = null;
        this.vehicleMake = make;
        this.vehicleModel = model;
        this.vehicleYear = year;
        listeners = new ArrayList<>();
    }

    private void notifyListeners() {
        for (Listener listener : getListeners()) {
            listener.update();
        }
    }

    private ArrayList<Listener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        return listeners;
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
        notifyListeners();
    }

    public String getUniqueUserName() {
        return uniqueUserName;
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
        notifyListeners();
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
        notifyListeners();
    }

    /**
     * This returns the login status of the user
     * @return isLoggedIn
     */
    public boolean getLoginStatus() {
        try {
            return this.isLoggedIn;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This changes the user's login status
     * @param status
     */
    public void setLoginStatus(Boolean status) {
        this.isLoggedIn = status;
        notifyListeners();
    }

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
        notifyListeners();
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
        notifyListeners();
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
        notifyListeners();
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
        notifyListeners();
    }

    @Override
    public String toString() {
        return getUniqueUserName() + "\n\n" + "\nEmail: " + getEmail() + "\nPhone: " + getPhoneNumber();
    }

    public boolean equals(Object compareUserAccount) {
        if (compareUserAccount != null &&
                compareUserAccount.getClass()==this.getClass())
        {
            return this.equals((UserAccount)compareUserAccount);
        }
        else
        {
            return false;
        }
    }

    public void addListener(Listener l) {
        getListeners().add(l);
    }
}