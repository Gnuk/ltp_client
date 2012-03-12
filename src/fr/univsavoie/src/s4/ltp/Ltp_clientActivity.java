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
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        TextView potesView = (TextView)this.findViewById(R.id.potes_text_view);
        potesView.setText(response);
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