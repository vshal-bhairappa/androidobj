package com.along.friendlyreminder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * 距离传感器，接近距离传感器
 * 
 * @author Windows
 * 
 */

public class ProximityActivity extends Activity implements SensorEventListener {

	private static String TAG = "ProximityActivity";

	private Button btn;

	private TextView tv_context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		infoViews();// 初始化控件

	}

	private void infoViews() {

		btn = (Button) findViewById(R.id.btn_sensor);
		tv_context = (TextView) findViewById(R.id.tv_context);
		tv_context.setText("接近，近距离传感器");
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	}

	private Sensor mAccelerometer;

	private SensorManager mSensorManager;
	private ScreenOFFReceiver offReceiver;
	@Override
	protected void onResume() {
		//注册闭屏广播
		IntentFilter mFilter01;
		mFilter01 = new IntentFilter("android.intent.action.SCREEN_OFF");
		offReceiver = new ScreenOFFReceiver();
		registerReceiver(offReceiver, mFilter01);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}
	//在接受到闭屏广播之后再重新注册一下感应器监听
	public class ScreenOFFReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			mSensorManager.registerListener(ProximityActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		}
		
	}
	
	protected void onPause() {

		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

		Log.d(TAG, accuracy + "--" + sensor.getMaximumRange());
	}

	long lastTime;

	private MediaPlayer mp;
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			long currentTime = SystemClock.elapsedRealtime();
			long spaceTime = currentTime - lastTime;
			lastTime = currentTime;
			float distance = event.values[0];

			boolean active = (distance >= 0.0 && distance < 10 && distance < mAccelerometer.getMaximumRange()); // 如果距离小于某一个距离阈值，默认是5.0f，说明手机和脸部距离贴近，应该要熄灭屏幕。
//			MediaPlayer mp=new MediaPlayer();
			if (spaceTime > 1000) {
				Log.e(TAG, distance + "::" + active);
				if (active) {
					try {
						mp = MediaPlayer.create(this, R.raw.refreshing_sound);
//						mp.release();
//						mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//	                    mp.prepare();
	                    mp.start();
                    } catch (IllegalStateException e) {
                    	Log.e(TAG, ":IllegalStateException:" + active);
	                    e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                    	Log.e(TAG, distance + ":IllegalArgumentException:" + active);
	                    e.printStackTrace();
                    } catch (SecurityException e) {
                    	Log.e(TAG, distance + ":SecurityException:" + active);
	                    e.printStackTrace();
                    } 
//					catch (IOException e) {
//                    	Log.e(TAG, distance + ":IOException:" + active);
//	                    e.printStackTrace();
//                    } 
				}else{
//					mp.pause();
//					mp.stop();
//					mp.release();
				}
			}
		}
	}
}
