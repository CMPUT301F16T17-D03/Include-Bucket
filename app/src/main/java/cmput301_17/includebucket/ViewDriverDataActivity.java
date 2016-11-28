package cmput301_17.includebucket;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
public class ViewDriverDataActivity extends MainMenuActivity {

    private TextView loginTextView, makeView, modelView, yearView;
    private Button acceptButton, emailButton, phoneButton;
    private UserAccount driver = new UserAccount();


    public void createNotification(View view) {
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notification = new Intent(this, RiderCurrentRequestsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,(int) System.currentTimeMillis(), notification, 0);


        Notification mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle("New Notifications")
                .setContentText("Your Acceptance Has Been Accepted")
                .setContentIntent(pendingIntent)
                .build();
        mBuilder.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationmanager.notify(1, mBuilder);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_data);

        UserFileManager.initManager(this.getApplicationContext());

        final UserAccount driver = (UserAccount) getIntent().getSerializableExtra("User");

        final Request request = (Request) getIntent().getSerializableExtra("Request");

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        emailButton = (Button) findViewById(R.id.emailTextView);
        phoneButton = (Button) findViewById(R.id.phoneTextView);
        makeView = (TextView) findViewById(R.id.MakeView);
        modelView = (TextView) findViewById(R.id.ModelView);
        yearView = (TextView) findViewById(R.id.YearView);

        acceptButton = (Button) findViewById(R.id.confirmButton);


        String login = "Username: " + driver.getUniqueUserName();
        String email = "Email: " + driver.getEmail();
        String phone = "Phone number: " + driver.getPhoneNumber();
        String make  = "Make: " + driver.getVehicleMake();
        String model = "Model: " + driver.getVehicleModel();
        String year  = "Year: " + driver.getVehicleYear();

        loginTextView.setText(login);
        emailButton.setText(email);
        phoneButton.setText(phone);
        modelView.setText(model);
        makeView.setText(make);
        yearView.setText(year);

        if (request.getRequestStatus() != Request.RequestStatus.Closed) {
            acceptButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setResult(RESULT_OK);

                    Request newRequest = new Request();
                    newRequest = request;

                    DriverRequestsController.deleteRequest(request);

                    ElasticsearchRequestController.CreateRequest createRequest;
                    createRequest = new ElasticsearchRequestController.CreateRequest();
                    createRequest.execute(newRequest);

                    newRequest.setRequestStatus(Request.RequestStatus.Closed);
                    newRequest.chooseDriver(driver);
                    newRequest.setRiderAccepted(true);
                    newRequest.clearDrivers();

                    Toast.makeText(ViewDriverDataActivity.this, "Driver Accepted", Toast.LENGTH_SHORT).show();
                    createNotification(acceptButton);
                    Intent intent = new Intent(ViewDriverDataActivity.this, RiderCurrentRequestsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else
        {
            acceptButton.setText("REQUESTS");
            acceptButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    Intent intent = new Intent(ViewDriverDataActivity.this, RiderCurrentRequestsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

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