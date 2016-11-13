package cmput301_17.includebucket;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * User data controller?
 *
 */
public class EditUserDataActivity extends MainMenuActivity {

    UserAccount user = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        user = (UserAccount) getIntent().getSerializableExtra("User");

    }

}