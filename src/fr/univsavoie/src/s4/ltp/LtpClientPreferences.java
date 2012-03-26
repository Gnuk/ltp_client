package fr.univsavoie.src.s4.ltp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class LtpClientPreferences extends PreferenceActivity {

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

}
