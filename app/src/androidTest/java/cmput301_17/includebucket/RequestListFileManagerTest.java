package cmput301_17.includebucket;

import android.test.AndroidTestCase;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by michelletagarino on 16-11-25.
 */
public class RequestListFileManagerTest extends AndroidTestCase {

    /**
     * Tests that a request was written to file correctly
     */
    /*
    @Test
    public void requestListFileManager() {
        try {
            RequestList testRequests = new RequestList();
            Request testUser = new Request("TestStory");
            testRequests.addRequest(testUser);
            RequestListFileManager drfm = new RequestListFileManager(getContext());
            drfm.saveRequestList(testRequests);
            RequestList testRequests2 = new RequestList();
            assertTrue("TestDriver is in Rider List 2", testRequests2.contains(testUser));
            assertTrue("TestDriver is in Rider List", testRequests.contains(testUser));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("IOException Thrown" + e.toString(),false);
        }
    }

    @Test
    public void testRequestListFileManager() throws ClassNotFoundException {
        RequestList rl = new RequestList();
        Request testRequest = new Request("TestStory");
        rl.addRequest(testRequest);
        try {
            String str = RequestListFileManager.requestListToString(rl);
            assertTrue("String is too small",str.length() > 0);
            RequestList rl2 = RequestListFileManager.requestListFromString(str);
            assertTrue("sl2 size > 0",str.length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("IOException " +e,false);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            assertTrue("ClasNotFoundException " +e,false);
        }
    } */
}

