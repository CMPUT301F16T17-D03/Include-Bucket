package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
         *  search for username in elasticsearch database
         *  create condition where, if the username is not in the database, automatically
         *  switch to RegisterActivity, otherwise login
         */
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                /**
                 * Check to see if the user is valid (in elasticsearch)
                 */
                ElasticsearchController.RetrieveUserTask retrieveUserTask;
                retrieveUserTask = new ElasticsearchController.RetrieveUserTask();
                retrieveUserTask.execute(userLogin.getText().toString());

                try {
                    UserAccount foundUser = retrieveUserTask.get();

                    String foundUserName = foundUser.getUniqueUserName();
                    Log.i("Success", "It got a user.");
                    //if ((userLogin.getText().toString()).equals(foundUserName)){
                     //   Intent intent = new Intent(LoginActivity.this,MainMenuActivity.class);
                       // startActivity(intent);
                    //}
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
         * Will retrieve the user input login name from RegisterActivity and
         * put it in the EditText box in the LoginActivity (essentially, the user
         * will not have to retype the user login name that they just created).
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
