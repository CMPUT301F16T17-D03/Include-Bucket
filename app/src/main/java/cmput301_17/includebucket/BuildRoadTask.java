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
 */

public class BuildRoadTask extends AsyncTask<ArrayList<GeoPoint>, Void, Polyline>
{
    public MapView map;
    public RoadManager manager;
    public BuildRoadTask(MapView m, RoadManager r)
    {
        this.map = m;
        this.manager =r;
    }

    protected Polyline doInBackground(ArrayList<GeoPoint>... waypoints)
    {   Road road = manager.getRoad(waypoints[0]);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        return roadOverlay;
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    @Override
    protected void onPostExecute(Polyline result) {
        super.onPostExecute(result);
        map.getOverlays().add(result);
    }
}
