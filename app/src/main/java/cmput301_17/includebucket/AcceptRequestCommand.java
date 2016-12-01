package cmput301_17.includebucket;

import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by michelletagarino on 16-11-26.
 */
public class AcceptRequestCommand implements Command {

    private Request request;
    private UserAccount user;

    public void createAcceptRequest(GeoPoint loc1, GeoPoint loc2, UserAccount rider, String story, ArrayList<UserAccount> pendingDrivers, UserAccount driver, String startAddress, String endAddress, Double fare, String id) {
        user = UserController.getUserAccount();
        request = new Request(loc1, loc2, rider, story, pendingDrivers, driver, startAddress, endAddress, fare, id);
        Log.i("Success",""+request.getRequestID());
        request.setRequestStatus(Request.RequestStatus.PendingDrivers);
        request.setDriverAccepted(true);
        request.addDriver(driver);
    }

    @Override
    public void execute() {

        try {
            if (request != null) {

                DriverRequestsController.deleteRequestFromElasticsearch(request);

                ElasticsearchRequestController.CreateRequest createRequest;
                createRequest = new ElasticsearchRequestController.CreateRequest();
                createRequest.execute(request);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong with the query");
        }
    }
}