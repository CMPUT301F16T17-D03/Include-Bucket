package cmput301_17.includebucket;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * ViewUserDataActivity
 *
 * This is a view to see the user's data. It shows contact information: phone number and email address.
 *
 * username, phone number, name , address, back button, save button.
 *
 */
public class ViewUserDataActivity extends MainMenuActivity {


    protected TextView userEmail, userPhone, userLogin;

    UserAccount user = new UserAccount();

    /**
     * This is the onCreate method. It sets the user data on the screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_data);

        user = (UserAccount) getIntent().getSerializableExtra("User");


        userEmail = (TextView) findViewById(R.id.emailTextView);
        userPhone = (TextView) findViewById(R.id.phoneTextView);
        userLogin = (TextView) findViewById(R.id.loginTextView);

        String login = user.getUniqueUserName();
        String email = user.getEmail();
        String phone = user.getPhoneNumber();

        userLogin.setText("Username: \n\t" + login);
        userEmail.setText("Email: \n\t" + email);
        userPhone.setText("Phone Number: \n\t" + phone);


        Button accountButton = (Button) findViewById(R.id.confirmButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                Intent intent = new Intent(ViewUserDataActivity.this, EditUserDataActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }

}