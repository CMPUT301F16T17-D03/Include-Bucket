package cmput301_17.includebucket;

import java.util.ArrayList;

/**
 * Created by michelletagarino on 16-11-26.
 */
public class CreateRequestCommand implements Command {

    private Request request;

    public void createRequest(String loc1, String loc2, UserAccount rider, String story, ArrayList<UserAccount> pendingDrivers, UserAccount driver) {
        request = new Request(loc1, loc2, rider, story, pendingDrivers, driver);
    }

    @Override
    public void execute() {

        if (request != null) {

            ElasticsearchRequestController.CreateRequest createRequest;
            createRequest = new ElasticsearchRequestController.CreateRequest();
            createRequest.execute(request);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
