package com.egco428.cms_simple;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Map;
import java.util.Vector;

public class ViewActivity extends AppCompatActivity implements ViewFragment.OnFragmentInteractionListener,SensorEventListener {

    private ViewFragment fragment;
    private ListView listView;
    private ArticleHelper helper;
    private static final Vector<Map<String,String>> articles = new Vector<Map<String,String>>();
    private ListAdapter adapter;

    private SensorManager manager;
    private Sensor quake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        fragment = (ViewFragment)getFragmentManager().findFragmentById(R.id.fragment);
        listView = (ListView)findViewById(R.id.listView);

        helper = new ArticleHelper(this.getApplicationContext());
        helper.cLArticle(articles);
        adapter = new SimpleAdapter(this,articles,android.R.layout.simple_list_item_2,new String[] {ViewFragment.ARG_KEY,ViewFragment.ARG_COL},new int[] {android.R.id.text1,android.R.id.text2});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> article = (Map<String, String>) listView.getItemAtPosition(position);
                String key = article.get(ViewFragment.ARG_KEY);
                String col = article.get(ViewFragment.ARG_COL);
                fragment.onListViewPressed(key, col);
            }
        });

        //Set Sensor.
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        quake = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this, quake, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Mandatory function to fly from the article view page to the main page.
     * The function should be added through main page's XML so its needs to get a View argument is
     * mandatory. Otherwise that View argument is not so needed.
     * @param view Nothing else in particular.
     */
    public void createToMain(View view) {
        manager.unregisterListener(this,quake);
        Intent intent = new Intent(ViewActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Reload the database (which should be editted via fragment), update adapter and thus change
     * the list.
     */
    @Override
    public void onFragmentInteraction(String key, String col, boolean update) {

        //Value from fragment is going to database here, either update or delete.
        if(update) {helper.u1Article(key,col);}
        else {helper.d1Article(key);}
        //change the value of that Vector articles and then reload it.
        articles.removeAllElements();
        helper.cLArticle(articles);
        adapter = new SimpleAdapter(this,articles,android.R.layout.simple_list_item_2,new String[] {ViewFragment.ARG_KEY,ViewFragment.ARG_COL},new int[] {android.R.id.text1,android.R.id.text2});
        listView.setAdapter(adapter);
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
            Intent intent = new Intent(ViewActivity.this,MainActivity.class);
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
