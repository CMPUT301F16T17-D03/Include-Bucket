package cmput301_17.includebucket;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * User data controller?
 *
 */
public class EditUserDataActivity extends MainMenuActivity {

    protected EditText userLogin, userName, userEmail, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);

        userName  = (EditText) findViewById(R.id.nameTextField);
        userEmail = (EditText) findViewById(R.id.emailTextField);
        userPhone = (EditText) findViewById(R.id.phoneTextField);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);


            }
        });
    }
}