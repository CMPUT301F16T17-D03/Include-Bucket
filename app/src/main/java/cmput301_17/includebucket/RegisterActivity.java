package cmput301_17.includebucket;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

/**
 * Created by michelletagarino on 16-10-29.
 */
public class RegisterActivity extends MainMenuActivity {

    protected EditText userLogin, userName, userEmail, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userLogin = (EditText) findViewById(R.id.loginTextField);
        userName  = (EditText) findViewById(R.id.nameTextField);
        userEmail = (EditText) findViewById(R.id.emailTextField);
        userPhone = (EditText) findViewById(R.id.phoneTextField);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        /**
         * When the user presses the Accept button, they are directed back into the login activity
         * Here they will be prompted for their login that they just created
         * This is a way of verifying to the user that their account registration was successful
         *     the string of their unique username will be automatically filled in the text field,
         *     so that they do not have to retype it.
         */
        Button registerButton = (Button) findViewById(R.id.acceptButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Boolean userCreated = Boolean.FALSE;
                try {
                    /**
                     * Check first to see if the username is unique.
                     * Create user if not already in database.
                     */
                    ElasticsearchController.RetrieveUserTask retrieveUserTask;
                    retrieveUserTask = new ElasticsearchController.RetrieveUserTask();
                    retrieveUserTask.execute(userLogin.getText().toString());

                    UserAccount foundUser = retrieveUserTask.get();
                    if ((userLogin.getText().toString()).equals(foundUser.getUniqueUserName())){
                        /**
                         * Taken from http://stackoverflow.com/questions/8472349/how-to-set-text-color-to-a-text-view-programmatically
                         * Accessed on November 11, 2016
                         * Author: user370305
                         */
                        userLogin.setTextColor(Color.parseColor("#E40000"));
                    }
                } catch (Exception e) {
                    createUser();
                    userCreated = Boolean.TRUE;
                }

                /**
                 * If a user was created, go back to LoginActivity with the user's new
                 * uniqueUserName automatically filled in the EditText field.
                 */
                if (userCreated) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("user_login", userLogin.getText().toString());
                    startActivity(intent);
                } else Log.i("Error","A user with this name has already been created.");
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
        ElasticsearchController.CreateUserTask createUserTask;
        createUserTask = new ElasticsearchController.CreateUserTask();
        createUserTask.execute(user);
    }
}