package fr.univsavoie.src.s4.ltp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

public class PotesItemizedOverlay extends ItemizedOverlay<PoteOverlayItem> {
	private JSONArray potes = null;

	public PotesItemizedOverlay(Drawable defaultMarker, JSONArray potes) {
		this(boundCenter(defaultMarker));
		this.potes = potes;
		populate();
	}

	public PotesItemizedOverlay(Drawable defaultMarker) {
		super(defaultMarker);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected PoteOverlayItem createItem(int i) {
		PoteOverlayItem item = null;
		try {
			JSONObject pote = potes.getJSONObject(i) ;
			GeoPoint pos = new GeoPoint((int)(pote.getDouble("@lat")*1e6), (int)(pote.getDouble("@lon")*1e6));
			item = new PoteOverlayItem(pos, pote.getString("name"), pote.optString("desc", "Je suis là"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public int size() {
		int size = 0;
		if (potes != null){
			size = potes.length();
		}
		return size;
	}

}
