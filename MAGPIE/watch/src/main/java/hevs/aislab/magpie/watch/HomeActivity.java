package hevs.aislab.magpie.watch;

import android.app.PendingIntent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.android.MagpieActivityWatch;
import ch.hevs.aislab.magpie.behavior.PriorityBehaviorAgentMind;
import ch.hevs.aislab.magpie.environment.Services;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import hevs.aislab.magpie.watch.agents.GlucoseBehaviour;
import hevs.aislab.magpie.watch.agents.PressureBehaviour;
import hevs.aislab.magpie.watch.agents.PulseBehaviour;
import hevs.aislab.magpie.watch.agents.StepBehaviour;
import hevs.aislab.magpie.watch.agents.WeightBehaviour;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetPressure;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetValue;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetGlucose;
import hevs.aislab.magpie.watch.gui.FragmentHome;
import hevs.aislab.magpie.watch.gui.FragmentSettings;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetWeight;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.threads.IhomeActivity;
import hevs.aislab.magpie.watch.threads.SensorsThreadLifecircle;




/**
 * Created by teuft on 31.05.2017.
 * this class will handle all other fragment related the the apps
 */

//implement the listener for the sensors
public class HomeActivity extends MagpieActivityWatch implements SensorEventListener,DialogFragmentSetValue.IdialogToActivity, IhomeActivity {



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

    //List where we will store value in the pulse to make the average
    private ArrayList<Double>listPulse=new ArrayList<Double>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initFragment();
        //display the fragment home witout any value


        //init the sensors
        this.sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_pulse=sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensor_step=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //register the sensors
        sensorManager.registerListener(this,sensor_step,SensorManager.SENSOR_DELAY_NORMAL);
        //activate a thread for the pulse sensors
        Thread threadSensorsPulse=new Thread(new SensorsThreadLifecircle(this,sensor_pulse,30000,30000));
        threadSensorsPulse.start();

        displayFragmentHome("");
    }

    private void initFragment()
    {
        fragmentSettings=new FragmentSettings();
        fragmentHome=new FragmentHome();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentSettings,"settingsFrag").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentHome,"homeFrag").commit();
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
        FragmentManager fm = getSupportFragmentManager();
        DialogFragmentSetWeight myDialogFragment = new DialogFragmentSetWeight();
        myDialogFragment.show(fm,"tag");
    }
    public void click_setPressure(View view)
    {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragmentSetPressure myDialogFragment = new DialogFragmentSetPressure();
        myDialogFragment.show(fm,"tag");

    }
    //only inform the user that the measure are taken automaticaly
    public void click_setPulse(View view)
    {
        Toast.makeText(this, getString(R.string.information_pulse_device), Toast.LENGTH_SHORT).show();
    }

    public void click_setSteps(View view)
    {
        Toast.makeText(this, getString(R.string.information_step_device), Toast.LENGTH_SHORT).show();
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
        //get the spocken language in a string
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
        String weight=getString(R.string.voice_weight);

        if (arraySpocken[0].toLowerCase().equals(weight))
        {
            if (arraySpocken.length>=2)
            {
                voiceAction_addValue(Const.CATEGORY_WEIGHT,arraySpocken[1]);
                return;
            }
            Toast.makeText(this, getString(R.string.voice_incorrect), Toast.LENGTH_SHORT).show();
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
        getSupportFragmentManager().beginTransaction().hide(fragmentSettings).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentHome).commit();
    }
    private void displayFragmentSettings()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentHome).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentSettings).commit(); // newInstance() is a static factory method.
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
        behaviorMind.addBehavior(new StepBehaviour(this,behaviorAgent,1));
        behaviorMind.addBehavior(new WeightBehaviour(this,behaviorAgent,1));

        //TODO ADD THE WEIGHT



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

    //this methode will be called by the thread. we will send the pulse to magpie
    @Override
    public void processPulse()
    {
        if (listPulse.size()==0)
            return;

        double average=0;
        //make the average of the list
        for(Double aPulse : listPulse)
        {
            average+=aPulse;
        }
        average/=listPulse.size();
        Log.d("averageOfPulse:",average+"");
        processEvent(System.currentTimeMillis(),Const.CATEGORY_PULSE,average+"");
        listPulse.clear();
    }

    //-------------SENSORS METHODE---------------------------
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.getType())
        {   //handle heart event
            case Sensor.TYPE_HEART_RATE :
                double value=sensorEvent.values[0];
                listPulse.add(value);
                break;
            case Sensor.TYPE_STEP_COUNTER :
                processEvent(System.currentTimeMillis(),Const.CATEGORY_STEP,sensorEvent.values[0]+"");
                break;

        }
    }

    //***************************************HANDLE THE LIFE CIRCLE OF SENSORS***************************
    @Override
    public void registerSensor(Sensor sensor, int sensorsType) {
        sensorManager.registerListener(this,sensor,sensorsType);
    }

    @Override
    public void unregisterSensors(Sensor sensor) {
        sensorManager.unregisterListener(this,sensor);

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
