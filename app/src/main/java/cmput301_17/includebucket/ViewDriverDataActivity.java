package cmput301_17.includebucket;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
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
                    newRequest.setDriver(driver);
                    newRequest.setChosenDriver(driver);
                    newRequest.setRiderAccepted(true);
                    newRequest.clearDrivers();

                    Toast.makeText(ViewDriverDataActivity.this, "Driver Accepted", Toast.LENGTH_SHORT).show();
                    createNotification(acceptButton);

                    finish();
                }
            });
        }
        else
        {
            acceptButton.setVisibility(View.GONE);
        }

        emailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                /**
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
                    Toast.makeText(ViewDriverDataActivity.this, "There are no email applications installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                /**
                 * code from http://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
                 * accessed on November 26, 2016
                 * user: AAnkit
                 */
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + driver.getPhoneNumber()));
                startActivity(intent);
            }
        });

    }
}