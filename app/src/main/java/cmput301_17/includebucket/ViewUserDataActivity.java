package cmput301_17.includebucket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * ViewUserDataActivity
 *
 * This is a view to see the user's data. It shows contact information: phone number and email address.
 *
 * username, phone number, nmae , address, back button, save button.
 *
 */
public class ViewUserDataActivity extends MainMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_data);
    }
}