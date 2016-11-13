package cmput301_17.includebucket;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.lang3.ObjectUtils;

/**
 * User data controller?
 *
 */
public class EditUserDataActivity extends MainMenuActivity {


    protected EditText userName, userEmail, userPhone;

    UserAccount user = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);

        user = (UserAccount) getIntent().getSerializableExtra("User");

        userName  = (EditText) findViewById(R.id.editName);
        userEmail = (EditText) findViewById(R.id.editEmail);
        userPhone = (EditText) findViewById(R.id.editPhone);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                editUser();
                finish();
            }
        });
    }
    public void editUser() {

        String login = user.getUniqueUserName();
        String name  = userName.getText().toString();

        String email;
        String phone;

        if (userEmail.getText().toString().equals("")){
             email= user.getEmail();
        } else{
             email = userEmail.getText().toString();
        }
        if (userPhone.getText().toString().equals("")){
            phone= user.getPhoneNumber();
        } else{
            phone = userPhone.getText().toString();
        }

        UserAccount user = new UserAccount(login, name, email, phone);

        ElasticsearchUserController.CreateUser editUser;
        editUser = new ElasticsearchUserController.CreateUser();
        editUser.execute(user);
    }
}