package com.egco428.cms_simple;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateActivity extends AppCompatActivity implements SensorEventListener {

    private ArticleHelper helper;
    private EditText contentKey;
    private EditText contentCol;

    private SensorManager manager;
    private Sensor quake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //Set components.
        helper = new ArticleHelper(this.getApplicationContext());
        contentKey = (EditText)findViewById(R.id.contentKey);
        contentCol = (EditText)findViewById(R.id.contentCol);

        //Set Sensor.
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        quake = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this,quake,SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Mandatory function to fly from the article create page to the main page.
     * The function should be added through main page's XML so its needs to get a View argument is
     * mandatory. Otherwise that View argument is not so needed.
     * @param view Nothing else in particular.
     */
    public void createToMain(View view) {
        manager.unregisterListener(this,quake);
        Intent intent = new Intent(CreateActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This function is for saving the article data created from here. It should involve database
     * invoking and stuffs before navigate elsewhere.
     * The function should be added through main page's XML so its needs to get a View argument is
     * mandatory. Otherwise that View argument is not so needed.
     * @param view
     */
    public void confirmButton(View view) {
        helper.i1Article(contentKey.getText().toString(), contentCol.getText().toString());
        createToMain(view);
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
        double x = Math.pow(event.values[0]/SensorManager.GRAVITY_EARTH,2);
        double y = Math.pow(event.values[1]/SensorManager.GRAVITY_EARTH,2);
        double z = Math.pow(event.values[2]/SensorManager.GRAVITY_EARTH,2);

        if(Math.sqrt(x + y + z) < 2.7) {return;}
        else {
            //return to main without save.
            manager.unregisterListener(this,quake);
            Intent intent = new Intent(CreateActivity.this,MainActivity.class);
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
        //Ignore it. Is not needed anyway.
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.unregisterListener(this, quake);
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.registerListener(this, quake, SensorManager.SENSOR_DELAY_GAME);
    }
}
