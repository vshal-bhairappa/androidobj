package com.along.friendlyreminder;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * ���ߴ�����
 * 
 * ���ߴ����������ͳ�����Sensor.TYPE_LIGHT��values����ֻ�е�һ��Ԫ�أ�values[0]�������塣��ʾ���ߵ�ǿ�ȡ�
 * ����ֵ��120000.0f��Android SDK������ǿ�ȷ�Ϊ��ͬ�ĵȼ���ÿһ���ȼ������ֵ��һ��������ʾ��
 * ��Щ������������SensorManager���У��������£�
 * 
 * public static final float LIGHT_SUNLIGHT_MAX =120000.0f;
 * 
 * public static final float LIGHT_SUNLIGHT=110000.0f;
 * 
 * public static final float LIGHT_SHADE=20000.0f;
 * 
 * public static final float LIGHT_OVERCAST= 10000.0f;
 * 
 * public static final float LIGHT_SUNRISE= 400.0f;
 * 
 * public static final float LIGHT_CLOUDY= 100.0f;
 * 
 * public static final float LIGHT_FULLMOON= 0.25f;
 * 
 * public static final float LIGHT_NO_MOON= 0.001f;
 * 
 * ����İ˸�����ֻ���ٽ�ֵ��������ʵ��ʹ�ù��ߴ�����ʱҪ����ʵ�����ȷ��һ����Χ��
 * ���磬��̫��������ʱ��values[0]��ֵ�ܿ��ܻᳬ��LIGHT_SUNRISE����values[0]��ֵ������ʱ��
 * �ͻ���Խ��LIGHT_OVERCAST�����ﵽLIGHT_SHADE����Ȼ��������ر�õĻ���Ҳ���ܻ�ﵽLIGHT_SUNLIGHT����������
 * 
 * @author Windows
 * 
 */
public class LightActivity extends Activity implements SensorEventListener {

	private static String TAG = "LightActivity���ߴ�����";

	private Button btn;

	private TextView tv_context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		infoViews();// ��ʼ���ؼ�

	}

	private void infoViews() {

		btn = (Button) findViewById(R.id.btn_sensor);
		tv_context = (TextView) findViewById(R.id.tv_context);
		tv_context.setText("���߸�Ӧ������");
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	}

	private Sensor mAccelerometer;

	private SensorManager mSensorManager;

	@Override
	protected void onResume() {

		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	protected void onPause() {

		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

		// Log.d(TAG, accuracy + "--" + sensor.getMaximumRange());
	}

	public void onSensorChanged(SensorEvent event) {

		float[] values = event.values;
		tv_context.setText("X��⴫������ֵ����"+values[0]+"\nY�⴫������ֵ����"+values[1]+"\nZ��⴫������ֵ����"+values[2]);
		Log.d(TAG, "��һ���������ã�����ǿ�ȣ�" + values[0]);
		if(values[0]<100){
			//��ʾ����̫��
		}else if(values[0]>320){
			//��ʾ����̫ǿ
		}
	}
}