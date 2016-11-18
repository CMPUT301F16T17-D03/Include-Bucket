package cmput301_17.includebucket;

import android.app.Activity;
import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Owner on 11/9/2016.
 *
 * Builds the road for a geolocation path
 *
 */

public class BuildRoadTask extends AsyncTask<ArrayList<GeoPoint>, Void, Road>
{
//http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    public interface AsyncResponse {
        void processFinish(Road output);
    }
    public AsyncResponse delegate = null;
    public MapView map;
    public RoadManager manager;
    public Polyline roadline;
    public BuildRoadTask(MapView m, RoadManager r, AsyncResponse delegate)
    {
        this.map = m;
        this.manager =r;
        this.delegate = delegate;

    }

    protected Road doInBackground(ArrayList<GeoPoint>... waypoints)
    {   Road road = manager.getRoad(waypoints[0]);

        roadline = RoadManager.buildRoadOverlay(road);

        return road;
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    @Override
    protected void onPostExecute(Road result) {
        super.onPostExecute(result);

        map.getOverlays().add(roadline);
        delegate.processFinish(result);
        map.invalidate();
    }
}
