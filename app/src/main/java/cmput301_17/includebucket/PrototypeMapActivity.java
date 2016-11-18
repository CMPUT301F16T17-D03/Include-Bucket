package cmput301_17.includebucket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * Created by Owner on 11/9/2016.
 *
 * This is a test for the map.
 */

public class PrototypeMapActivity extends Activity implements MapEventsReceiver, LocationListener{

    Marker startMarker;
    Marker endMarker;
    GeoPoint startPoint;
    GeoPoint endPoint;
    MapView map;
    RoadManager roadManager;
    LocationManager locationManager;
    LocationListener locationListener;
    GeoPoint currentPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prototype_map);
        //int permissionCheckCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //Toast.makeText(getApplicationContext(), "Coarse Location " +permissionCheckCoarseLocation, Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //important! set your user agent to prevent getting banned from the osm servers
        //OpenStreetMapTileProviderConstants.setCachePath(new File("/sdcard/osmdroid2/").getAbsolutePath());
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);

        map = (MapView) findViewById(R.id.NRRAMap);
        map.getOverlays().add(0, mapEventsOverlay);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        startPoint = new GeoPoint(53.5444, -113.4909);
        endPoint = new GeoPoint(53.5443, -113.4908);
        mapController.setCenter(startPoint);
        roadManager = new OSRMRoadManager(this);
        startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker = new Marker(map);
        endMarker.setPosition(endPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        map.getOverlays().add(endMarker);
        OnMarkerDragDrawerold dragger = new OnMarkerDragDrawerold(map, startMarker, endMarker, roadManager);
        startMarker.setIcon(getResources().getDrawable(R.mipmap.marker_green));
        endMarker.setIcon(getResources().getDrawable(R.mipmap.marker_red));
        startMarker.setTitle("Start point");
        startMarker.setDraggable(true);
        startMarker.setOnMarkerDragListener(dragger);
        endMarker.setTitle("End point");
        endMarker.setDraggable(true);
        endMarker.setOnMarkerDragListener(dragger);
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        AsyncTask<ArrayList<GeoPoint>, Void, Polyline> task = new BuildRoadTask(map, roadManager).execute(waypoints);
        //Road road = roadManager.getRoad(waypoints);
        //AsyncTask<ArrayList<GeoPoint>, Void, Polyline> task = new BuildRoadTask(map, roadManager).execute(waypoints);
        //Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        //map.getOverlays().add(roadOverlay);

    }

    @Override public boolean singleTapConfirmedHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }
    @Override public boolean longPressHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }
    @Override
    public void onLocationChanged(Location location) {
        currentPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        //
    }

    @Override
    public void onProviderEnabled(String provider) {
        //
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //
    }
}