package cmput301_17.includebucket;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * LoginActivity
 *
 * This deals with login functionality.
 */
public class LoginActivity extends MainMenuActivity {

    private Button loginButton;
    private Button registerButton;
    private EditText userLogin;
    private UserAccount foundUser;
    private RequestList requestList;

    UserController userController;

    /**
     * This method get permissions to run and deals with button presses.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        UserFileManager.initManager(this.getApplicationContext());
        RiderRequestsFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        userController = new UserController();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        /**
         * Invoking the login button will check if the text matches any
         * usernames in the server. If it passes, the user gets logged in.
         */
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                ElasticsearchUserController.RetrieveUser retrieveUser;
                retrieveUser = new ElasticsearchUserController.RetrieveUser();
                retrieveUser.execute(userLogin.getText().toString());

                UserAccount user = new UserAccount();

                try {

                    user = retrieveUser.get();

                    UserFileManager.getUserFileManager().saveUser(user);

                    UserAccount u = UserController.getUserAccount();

                    Log.i("HELLLO","This user is "+ u.getUniqueUserName());

                    if (userLogin.getText().toString().equals(user.getUniqueUserName()))
                    {
                        Log.i("Success", "User " + user.getUniqueUserName() + " was found.");
                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Retrieves user input login name from RegisterActivity and puts it in the EditText field
         * of the LoginActivity (user does not have to retype the login name they just created).
         */
        Intent intent = getIntent();
        String str = intent.getStringExtra("user_login");
        userLogin = (EditText) findViewById(R.id.loginTextField);
        userLogin.clearComposingText();
        userLogin.setText(str);

        userLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ElasticsearchUserController.RetrieveUser retrieveUser;
                    retrieveUser = new ElasticsearchUserController.RetrieveUser();
                    retrieveUser.execute(userLogin.getText().toString());

                    UserAccount user = new UserAccount();

                    try {

                        user = retrieveUser.get();

                        if (userLogin.getText().toString().equals(user.getUniqueUserName()))
                        {
                            Log.i("Success", "User " + user.getUniqueUserName() + " was found.");
                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }
}
