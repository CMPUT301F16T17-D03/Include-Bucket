package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * LauncherActivity
 *
 * This class is the first activity called when the application starts.
 * It has no UI (as stated in the AndroidManifest.xml file on line 27).
 * It is the activity class that decides whether LoginActivity or
 * MainMenuActivity is viewed after checking the login state of the user.
 * If the user is logged in, it will switch directly to MainMenuActivity.
 * If the user is not logged in, it will switch to LoginActivity.
 */
public class LauncherActivity extends Activity {

    UserAccount user = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserFileManager.initManager(this.getApplicationContext());

        user = UserController.getUserAccount();

        try {
            UserFileManager.getUserFileManager().saveUser(user);
        } catch (Exception e) {
            Log.i("Fail", "Could not deserialize the UserAccount in UserFileManager");
        }

        Intent intent;
        if (!user.getLoginStatus()){
            intent = new Intent(LauncherActivity.this,LoginActivity.class);
        }
        else {
            intent = new Intent(LauncherActivity.this,MainMenuActivity.class);
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
