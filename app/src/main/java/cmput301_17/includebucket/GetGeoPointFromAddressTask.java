package cmput301_17.includebucket;

import android.location.Address;
import android.os.AsyncTask;
import android.widget.Toast;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;

/**
 * GetGeoPointFromAddressTask
 *
 * This class deals with getting a set of coordinates from an address.
 */

public class GetGeoPointFromAddressTask extends AsyncTask<String, Void, GeoPoint>
{

    public interface AsyncResponse {
        void processFinish(GeoPoint output);
    }
    public AsyncResponse delegate = null;
    public GeocoderNominatim geoCoder;

    public GetGeoPointFromAddressTask(AsyncResponse delegate)
    {

        this.delegate = delegate;

    }

    protected GeoPoint doInBackground(String... addresses)
    { //http://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address
        GeoPoint ret;
        try {
            GeocoderNominatim geoCoder = new GeocoderNominatim("Include-Bucket");
            List<Address> address = geoCoder.getFromLocationName(addresses[0], 1);
            if (address==null)
            {
                ret = null;
                return ret;
            }
            Address location = address.get(0);
            ret = new GeoPoint(location.getLatitude(), location.getLongitude());
            //String country = addresses.get(0).getCountryName();
            //String postalCode = addresses.get(0).getPostalCode();
            //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL



        }catch(Exception e){
            ret = null;
        }

        return ret;
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    @Override
    protected void onPostExecute(GeoPoint result) {
        super.onPostExecute(result);
        if (result!=null) {
            delegate.processFinish(result);
        }

    }
}


