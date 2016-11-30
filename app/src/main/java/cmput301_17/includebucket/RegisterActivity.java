package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * RegisterActivity
 *
 * This deals with registering a new user.
 */
public class RegisterActivity extends MainMenuActivity {

    protected EditText userLogin, userEmail, userPhone, vehicleMake, vehicleModel, vehicleYear;

    /**
     *  When the user presses the Accept button, they are directed back into the login activity
     * Here they will be prompted for their login that they just created
     * This is a way of verifying to the user that their account registration was successful
     *     the string of their unique username will be automatically filled in the text field,
     *     so that they do not have to retype it.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        UserFileManager.initManager(this.getApplicationContext());
        RiderRequestsFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        userLogin = (EditText) findViewById(R.id.loginTextField);
        userEmail = (EditText) findViewById(R.id.emailTextField);
        userPhone = (EditText) findViewById(R.id.phoneTextField);

        vehicleMake  = (EditText) findViewById(R.id.makeTextField);
        vehicleModel = (EditText) findViewById(R.id.modelTextField);
        vehicleYear  = (EditText) findViewById(R.id.yearTextField);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                String textLogin  = userLogin.getText().toString();

                if (userLogin.getText().toString().length() == 0)
                {
                    userLogin.setError("Username field required!");
                }

                /**
                 * Check first to see if the username is unique.
                 * Create user if Elasticsearch failed to find the username.
                 */
                ElasticsearchUserController.RetrieveUser retrieveUser;
                retrieveUser = new ElasticsearchUserController.RetrieveUser();
                retrieveUser.execute(textLogin);

                try {
                    UserAccount foundUser = retrieveUser.get();
                    String foundLogin = foundUser.getUniqueUserName();

                    if (textLogin.equals(foundLogin))
                    {
                        Toast.makeText(RegisterActivity.this, "The Login " + textLogin + " is already taken.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    if (userLogin.getText().toString().length() != 0)
                    {
                        if (userEmail.getText().toString().length() == 0
                            && userPhone.getText().toString().length() == 0)
                        {
                            Toast.makeText(RegisterActivity.this, "One of Email or Phone is required", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String login = userLogin.getText().toString();
                            String email = userEmail.getText().toString();
                            String phone = userPhone.getText().toString();

                            String make = vehicleMake.getText().toString();
                            String model = vehicleModel.getText().toString();
                            String year = vehicleYear.getText().toString();

                            UserAccount user = new UserAccount(login, email, phone, make, model, year);

                            UserController.createUserInElasticSearch(user);

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("user_login", textLogin);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}