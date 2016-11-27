package cmput301_17.includebucket;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

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

import java.util.ArrayList;
import java.util.Formatter;

/**
 * RiderSingleRequestActivity
 *
 * This class displays a request's information in full when it is invoked by the user in the
 * RiderCurrentRequestsActivity.
 */
public class RiderSingleRequestActivity extends MainMenuActivity implements MapEventsReceiver {

    private TextView startEditText, endEditText, priceEditText, storyText, dummyTextView;
    private Button completeButton;
    private ArrayList<UserAccount> driverList;
    private ArrayAdapter<UserAccount> driverListAdapter;

    private Marker startMarker;
    private Marker endMarker;
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private MapView map;
    private RoadManager roadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_request);

        UserFileManager.initManager(this.getApplicationContext());
        RiderRequestsFileManager.initManager(this.getApplicationContext());

        startEditText = (TextView) findViewById(R.id.DSRAStartEditText);
        endEditText = (TextView) findViewById(R.id.DSRAEndEditText);
        priceEditText = (TextView) findViewById(R.id.DSRAPriceEditText);
        storyText = (TextView) findViewById(R.id.DSRAstory);
        completeButton = (Button) findViewById(R.id.completeRequestButton);

        /**
         * Important! set your user agent to prevent getting banned from the osm servers
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        final Request request = (Request) getIntent().getSerializableExtra("Request");
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map = (MapView) findViewById(R.id.SRMap);
        map.getOverlays().add(0, mapEventsOverlay);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);

        Double price    = request.getFare();
        String startLoc = request.getStartLocation();
        String endLoc   = request.getEndLocation();
        String story    = request.getRiderStory();
        String startAddress = request.getStartAddress();
        String endAddress = request.getEndAddress();

        startPoint = new GeoPoint(Double.parseDouble(startLoc.split(",")[0]), Double.parseDouble(startLoc.split(",")[1]));
        endPoint = new GeoPoint(Double.parseDouble(endLoc.split(",")[0]), Double.parseDouble(endLoc.split(",")[1]));

        startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        startMarker.setIcon(getResources().getDrawable(R.mipmap.marker_green));
        startMarker.setTitle("Start point");
        startMarker.setDraggable(false);

        endMarker = new Marker(map);
        endMarker.setPosition(endPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(endMarker);
        endMarker.setIcon(getResources().getDrawable(R.mipmap.marker_red));
        endMarker.setTitle("End point");
        endMarker.setDraggable(false);

        mapController.setCenter(startPoint);
        roadManager = new OSRMRoadManager(this);
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        AsyncTask<ArrayList<GeoPoint>, Void, Road> task = new BuildRoadTask(map, roadManager, new BuildRoadTask.AsyncResponse(){
            @Override
            public void processFinish(Road output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
            }
        }).execute(waypoints);

        startEditText.setText(request.getStartAddress());
        endEditText.setText(endLoc);
        storyText.setText(request.getRiderStory());
        endEditText.setText(request.getEndAddress());

        Formatter formatter = new Formatter();
        String p = formatter.format("%.2f%n", request.getFare()).toString();
        priceEditText.setText("$"+p);

        if (request.getRequestStatus() != Request.RequestStatus.Closed) {
            completeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    RiderRequestsController.deleteRequest(request);
                    RiderRequestsController.deleteRequestFromElasticsearch(request);
                    Toast.makeText(RiderSingleRequestActivity.this, "Request Completed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else
        {
            storyText.setText("You already accepted this request.");
            storyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            completeButton.setText("BACK TO YOUR REQUESTS");
            completeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(RiderSingleRequestActivity.this, RiderCurrentRequestsActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public boolean longPressHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }
}