package ca.uwaterloo.lab4_202_11;

import mapper.MapLoader;
import mapper.MapView;
import mapper.NavigationalMap;
import mapper.PositionListener;
import android.app.Activity;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	LinearLayout layout;
	
	TextView title;
	TextView steps;
	TextView displacement;
	
	//mapview 
	public static MapView mapView;
	NavigationalMap map;
	
	SensorEventListener sensorEventListener;	
	SensorManager sensorManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout) findViewById(R.id.layout_main);
        title = createLabel("Lab 4 - PathFinding\n");
        
        // Generate Map*****
        mapView = new MapView(getApplicationContext(), 700, 700, 40, 40);
        
        registerForContextMenu(mapView);
        
        
        map = MapLoader.loadMap(getExternalFilesDir(null), "Lab-room-peninsula.svg");
        
        mapView.setMap(map);
        
        layout.addView(mapView);
        
        PositionListener Plistener = new PositionListener() {

			@Override
			public void originChanged(MapView source, PointF loc) {
				((AllSensorEventListener) sensorEventListener).setOrigin(loc);
				mapView.setUserPoint(loc);
			}

			@Override
			public void destinationChanged(MapView source, PointF dest) {
				((AllSensorEventListener) sensorEventListener).setDestination(dest);				
			}
        	
        };
        mapView.addListener(Plistener);    
        //******************
        
        
        //graph = createGraph(Arrays.asList("Raw Linear Acceleration(Z)", "Filtered Acceleration(Z)"));
        steps = createLabel("Steps: ");
        displacement = createLabel("Displacement: ");
        initalizeSensors();
        
        
       	
    }


	/*private MapView createMap(int width,int height,int xScale,int yScale) {
		return new MapView(getApplicationContext(),width,height,xScale,yScale);
	}*/

	/*public void loadMap(String fileName) {
		// Check if an SD card is available to hold the maps
		Boolean SDAvailable = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(SDAvailable) {
			NavigationalMap map = MapLoader.loadMap(getExternalFilesDir(null), fileName);
			mapView.setMap(map);
		}
		// No SD Card available
		else {
			Toast.makeText(getApplicationContext(), "Maps are not available", Toast.LENGTH_SHORT).show();
			
		}
	}*/

	private void initalizeSensors() {
        // Sensor Manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Initialize Sensors
        Sensor linearAccelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	Sensor geomagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorEventListener = new AllSensorEventListener(this,steps,displacement,mapView, map);
        
        // Register Sensors
        sensorManager.registerListener(sensorEventListener, linearAccelSensor, SensorManager.SENSOR_DELAY_FASTEST);
       	sensorManager.registerListener(sensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, geomagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		boolean Isnull = true;
    	if(mapView != null){
    		Isnull = false;
    		}
    	Log.d("MapView", String.valueOf(Isnull));
		super.onCreateContextMenu(menu, v, menuInfo);
		mapView.onCreateContextMenu(menu,v,menuInfo);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		// Pause
		case R.id.menu_pause:
			AllSensorEventListener.pause();
			break;
		// Clear values
		case R.id.menu_clear:
			if(reset()) {
				Toast.makeText(this, "Steps Cleared", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
    
	
    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	boolean Isnull = true;
    	if(item != null){
    		Isnull = false;
    		}
    	Log.d("Item", String.valueOf(Isnull));
    	return super.onContextItemSelected(item) || mapView.onContextItemSelected(item);
	}

	// Creates an individual text view and displays it
    // Returns the reference of the view
    public TextView createLabel (String name) {
    	
    	TextView tv = new TextView(getApplicationContext());
    	tv.setText(name);
    	layout.addView(tv);
    	return tv;
    }
    
    // Clears step count
    public boolean reset() {
    	((AllSensorEventListener) sensorEventListener).reset();
    	return true;
    }
}
