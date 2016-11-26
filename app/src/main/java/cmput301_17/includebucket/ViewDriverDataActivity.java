package cmput301_17.includebucket;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ViewDriverDataActivity
 *
 * This is a view to see the driver's data. It shows contact information: phone number and email address.
 */
public class ViewDriverDataActivity extends MainMenuActivity {

    private TextView loginTextView, emailTextView, phoneTextView;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_data);

        UserFileManager.initManager(this.getApplicationContext());

        final UserAccount driver = (UserAccount) getIntent().getSerializableExtra("User");
        final Request request = (Request) getIntent().getSerializableExtra("Request");

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        confirmButton = (Button) findViewById(R.id.confirmButton);

        String login = driver.getUniqueUserName();
        String email = driver.getEmail();
        String phone = driver.getPhoneNumber();

        loginTextView.setText(login);
        emailTextView.setText(email);
        phoneTextView.setText(phone);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                request.setRequestStatus(Request.RequestStatus.Closed);
                request.chooseDriver(driver);
                request.setRiderAccepted(true); // Eventually we won't need these booleans, but they are kept for testing purposes
                DriverRequestsController.deleteRequest(request);
                ElasticsearchRequestController.CreateRequest createRequest;
                createRequest = new ElasticsearchRequestController.CreateRequest();
                createRequest.execute(request);
                // Do not delete request yet
                // US 05.03.01 As a driver, I want to see if my acceptance was accepted.
                // This means that when the driver goes to his Current requests (DriverCurrentRequestActivity)
                // the Driver has to see that the request is now closed and was confirmed by the rider
                // i.e. "Confirmed by... [rider's login]"
                // This is not the same as a notification
                Toast.makeText(ViewDriverDataActivity.this, "Request Confirmed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}