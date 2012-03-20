package fr.univsavoie.src.s4.ltp;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PoteOverlayItem extends OverlayItem {

	public PoteOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

}
