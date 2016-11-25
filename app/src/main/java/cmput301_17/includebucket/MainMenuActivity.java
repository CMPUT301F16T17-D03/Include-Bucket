package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The view for the main menu
 *
 * Created by Duncan on 10/22/2016.
 */

public class MainMenuActivity extends Activity {
   // private Activity activity = this;

    private UserAccount user;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        userController = new UserController();

        Button riderNewButton = (Button) findViewById(R.id.newRequest);
        riderNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                userController.setContext(MainMenuActivity.this);

                user = UserController.getUserAccount();
                user.setUserState(UserAccount.UserState.rider);

                UserController.saveUserAccountInLocalFile(user, userController.getContext());
                UserController.loadUserAccountFromLocalFile();

                Intent intent = new Intent(MainMenuActivity.this, NewRiderRequestActivity.class);
                startActivity(intent);
            }
        });

        Button logOutButton = (Button) findViewById(R.id.Logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                UserController.logUserOut(MainMenuActivity.this);
                //RiderRequestsController.clearRiderRequests();
                finish();
            }
        });

        Button riderRequestsButton = (Button) findViewById(R.id.MyRequests);
        riderRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                userController.setContext(MainMenuActivity.this);

                user = UserController.getUserAccount();
                user.setUserState(UserAccount.UserState.rider);
                UserController.saveUserAccountInLocalFile(user, userController.getContext());
                UserController.loadUserAccountFromLocalFile();
                RiderRequestsController.loadRequestsFromElasticSearch();

                Intent intent = new Intent(MainMenuActivity.this, RiderCurrentRequestsActivity.class);
                startActivity(intent);
            }
        });

        Button driverBrowseRequestsButton = (Button) findViewById(R.id.Browse);
        driverBrowseRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                userController.setContext(MainMenuActivity.this);

                user = UserController.getUserAccount();
                user.setUserState(UserAccount.UserState.driver);

                UserController.saveUserAccountInLocalFile(user, userController.getContext());
                UserController.loadUserAccountFromLocalFile();

                Intent intent = new Intent(MainMenuActivity.this, DriverBrowseRequestsActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        Button driverCurrentRequestsButton = (Button) findViewById(R.id.Current);
        driverCurrentRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                userController.setContext(MainMenuActivity.this);

                user = UserController.getUserAccount();
                user.setUserState(UserAccount.UserState.driver);

                UserController.saveUserAccountInLocalFile(user, userController.getContext());
                UserController.loadUserAccountFromLocalFile();

                Intent intent = new Intent(MainMenuActivity.this, DriverCurrentRequestsActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        Button accountButton = (Button) findViewById(R.id.Account);
        accountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainMenuActivity.this, EditUserDataActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}