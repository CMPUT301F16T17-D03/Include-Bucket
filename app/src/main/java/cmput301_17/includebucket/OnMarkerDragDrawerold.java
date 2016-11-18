package cmput301_17.includebucket;

import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;



/**
 * Created by Owner on 11/11/2016.
 */

public class OnMarkerDragDrawerold implements Marker.OnMarkerDragListener {
    public ArrayList<GeoPoint> mTrace;

    MapView map;
    int flag;

    RoadManager roadManager;
    OnMarkerDragDrawerold(MapView passedMap, Marker source, Marker end, RoadManager passedManager) {
        mTrace = new ArrayList<GeoPoint>(2);
        mTrace.add(source.getPosition());
        mTrace.add(end.getPosition());
        map = passedMap;
        roadManager = passedManager;
    }

    @Override public void onMarkerDrag(Marker marker) {
        //mTrace.add(marker.getPosition());
    }

    @Override public void onMarkerDragEnd(Marker marker) {

        mTrace.add(marker.getPosition());
        map.getOverlays().remove(3);
        AsyncTask<ArrayList<GeoPoint>, Void, Road> task = new BuildRoadTask(map, roadManager, new BuildRoadTask.AsyncResponse(){
            @Override
            public void processFinish(Road output){
            }
        }).execute(mTrace);
        if (marker.getTitle().equals("Start Point")){
            //update start location text
        }
        if (marker.getTitle().equals("End Point")){
            //update end location text
        }
        // update suggested fare
    }

    @Override public void onMarkerDragStart(Marker marker) {
        if(marker.getPosition().equals(mTrace.get(0))){
            mTrace.remove(0);
        }
        else{
            mTrace.remove(1);
        }
    }
}