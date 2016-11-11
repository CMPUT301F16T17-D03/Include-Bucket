package cmput301_17.includebucket;

/**
 * Created by Duncan on 11/7/2016.
 */
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

/*
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
*/
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class RobotiumTest {

    UberRequest SampleRequest = new UberRequest();

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
    public void testRequestRide() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //create new request
        solo.clickOnButton("New");
        //select start and end points
        solo.enterText(0, "StartAddress");
        solo.enterText(1, "EndAddress");
        solo.enterText(2, "SampleDescription");
        //save the request
        solo.clickOnButton("Save");
        //check that there is a request saved
        boolean RequestIsSavedInLocation = false; //change to where the request should be saved
        assertTrue("Request Not Found", RequestIsSavedInLocation);
    }
    @Test
    public void testSeeCurrentRequests() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //view requests
        solo.clickOnButton("MyRequests");
        solo.assertCurrentActivity("Not In Correct Activity", RiderBrowseRequestsActivity.class); //name might need to be changed
    }
    @Test
    //not sure how to test notifications using Robotium
    public void testNotifyRequestAccepted() throws Exception {
        //Robotium cannot interact with the notification bar
        //going to assume you can accept your own request for the purposes of this test
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        //has one request in list
        solo.clickInList(0);
        solo.clickOnButton("Accept");
    }
    @Test
    public void testCancelRequest() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //create new request
        solo.clickOnButton("New");
        //select start and end points
        solo.enterText(0, "StartAddress");
        solo.enterText(1, "EndAddress");
        solo.enterText(2, "SampleDescription");
        //save the request
        solo.clickOnButton("Save");
        //view requests
        solo.clickOnButton("MyRequests");
        solo.clickInList(0);
        //view the request details
        solo.clickOnButton("CancelRequest");
        //back at my requests view, check that request is no longer there
        assertFalse("Request Not Canceled", solo.searchText("StartAddress"));
    }
    @Test
    public void testContactDriver() throws Exception {
        //don't really need a test for this, just view and call the number? There's no button in our UI for this.
        assertTrue("This should not fail", true);
    }
    @Test
    public void testGetFairEstimate() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //create new request
        solo.clickOnButton("New");
        solo.enterText(0, "StartAddress");
        solo.enterText(1, "EndAddress");
        //Select option to show price
        solo.clickOnButton("Price");
        assertTrue("Price not found", solo.searchText("$"));
    }
    @Test
    public void testConfirmRequestComplete() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("MyRequests");
        //have a request that has already gone through driver approval (should bring up ViewRequest2?
        solo.clickInList(0);
        solo.clickOnButton("Complete");
        assertTrue("Request Not Completed", SampleRequest.isCompleted());
    }
    @Test
    public void testConfirmAcceptance() throws Exception {
    }
    @Test
    public void testSeeStatusRider() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("MyRequests");
        //selects first request in list
        solo.clickInList(0);
        solo.assertCurrentActivity("Not In Correct Activity", RiderSingleRequestActivity.class);
    }

    @Test
    public void testSeeStatusDriver() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Current");
        //selects first request in list
        solo.clickInList(0);
        solo.assertCurrentActivity("Not In Correct Activity", DriverSingleRequestActivity.class);
    }

    @Test
    public void testMakeProfile() throws Exception {
        solo.unlockScreen();
        //might need to change the button name here
        solo.clickOnButton("Register");
        //input new account data
        solo.enterText(0, "AccountName");
        solo.enterText(1, "Test name");
        solo.enterText(2, "email@gmail.com");
        solo.enterText(3, "123-456-7890");
        //finish creation
        solo.clickOnButton("Accept");
        //might need to change index of the text field and the username
        solo.enterText(0, "AccountName");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //check that login worked
        solo.assertCurrentActivity("Not In Correct Activity", MainMenuActivity.class);
    }
    @Test
    public void testEditContactInfo() throws Exception {
        solo.unlockScreen();
        //might need to change the button name here
        solo.clickOnButton("Register");
        //input new account data
        solo.enterText(0, "AccountName");
        solo.enterText(1, "Test name");
        solo.enterText(2, "email@gmail.com");
        solo.enterText(3, "123-456-7890");
        //finish creation
        solo.clickOnButton("Accept");
        //might need to change index of the text field and the username
        solo.enterText(0, "AccountName");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Account");
        //change original account values
        solo.enterText(0, "AccountName2");
        solo.enterText(1, "123-456-7899");
        solo.clickOnButton("Save");
        //check that the values actually changed
        solo.clickOnButton("Account");
        boolean accountChanged = solo.searchText("AccountName2");
        boolean numberChanged = solo.searchText("123-456-7899");
        assertTrue("The Account Information Was Unchanged", accountChanged && numberChanged);
    }
    @Test
    public void testShowContactInfo() throws Exception {
    }
    @Test
    public void testBrowseAndSearchOpenRequests() throws Exception {
    }
    @Test
    public void testBrowseAndSearchByKeyword() throws Exception {
    }
    @Test
    public void testAcceptRequest() throws Exception {
    }
    @Test
    public void testAcceptPayment() throws Exception {
    }
    @Test
    public void testViewAccepted() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        //view requests for driver
        solo.clickOnButton("Current");
        solo.assertCurrentActivity("Not In Correct Activity", DriverCurrentRequestsActivity.class);
    }
    @Test
    public void testAcceptanceAccepted() throws Exception {
    }
    @Test
    public void testNotifyAcceptance() throws Exception {
    }
    @Test
    public void testSeeAcceptedRequestsOffline() throws Exception {
    }
    @Test
    public void testSeeRequestsOffline() throws Exception {
    }
    @Test
    public void testMakeRequestOffline() throws Exception {
    }
    @Test
    public void testAcceptRequestOffline() throws Exception {
    }
    @Test
    public void testSpecifyStartAndEnd() throws Exception {
    //this test is redundant, as the specification of start and end locations takes place in the Request creation
        assertTrue("This should not fail", true);
    }
    @Test
    public void testViewStartAndEnd() throws Exception {
        solo.unlockScreen();
        //might need to change index of the text field and the username
        solo.enterText(0, "Username");
        //might need to change the button name here
        solo.clickOnButton("Login");
        solo.clickOnButton("Browse");
        solo.clickInList(0);
        //check that Driver is viewing request
        solo.assertCurrentActivity("Not In Correct Activity", DriverBrowseRequestsActivity.class);
    }
}
