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

                /**
                 * code from http://stackoverflow.com/questions/9259856/displaying-the-to-address-prefilled-in-email-intent
                 * accessed on November 26, 2016
                 * user: goodm
                 *
                 * Requires emailing application installed on phone to work. Crashes when it runs.
                 */
                //Intent intent = new Intent(Intent.ACTION_SEND);
                //intent.setType("plain/text");
                //intent.putExtra(Intent.EXTRA_EMAIL, new String[] { driver.getEmail()});
                //startActivity(intent);


                /**
                 * Hello, email handler friend!
                 *
                 * I hope you see disssss
                 *
                 * The code above made the app crash, maybe you knew this, maybe you didn't. I don't know.
                 * I tried looking for solutions and came upon this one. It's pretty useless without
                 * some sort of 'email application' that everyone says needs to be installed on the app
                 * for it to work. Hope this helps (or not, you decide).
                 *
                 * Taken from: http://stackoverflow.com/questions/14604349/activitynotfoundexception-while-sending-email-from-the-application
                 * Accessed November 28, 2016
                 * Author: Sahil Mahajan Mj
                 */
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ViewRiderDataActivity.this, "There are no email applications installed.", Toast.LENGTH_SHORT).show();
                }
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