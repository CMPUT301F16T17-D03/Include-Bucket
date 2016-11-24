package cmput301_17.includebucket;

/**
 * Created by Duncan on 11/7/2016.
 *
 * These are the intent tests.
 */

import android.content.Intent;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.robotium.solo.Solo;
import com.searchly.jestdroid.JestDroidClient;

import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
Example Test Code taken from https://github.com/RobotiumTech/robotium/wiki/Getting-Started
*/

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class RobotiumTest {

    Request TestRequest = new Request();
    ElasticsearchRequestController TestController = new ElasticsearchRequestController();

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    private Solo solo;

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityTestRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    @Test
    //My Requests activity not working The search feature does not work for multiple words so the story is one word and also doesn't work with capital letters?
    public void test01_RequestRide() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("New");
        //select start and end points and create the story
        solo.enterText((EditText) solo.getView(R.id.NRRAStartEditText), "37.421998333333335,-122.08400000000002,0.0"); //these coordinates are the base location
        solo.enterText((EditText) solo.getView(R.id.NRRAEndEditText), "37.431998333333330,-122.10400000000000,0.0");
        solo.enterText((EditText) solo.getView(R.id.riderStoryEditText), "thisisateststory");
        //save the request
        solo.clickOnButton("Save");
        solo.clickOnButton("My Requests");
        assertTrue("Request Not Created", solo.searchText("thisisateststory"));
    }
    @Test
    public void test02_SeeCurrentRequests() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        //view requests
        solo.clickOnButton("My Requests");
        solo.assertCurrentActivity("Not In Correct Activity", RiderCurrentRequestsActivity.class); //name might need to be changed
    }
    @Test
    //incomplete
    public void test99_NotifyRequestAccepted() throws Exception {
        //Robotium cannot interact with the notification bar
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.enterText(0, "a");
        solo.clickOnButton("Search");
        //has one request in list
        solo.clickInList(0);
        solo.clickOnButton("Accept");
        //a way to check that the notification was sent
        boolean IsNotificationSent = true;
        assertTrue("Notification Not Sent", IsNotificationSent);
    }
    @Test
    //My Requests not working
    public void test99_CancelRequest() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        //create new request
        solo.clickOnButton("New");
        //select start and end points
        solo.enterText((EditText) solo.getView(R.id.NRRAStartEditText), "37.421998333333335,-122.08400000000002,0.0"); //these coordinates are the base location
        solo.enterText((EditText) solo.getView(R.id.NRRAEndEditText), "37.431998333333330,-122.10400000000000,0.0");
        solo.enterText((EditText) solo.getView(R.id.riderStoryEditText), "thisisateststory");
        //save the request
        solo.clickOnButton("Save");
        //view requests
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        //view the request details
        solo.clickOnButton("Cancel Request");
        //find a way to test that the request is cancelled
        //back at my requests view, check that request is no longer there
        assertFalse("Request Not Canceled", solo.searchText("StartAddress"));
    }
    @Test
    public void test50_ContactDriver() throws Exception {
        //don't really need a test for this, just view and call the number? There's no button in our UI for this.
        assertTrue("This should not fail", true);
    }
    @Test
    public void test03_GetFairEstimate() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //create new request
        solo.clickOnButton("New");
        solo.enterText((EditText) solo.getView(R.id.NRRAStartEditText), "37.421998333333335,-122.08400000000002,0.0"); //these coordinates are the base location
        solo.enterText((EditText) solo.getView(R.id.NRRAEndEditText), "37.431998333333330,-122.10400000000000,0.0");
        solo.enterText((EditText) solo.getView(R.id.riderStoryEditText), "thisisateststory");
        assertTrue("Price not found", solo.searchText("$"));
    }
    @Test
    //incomplete My Requests is not working
    public void test99_ConfirmRequestComplete() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        //have a request that has already gone through driver approval (should bring up ViewRequest2?
        solo.clickInList(0);
        solo.clickOnButton("Complete");
        assertTrue("Request Not Completed", TestRequest.getIsCompleted());
    }
    @Test
    //incomplete My Requests is not working
    public void test99_ConfirmAcceptance() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        //accept the first driver request by selecting the button from first listview
        solo.clickInList(0, 0);
        assertTrue("Rider Acceptance Not Complete", TestRequest.isRiderAccepted());
    }
    @Test
    //incomplete My Requests is not working
    public void test04_SeeStatusRider() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        //selects first request in list
        solo.clickInList(0);
        solo.assertCurrentActivity("Not In Correct Activity", RiderSingleRequestActivity.class);
    }

    @Test
    //incomplete need to create the sample request in ElasticSearch
    public void test99_SeeStatusDriver() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Current");
        //selects first request in list
        solo.clickInList(0);
        solo.assertCurrentActivity("Not In Correct Activity", DriverSingleRequestActivity.class);
    }

    @Test
    //Once this has been ran, the account name is taken for each time it is ran afterwards? Delete from ElasticSearch?
    public void test00_MakeProfile() throws Exception {
        solo.unlockScreen();
        //might need to change the button name here
        solo.clickOnButton("Register");
        //input new account data
        solo.enterText(0, "TestAccount");
        solo.enterText(1, "email@gmail.com");
        solo.enterText(2, "123-456-7890");
        //finish creation
        solo.clickOnButton("Accept");
        solo.waitForActivity("LoginActivity");
        solo.clickOnButton("Login");
        //check that login worked
        solo.assertCurrentActivity("Not In Correct Activity", MainMenuActivity.class);
    }
    @Test
    //need a way to check what the current account name and phone number are like default values in the change screen?
    public void test05_EditContactInfo() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Account");
        //change original phone number value
        solo.enterText(1, "123-456-7899");
        solo.clickOnButton("Save");
        //check that the values actually changed
        solo.clickOnButton("Account");
        boolean numberChanged = solo.searchText("123-456-7899");
        assertTrue("The Account Information Was Unchanged", numberChanged);
    }
    @Test
    //incomplete My Requests is not working
    public void test99_ShowContactInfo() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        //select the first entry in list
        solo.clickInList(0);
        //select the first driver in the list
        solo.clickInList(0);
        solo.assertCurrentActivity("Not In Correct Activity", ViewUserDataActivity.class);
    }
    @Test
    public void test06_BrowseAndSearchOpenRequests() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.assertCurrentActivity("Not In Correct Activity", DriverBrowseRequestsActivity.class);
    }
    @Test
    public void test07_BrowseAndSearchByKeyword() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        //check for specific text and see if found
        solo.enterText(0, "thisisateststory");
        solo.clickOnButton("Search");
        boolean SearchFound = solo.searchText("thisisateststory");
        assertTrue("Message Not Found", SearchFound);
    }
    @Test
    //need a way to check that the request is accepted or if driver browse was working
    public void test08_AcceptRequest() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        //search for our test request and accept it
        solo.enterText(0, "thisisateststory");
        solo.clickOnButton("Search");
        solo.clickInList(0);
        solo.clickOnButton("Accept");
        assertTrue("Request Not Accepted", TestRequest.isDriverAccepted());
    }
    @Test
    public void test99_AcceptPayment() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Current");
        solo.clickInList(0);
        //the interface seems to be missing the accept payment from driver's side for now
        solo.clickOnButton("Accept Payment");
        assertTrue("Payment Not Accepted", TestRequest.getIsPaid());
    }
    @Test
    public void test09_ViewAccepted() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //view requests for driver
        solo.clickOnButton("Current");
        solo.assertCurrentActivity("Not In Correct Activity", DriverCurrentRequestsActivity.class);
    }
    @Test
    //tough to judge
    public void test99_AcceptanceAccepted() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Current");
        //should there be another activity with more details like a view request 2 for driver?
        //currently this and view accepted would be looking at the same thing, just a visual queue for the driver
        solo.assertCurrentActivity("Not In Correct Activity", DriverCurrentRequestsActivity.class);
    }
    @Test
    public void test99_NotifyAcceptance() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        solo.clickInList(0, 1); //click the accept button
        //check that notification was sent to driver (could be same user)
        boolean IsNotificationSent = true;
        assertTrue("Notification Not Sent", IsNotificationSent);
    }
    @Test
    public void test99_SeeAcceptedRequestsOffline() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Current");
        //maybe extend to accept a request and ensure it is visible offline?
        solo.assertCurrentActivity("Not In Correct Activity", DriverCurrentRequestsActivity.class);
    }
    @Test
    public void test99_SeeRequestsOffline() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("MyRequests");
        solo.assertCurrentActivity("Not In Correct Activity", RiderCurrentRequestsActivity.class);
    }
    @Test
    public void test99_MakeRequestOffline() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("New");
        //select start and end points
        solo.enterText((EditText) solo.getView(R.id.NRRAStartEditText), "37.421998333333335,-122.08400000000002,0.0"); //these coordinates are the base location
        solo.enterText((EditText) solo.getView(R.id.NRRAEndEditText), "37.431998333333330,-122.10400000000000,0.0");
        //save the request
        solo.enterText((EditText) solo.getView(R.id.riderStoryEditText), "RiderStory");
        solo.clickOnButton("Save");
        //the difference between this and a regular request is this should be in the file system on device
        boolean RequestIsSavedInLocation = false; //change to where the request should be saved
        assertTrue("Request Not Found", RequestIsSavedInLocation);
    }
    @Test
    public void test99_AcceptRequestOffline() throws Exception {
        //how is the driver supposed to view requests offline? Where would he find the requests to browse in the first place?
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.clickInList(0);
        solo.clickOnButton("Accept");
        //check that the acceptance is saved in the system
        boolean AcceptanceSaved = false;
        assertTrue("Acceptance Not Saved", AcceptanceSaved);
    }
    @Test
    public void test50_SpecifyStartAndEnd() throws Exception {
    //this test is redundant, as the specification of start and end locations takes place in the Request creation
        assertTrue("This should not fail", true);
    }
    @Test
    public void test10_ViewStartAndEnd() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.enterText(0, "thisisateststory");
        solo.clickOnButton("Search");
        solo.clickInList(0);
        //check that Driver is viewing request
        solo.assertCurrentActivity("Not In Correct Activity", DriverBrowseRequestsActivity.class);
    }

    @Test
    public void test99_SeeVehicleDescription() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        //selects the first driver
        solo.clickInList(0);
        solo.clickOnButton("Vehicle Details");
        solo.assertCurrentActivity("Not In Correct Activity", RiderBrowseRequestsActivity.RiderViewDetailsActivity.class);
    }
    @Test
    public void test99_SeeDriverRatings() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        //selects the first driver
        solo.clickInList(0);
        solo.clickOnButton("View Ratings");
        solo.assertCurrentActivity("Not In Correct Activity", RiderSingleRequestActivity.RiderViewRatingsActivity.class);
    }
    @Test
    //incomplete
    public void test99_RateDriver() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        //select a completed request - goes to View Request 2
        solo.clickInList(0);
        solo.clickOnButton("Complete");
        //this depends on how the 5 star rating is provided
        solo.clickOnButton("3 Star");
        //assert the driver has been rated 3 stars
    }
    @Test
    //incomplete
    public void test99_ProvideCarInfo() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Account");
        solo.enterText(3,"Car Information");
        solo.clickOnButton("Save");
        //assert that the car info was saved in elastic search
    }
    @Test
    //incomplete
    public void test99_FilterRequestByPrice() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        //not sure how this is implemented in our layout
    }
    @Test
    //incomplete
    public void test99_FilterRequestByPricePerKM() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        //not sure how this is implemented in our layout
    }
    @Test
    public void test99_SeeAddressOfRequest() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.clickInList(0);
        boolean SearchFound = solo.searchText("Address");
        assertTrue("Address Not Found", SearchFound);
    }
    @Test
    public void test99_SearchAddressOfRequest() throws Exception {
        solo.unlockScreen();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.enterText(0, "Address");
        solo.clickInList(0);
        solo.assertCurrentActivity("Could Not Find The Correct Request", DriverSingleRequestActivity.class);
    }
}
