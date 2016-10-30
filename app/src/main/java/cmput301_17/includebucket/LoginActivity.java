package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by michelletagarino on 16-10-29.
 */
public class LoginActivity extends MainMenuActivity {

    private EditText userLogin = (EditText) findViewById(R.id.loginTextField);
    private UserList userList = new UserList();
    private UserListController userListController = new UserListController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                userList = userListController.getUserList();

                //if (userList.contains(userLogin.getText().toString())) {}

                Intent intent = new Intent(LoginActivity.this,MainMenuActivity.class);
                startActivity(intent);
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginAction(View v) {



    }
}
