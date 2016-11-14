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
     * This is to test whether login activity works when the user is not already logged in
     * All users should be in the elasticsearch database
     * We would have to find a way to query whether or not a username is in the elasticsearch
     * database based on the text input in LoginActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        UserAccount newUser = new UserAccount("Jimmeh","Jimmy John","jemm@idk.com","null");

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
