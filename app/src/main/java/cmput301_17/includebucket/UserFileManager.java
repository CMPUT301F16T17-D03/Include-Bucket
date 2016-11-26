package cmput301_17.includebucket;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbRequest;
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
public class UserFileManager {

    private static final String prefFile = "USER_FILE";
    private static final String userKey = "USER_KEY";

    private Context context;

    static private UserFileManager userFileManager = null;

    public static void initManager(Context context) {
        if (userFileManager == null)
        {
            if (context == null)
            {
                throw new RuntimeException("Missing context for UserFileManager");
            }
            userFileManager = new UserFileManager(context);
        }
    }

    public static UserFileManager getUserFileManager() {
        if (userFileManager == null)
        {
            throw new RuntimeException("Did not initialize Manager");
        }
        return userFileManager;
    }


    public UserFileManager(Context context) {
        this.context = context;
    }



    static public String userToString(UserAccount u) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(u);
        oo.close();
        byte bytes[] = bo.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    static public UserAccount userFromString(String userData) throws ClassNotFoundException, IOException {
        ByteArrayInputStream bi = new ByteArrayInputStream(Base64.decode(userData, Base64.DEFAULT));
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (UserAccount) oi.readObject();
    }

    public UserAccount loadUser() throws IOException, ClassNotFoundException {
        SharedPreferences settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        String userData = settings.getString(userKey, "");
        if (userData.equals(""))
        {
            return new UserAccount();
        }
        else
        {
            Gson gson = new Gson();
            UserAccount user = gson.fromJson(userData, UserAccount.class);
            return user;
        }
    }

    public void saveUser(UserAccount u) throws IOException {
        SharedPreferences settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(u);
        editor.putString(userKey, json);
        editor.commit();
    }
}

