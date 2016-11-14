package cmput301_17.includebucket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by michelletagarino on 16-10-29.
 *
 * This deals with registering a new user.
 */
public class RegisterActivity extends MainMenuActivity {

    protected EditText userLogin, userName, userEmail, userPhone;

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
        setContentView(R.layout.activity_register);

        userLogin = (EditText) findViewById(R.id.loginTextField);
        userName  = (EditText) findViewById(R.id.nameTextField);
        userEmail = (EditText) findViewById(R.id.emailTextField);
        userPhone = (EditText) findViewById(R.id.phoneTextField);


        Button registerButton = (Button) findViewById(R.id.acceptButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                String textLogin  = userLogin.getText().toString();

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
                        /**
                         * Taken from http://stackoverflow.com/questions/8472349/how-to-set-text-color-to-a-text-view-programmatically
                         * Accessed on November 11, 2016
                         * Author: user370305
                         */
                        Toast.makeText(RegisterActivity.this, "The Login " + textLogin + " is already taken.", Toast.LENGTH_SHORT).show();
                        userLogin.setTextColor(Color.parseColor("#E40000"));
                    }
                } catch (Exception e) {

                    createUser();

                    Log.i("Success","A user with Login: " + textLogin + " has been created.");

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("user_login", textLogin);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * When a user clicks the Accept button, a new UserAccount instance is created
     */
    public void createUser() {

        String login = userLogin.getText().toString();
        String name  = userName.getText().toString();
        String email = userEmail.getText().toString();
        String phone = userPhone.getText().toString();

        UserAccount user = new UserAccount(login, name, email, phone);

        /**
         * If username is unique, create a new user.
         */
        ElasticsearchUserController.CreateUser createUser;
        createUser = new ElasticsearchUserController.CreateUser();
        createUser.execute(user);
    }
}