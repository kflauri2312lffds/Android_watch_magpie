package hevs.aislab.magpie.watch;

import android.app.DialogFragment;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.android.MagpieActivityWatch;
import ch.hevs.aislab.magpie.behavior.PriorityBehaviorAgentMind;
import ch.hevs.aislab.magpie.environment.Services;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.support.Rule;
import hevs.aislab.magpie.watch.agents.PulseBehaviour;
import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.gui.DialogFragmentSetGlucose;
import hevs.aislab.magpie.watch.gui.FragmentAddGlucose;
import hevs.aislab.magpie.watch.gui.FragmentHome;
import hevs.aislab.magpie.watch.gui.FragmentSettings;
import hevs.aislab.magpie.watch.libs.Lib;
import hevs.aislab.magpie.watch.models.DaoSession;
import hevs.aislab.magpie.watch.models.Rules;
import hevs.aislab.magpie.watch.models.RulesDao;
import hevs.aislab.magpie.watch.repository.RulesRepository;

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

    //store the current values

    //set the current rules, we will search in the db


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

        //Db
      //  Core.getInstance().setDaoSession((Core.getInstance().getDaoMaster().newSession()));
    }


  //    BUTTON MENU EVENT
    public void click_voiceRecongnition(View view)
    {

        //fragmentHome.setSeverity("sever",3);

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
    public void click_alert(View view)
    {
        //TODO : DISPLAY THE ALERT FRAGMENT
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
        //get the timestamp
        long timestamp=System.currentTimeMillis();
        processEvent(value,"glucose",timestamp);
    }

    //BUTTON to set value manually
    public void click_setGlucose(View view)
    {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragmentSetGlucose myDialogFragment = new DialogFragmentSetGlucose();
        myDialogFragment.show(fm,"tag");

    }
    public void click_setWeight(View view)
    {

    }
    public void click_setPressure(View view)
    {

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

        //Java rules
        MagpieAgent behaviorAgent = new MagpieAgent("priority_agent", Services.LOGIC_TUPLE);
        PriorityBehaviorAgentMind behaviorMind = new PriorityBehaviorAgentMind();
        behaviorMind.addBehavior(new PulseBehaviour(this, behaviorAgent, 1));

        behaviorAgent.setMind(behaviorMind);
        registerAgent(behaviorAgent);

    }

    @Override
    public void onAlertProduced(LogicTupleEvent alert) {

    }

    //PROCESS EVENT WITH MAGPIE

    public void processEvent(String value, String type, long timeStamp)
    {
        LogicTupleEvent lte = new LogicTupleEvent(type, value);
        lte.setTimestamp(timeStamp);
        sendEvent(lte);
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
//                if (Lib.isPhonePluggedIn(this))
//                    return;

                String value=((int)sensorEvent.values[0])+"";
                updatePulseDisplay(value);
                long timeStamp=System.currentTimeMillis();
                processEvent(value,"pulse",timeStamp);

                break;
        }
    }

    public FragmentHome getFragmentHome()
    {
        return this.fragmentHome;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
//        Toast.makeText(this, i+"", Toast.LENGTH_SHORT).show();

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
