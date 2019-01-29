package tawhidz13.servicedemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.R.attr.x;
import static android.R.attr.y;

public class MyService extends Service implements SensorEventListener {
	
	private static final String TAG = "MotionControlService";

    private float mGZ = 0;//gravity acceleration along the z axis
    private int mEventCountSinceGZChanged = 0;
    private static final int MAX_COUNT_GZ_CHANGE = 4;
    private SensorManager mSensorManager;
    private SensorManager mSensorManager2;
    private static final int SENSOR_SENSITIVITY = 4;
	private static final int SHAKE_THRESHOLD = 800;
	//private float x=0,y=0,z=0,last_x=0,last_y=0,last_z=0;
	//private long lastUpdate=0;

	AudioManager aMan;
	private int flag=0;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
		Log.d("a","Created");
		 aMan=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show();
		Log.d("a","Destroyed");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Log.d("a","Started");
		Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
            mSensorManager2.registerListener(this, mSensorManager2.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_GAME);

		return START_STICKY;
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		 int type = arg0.sensor.getType();

		 if (type == Sensor.TYPE_PROXIMITY) {
	            if (arg0.values[0] >= -SENSOR_SENSITIVITY && arg0.values[0] <= SENSOR_SENSITIVITY) {
	                //near
	            	Log.d(TAG,"near");
	            	flag=1;
					Toast.makeText(this,"near", Toast.LENGTH_SHORT).show();
	            } else {
	                //far
	            	Log.d(TAG,"far");
	            	flag=0;
					Toast.makeText(this, "far", Toast.LENGTH_SHORT).show();
	            }
	        }
		 if (type == Sensor.TYPE_ACCELEROMETER) {
	            float gz = arg0.values[2];
	            if (mGZ == 0) {
	                mGZ = gz;
	            } else {
	                if ((mGZ * gz) < 0) {
	                    mEventCountSinceGZChanged++;
	                    if (mEventCountSinceGZChanged == MAX_COUNT_GZ_CHANGE) {
	                        mGZ = gz;
	                        mEventCountSinceGZChanged = 0;
	                        if (gz > 0 ) {
	                            Log.d(TAG, "now screen is facing up.");
	                    		Toast.makeText(this, "ScreeN uP", Toast.LENGTH_SHORT).show();
	                        } else if (gz < 0 && flag==1) {
	                            Log.d(TAG, "now screen is facing down.");
	                            makeSilent();
	                            Toast.makeText(this, "ScreeN down", Toast.LENGTH_SHORT).show();
	                        }
	                    }
	                } else {
	                    if (mEventCountSinceGZChanged > 0) {
	                        mGZ = gz;
	                        mEventCountSinceGZChanged = 0;
	                    }
	                }
	            }
		    }


		/*if (type == Sensor.TYPE_ACCELEROMETER) {
			long curTime = System.currentTimeMillis();
			// only allow one update every 100ms.
			if ((curTime - lastUpdate) > 100) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				x = arg0.values[0];
				y = arg0.values[1];
				z = arg0.values[2];

				float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

				if (speed > SHAKE_THRESHOLD) {
					Log.d("sensor", "shake detected w/ speed: " + speed);
					Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
					makeSilent();
				}
				last_x = x;
				last_y = y;
				last_z = z;
			}
		}
*/
	}

	public void makeSilent()
    {
    	/*for(int i=0;i<20;i++)
    	{
    	aMan.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    	}*/
    	//aMan.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    	aMan.setStreamVolume(AudioManager.STREAM_RING ,AudioManager.RINGER_MODE_SILENT,AudioManager.FLAG_SHOW_UI);
    }
	
	

}
