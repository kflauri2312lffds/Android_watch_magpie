package hevs.aislab.magpie.watch;

import android.hardware.SensorManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import ch.hevs.aislab.magpie.android.MagpieActivityWatch;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import hevs.aislab.magpie.watch.gui.FragmentAddGlucose;
import hevs.aislab.magpie.watch.gui.FragmentHome;

/**
 * Created by teuft on 31.05.2017.
 * this class will handle all other fragment related the the apps
 */

//implement the listener for the sensors
public class HomeActivity extends MagpieActivityWatch implements SensorEventListener {

    //handle the voice variable
    private static final int SPEECH_REQUEST_CODE = 0;
    FragmentManager manager;

    //fragment
    FragmentHome fragmentHome;
    FragmentAddGlucose fragmentAddGlucose;

    //sensors
    SensorManager sensorManager;
    Sensor sensor_pulse;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //display the fragment home witout any value
        displayFragment_home("");

        //init the sensors
        this.sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_pulse=sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        registerSensors();


    }

    //UPDATE VIEW IN FRAGMENT
    private void updatePulseDisplay(String value)
    {
        try {
            fragmentHome.setPulseValue(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-------------------ON CLICK HANDLER--------------------
    /* theses methodes will handle the activity and fragment on click*/

    //get back the value of the glucose directly in home view
    public void click_sendGlucoseLevel(View view )
    {
        //get the value from the editText
        EditText editText=(EditText) findViewById(R.id.edit_text_add_glucose);
        String value=editText.getText().toString();
        //change the current fragment
        displayFragment_home(value);
        //process event by magpie
        processPulseEvent(value);
    }

    //event for the button voice
    public void click_voiceRecongnition(View view)
    {
        displaySpeechRecognizer();

    }




    //------------VOICE RECOGNITION HANDLER------------------



    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    //handle the voice result
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0).toLowerCase();

            //action based on the r

            //get the texte

            //create the possiblity and handle multi language
            String glucose=getString(R.string.voice_glucose);

            if (spokenText.equals(glucose))
            {
                displayFragment_addGlucose();
            }


            else
            {
                Toast.makeText(this, getString(R.string.voice_not_found), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //display the different fragment

    private void displayFragment_addGlucose()
    {
        this.manager= getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

            fragmentAddGlucose=new FragmentAddGlucose();
        transaction.replace(R.id.main_container, fragmentAddGlucose); // newInstance() is a static factory method.
        transaction.commitAllowingStateLoss();
    }

    private void displayFragment_home(String value)
    {
        fragmentHome=new  FragmentHome();
    Bundle bundle=new Bundle();
        bundle.putString("glucoseValue",value);
        fragmentHome.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentHome,"homeTag").addToBackStack(null).commit(); // newInstance() is a static factory method.

        Log.d("stateofMessage","display fragment");
       // fragmentHome.setGlucoseValue(value);
    }


    //-----------MAGPIE METHODE-------------
    @Override
    public void onEnvironmentConnected() {

    }

    @Override
    public void onAlertProduced(LogicTupleEvent alert) {

    }

    //PROCESS EVENT WITH MAGPIE
    public void processGlucose(String value)
    {

    }
    public void processPulseEvent(String value)
    {

    }

    //------------SENSORS METHODE--------------

    private void registerSensors()
    {
        sensorManager.registerListener(this,sensor_pulse,SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void unregisterSensors()
    {
        sensorManager.unregisterListener(this,sensor_pulse);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.getType())
        {   //handle heart event
            case Sensor.TYPE_HEART_RATE :
                String value=sensorEvent.values[0]+"";
                updatePulseDisplay(value);
                processPulseEvent(value);
                break;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //No call for super(). Bug on API Level > 11.
//    }
}
