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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by orlick on 11/7/16.
 *
 * In this class, the user should be able to specify the start and end locations by typing in an
 * address or by clicking on the map (clicking on the map should automatically fill in the start
 * and end locations). These locations are values for instantiating a new Request.
 */
public class NewRiderRequestActivity extends Activity implements MapEventsReceiver, LocationListener {

    EditText startEditText;
    EditText endEditText;
    EditText priceEditText;
    Marker startMarker;
    Marker endMarker;
    GeoPoint startPoint;
    GeoPoint endPoint;
    MapView map;
    OnMarkerDragDrawer dragger;
    RoadManager roadManager;
    LocationManager locationManager;
    LocationListener locationListener;
    GeoPoint currentPoint;

    UserAccount user = new UserAccount();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_rider_request);

        user = (UserAccount) getIntent().getSerializableExtra("User");

        //int permissionCheckCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //Toast.makeText(getApplicationContext(), "Coarse Location " +permissionCheckCoarseLocation, Toast.LENGTH_SHORT).show();

        startEditText = (EditText) findViewById(R.id.NRRAStartEditText);
        endEditText = (EditText) findViewById(R.id.NRRAEndEditText);
        priceEditText = (EditText) findViewById(R.id.NRRAPriceEditText);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
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

        //OpenStreetMapTileProviderConstants.setCachePath(new File("/sdcard/osmdroid2/").getAbsolutePath());

        startEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus){
                    //User is typing, do nothing
                    //Toast toast = Toast.makeText(getApplicationContext(), "Has Focus", Toast.LENGTH_SHORT);
                    //toast.show();
                }
                else{
                    //User has stopped typing, update the marker and the map
                    //This is caused by hitting return or by clicking off the edit text
                    //TODO Should probably give suggestions as to exact address.
                    //assume is in format of lat,long
                    try {
                        Double latdub = (Double.parseDouble(startEditText.getText().toString().split(",")[0]));
                        Double lngdub = (Double.parseDouble(startEditText.getText().toString().split(",")[1]));
                        GeoPoint tempGeo = new GeoPoint(latdub, lngdub);
                        dragger.onMarkerDragStart(startMarker);
                        startMarker.setPosition(tempGeo);
                        dragger.onMarkerDragEnd(startMarker);
                    }
                    catch(Exception e){
                        Log.i("Error","Failed to get a valid geolocation.");
                    }
                }
            }
        });

        endEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus){
                    //User is typing, do nothing
                    //Toast toast = Toast.makeText(getApplicationContext(), "Has Focus", Toast.LENGTH_SHORT);
                    //toast.show();
                }
                else{
                    //User has stopped typing, update the marker and the map
                    //This is caused by hitting return or by clicking off the edit text
                    //TODO Should probably give suggestions as to exact address.
                    //assume is in format of lat,long
                    try {
                        Double startLat = (Double.parseDouble(startEditText.getText().toString().split(",")[0]));
                        Double startLon = (Double.parseDouble(startEditText.getText().toString().split(",")[1]));
                        GeoPoint tempGeoStart = new GeoPoint(startLat, startLon);

                        Double endLat = (Double.parseDouble(endEditText.getText().toString().split(",")[0]));
                        Double endLon = (Double.parseDouble(endEditText.getText().toString().split(",")[1]));
                        GeoPoint tempGeoEnd = new GeoPoint(endLat, endLon);

                        dragger.onMarkerDragStart(startMarker);
                        startMarker.setPosition(tempGeoStart);
                        dragger.onMarkerDragEnd(startMarker);

                        dragger.onMarkerDragStart(endMarker);
                        endMarker.setPosition(tempGeoEnd);
                        dragger.onMarkerDragEnd(endMarker);
                    }
                    catch(Exception e){
                        Log.i("Error","Failed to get a valid geolocation.");
                    }
                }
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                String startLocation = startEditText.getText().toString();
                String endLocation = endEditText.getText().toString();

                Request request = new Request(startLocation, endLocation, user);

                ElasticsearchRequestController.CreateRequest createRequest;
                createRequest = new ElasticsearchRequestController.CreateRequest();
                createRequest.execute(request);
            }
        });

        /**
         * Important! set your user agent to prevent getting banned from the osm servers
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        startEditText.setText(currentPoint.toString());
        endEditText.setText(currentPoint.toString());
        map = (MapView) findViewById(R.id.NRRAMap);
        map.getOverlays().add(0, mapEventsOverlay);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        startPoint = currentPoint;
        endPoint = currentPoint;
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
        dragger = new OnMarkerDragDrawer();

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
        if(dragger.mTrace.get(0).equals(dragger.mTrace.get(1))){
            dragger.mTrace.remove(0);
            endMarker.setPosition(p);
            dragger.onMarkerDragEnd(endMarker);
            return true;
        }
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

    /**
     * Handles the dragging of a marker, and updates dependant widgets.
     */
    private class OnMarkerDragDrawer implements Marker.OnMarkerDragListener {
        ArrayList<GeoPoint> mTrace;

        //RoadManager roadManager;
        OnMarkerDragDrawer() {
            mTrace = new ArrayList<GeoPoint>(2);
            mTrace.add(startMarker.getPosition());
            mTrace.add(endMarker.getPosition());

        }

        @Override public void onMarkerDrag(Marker marker) {
            //mTrace.add(marker.getPosition());
        }

        @Override public void onMarkerDragEnd(Marker marker) {

            mTrace.add(marker.getPosition());
            map.getOverlays().remove(3);
            AsyncTask<ArrayList<GeoPoint>, Void, Polyline> task = new BuildRoadTask(map, roadManager).execute(mTrace);
            if (marker.equals(startMarker)){
                //update start location text
                startEditText.setText(startMarker.getPosition().toString());
            }
            else if (marker.equals(endMarker)){
                endEditText.setText(endMarker.getPosition().toString());
            }
            // update suggested fare
            String price = ""+startMarker.getPosition().distanceTo(endMarker.getPosition());
            priceEditText.setText(price);

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
}