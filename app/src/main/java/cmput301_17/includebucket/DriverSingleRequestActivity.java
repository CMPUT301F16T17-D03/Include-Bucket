package cmput301_17.includebucket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
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

import java.lang.reflect.Array;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;

/**
 * DriverSingleRequestActivity
 *
 * In this class, the user should be able to specify the start and end locations by typing in an
 * address or by clicking on the map (clicking on the map should automatically fill in the start
 * and end locations). These locations are values for instantiating a new Request.
 */
public class DriverSingleRequestActivity extends Activity implements MapEventsReceiver {

    private TextView startEditText;
    private TextView endEditText;
    private TextView priceEditText;
    private TextView storyText;
    private Marker startMarker;
    private Marker endMarker;
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private MapView map;
    private RoadManager roadManager;
    private ArrayList<UserAccount> drivers;
    private UserAccount driver;
    private Button acceptButton, riderDetailsButton;
    private ConnectivityManager connectivityManager;
    private boolean connected, browse;
    private Request newRequest = new Request();
    private UserAccount user = new UserAccount();
    private Request request;

    /**
     * Deals with most map functionality. Gets permissions to run map in phone.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_single_request_activity);

        UserFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        user    = UserController.getUserAccount();
        request = (Request) getIntent().getSerializableExtra("Request");
        browse  = (boolean) getIntent().getExtras().getBoolean("BrowseActivity");

        drivers = new ArrayList<UserAccount>();
        driver  = new UserAccount();

        startEditText = (TextView) findViewById(R.id.DSRAStartEditText);
        endEditText = (TextView) findViewById(R.id.DSRAEndEditText);
        priceEditText = (TextView) findViewById(R.id.DSRAPriceEditText);
        storyText = (TextView) findViewById(R.id.DSRAstory);
        acceptButton = (Button) findViewById(R.id.DSRAAcceptButton);
        riderDetailsButton = (Button) findViewById(R.id.riderDetailsButton);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() ==
                NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() ==
                        NetworkInfo.State.CONNECTED)
        {
            connected = true;
        }
        else connected = false;

        /*************************************** PERMISSIONS **************************************/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {}
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {}
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        /****************************************** MAPS ******************************************/
        /**
         * Important! set your user agent to prevent getting banned from the osm servers
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);

        map = (MapView) findViewById(R.id.DSRAmap);
        map.getOverlays().add(0, mapEventsOverlay);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);

        request = (Request) getIntent().getSerializableExtra("Request");
        startEditText.setText(request.getStartAddress());
        startPoint = new GeoPoint(Double.parseDouble(request.getStartLocation().split(",")[0]),Double.parseDouble(request.getStartLocation().split(",")[1])) ;

        endEditText.setText(request.getEndAddress());
        endPoint = new GeoPoint(Double.parseDouble(request.getEndLocation().split(",")[0]),Double.parseDouble(request.getEndLocation().split(",")[1]));

        Formatter formatter = new Formatter();
        String p = formatter.format("%.2f%n", request.getFare()).toString();
        priceEditText.setText("$"+p);
        storyText.setText(request.getRiderStory());

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

        startMarker.setIcon(getResources().getDrawable(R.mipmap.marker_green));
        endMarker.setIcon(getResources().getDrawable(R.mipmap.marker_red));

        startMarker.setTitle("Start point");
        startMarker.setDraggable(false);

        endMarker.setTitle("End point");
        endMarker.setDraggable(false);

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        AsyncTask<ArrayList<GeoPoint>, Void, Road> task = new BuildRoadTask(map, roadManager, new BuildRoadTask.AsyncResponse(){
            @Override
            public void processFinish(Road output){
            }
        }).execute(waypoints);

        /******************************************************************************************/


        if (request.getRequestStatus() == Request.RequestStatus.Open)
        {
            acceptButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setResult(RESULT_OK);

                    if (connected)
                    {
                        request.setRequestStatus(Request.RequestStatus.PendingDrivers);
                        request.setDriverAccepted(true);
                        request.addDriver(user);

                        DriverRequestsController.deleteRequestFromElasticsearch(request);

                        ElasticsearchRequestController.CreateRequest createRequest;
                        createRequest = new ElasticsearchRequestController.CreateRequest();
                        createRequest.execute(request);
                    }
                    else
                    {
                        String riderStory = request.getRiderStory();
                        ArrayList<UserAccount> pendingDrivers = new ArrayList<>(request.getDrivers());

                        AcceptRequestCommand acceptRequestCommand = new AcceptRequestCommand();
                        acceptRequestCommand.createAcceptRequest(startPoint, endPoint, request.getRider(), riderStory, pendingDrivers, user, request.getStartAddress(), request.getEndAddress(), request.getFare(), request.getRequestID());

                        OfflineRequestQueue.addCommand(acceptRequestCommand);
                        Log.i("\n\n\nRequest ID","" +request.getRequestID());
                    }
                    Toast.makeText(DriverSingleRequestActivity.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }
        else if (request.getRequestStatus() == Request.RequestStatus.PendingDrivers)
        {
            ArrayList<UserAccount> drivers = new ArrayList<>(request.getDrivers());

            boolean foundDriver = Boolean.FALSE;
            for (UserAccount u : drivers) {
                if (u.getUniqueUserName().equals(user.getUniqueUserName()))
                {
                    foundDriver = Boolean.TRUE;
                }
            }

            if (foundDriver && browse)
            {
                acceptButton.setText("REQUESTS");
                acceptButton.setGravity(Gravity.CENTER_HORIZONTAL);
                AlertDialog.Builder adb = new AlertDialog.Builder(DriverSingleRequestActivity.this);
                adb.setMessage("You already accepted this request! You can view this request in YOUR OFFERS.");
                adb.setCancelable(true);

                adb.setPositiveButton("Got It!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                adb.show();

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
            else
            {
                acceptButton.setText("ACCEPT");
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setResult(RESULT_OK);
                        if (connected)
                        {
                            request.setRequestStatus(Request.RequestStatus.PendingDrivers);
                            request.setDriverAccepted(true);
                            request.addDriver(user);

                            DriverRequestsController.deleteRequestFromElasticsearch(request);

                            ElasticsearchRequestController.CreateRequest createRequest;
                            createRequest = new ElasticsearchRequestController.CreateRequest();
                            createRequest.execute(request);
                        }
                        else
                        {
                            String riderStory = request.getRiderStory();
                            ArrayList<UserAccount> pendingDrivers = new ArrayList<>(request.getDrivers());

                            AcceptRequestCommand acceptRequestCommand = new AcceptRequestCommand();
                            acceptRequestCommand.createAcceptRequest(startPoint, endPoint, request.getRider(), riderStory, pendingDrivers, user, request.getStartAddress(), request.getEndAddress(), request.getFare(), request.getRequestID());

                            OfflineRequestQueue.addCommand(acceptRequestCommand);
                            Log.i("\n\n\nRequest ID","" +request.getRequestID());
                        }
                        Toast.makeText(DriverSingleRequestActivity.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

        }
        else if (request.getRequestStatus() == Request.RequestStatus.Closed)
        {
            storyText.setText("This request is now closed.");
            acceptButton.setText("REQUESTS");
            acceptButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }

        riderDetailsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(DriverSingleRequestActivity.this, ViewRiderDataActivity.class);
                intent.putExtra("Request", request);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }
    @Override public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    /**
     * Handles the dragging of a marker, and updates dependant widgets.
     */
    private class OnMarkerDragDrawer implements Marker.OnMarkerDragListener {
        ArrayList<GeoPoint> mTrace;
        OnMarkerDragDrawer() {
            mTrace = new ArrayList<GeoPoint>(2);
            mTrace.add(startMarker.getPosition());
            mTrace.add(endMarker.getPosition());

        }

        @Override
        public void onMarkerDrag(Marker marker) {}

        /**
         * Sets the position when the makrer stops being dragged
         *
         * @param marker
         */
        @Override
        public void onMarkerDragEnd(Marker marker) {

            mTrace.add(marker.getPosition());
            map.getOverlays().remove(3);
            AsyncTask<ArrayList<GeoPoint>, Void, Road> task = new BuildRoadTask(map, roadManager, new BuildRoadTask.AsyncResponse(){
                @Override
                public void processFinish(Road output){
                }
            }).execute(mTrace);
            if (marker.equals(startMarker))
            {
                startEditText.setText(startMarker.getPosition().toString());
            }
            else if (marker.equals(endMarker))
            {
                endEditText.setText(endMarker.getPosition().toString());
            }
            String price = ""+startMarker.getPosition().distanceTo(endMarker.getPosition());
            priceEditText.setText(price);
        }

        /**
         * Starts the maker being dragged and gets the position
         *
         * @param marker
         */
        @Override
        public void onMarkerDragStart(Marker marker)
        {
            if(marker.getPosition().equals(mTrace.get(0)))
            {
                mTrace.remove(0);
            }
            else
            {
                mTrace.remove(1);
            }
        }
    }
}