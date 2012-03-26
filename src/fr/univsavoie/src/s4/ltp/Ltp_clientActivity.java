package fr.univsavoie.src.s4.ltp;

import java.io.IOException;
import java.text.DecimalFormat;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import 	org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class Ltp_clientActivity extends MapActivity {
	private MapView mapView;
	private MapController controller;
	private MyLocationOverlay me;
    /* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView)this.findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(true);
        controller = mapView.getController();
        me = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(me);
        
        DecimalFormat fmt = new DecimalFormat("#.000");
        
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new  HttpGet("http://jibiki.univ-savoie.fr/teapot/api/localisations/carron/friendships?format=json");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = null;
        try {
			response = httpClient.execute(httpGet, responseHandler);

			JSONObject potes = new JSONObject(response);			
			JSONArray potesArray = potes.getJSONObject("gpx").getJSONArray("wpt");
			
			Drawable marker = this.getResources().getDrawable(R.drawable.tea);
			PotesItemizedOverlay potesOverlay = new PotesItemizedOverlay(marker, potesArray);
			mapView.getOverlays().add(potesOverlay);

			String potesDesc = "";
			
			LayoutInflater lf = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
	        LinearLayout potesView = (LinearLayout)this.findViewById(R.id.potes_view);

			
	        for (int i = 0 ; (i < potesArray.length()) ; i++ ){
	        	View poteView = lf.inflate(R.layout.pote_view, null);	        	
	        	TextView nameTF = (TextView)poteView.findViewById(R.id.pote_name);
	        	TextView latTF = (TextView)poteView.findViewById(R.id.pote_lat);
	        	TextView lonTF = (TextView)poteView.findViewById(R.id.pote_lon);
	        	JSONObject pote = potesArray.getJSONObject(i);
	        	nameTF.setText(pote.getString("name"));
	        	latTF.setText(fmt.format(pote.getDouble("@lat")));
	        	lonTF.setText(fmt.format(pote.getDouble("@lon")));
	        	potesView.addView(poteView);
	        	poteView.setOnClickListener(new PoteClickListener(mapView, pote));
	        }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		controller.setZoom(10);
		controller.animateTo(new GeoPoint(45000000,5000000));
        me.enableMyLocation();
        me.runOnFirstFix(new Runnable() {
			
			@Override
			public void run() {
				controller.animateTo(me.getMyLocation());
				//me.disableMyLocation();
			}
		});

	}

	@Override
	protected void onPause() {
        me.disableMyLocation();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}