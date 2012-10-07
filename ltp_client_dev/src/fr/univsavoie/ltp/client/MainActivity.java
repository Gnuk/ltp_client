package fr.univsavoie.ltp.client;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * MainActivity is the main apps class
 *
 */
public class MainActivity extends Activity 
{
	/* Global variables */
	private MapView myOpenMapView;
	private MapController myMapController;
	private LocationManager locationManager;
	private Location lastLocation;
	private ArrayList<OverlayItem> overlayItemArray;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // MapView settings
        myOpenMapView = (MapView)findViewById(R.id.openmapview);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
		myOpenMapView.setBuiltInZoomControls(true);
		myOpenMapView.setMultiTouchControls(true);
		
		// MapController settings
        myMapController = myOpenMapView.getController();
        myMapController.setZoom(6);
        
        // Create Overlay
        overlayItemArray = new ArrayList<OverlayItem>();
        DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);
        MyItemizedIconOverlay myItemizedIconOverlay = new MyItemizedIconOverlay(overlayItemArray, null, defaultResourceProxyImpl);
        myOpenMapView.getOverlays().add(myItemizedIconOverlay);

        // Trace user thanks to GPS or Network Provider or Passive Provider (with chance, on of them must work !)
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
        	lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } 
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
        	lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        else
        {
        	lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        
        if(lastLocation != null)
        {
        	// Update current location thanks to infos send by location manager
        	updateLoc(lastLocation);
        }
        else
        {
    		// Set default GeoPoint
    		GeoPoint point2 = new GeoPoint(46.227638, 2.213749);
    		myMapController.setCenter(point2);
        }
        
        //Add Scale Bar
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this);
        myOpenMapView.getOverlays().add(myScaleBarOverlay);
        
        // Call for update user gps coordinates
        updateUserInterface(lastLocation);
    }
    
    @Override
	protected void onResume() 
    {
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		locationManager.removeUpdates(myLocationListener);
	}

	private void updateLoc(Location loc)
	{
        // Set new local GeoPoint for point to the map
    	GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
    	myMapController.setCenter(locGeoPoint);
    	
    	setOverlayLoc(loc);
    	
    	myOpenMapView.invalidate();
    	
    	updateUserInterface(loc);
    }
	
	private void updateUserInterface(Location loc)
	{
    	// Update user localization coordinates
        TextView myLocationText = (TextView)findViewById(R.id.textViewGeolocation);
        String latLongString = "";
        if (loc != null) {
            double lat = loc.getLatitude();
            double lng = loc.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;
        } else {
            latLongString = "No location found";
        }
        myLocationText.setText("Your Current Position is:\n" + latLongString);
	}
	
	private void setOverlayLoc(Location overlayloc)
	{
		GeoPoint overlocGeoPoint = new GeoPoint(overlayloc);
    	overlayItemArray.clear();
    	
    	OverlayItem newMyLocationItem = new OverlayItem("My Location", "My Location", overlocGeoPoint);
    	overlayItemArray.add(newMyLocationItem);
	}
    
    private LocationListener myLocationListener = new LocationListener()
    {
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			updateLoc(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    };
    
    private class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem>
    {
		public MyItemizedIconOverlay(
				List<OverlayItem> pList,
				org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
				ResourceProxy pResourceProxy) {
			super(pList, pOnItemGestureListener, pResourceProxy);
		}

		@Override
		public void draw(Canvas canvas, MapView mapview, boolean arg2) 
		{
			super.draw(canvas, mapview, arg2);
			
			if(!overlayItemArray.isEmpty()){
				
				//overlayItemArray have only ONE element only, so I hard code to get(0)
				GeoPoint in = overlayItemArray.get(0).getPoint();
				
				Point out = new Point();
				mapview.getProjection().toPixels(in, out);
				
				Bitmap bm = BitmapFactory.decodeResource(getResources(), 
						R.drawable.ic_menu_mylocation);
				canvas.drawBitmap(bm, 
						out.x - bm.getWidth()/2, 	//shift the bitmap center
						out.y - bm.getHeight()/2, 	//shift the bitmap center
						null);
			}
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event, MapView mapView) 
		{
			//return super.onSingleTapUp(event, mapView);
			return true;
		}
    }
}
