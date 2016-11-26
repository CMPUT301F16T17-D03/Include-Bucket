package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Driver;

/**
 *
 * MainMenuActivity
 *
 * The view for the main menu
 *
 *
 */

public class MainMenuActivity extends Activity {
   // private Activity activity = this;

    private UserAccount user;
    private UserController userController;
    private RiderRequestsController riderController;
    private DriverRequestsController driverController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        UserFileManager.initManager(this.getApplicationContext());
        RiderRequestsFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        Button riderNewButton = (Button) findViewById(R.id.newRequest);
        riderNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, NewRiderRequestActivity.class);
                startActivity(intent);
            }
        });

        Button logOutButton = (Button) findViewById(R.id.Logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        Button riderRequestsButton = (Button) findViewById(R.id.MyRequests);
        riderRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, RiderCurrentRequestsActivity.class);
                startActivity(intent);
            }
        });

        Button driverBrowseRequestsButton = (Button) findViewById(R.id.Browse);
        driverBrowseRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, DriverBrowseRequestsActivity.class);
                startActivity(intent);
            }
        });

        Button driverCurrentRequestsButton = (Button) findViewById(R.id.Current);
        driverCurrentRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, DriverCurrentRequestsActivity.class);
                startActivity(intent);
            }
        });

        Button accountButton = (Button) findViewById(R.id.Account);
        accountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, EditUserDataActivity.class);
                startActivity(intent);
            }
        });
    }
}