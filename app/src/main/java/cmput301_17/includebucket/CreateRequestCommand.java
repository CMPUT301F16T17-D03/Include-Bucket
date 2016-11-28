package cmput301_17.includebucket;

import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;

/**
 * Created by michelletagarino on 16-11-26.
 */
public class CreateRequestCommand implements Command {

    private Request request;

    public void createRequest(GeoPoint loc1, GeoPoint loc2, UserAccount rider, String story, ArrayList<UserAccount> pendingDrivers, UserAccount driver, String startAddress, String endAddress, Double fare, Double distance) {
        request = new Request(loc1, loc2, rider, story, pendingDrivers, driver, startAddress, endAddress, fare, distance);

    }

    @Override
    public void execute() {

        try {
            if (request != null) {

                ElasticsearchRequestController.CreateRequest createRequest;
                createRequest = new ElasticsearchRequestController.CreateRequest();
                createRequest.execute(request);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong with the query");
        }
    }
}
