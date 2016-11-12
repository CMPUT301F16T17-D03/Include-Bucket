package cmput301_17.includebucket;

//
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }
//}

import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import java.util.Date;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleTests extends ActivityInstrumentationTestCase2 {
    public ExampleTests() {
        super(Application.class);
    }

//US 01.01.01

//As a rider, I want to request rides between two locations.
    @Test
    public void testNewRequest(){
        Date date =  new Date();
        //This should probably have 2 locations as opposed to just one postal code?
        Request request = new Request("test", date, "T6G1H1");
        assertNotNull(request);
    }

//US 01.02.01

//As a rider, I want to see current requests I have open.

    public void testGetRequests(){
        UserAccount user = UserAccount(1234);
        ArrayList<Request> request = getRequest(user);
        //Will getRequest never return Null? What if the User has no Requests?
        assertNotNull(request);
    }

//US 01.03.01

//As a rider, I want to be notified if my request is accepted.

    public void testAcceptRequest(){
        Date date =  new Date();
        UserAccount user = UserAccount(1234);
        Request request = newRequest("test", date, "T6G1H1", user);
        request.acceptRequest(user);
        assertbool(request.getAccepted());
    }

//US 01.04.01

//As a rider, I want to cancel requests.

    public void testCancelRequest(){
        Date date =  new Date();
        UserAccount user = UserAccount(1234);
        Request request = newRequest("test", date, "T6G1H1", user);
        cancelRequest(request);
        assertbool(!request.getAccepted());
    }

//US 01.05.01

//As a rider, I want to be able to phone or email the driver who accepted a request.

    public void testEmailDriver(){
        Date date =  new Date();
        UserAccount user = UserAccount(1234);
        UserAccount driver = UserAccount(4321);
        driver.setEmail(test@gmail.com);
        Request request = newRequest("test", date, "T6G1H1", user);
        request.acceptRequest(driver);
        assertNotNull(request.getDriver().getEmail());
    }

//US 01.06.01

//As a rider, I want an estimate of a fair fare to offer to drivers.

    public void testFairFare(){
// compare to previous payments/mile? Does Uber list a rate?
        Float fare = getFare(5);
        assertNotNull(fare);
    }

//US 01.07.01

//As a rider, I want to confirm the completion of a request and enable payment.

    public void testCompleteRequest(){
        Date date =  new Date();
        UserAccount user = UserAccount(1234);
        UserAccount driver = UserAccount(4321);
        driver.setEmail(test@gmail.com);
        Request request = newRequest("test", date, "T6G1H1", user);
        request.acceptRequest(driver);
        request.confirmDriver(driver);
        Boolean complete = request.completeRequest(); // calls payment with user and driver
        assertBool(complete);
    }

//US 01.08.01

//As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.

    public void testConfirmAcceptance(){
        Date date =  new Date();
        UserAccount user = UserAccount(1234);
        UserAccount drivera = UserAccount(4321);
        drivera.setEmail(test@gmail.com);
        UserAccount driverb = UserAccount(4322);
        driverb.setEmail(test@gmail.com);
        Request request = new Request("test", date, "T6G1H1", user);
        request.acceptRequest(drivera);
        request.acceptRequest(driverb);
        ArrayList<UserAccount> driverList = request.getDrivers();
        assertNotNull(driverList);
    }

    //  Status

//US 02.01.01

//As a rider or driver, I want to see the status of a request that I am involved in

    public void testRequestStatus(){
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        String status = request.getStatus();
        assertNotNull(status);
    }

    //  User profile

//US 03.01.01

    //As a user, I want a profile with a unique username and my contact information.
    public void testMakeProfile() {
        UserAccount user = UserAccount(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        assertFalse(UserList.contains(user.getUsername()));
        assertNotNull(user.getPhone());
        assertNotNull(user.getEmail());
    }


    // US 03.02.01 As a user, I want to edit the contact information in my profile.

    public void testEditContact(){
        UserAccount user = UserAccount(1234);
        String oldPhone = user.getPhoneNumber();
        String oldEmail = user.getEmail();
        user.setEmail("test@ualberta.ca");
        user.setPhoneNumber("17801234567");
        //Test that it was edited?
        assertFalse(user.getPhoneNumber() == oldPhone);
        assertFalse(user.getEmail() == oldEmail);
    }

    // US 03.03.01 As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.

    public void testFindUsername(){
        Uid user = Uid(1234);
        user.setUsername("Include_Bucket");
        String userName = user.getUsername;
        assertEquals(username, "Include_Bucket");
    }

    // Searching

    // US 04.01.01 As a driver, I want to browse and search for open requests by geo-location.

    public void testBrowseAndSearchLocation() {
        //do when we know elastic search
    }

    //        US 04.02.01 As a driver, I want to browse and search for open requests by keyword.

    public void testBrowseAndSearchKeyword() {
        //do when we know elastic search
    }

    // Accepting US 05.01.01 As a driver, I want to accept a request I agree with and accept that offered payment upon completion.

    public void testAcceptRequestDriver() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        request.accept(user);
        assertTrue(request.getAcceptStatus());
        user.pay(request);
        assertTrue(user, payStatus());
        request.complete();
        assertTrue(request.getComplete());

    }

    //  US 05.02.01 As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.

    public void testListPendingDriver() {
        ArrayList<Request> list = new ArrayList<Request>();
        Date date =  new Date();
        Request request1 = new Request("test", date, "T6G1H1");
        list.add(request1);
        Date date =  new Date();
        Request request2 = new Request("test2", date, "T6G1H2");
        list.add(request2);
        // add ui test later...
        assertTrue(list.contains(request1));
        assertTrue(list.contains(request2));
    }

    //         US 05.03.01 As a driver, I want to see if my acceptance was accepted.

    public void testSeeAcceptance() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        request.accept(user);
        assertTrue(request.getAcceptStatus());
        //add ui test later ...
    }

    //  US 05.04.01 As a driver, I want to be notified if my ride offer was accepted.

    public void testNotifyOfferAcceptance() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        request.accept2(user);
        // add ui test...
        assertTrue(request.getAcceptStatus2());
    }

    //  Offline behavior
    // US 08.01.01 As a driver, I want to see requests that I already accepted while offline.

    public void testSeeRequestsOffline() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        // add ui test...
        request.accept2(user);
        asserTrue(request.getAcceptStatus());
        // this is offline
    }

    //        US 08.02.01 As a rider, I want to see requests that I have made while offline.

    public void testSeeRequestsOfflineRider() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        // add ui test...
        request.accept2(user);
        asserTrue(request.getAcceptStatus());
        // this is offline seeing status

    }
    //        US 08.03.01 As a rider, I want to make requests that will be sent once I get connectivity again.

    public void testRequestQueueRider() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        request.addToQueue();
        assertTrue(request.inQueue());
        request.removeFromQueue();
        // assert that its in elastic search
    }
    //  US 08.04.01 As a driver, I want to accept requests that will be sent once I get connectivity again.

    public void testRequestQueueDriver() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        request.accept2(user);
        request.addToQueue2();
        assertTrue(request.inQueue2());
        request.removeFromQueue2();
        // assert that its in elastic search
    }
    // Location
    // US 10.01.01 As a rider, I want to specify a start and end geo locations on a map for a request.

    public void testSpecifyLocation() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        //maps location stuff
    }
    // US 10.02.01 (added 2016-02-29)

    // As a driver, I want to view start and end geo locations on a map for a request.
    public void testSearchLocation() {
        Date date =  new Date();
        Request request = new Request("test", date, "T6G1H1");
        Uid user = Uid(1234);
        user.setUsername("SomethingCleverIHope");
        user.setPhone("1234567890");
        user.setEmail("me@email.com");
        //maps location stuff
    }
}