package hevs.aislab.magpie.watch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.dummy.DummyValue;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentConfirmDelete;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.phone_communication.PushMeasureThread;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch.shared_pref.PrefAccessor;
import hevs.aislab.magpie.watch_library.lib.Const;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;


/**
 * This class will essentially be used to init the different component of our application.
 * it include: init the database
 * ask permission, and if the user don't allow, he won't be able to continue
 * insert the first rules in the databse, if it's the first time the user launch app
 * detect if the device has can create song (for notification)
 * user 
 */
public class MainActivity extends FragmentActivity implements
        // USED TO COMMUNICATE WITH THE PHONE
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{


    // Used to communicate with the phone
    GoogleApiClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Used to display the logs in GreeenDao, FOR DEBUG ONLY, REMOVE THIS IN PRODUCTION.
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        //initialize the data base
        initDB();
        //Ask all permission for the functionalites
        askAllPermission();

        //test if it's the first time we open the apps
        boolean hasBeenInit=PrefAccessor.getInstance().getBoolean(this,"first");

       if (!hasBeenInit)
       {
           PrefAccessor.getInstance().save(this,"first",true);
           PrefAccessor.getInstance().save(this,"hasSpeacker",hasSpeacker());
           insertFirstRules();
       }

        // Build a new GoogleApiClient for the the Wearable API, used for the communication with the phone
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



    }
    // if user has set the permission, we change the activity
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void askAllPermission()
    {
        //ask for all the permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BODY_SENSORS},
                3);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BATTERY_STATS},
                2);
    }

    private boolean hasUserPermission()
    {
        //get the result of permission
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.BODY_SENSORS);

        //check permission. if we have the permission, we continue, otherwise, we reload the activity

       return permissionCheck== PackageManager.PERMISSION_GRANTED;

    }


    private void initDB() {
        //init the session
        Core.getInstance().setHelper(new DaoMaster.DevOpenHelper(this,"Prototype-db",null));
        Core.getInstance().setDb(Core.getInstance().getHelper().getWritableDatabase());
        Core.getInstance().setDaoMaster(new DaoMaster(Core.getInstance().getDb()));
        Core.getInstance().setDaoSession( (Core.getInstance().getDaoMaster().newSession()));

    }

    public void startHomeActivity(View view)
    {

        //disallow the access if the user has not given the autorisation
        //ask for all the permission

        if (this.hasUserPermission())
        {
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
        else
        {
            askAllPermission();
        }
    }

    /**
     * This method will be called when the user click on the button "push and delete data"
     * it will delete the data from the watch and sent it directly to the phone
     */
    public void click_syncData(View view)
    {
        //create the object we will send to the phone
        sendDataToPhone();

        //toast to inform the user
        CustomToast.getInstance().confirmToast(getString(R.string.confirm_syncdata),this);
    }

    public void click_insertDummyData(View view )
    {
        DummyValue dummy=new DummyValue();
        dummy.insertDummyMeasure();
        CustomToast.getInstance().confirmToast("dummy data added",this);


      List<Measure>measureList=  MeasuresRepository.getInstance().getAll();

        for (Measure aMeasure : measureList)
        {
            Log.d("Display_ID",aMeasure.getId()+"");
        }


    }

    public void click_deleteData(View view)
    {
       //open confirm dialog to confirm action
        android.support.v4.app.DialogFragment fragmentConfirm=new DialogFragmentConfirmDelete();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentConfirm.show(fragmentTransaction,"tag");

        //check if the id are correct

        List<Measure>measureList=MeasuresRepository.getInstance().getAll();

        for (Measure aMeasure : measureList)
        {
            Log.d("measure_id_wesh",aMeasure.getId()+"");
        }
    }

    private void sendDataToPhone()
    {
        new PushMeasureThread(googleClient).start();
    }


    private void insertFirstRules()
    {
        //GLUCOSE RULES
        CustomRules glucoseRules=new CustomRules();
        glucoseRules.setCategory(Const.CATEGORY_GLUCOSE);
        //6 HOURS
        long glucoseTimeWindow= 1000*60*60*6;
        glucoseRules.setTimeWindow(glucoseTimeWindow);
        glucoseRules.setConstraint_1("oldGlucose<=Value_1Min");
        glucoseRules.setConstraint_2("currentGlucose>=Value_2Max");
        glucoseRules.setConstraint_3("Tev2>Tev1");
        //SET THE VALUES
        glucoseRules.setVal_1_min(3.8);
        glucoseRules.setVal_2_max(8.0);

        RulesRepository.getInstance().insert(glucoseRules);

        //PRESSEURE RULES
        CustomRules pressureRules=new CustomRules();
        pressureRules.setCategory(Const.CATEGORY_PRESSURE);

        //1 WEEK
        long pressuresTimeWindow=1000*60*60*24*7;

        pressureRules.setTimeWindow(pressuresTimeWindow);
        pressureRules.setVal_1_max(130.0);
        pressureRules.setVal_2_max(80.0);
        pressureRules.setConstraint_1("Sys>=Value_1Max");
        pressureRules.setConstraint_2("Dias>=Value_2Max");
        RulesRepository.getInstance().insert(pressureRules);


          // WEIGHT RULES

        long weightTimeWindows=1000*60*60*24*7;

        CustomRules weightRules=new CustomRules();
        weightRules.setTimeWindow(weightTimeWindows);
        weightRules.setConstraint_1("Current_weight>="+Const.VALUE_Value_2Max+"*old_weight");
        weightRules.setConstraint_2("OR Current_weight<="+Const.VALUE_Value_2Min+"%*old_weight");

        // max % of weight loss allowed
        weightRules.setVal__2_min(98.0);
        // max % of weight gain allowed
        weightRules.setVal_2_max(101.0);
        weightRules.setCategory(Const.CATEGORY_WEIGHT);
        RulesRepository.getInstance().insert(weightRules);

        //SIMPLE RULES
        /*the following rules are simple rules. They don't contains any timestamp check and are simple teste if the values if in a certain range
         * user will be able to change the rules. For thoses rules, we will use only the first value (value1_min and value1_max */

        //PULSE RULES
        CustomRules pulseRules=new CustomRules();
        pulseRules.setVal_1_min(30.0);
        pulseRules.setVal_1_max(150.0);
        pulseRules.setConstraint_1("pulse<Value_1Min");
        pulseRules.setConstraint_2("pulse>Value_1Max");
        pulseRules.setCategory(Const.CATEGORY_PULSE);

        RulesRepository.getInstance().insert(pulseRules);


        //STEP RULES
        /*the steps rules will be applied in a day and not at every steps*/

        CustomRules stepRules=new CustomRules();
        stepRules.setCategory(Const.CATEGORY_STEP);
        stepRules.setVal_1_min(4000.0);
        stepRules.setVal_1_max(8000.00);

        stepRules.setConstraint_1("steps<Value_1Min");
        stepRules.setConstraint_2("steps>Value_1Max");

        RulesRepository.getInstance().insert(stepRules);




    }
    //methode used to create the first rules in our DB


    public boolean hasSpeacker()
    {
        PackageManager packageManager = this.getPackageManager();
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

// Check whether the device has a speaker.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check FEATURE_AUDIO_OUTPUT to guard against false positives.
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
                return false;
            }

            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                    return true;
                }
            }
        }
        return false;
    }




    /**
     * Phone communication methode
     * @param bundle
     */

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();
    }


    // Disconnect from the data layer when the Activity stops
    @Override
    protected void onStop() {
        if (null != googleClient && googleClient.isConnected()) {
            googleClient.disconnect();
        }
        super.onStop();
    }





}
