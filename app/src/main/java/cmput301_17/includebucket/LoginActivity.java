package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by michelletagarino on 16-10-29.
 */
public class LoginActivity extends MainMenuActivity {

    // EditText userLogin will be used to find whether the username is in the elasticsearch database...
    // private EditText userLogin = (EditText) findViewById(R.id.loginTextField);
    private EditText userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         *  create condition where, if the username is not in the database, automatically
         *  switch to RegisterActivity, otherwise login
         */
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                String textLogin  = userLogin.getText().toString();

                /**
                 * Checks to see if the user is valid:
                 *     If a user was returned, then the task was successful and thereby the login
                 *     name was valid. The user input and the username returned from elasticsearch
                 *     are compared; if equal, the activity will switch to the MainActivity (in other
                 *     words, the user will be logged in).
                 */
                ElasticsearchUserController.RetrieveUser findUser;
                findUser = new ElasticsearchUserController.RetrieveUser();
                findUser.execute(userLogin.getText().toString());

                try {
                    UserAccount foundUser = findUser.get();
                    String foundLogin = foundUser.getUniqueUserName();

                    if (textLogin.equals(foundLogin))
                    {
                        Log.i("Success", "User " + textLogin + " was found.");

                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        intent.putExtra("User", foundUser);
                        startActivity(intent);
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Failed to get the user out of the async object.");
                }
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
