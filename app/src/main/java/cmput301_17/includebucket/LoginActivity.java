package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Duncan on 11/10/2016.
 */

public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);
        Intent intent = getIntent();

        // If there is no data associated with the Intent, sets the data to the default URI, which
        // accesses a list of notes.
        if (intent.getData() == null) {
            intent.setAction(Intent.ACTION_SEND);
        }
}
}
