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
import hevs.aislab.magpie.watch.gui.FragmentSettings;
import hevs.aislab.magpie.watch.libs.Lib;

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
    private FragmentHome fragmentHome;
    private FragmentAddGlucose fragmentAddGlucose;
    private FragmentSettings fragmentSettings;

    //sensors
    private SensorManager sensorManager;
    private Sensor sensor_pulse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //display the fragment home witout any value
        displayFragmentHome("");

        //init the sensors
        this.sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_pulse=sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        registerSensors();
    }


  //    BUTTON MENU EVENT
    public void click_voiceRecongnition(View view)
    {
        displaySpeechRecognizer();

    }

    public void click_home(View view )
    {

        displayFragmentHome("");

    }
    public void click_settings(View view )
    {
        displayFragmentSettings();
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
        displayFragmentHome(value);
        //process event by magpie
        processPulseEvent(value);
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

            //replace eventual comma by "."
           spokenText= spokenText.replace(",",".");


            //create the possiblity and handle multi language
            String glucose=getString(R.string.voice_glucose);
            //split the texte spoken into an array
            String []arraySpocken=spokenText.split(" ");

            //ad the glucose by voice
            if (arraySpocken[0].toLowerCase().equals(glucose))
            {
                //we set the value of the glucose
                try
                {
                    //get the number. handle eventual null value or not number value, or the fact that the framgent is not fully charged
                    double value=Double.parseDouble(arraySpocken[1]);
                    fragmentHome.setGlucoseValue(value+"");

                }
                catch (Exception ex)
                {
                    Toast.makeText(this, getString(R.string.voice_incorrect), Toast.LENGTH_SHORT).show();
                }
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

    private void displayFragmentHome(String value)
    {
        fragmentHome=new  FragmentHome();
        Bundle bundle=new Bundle();
        bundle.putString("glucoseValue",value);
        fragmentHome.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentHome,"homeTag").addToBackStack(null).commit(); // newInstance() is a static factory method.

        Log.d("stateofMessage","display fragment");
       // fragmentHome.setGlucoseValue(value);
    }
    private void displayFragmentSettings()
    {
        fragmentSettings=new FragmentSettings();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentSettings,"settingsTag").addToBackStack(null).commit(); // newInstance() is a static factory method.
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
                //return if the watch is charging ==bad balue
                if (Lib.isPhonePluggedIn(this))
                    return;

                String value=sensorEvent.values[0]+"";
                updatePulseDisplay(value);
                processPulseEvent(value);

                break;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Toast.makeText(this, i+"", Toast.LENGTH_SHORT).show();

        if (sensor.getType()==Sensor.TYPE_HEART_RATE)
        {
           if (i== SensorManager.SENSOR_STATUS_NO_CONTACT)
           {
//               Toast.makeText(this, "No contact with wath", Toast.LENGTH_SHORT).show();
               return;
           }
            if (i== SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
            {
//                Toast.makeText(this, "good contact with watch", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //No call for super(). Bug on API Level > 11.
//    }
}
