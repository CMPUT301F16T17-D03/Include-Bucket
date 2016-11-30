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

    private TextView startEditText, endEditText, priceEditText, storyText;
    private Button completeButton, viewDriverButton;
    private Marker startMarker;
    private Marker endMarker;
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private MapView map;
    private RoadManager roadManager;

    private String toastMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_request);

        UserFileManager.initManager(this.getApplicationContext());
        RiderRequestsFileManager.initManager(this.getApplicationContext());

        final Request request = (Request) getIntent().getSerializableExtra("Request");

        startEditText = (TextView) findViewById(R.id.DSRAStartEditText);
        endEditText   = (TextView) findViewById(R.id.DSRAEndEditText);
        priceEditText = (TextView) findViewById(R.id.DSRAPriceEditText);
        storyText     = (TextView) findViewById(R.id.DSRAstory);
        completeButton   = (Button) findViewById(R.id.completeRequestButton);
        viewDriverButton = (Button) findViewById(R.id.viewDriverButton);

        /**
         * Important! set your user agent to prevent getting banned from the osm servers
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(
                BuildConfig.APPLICATION_ID);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map = (MapView) findViewById(R.id.SRMap);
        map.getOverlays().add(0, mapEventsOverlay);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);

        Double price = request.getFare();

        String story = request.getRiderStory();
        String startAddress = request.getStartAddress();
        String endAddress = request.getEndAddress();
        String make = request.getDriver().getVehicleMake();
        String model = request.getDriver().getVehicleModel();
        String year = request.getDriver().getVehicleYear();

        startPoint = new GeoPoint(Double.parseDouble(request.getStartLocation().split(",")[0]),
                Double.parseDouble(request.getStartLocation().split(",")[1]));
        endPoint = new GeoPoint(Double.parseDouble(request.getEndLocation().split(",")[0]),
                Double.parseDouble(request.getEndLocation().split(",")[1]));

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
        AsyncTask<ArrayList<GeoPoint>, Void, Road> task = new BuildRoadTask(
                map, roadManager, new BuildRoadTask.AsyncResponse() {
            @Override
            public void processFinish(Road output) {}}).execute(waypoints);

        startEditText.setText(startAddress);
        endEditText.setText(endAddress);
        storyText.setText(story);

        Formatter formatter = new Formatter();
        String p = formatter.format("%.2f%n", request.getFare()).toString();
        priceEditText.setText("$" + p);

        toastMsg = null;
        if (request.getRequestStatus() != Request.RequestStatus.Closed) {
            completeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    request.setRequestStatus(Request.RequestStatus.Closed);
                    Toast.makeText(RiderSingleRequestActivity.this, "Request Completed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            toastMsg = "Back to your requests!";
            storyText.setText("You already accepted this request.");
            storyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            completeButton.setText("COMPLETE");
            completeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RiderRequestsController.deleteRequestFromElasticsearch(request);
                    RiderRequestsController.deleteRequest(request);
                    Intent intent = new Intent(RiderSingleRequestActivity.this, RiderCurrentRequestsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        viewDriverButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(RiderSingleRequestActivity.this, ViewDriverDataActivity.class);
                intent.putExtra("User", request.getChosenDriver());
                intent.putExtra("Request", request);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }
}