package cmput301_17.includebucket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * In this class, the user should be able to specify the start and end locations by typing in an
 * address or by clicking on the map (clicking on the map should automatically fill in the start
 * and end locations). These locations are values for instantiating a new Request.
 */
public class NewRiderRequestActivity extends Activity implements MapEventsReceiver, LocationListener {

    private EditText startEditText;
    private EditText endEditText;
    private EditText priceEditText;
    private EditText storyEditText;

    private Marker startMarker;
    private Marker endMarker;
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private MapView map;
    private OnMarkerDragDrawer dragger;
    private RoadManager roadManager;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GeoPoint currentPoint;
    private String price;
    private Collection<UserAccount> pendingDrivers;
    private Double roadLength;

    private UserAccount driver;


    private Context context;


    private UserAccount user;
    private UserController userController;
    private RiderRequestsController riderRequestsController;


    /**
     * This method gets permissions, deals with the map and handles button presses.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_rider_request);

        pendingDrivers = new ArrayList<>();

        user = UserController.getUserAccount();
        userController = new UserController();
        riderRequestsController = new RiderRequestsController();

        /**
         * TODO : Fix this later.
         *
         * int permissionCheckCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
         * Toast.makeText(getApplicationContext(), "Coarse Location " +permissionCheckCoarseLocation, Toast.LENGTH_SHORT).show();
         */
        startEditText = (EditText) findViewById(R.id.NRRAStartEditText);
        endEditText = (EditText) findViewById(R.id.NRRAEndEditText);
        priceEditText = (EditText) findViewById(R.id.NRRAPriceEditText);
        storyEditText = (EditText) findViewById(R.id.riderStoryEditText);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                /**
                 * Show an explanation to the user *asynchronously* -- don't block
                 * this thread waiting for the user's response! After the user
                 * sees the explanation, try again to request the permission.
                 */

            } else {

                /**
                 * No explanation needed, we can request the permission.
                 */


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                /**
                 * MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                 * app-defined int constant. The callback method gets the
                 * result of the request.
                 */
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                /**
                 * Show an expanation to the user *asynchronously* -- don't block
                 * this thread waiting for the user's response! After the user
                 * sees the explanation, try again to request the permission.
                 */
            } else {

                /*
                 * No explanation needed, we can request the permission.
                 */

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                /**
                 * MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                 * app-defined int constant. The callback method gets the
                 * result of the request.
                 */
            }
        }

        //OpenStreetMapTileProviderConstants.setCachePath(new File("/sdcard/osmdroid2/").getAbsolutePath());

        startEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus){} //User is typing, do nothing.
                else{
                    /**
                     * User has stopped typing, update the marker and the map
                     * This is caused by hitting return or by clicking off the edit text
                     * TODO Should probably give suggestions as to exact address.
                     * assume is in format of lat,long
                     */
                     dragger.onMarkerDragStart(startMarker);
                    try {
                        AsyncTask<String, Void, GeoPoint> task = new GetGeoPointFromAddressTask(new GetGeoPointFromAddressTask.AsyncResponse(){

                            @Override
                            public void processFinish(GeoPoint output){

                                //Here you will receive the result fired from async class
                                //of onPostExecute(result) method.
                                startMarker.setPosition(output);
                                startPoint = output;
                                dragger.onMarkerDragEnd(startMarker);

                            }
                        }).execute(startEditText.getText().toString());

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
                if(hasFocus){} //User is typing, do nothing
                else{
                    /**
                     * User has stopped typing, update the marker and the map
                     * This is caused by hitting return or by clicking off the edit text
                     * Should probably give suggestions as to exact address.
                     * assume is in format of lat,long
                     */
                    dragger.onMarkerDragStart(endMarker);
                    try {
                        AsyncTask<String, Void, GeoPoint> task = new GetGeoPointFromAddressTask(new GetGeoPointFromAddressTask.AsyncResponse(){

                            @Override
                            public void processFinish(GeoPoint output){

                                //Here you will receive the result fired from async class
                                //of onPostExecute(result) method.
                                endMarker.setPosition(output);
                                endPoint = output;
                                dragger.onMarkerDragEnd(endMarker);

                            }
                        }).execute(endEditText.getText().toString());

                    }
                    catch(Exception e){
                        Log.i("Error","Failed to get a valid geolocation.");
                    }
                }
            }
        });
        priceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus){} //User is typing, do nothing
                else{
                    /**
                     * User has stopped typing, update the marker and the map
                     * This is caused by hitting return or by clicking off the edit text
                     * Should probably give suggestions as to exact address.
                     * assume is in format of lat,long
                     */
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    try {
                        priceEditText.setText(formatter.format(Double.parseDouble(priceEditText.getText().toString().substring(1, priceEditText.getText().toString().length()))).toString());
                    }
                    catch(Exception e){
                        priceEditText.setText(formatter.format(roadLength).toString());
                    }

                }
            }
        });
        /**
         * Save button that instantiates a new Request and creates a new index in Elasticsearch of
         * the type "request".
         */
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                userController.setContext(NewRiderRequestActivity.this);
                //requestListController.setContext(NewRiderRequestActivity.this);
                riderRequestsController.setContext(NewRiderRequestActivity.this);


                String startLocation = startPoint.toString();
                String endLocation = endPoint.toString();
                String startAddress = startEditText.getText().toString();
                String endAddress = endEditText.getText().toString();
                String riderStory = storyEditText.getText().toString();
                Double fare = Double.parseDouble(priceEditText.getText().toString().substring(1,priceEditText.getText().toString().length()));
                Double distance = roadLength;

                Request request = new Request(startLocation, endLocation, user, riderStory, pendingDrivers, driver);
                request.setRequestStatus(Request.RequestStatus.Open);
                request.setFare(fare);
                request.setStartAddress(startAddress);
                request.setEndAddress(endAddress);
                request.setRoadLength(distance);
                ElasticsearchRequestController.CreateRequest createRequest;
                createRequest = new ElasticsearchRequestController.CreateRequest();
                createRequest.execute(request);

                // Add the request to Elasticsearch
                RiderRequestsController.addRequestToElasticsearch(request);

                // Add to the local list
                RiderRequestsController.addRiderRequest(request);

                Collection<Request> requests = RiderRequestsController.getRiderRequests().getRequests();
                RiderRequestsController.saveRequestInLocalFile(requests, NewRiderRequestActivity.this);

                //user.getRiderRequestIds().add(requestId);
                //UserController.updateUser(user);

                finish();
            }
        });



        /********************************************** MAPS STUFF ********************************************/
        /**
         * Important! set your user agent to prevent getting banned from the osm servers
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);

        /*
         * TODO : Current location does not work on all machines for some reason
         * Comment out the next four lines for testing purposes,
         * figure out what is wrong with current location later.
         * It is not a requirement to have the current location as the starting point anyhow...
         */
        /*
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        startEditText.setText(currentPoint.toString());
        endEditText.setText(currentPoint.toString());
*/
        AsyncTask<GeoPoint, Void, String> getAddress = new GetAddressFromGeoPointTask(new GetAddressFromGeoPointTask.AsyncResponse() {

            @Override
            public void processFinish(String output) {
                //Double temp = output.mLength; //see also mDuration
                startEditText.setText(output);
                endEditText.setText(output);
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.

            }
        }).execute(currentPoint);
        //startEditText.setText(currentPoint.toString()); //replaced with GetAddressFromGeoPointTask()
        //endEditText.setText(currentPoint.toString());


        map = (MapView) findViewById(R.id.NRRAMap);
        map.getOverlays().add(0, mapEventsOverlay);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);

        /**
         * The startPoint and endPoint are subject to change once we figure out what causes the
         * program to crash when it tries to find the current location.
         * --> The problem may be when locationManager calls the getLastKnownLocation method.
         * --> (Lines: 214-215)
         */
          startPoint = currentPoint;
          endPoint = currentPoint;

        //startPoint = new GeoPoint(53.5232, -113.5263);
        //endPoint = new GeoPoint(53.5232, -113.5263);

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

        /**
         * Set icon markers on map for user to pick start and end location for new request.
         */
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
        AsyncTask<ArrayList<GeoPoint>, Void, Road> task = new BuildRoadTask(map, roadManager, new BuildRoadTask.AsyncResponse(){
            @Override
            public void processFinish(Road output){
                roadLength = output.mLength; //see also mDuration
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                priceEditText.setText(formatter.format(roadLength).toString());
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.

            }
        }).execute(waypoints);

    }


    @Override
    protected void onStop() {
        super.onStop();
        RequestList requestList = RiderRequestsController.getRiderRequests();
        RiderRequestsController.saveRequestInLocalFile(requestList, getApplicationContext());
    }



    /********************************************** MAPS GUI ***************************************/
    @Override public boolean singleTapConfirmedHelper(GeoPoint p) {
        if(dragger.mTrace.get(0).equals(dragger.mTrace.get(1))){
            dragger.onMarkerDragStart(endMarker);
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

    /**
     * deals with location changes
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    /**
     * Deals with a change in status of the map.
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

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

        /**
         * Deals with a marker drag, unused
         * @param marker
         */
        @Override public void onMarkerDrag(Marker marker) {
            //mTrace.add(marker.getPosition());
        }

        /**
         * Deals with the marker stopping being dragged.
         * @param marker
         */
        @Override public void onMarkerDragEnd(Marker marker) {

            mTrace.add(marker.getPosition());
            Road road;
            //http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
            AsyncTask<ArrayList<GeoPoint>, Void, Road> task = new BuildRoadTask(map, roadManager, new BuildRoadTask.AsyncResponse(){

                @Override
                public void processFinish(Road output){
                    roadLength = output.mLength; //see also mDuration
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    priceEditText.setText(formatter.format(roadLength).toString());
                    //Here you will receive the result fired from async class
                    //of onPostExecute(result) method.
                }
            }).execute(mTrace);
            if (marker.equals(startMarker)){
                //update start location text
                //startEditText.setText(startMarker.getPosition().toString());
                startPoint = marker.getPosition();
                AsyncTask<GeoPoint, Void, String> getAddress = new GetAddressFromGeoPointTask(new GetAddressFromGeoPointTask.AsyncResponse() {

                    @Override
                    public void processFinish(String output) {
                        startEditText.setText(output);
                        //Here you will receive the result fired from async class
                        //of onPostExecute(result) method.
                    }
                }).execute(startMarker.getPosition());
            }
            else if (marker.equals(endMarker)){
                //endEditText.setText(endMarker.getPosition().toString());
                endPoint = marker.getPosition();
                AsyncTask<GeoPoint, Void, String> getAddress = new GetAddressFromGeoPointTask(new GetAddressFromGeoPointTask.AsyncResponse() {

                    @Override
                    public void processFinish(String output) {
                        endEditText.setText(output);
                        //Here you will receive the result fired from async class
                        //of onPostExecute(result) method.
                    }
                }).execute(endMarker.getPosition());
            }
        }

        /**
         * Deals with the marker starting being dragged.
         * @param marker
         */
        @Override public void onMarkerDragStart(Marker marker) {
            if(mTrace.size()>=2)
            {
            if(marker.getPosition().equals(mTrace.get(0))) {
                mTrace.remove(0);
            }
            else {
                mTrace.remove(1);
            }
            try {
                map.getOverlays().remove(3); map.invalidate();
            } catch(Exception e){}
            }
        }
    }
}