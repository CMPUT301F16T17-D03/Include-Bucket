package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by michelletagarino on 16-10-29.
 */
public class RegisterActivity extends MainMenuActivity {

    private EditText userLogin, userName, userEmail, userPhone;
    final String LOGIN = "Login";
    final String NAME  = "Name";
    final String EMAIL = "Email";
    final String PHONE = "Phone";

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

        // When the user presses the Accept button, they are directed back into the login activity
        // Here they will be prompted for their login that they just created
        // This is a way of verifying to the user that there account registration was successful
        //     the string of their unique username will be automatically filled in the text box,
        //     so that they do not have to retype it.
        Button registerButton = (Button) findViewById(R.id.acceptButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                createUser();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("user_login", userLogin.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * When a user clicks the Accept button, a new UserAccount instance is created
     * with a unique ID number of long type
    */
    public void createUser() {

        // http://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
        // Accessed: October 31, 2016
        // Author: zengr
        int id = (int) Math.random() * 100000 + 10001;    // Generates a random five digit ID number
                                                          // between 10001 and 110001. It can be
                                                          // longer of course...

        String login = userLogin.getText().toString();
        String name  = userName.getText().toString();
        String email = userEmail.getText().toString();
        String phone = userPhone.getText().toString();

        UserAccount user = new UserAccount(id, login, name, email, phone);
    }
}
