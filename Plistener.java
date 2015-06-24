package ca.uwaterloo.lab4_202_11;

import mapper.MapView;
import mapper.PositionListener;
import android.graphics.PointF;

public class Plistener implements PositionListener {
	
	public void originChanged(MapView source, PointF loc){
		source.setUserPoint(loc);
	}
	
	public void destinationChanged(MapView source, PointF dest){
		source.setDestinationPoint(dest);
	}

}
