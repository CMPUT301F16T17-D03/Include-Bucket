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
public class DriverRequestsFileManager {

    private static final String prefFile = "DRIVER_REQUESTS_FILE";
    private static final String drKey = "DRIVER_REQUESTS_KEY";

    private Context context;

    static private DriverRequestsFileManager requestListFileManager = null;

    public static void initManager(Context context) {
        if (requestListFileManager == null)
        {
            if (context == null)
            {
                throw new RuntimeException("Missing context for RequestListFileManager");
            }
            requestListFileManager = new DriverRequestsFileManager(context);
        }
    }

    public static DriverRequestsFileManager getRequestListFileManager() {
        if (requestListFileManager == null)
        {
            throw new RuntimeException("Did not initialize Manager");
        }
        return requestListFileManager;
    }

    public DriverRequestsFileManager(Context context) {
        this.context = context;
    }

    public RequestList loadRequestList() throws IOException, ClassNotFoundException {
        SharedPreferences settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        String requestListData = settings.getString(drKey, "");
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
        editor.putString(drKey, json);
        editor.commit();
    }
}
