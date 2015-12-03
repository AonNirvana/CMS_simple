package com.egco428.cms_simple;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;
    private Sensor flirt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO sensor stuffs.
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        flirt = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        manager.registerListener(this,flirt,SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Mandatory function to fly from the main page to the article create page.
     * The function should be added through main page's XML so its needs to get a View argument is
     * mandatory. Otherwise that View argument is not so needed.
     * @param view Nothing else in particular.
     */
    public void mainToCreate(View view) {
        Intent intent = new Intent(MainActivity.this,CreateActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Mandatory function to fly from the main page to the article view page.
     * The function should be added through main page's XML so its needs to get a View argument is
     * mandatory. Otherwise that View argument is not so needed.
     * @param view Nothing else in particular.
     */
    public void mainToView(View view) {
        Intent intent = new Intent(MainActivity.this,ViewActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(Math.pow(event.values[0],2) > Math.pow(7 * Math.PI,2)) {
            //Should exit this application.
            //I keep finish all activities on page transition so this should what it be.
            finish();
        }
        if(event.values[1] > 1.5) {
            Intent intent = new Intent(MainActivity.this,CreateActivity.class);
            startActivity(intent);
            finish();
        }
        if(event.values[1] < -1.5) {
            Intent intent = new Intent(MainActivity.this,ViewActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     * <p/>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Nothing in particular. Can be ignored.
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.unregisterListener(this, flirt);
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.registerListener(this, flirt, SensorManager.SENSOR_DELAY_GAME);
    }
}
