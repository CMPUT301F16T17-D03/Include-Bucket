package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by michelletagarino on 16-10-30.
 *
 * This class is the first activity called when the application starts.
 * It has no UI (as stated in the AndroidManifest.xml file on line 27).
 * It is the activity class that decides whether LoginActivity or
 * MainMenuActivity is viewed after checking the login state of the user.
 * If the user is logged in, it will switch directly to MainMenuActivity.
 * If the user is not logged in, it will switch directly to LoginActivity.
 */
public class LauncherActivity extends Activity {

    /**
     * Maybe a solution would be to save the last person that was logged in into a local file
     * by way of using GSON. We would then have to retrieve that user and check their login
     * status to perform the necessary checks.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO : fix these checks using GSON
        UserAccount newUser = new UserAccount("Username","jemm@idk.com","null",null,null,null);

        Intent intent;
        if (!newUser.getLoginStatus()){
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
