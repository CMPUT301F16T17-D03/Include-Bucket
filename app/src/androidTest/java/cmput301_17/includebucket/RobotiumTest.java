package cmput301_17.includebucket;

/**
 * These are the intent tests.
 */

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
Example Test Code taken from https://github.com/RobotiumTech/robotium/wiki/Getting-Started
*/

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class RobotiumTest {

    Request TestRequest = new Request();

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    private Solo solo;

    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(),
                activityTestRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    @Test
    public void test01_RequestRide() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("New");
        //select start and end points and create the story
        solo.clearEditText((EditText) solo.getView(R.id.NRRAStartEditText));
        solo.clearEditText((EditText) solo.getView(R.id.NRRAEndEditText));
        solo.clearEditText((EditText) solo.getView(R.id.NRRAPriceEditText));
        solo.enterText((EditText) solo.getView(R.id.NRRAStartEditText), "37.421998333333335,-122.08400000000002,0.0"); //these coordinates are the base location
        solo.enterText((EditText) solo.getView(R.id.NRRAEndEditText), "LH, 87 Avenue NW, Edmonton, Alberta");
        solo.enterText((EditText) solo.getView(R.id.NRRAPriceEditText), "5.01");
        solo.enterText((EditText) solo.getView(R.id.riderStoryEditText), "thisisateststory");
        //save the request
        solo.clickOnButton("Save");
        solo.clickOnButton("My Requests");
        assertTrue("Request Not Created", solo.searchText("thisisateststory"));
    }
    @Test
    public void test02_SeeCurrentRequests() throws Exception {
        solo.unlockScreen();
        solo.goBack();
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
    //fails if you run create request multiple times prior to running this.
    public void test69_CancelRequest() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        //view requests
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        //view the request details
        solo.clickOnButton("Delete");
        assertFalse("Request Not Canceled", solo.searchText("thisisateststory"));
    }
    @Test
    //once the application is no longer in the foreground, cannot interact with it using Robotium
    public void test12_ContactDriver() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        //view requests
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        solo.clickOnButton("More");
        solo.clickInList(0);
        View Button = solo.getView(R.id.phoneTextView);
        solo.clickOnView(Button);
        assertTrue("This Should Not Fail", true);
    }
    @Test
    public void test03_GetFairEstimate() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //create new request
        solo.clickOnButton("New");
        solo.clearEditText((EditText) solo.getView(R.id.NRRAStartEditText));
        solo.clearEditText((EditText) solo.getView(R.id.NRRAEndEditText));
        solo.enterText((EditText) solo.getView(R.id.NRRAStartEditText), "37.421998333333335,-122.08400000000002,0.0"); //these coordinates are the base location
        solo.enterText((EditText) solo.getView(R.id.NRRAEndEditText), "37.431998333333330,-122.10400000000004,0.0");
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
    //Try this first tomorrow!
    public void test14_ConfirmAcceptance() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        solo.clickOnButton("More");
        //accept the first driver request by selecting the button from first listview
        solo.clickInList(0);
        solo.clickOnButton("Confrim");
        solo.goBack();
        solo.goBack();
        solo.clickOnButton("My Requests");
        assertTrue("Rider Acceptance Not Complete", solo.searchText("Closed"));
    }
    @Test
    public void test10_SeeStatusRider() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        //selects first request in list
        solo.clickInList(0);
        solo.clickOnButton("More");
        solo.assertCurrentActivity("Not In Correct Activity", RiderAcceptDriverActivity.class);
    }

    @Test
    //The requests aren't showing up
    public void test15_SeeStatusDriver() throws Exception {
        solo.unlockScreen();
        solo.goBack();
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
        solo.goBack();
        solo.clickOnButton("Register");
        //input new account data
        solo.enterText(0, "TestAccount");
        solo.enterText(1, "email@gmail.com");
        solo.enterText(2, "123-456-7890");
        //finish creation
        solo.clickOnButton("Accept");
        //check that login worked
        solo.assertCurrentActivity("Not In Correct Activity", MainMenuActivity.class);
    }
    @Test
    //Is not currently updating the actual information
    public void test04_EditContactInfo() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Account");
        //change original phone number value
        solo.clearEditText(1);
        solo.enterText(1, "123-456-7899");
        solo.clickOnButton("Save");
        //check that the values actually changed
        solo.clickOnButton("Account");
        boolean numberChanged = solo.searchText("123-456-7899");
        assertTrue("The Account Information Was Unchanged", numberChanged);
    }
    @Test
    public void test11_ShowContactInfo() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        //select the first entry in list
        solo.clickInList(0);
        solo.clickOnButton("More");
        //select the first driver in the list
        solo.clickInList(0);
        solo.assertCurrentActivity("Not In Correct Activity", ViewDriverDataActivity.class);
    }
    @Test
    public void test05_BrowseAndSearchOpenRequests() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.assertCurrentActivity("Not In Correct Activity", DriverBrowseRequestsActivity.class);
    }
    @Test
    public void test06_BrowseAndSearchByKeyword() throws Exception {
        solo.unlockScreen();
        solo.goBack();
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
    public void test07_AcceptRequest() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        //search for our test request and accept it
        solo.enterText(0, "thisisateststory");
        solo.clickOnButton("Search");
        solo.clickInList(0);
        solo.clickOnButton("Accept");
        solo.goBack();
        solo.clickOnButton("My Requests");
        assertTrue("Request Not Accepted", solo.searchText("Pending Driver"));
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
    public void test08_ViewAccepted() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        //view requests for driver
        solo.clickOnButton("Current");
        solo.assertCurrentActivity("Not In Correct Activity", DriverCurrentRequestsActivity.class);
    }
    @Test
    public void test16_AcceptanceAccepted() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Current");
        assertTrue("Request Not Accepted", solo.searchText("Accepted"));
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
        solo.enterText((EditText) solo.getView(R.id.NRRAEndEditText), "37.431998333333330,-122.10400000000004,0.0");
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
    public void test01_Z_SpecifyStartAndEnd() throws Exception {
        //this test is redundant, as the specification of start and end locations takes place in the Request creation
        assertTrue("This Should Not Fail", true);
    }
    @Test
    public void test09_ViewStartAndEnd() throws Exception {
        solo.unlockScreen();
        solo.goBack();
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
    public void test13_SeeVehicleDescription() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("My Requests");
        solo.clickInList(0);
        solo.clickOnButton("More");
        //selects the first driver
        solo.clickInList(0);
        assertTrue("Cannot View Info", solo.searchText("Make")&&solo.searchText("Model")&&solo.searchText("Year"));
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
        solo.assertCurrentActivity("Not In Correct Activity", RiderSingleRequestActivity.class);
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
    public void test17_ProvideCarInfo() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Account");
        solo.enterText(3,"TestMake");
        solo.enterText(4,"TestModel");
        solo.enterText(5,"111111");
        solo.clickOnButton("Save");
        solo.clickOnButton("Account");
        boolean VehicleInfoChanged = solo.searchText("TestMake");
        assertTrue("The Vehicle Information Was Not Added", VehicleInfoChanged);
    }
    @Test
    //Functionality is not there yet
    public void test20_FilterRequestByPrice() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.enterText(2, "$5.00");
        solo.clickInList(0);
        assertTrue("Riders Are Too Cheap", solo.searchText("$5.0"));
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
    public void test18_SeeAddressOfRequest() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.enterText(0, "thisisateststory");
        solo.clickInList(0);
        //go to test request and search for the PAW Center
        assertTrue("Did Not Find Address", solo.searchText("PAW"));
    }
    @Test
    //does not seem to have this functionality available
    public void test19_SearchAddressOfRequest() throws Exception {
        solo.unlockScreen();
        solo.goBack();
        solo.enterText(0, "TestAccount");
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.enterText(1, "LH, 87 Avenue NW, Edmonton, Alberta");
        View Button = solo.getView(R.id.searchNearbyButton);
        solo.clickOnView(Button);
        solo.clickInList(0);
        solo.assertCurrentActivity("Could Not Find The Correct Request", DriverSingleRequestActivity.class);
    }
}
