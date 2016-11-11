package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by Duncan on 10/22/2016.
 */

public class MainMenuActivity extends Activity{

    Button Rider, Driver, Browse, Account, Current, Logout, New, Request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        Rider = (Button)findViewById(R.id.Rider);
        Driver = (Button)findViewById(R.id.Driver);
        Browse = (Button)findViewById(R.id.Browse);
        Account = (Button)findViewById(R.id.Account);
        Current = (Button)findViewById(R.id.Current);
        Logout = (Button)findViewById(R.id.Logout);
        New = (Button)findViewById(R.id.New);
        Request = (Button)findViewById(R.id.MyRequests);
    }

    public void onClick(View v){

        if(v.getId() == R.id.Rider){
            Intent intent = new Intent(this, RiderActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.Driver){
            Intent intent = new Intent(this, DriverActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.Browse) {
            Intent intent = new Intent(this, BrowseActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.Account) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.Current) {
            Intent intent = new Intent(this, CurrentActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.Logout) {
            Intent intent = new Intent(this, LogoutActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.New) {
            Intent intent = new Intent(this, NewActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.MyRequests) {
            Intent intent = new Intent(this, RequestActivity.class);
            startActivity(intent);
        }

    }

}
