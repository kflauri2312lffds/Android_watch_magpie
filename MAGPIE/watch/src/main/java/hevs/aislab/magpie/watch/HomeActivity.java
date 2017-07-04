package hevs.aislab.magpie.watch;

import android.hardware.SensorManager;
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
import hevs.aislab.magpie.watch.gui.FragmentDisplayAlertes;
import hevs.aislab.magpie.watch.gui.IdialogToActivity;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetPressure;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetValue;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetGlucose;
import hevs.aislab.magpie.watch.gui.FragmentHome;
import hevs.aislab.magpie.watch.gui.FragmentSettings;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetWeight;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.libs.DateFormater;
import hevs.aislab.magpie.watch.libs.Validator;
import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.shared_pref.PrefAccessor;
import hevs.aislab.magpie.watch.threads.IhomeActivity;
import hevs.aislab.magpie.watch.threads.SensorsThreadLifecircle;




/**
 * Created by teuft on 31.05.2017.
 * this class will handle all other fragment related the the apps
 */

//implement the listener for the sensors
public class HomeActivity extends MagpieActivityWatch implements SensorEventListener,IdialogToActivity, IhomeActivity {



    //handle the voice variable
    private static final int SPEECH_REQUEST_CODE = 0;


    //fragment
    private FragmentHome fragmentHome;
    private FragmentSettings fragmentSettings;
    private FragmentDisplayAlertes fragmentDisplayAlertes;

    //sensors
    private SensorManager sensorManager;
    private Sensor sensor_pulse;
    private Sensor sensor_step;


    private Thread threadPulse;

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


            Log.d("TreadInformation_","THREAD IS INITIALIZED AND LAUNCHED");
            threadPulse=new SensorsThreadLifecircle(this,sensor_pulse,30000,30000);
            threadPulse.start();



        displayFragmentHome();
    }

    private void initFragment()
    {
        fragmentSettings=new FragmentSettings();
        fragmentHome=new FragmentHome();
        fragmentDisplayAlertes=new FragmentDisplayAlertes();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentSettings,"settingsFrag").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentHome,"homeFrag").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentDisplayAlertes,"display_alertTag").commit();
    }


  //    BUTTON MENU EVENT
    public void click_voiceRecongnition(View view)
    {
         displaySpeechRecognizer();
    }

    public void click_home(View view )
    {
        displayFragmentHome();
    }
    public void click_settings(View view )
    {
        displayFragmentSettings();
    }

    //UPDATE VIEW IN FRAGMENT
    public void click_alert(View view)
    {
        displayFragmentAlert();
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
        //check the entry to see is valide. if not, we don't send value to magpie

                //send to magpie if it's valide
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
        CustomToast.getInstance().warningToast(getString(R.string.information_pulse_device),this);
        //TODO TESTE IMPLEMENTATION ! REMOVE THIS METHODE
        processEvent(System.currentTimeMillis(),Const.CATEGORY_PULSE,"100");
    }
    public void click_setSteps(View view)
    {
        CustomToast.getInstance().warningToast(getString(R.string.information_step_device),this);
        addStep(1000);
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
            CustomToast.getInstance().warningToast(getString(R.string.voice_incomplet_number), this);
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
            CustomToast.getInstance().errorTOast(getString(R.string.voice_incomplet_number), this);
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
            CustomToast.getInstance().errorTOast(getString(R.string.voice_incorrect), this);
        }
        CustomToast.getInstance().errorTOast(getString(R.string.voice_not_found), this);
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
            CustomToast.getInstance().errorTOast(getString(R.string.voice_incorrect), this);

        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
            CustomToast.getInstance().errorTOast("unespected error", this);
        }
    }

    private void displayFragmentHome()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentSettings).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentDisplayAlertes).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentHome).commit();
    }
    private void displayFragmentSettings()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentDisplayAlertes).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentHome).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentSettings).commit(); // newInstance() is a static factory method.
    }
    private void displayFragmentAlert()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentSettings).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentHome).commit();
        getSupportFragmentManager().beginTransaction().remove(fragmentDisplayAlertes).commit();
        //create a new fragment each time to be sure to have the last values
        fragmentDisplayAlertes=new FragmentDisplayAlertes();

        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentDisplayAlertes,"display_alertTag").commit();
        getSupportFragmentManager().beginTransaction().show(fragmentDisplayAlertes).commit();
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

    public void processEvent(long timeStamp, String category, String ... value )
    {
        LogicTupleEvent lte = new LogicTupleEvent(category, value);

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
                //for this sensor, we juste update the value. the value is processed only once a week

                addStep(1);
                break;

        }
    }

    private void addStep(long newStep) {
        //retrive the current step
        long currentStep= PrefAccessor.getInstance().getLong(this, "steps_counter");
        //check if it's a new day. If it is, we store the actual data in the data base
        String today=DateFormater.getInstance().getDate(System.currentTimeMillis());
        if (!today.equals(PrefAccessor.getInstance().getString(this,Const.KEY_DATE_STEP)))
        {
            //write the new date in shared pref
            PrefAccessor.getInstance().save(this,Const.KEY_DATE_STEP,today);
            //send event to magpie
            processEvent(System.currentTimeMillis(),Const.CATEGORY_STEP,currentStep+"");
            //reset the number of step
            currentStep=0;
        }
        //get the current step. 0 if null.
        currentStep+=newStep;
        PrefAccessor.getInstance().save(this,Const.KEY_CURRENT_STEP,currentStep);
        //change the display of the step
        fragmentHome.setStepValue((double)currentStep);
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    /**
     * used to destroy the current thread running (thread that manage pulse
     */
    @Override
    protected void onDestroy() {

        Log.d("DestroyHasBennCalled","blablalbalblablablablabl");
        //close the current thread for the pulse
        //interrupt and stop the current running thread
        ((SensorsThreadLifecircle)threadPulse).cancel();
        threadPulse.interrupt();
        super.onDestroy();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //No call for super(). Bug on API Level > 11.
//    }



}
