package fr.univsavoie.src.s4.ltp;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import 	org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ltp_clientActivity extends Activity {
    /* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new  HttpGet("http://jibiki.univ-savoie.fr/teapot/api/localisations/carron/friendships?format=json");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = null;
        try {
			response = httpClient.execute(httpGet, responseHandler);

			JSONObject potes = new JSONObject(response);			
			JSONArray potesArray = potes.getJSONObject("gpx").getJSONArray("wpt");

			String potesDesc = "";
			
	        
	        for (int i = 0 ; (i < potesArray.length()) ; i++ ){
	        	JSONObject pote = potesArray.getJSONObject(i);
	        	String poteDesc = "nom : "+ pote.getString("name")+"\n\tlat : "+pote.getDouble("@lat")+"\n\tlon : "+pote.getDouble("@lon");
	        	potesDesc = potesDesc + poteDesc + "\n";
	        }
	        TextView potesView = (TextView)this.findViewById(R.id.potes_text_view);
	        potesView.setText(potesDesc);

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
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

}