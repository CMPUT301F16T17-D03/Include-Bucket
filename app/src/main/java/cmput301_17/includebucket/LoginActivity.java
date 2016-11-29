package cmput301_17.includebucket;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.io.IOException;
import java.sql.Driver;
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
    private ConnectivityManager connectivityManager;
    private boolean connected;
    private UserAccount userMaster,user;

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

        userMaster = UserController.getUserAccount();
        if (userMaster != null)
        {
            if (userMaster.getLoginStatus())
            {
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        }

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
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

                /**
                 * Taken from: http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
                 * Accessed: November 26, 2016
                 * Author: binnyb
                 */
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                {
                    connected = Boolean.TRUE;
                }
                else connected = Boolean.FALSE;

                String toastMsg = null;
                if (connected)
                {
                    ElasticsearchUserController.RetrieveUser retrieveUser;
                    retrieveUser = new ElasticsearchUserController.RetrieveUser();
                    retrieveUser.execute(userLogin.getText().toString());

                    try {
                        user = retrieveUser.get();
                        if (user == null)
                        {
                            toastMsg = "Username does not exist";
                        }

                    } catch (Exception e) {
                        Log.i("Fail","Something went wrong with the search!");
                    }
                }
                else
                {
                    toastMsg = "You need internet to log in!";
                }

                try {
                    if (!userLogin.getText().toString().equals(user.getUniqueUserName()))
                    {
                        toastMsg = "Username does not exist";
                    }
                    else
                    {
                        UserController.loadUserFromElasticSearch(userLogin.getText().toString());
                        UserAccount saveUser = UserController.getUserAccount();
                        saveUser.setLoginStatus(Boolean.TRUE);

                        UserFileManager.getUserFileManager().saveUser(saveUser);

                        RiderRequestsController.loadRequestsFromElasticSearch();
                        DriverRequestsController.loadOpenRequestsFromElasticsearch();

                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
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
                    /**
                     * Taken from: http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
                     * Accessed: November 26, 2016
                     * Author: binnyb
                     */
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                    {
                        connected = Boolean.TRUE;
                    }
                    else connected = Boolean.FALSE;

                    String toastMsg = null;
                    if (connected)
                    {
                        ElasticsearchUserController.RetrieveUser retrieveUser;
                        retrieveUser = new ElasticsearchUserController.RetrieveUser();
                        retrieveUser.execute(userLogin.getText().toString());

                        try {
                            user = retrieveUser.get();
                            if (user == null)
                            {
                                toastMsg = "Username does not exist";
                            }

                        } catch (Exception e) {
                            Log.i("Fail","Something went wrong with the search!");
                        }
                    }
                    else
                    {
                        toastMsg = "You need internet to log in!";
                    }

                    try {
                        if (!userLogin.getText().toString().equals(user.getUniqueUserName()))
                        {
                            toastMsg = "Username does not exist";
                        }
                        else
                        {
                            user.setLoginStatus(Boolean.TRUE);
                            UserFileManager.getUserFileManager().saveUser(user);
                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }
}
