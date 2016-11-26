package cmput301_17.includebucket;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by michelletagarino on 16-11-25.
 */
public class RiderRequestsFileManager {

    private static final String prefFile = "RIDER_REQUESTS_FILE";
    private static final String rrKey = "RIDER_REQUESTS_KEY";

    private Context context;

    static private RiderRequestsFileManager requestListFileManager = null;

    public static void initManager(Context context) {
        if (requestListFileManager == null)
        {
            if (context == null)
            {
                throw new RuntimeException("Missing context for RequestListFileManager");
            }
            requestListFileManager = new RiderRequestsFileManager(context);
        }
    }

    public static RiderRequestsFileManager getRequestListFileManager() {
        if (requestListFileManager == null)
        {
            throw new RuntimeException("Did not initialize Manager");
        }
        return requestListFileManager;
    }

    public RiderRequestsFileManager(Context context) {
        this.context = context;
    }

    public RequestList loadRequestList() throws IOException, ClassNotFoundException {
        SharedPreferences settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        String requestListData = settings.getString(rrKey, "");
        if (requestListData.equals(""))
        {
            return new RequestList();
        }
        else
        {
            Gson gson = new Gson();
            RequestList requestList = gson.fromJson(requestListData, RequestList.class);
            return requestList;
        }
    }

    public void saveRequestList(RequestList rl) throws IOException {
        SharedPreferences settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(rl);
        editor.putString(rrKey, json);
        editor.commit();
    }
}
