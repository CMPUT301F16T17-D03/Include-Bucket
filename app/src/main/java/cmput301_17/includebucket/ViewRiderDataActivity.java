package cmput301_17.includebucket;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * ViewDriverDataActivity
 *
 * This is a view to see the driver's data. It shows contact information: phone number and email address.
 */
public class ViewRiderDataActivity extends MainMenuActivity {

    private TextView loginTextView;
    private Button emailButton, phoneButton;
    private UserAccount driver = new UserAccount();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rider_data);

        UserFileManager.initManager(this.getApplicationContext());

        driver = UserController.getUserAccount();

        final Request request = (Request) getIntent().getSerializableExtra("Request");

        final UserAccount rider = request.getRider();

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        emailButton = (Button) findViewById(R.id.emailTextView);
        phoneButton = (Button) findViewById(R.id.phoneTextView);

        String login = "Username: " + rider.getUniqueUserName();
        String email = "Email: " + rider.getEmail();
        String phone = "Phone number: " + rider.getPhoneNumber();

        loginTextView.setText(login);
        emailButton.setText(email);
        phoneButton.setText(phone);

        emailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                // code from http://stackoverflow.com/questions/9259856/displaying-the-to-address-prefilled-in-email-intent
                // accessed on November 26, 2016
                // user: goodm
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {driver.getEmail()});
                startActivity(intent);
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                // code from http://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
                // accessed on November 26, 2016
                // user: AAnkit
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + driver.getPhoneNumber()));
                startActivity(intent);
            }
        });
    }
}