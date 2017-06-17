package hevs.aislab.magpie.watch;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.android.MagpieActivityWatch;
import ch.hevs.aislab.magpie.behavior.PriorityBehaviorAgentMind;
import ch.hevs.aislab.magpie.environment.Services;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import hevs.aislab.magpie.watch.agents.GlucoseBehaviour;
import hevs.aislab.magpie.watch.agents.PressureBehaviour;
import hevs.aislab.magpie.watch.agents.PulseBehaviour;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetPressure;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetValue;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetGlucose;
import hevs.aislab.magpie.watch.gui.FragmentHome;
import hevs.aislab.magpie.watch.gui.FragmentSettings;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.libs.Lib;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;

/**
 * Created by teuft on 31.05.2017.
 * this class will handle all other fragment related the the apps
 */

//implement the listener for the sensors
public class HomeActivity extends MagpieActivityWatch implements SensorEventListener,DialogFragmentSetValue.IdialogToActivity {



    //handle the voice variable
    private static final int SPEECH_REQUEST_CODE = 0;
    FragmentManager manager;

    //fragment
    private FragmentHome fragmentHome;
    private FragmentSettings fragmentSettings;

    //sensors
    private SensorManager sensorManager;
    private Sensor sensor_pulse;
    private Sensor sensor_step;


    //CURRENT VALUES (USED TO POPULATE THE DISPLAY when we change fragment)
    private String currentValue_pulse;
    private String currentValue_step;
    private String currentValue_glucose;
    private String currentValue_systol;
    private String currentValue_diastol;
    private String currentValue_weight;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //display the fragment home witout any value
        displayFragmentHome("");

        //init the sensors
        this.sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_pulse=sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensor_step=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

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

    public void click_alert(View view)
    {
        //TODO : DISPLAY THE ALERT FRAGMENT
    }

    //------------------METHODE TO PROCESS VALUE FROM THE DIALOG FRAGMENT---------------------


    /**
     *
     * Used by the dialog fragment to communicate with the activity.
     * @param category
     * @param value
     */
    @Override
    public void sendValue(String category, String ... value)
    {
                //we send the values directly to magpie methode
                processEvent(System.currentTimeMillis(),category,value);
    }


    //-------------------DIALOG FRAGMENT --------------------

    /**
     *
     * Open the dialog fragment glucose. Then, user will be able to add new glucose value.
     * @param view
     */
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
        FragmentManager fm = getSupportFragmentManager();
        DialogFragmentSetPressure myDialogFragment = new DialogFragmentSetPressure();
        myDialogFragment.show(fm,"tag");

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
            handleVoiceEvent(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //DEFINE THE ACTION IN RESPONSE OF VOICE EVENT

    private void handleVoiceEvent(Intent data) {
        List<String> results = data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS);
        String spokenText = results.get(0).toLowerCase();
        //replace eventual comma by "."
        spokenText = spokenText.replace(",", ".");
        String glucose = getString(R.string.voice_glucose);
        //split the texte spoken into an array. The first value will be category, the second the value
        String[] arraySpocken = spokenText.split(" ");

        //ad the glucose by voice
        if (arraySpocken[0].toLowerCase().equals(glucose))
        {
            if (arraySpocken.length >= 1)
            {
                voiceAction_addValue(Const.CATEGORY_GLUCOSE, arraySpocken[1]);
                return;
            }
            // no number has been specified, so command is not complet
            Toast.makeText(this, getString(R.string.voice_incomplet_number), Toast.LENGTH_SHORT).show();
            return;
        }

        String pressure=getString(R.string.voice_pressure);
        if (arraySpocken[0].toLowerCase().equals(pressure))
        {
            if (arraySpocken.length >= 3)
            {
                voiceAction_addValue( Const.CATEGORY_PRESSURE, arraySpocken[1],arraySpocken[3]);
                return;
            }
            // no number has been specified, so command is not complet
            Toast.makeText(this, getString(R.string.voice_incomplet_number), Toast.LENGTH_SHORT).show();
            return;
        }
            Toast.makeText(this, getString(R.string.voice_not_found), Toast.LENGTH_SHORT).show();
    }

    private void voiceAction_addValue( String category, String ... rawvalue) {
        //we set the value of the glucose
        try
        {
            //get the number. handle eventual null value or not number value, or the fact that the framgent is not fully charged
            double value[]=new double[rawvalue.length];
            //try to cast all value into double. if it fails, we return passe in the exception
            for (int k=0;k<value.length;k++)
            {
                value[k]=Double.parseDouble(rawvalue[k]);
            }
            processEvent(System.currentTimeMillis(), category,rawvalue);
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
            Toast.makeText(this, getString(R.string.voice_incorrect), Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
            Toast.makeText(this, "Error with the value inserted", Toast.LENGTH_SHORT).show();
        }
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
        behaviorMind.addBehavior(new GlucoseBehaviour(this,behaviorAgent,1));
        behaviorMind.addBehavior(new PressureBehaviour(this,behaviorAgent,1));


        behaviorAgent.setMind(behaviorMind);
        registerAgent(behaviorAgent);

    }

    @Override
    public void onAlertProduced(LogicTupleEvent alert) {

    }

    //PROCESS EVENT WITH MAGPIE

    public void processEvent(long timeStamp, String type, String ... value )
    {
        LogicTupleEvent lte = new LogicTupleEvent(type, value);

        lte.setTimestamp(timeStamp);
        sendEvent(lte);
    }


    //------------SENSORS METHODE--------------

    private void registerSensors()
    {
        sensorManager.registerListener(this,sensor_pulse,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensor_step,SensorManager.SENSOR_DELAY_NORMAL);

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

                //Get the value and send it to magpie
                String value=((int)sensorEvent.values[0])+"";
                long timeStamp=System.currentTimeMillis();
                try
                {
                    processEvent(timeStamp,"pulse",value);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
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


    //***************************************TESTE METHODE***************************
    public void testShowAllMeasures()
    {
        Log.d("CallIN ","Activity");
    }

}
