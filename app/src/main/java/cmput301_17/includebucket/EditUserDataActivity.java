package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

/**
 *
 * EditUserDataActivity
 *
 * Controller class for user data. Deals with a user editing their data.
 *
 */
public class EditUserDataActivity extends MainMenuActivity {


    protected EditText userEmail, userPhone, vehicleMake, vehicleModel, vehicleYear;
    private UserAccount user = UserController.getUserAccount();
    private UserAccount newUser = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_data);

        UserFileManager.initManager(this.getApplicationContext());

        userEmail = (EditText) findViewById(R.id.edit_email);
        userEmail.setText(user.getEmail());
        userPhone = (EditText) findViewById(R.id.edit_phone);
        userPhone.setText(user.getPhoneNumber());
        vehicleMake = (EditText) findViewById(R.id.edit_vehicle_make);
        vehicleMake.setText(user.getVehicleMake());
        vehicleModel = (EditText) findViewById(R.id.edit_vehicle_model);
        vehicleModel.setText(user.getVehicleModel());
        vehicleYear = (EditText) findViewById(R.id.edit_vehicle_year);
        vehicleYear.setText(user.getVehicleYear());

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                editUser();

                Intent intent = new Intent(EditUserDataActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(EditUserDataActivity.this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void editUser() {

        String login = user.getUniqueUserName();

        String email, phone, make, model, year;

        if (userEmail.getText().toString().equals("")){
             email = user.getEmail();
        } else{
             email = userEmail.getText().toString();
        }
        if (userPhone.getText().toString().equals("")){
            phone = user.getPhoneNumber();
        } else{
            phone = userPhone.getText().toString();
        }
        if (vehicleMake.getText().toString().equals("")){
            make = user.getVehicleMake();
        } else{
            make = vehicleMake.getText().toString();
        }
        if (vehicleModel.getText().toString().equals("")){
            model = user.getVehicleModel();
        } else{
            model = vehicleModel.getText().toString();
        }
        if (vehicleYear.getText().toString().equals("")){
            year = user.getVehicleYear();
        } else{
            year = vehicleYear.getText().toString();
        }

        newUser = new UserAccount(login, email, phone, make, model, year);

        ElasticsearchUserController.DeleteUser deleteUser;
        deleteUser = new ElasticsearchUserController.DeleteUser();
        deleteUser.execute(user);

        ElasticsearchUserController.CreateUser editUser;
        editUser = new ElasticsearchUserController.CreateUser();
        editUser.execute(newUser);

        try {
            UserFileManager.getUserFileManager().saveUser(newUser);
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize UserAccount with UserFileManager");
        }
    }
}