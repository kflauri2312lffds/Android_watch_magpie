package hevs.aislab.magpie.watch;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import ch.hevs.aislab.magpie.support.Rule;
import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch.shared_pref.PrefAccessor;


import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;


/**
 * This class will essentially be used to init the different component of our application.
 * it include: init the database
 * ask permission, and if the user don't allow, he won't be able to continue
 * insert the first rules in the databse, if it's the first time the user launch app
 * detect if the device has can create song (for notification)
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        //initialize the data base
        initDB();
        askAllPermission();

        //test if it's the first time we open the apps
        boolean hasBeenInit=PrefAccessor.getInstance().getBoolean(this,"first");


       if (!hasBeenInit)
       {
           PrefAccessor.getInstance().save(this,"first",true);
           PrefAccessor.getInstance().save(this,"hasSpeacker",hasSpeacker());
           insertFirstRules();
       }
        testGetFirstCategory();
        Log.d("speackerInformation",this.hasSpeacker()+"");

        testDisplayAllMeasure();


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
    private void insertFirstRules()
    {
        //GLUCOSE RULES
        CustomRules glucoseRules=new CustomRules();
        glucoseRules.setCategory(Const.CATEGORY_GLUCOSE);
        //6 HOURS
        long timewindows= 1000*60*60*6;
        glucoseRules.setTimeWindow(timewindows);
        glucoseRules.setConstraint_1("Val1<=Val1Min");
        glucoseRules.setConstraint_2("Val2>=Val2Max");
        glucoseRules.setConstraint_3("Tev2>Tev1");
        //SET THE VALUES
        glucoseRules.setVal_1_min(3.8);
        glucoseRules.setVal_2_max(8.0);

        RulesRepository.getInstance().insert(glucoseRules);

        //PRESSEURE RULES
        CustomRules pressureRules=new CustomRules();
        pressureRules.setCategory(Const.CATEGORY_PRESSURE);

        //1 WEEK
        long timewindow2=1000*60*60*24*7;

        pressureRules.setTimeWindow(timewindow2);
        pressureRules.setVal_1_max(130.0);
        pressureRules.setVal_2_max(80.0);
        pressureRules.setConstraint_1("Sys>=130");
        pressureRules.setConstraint_2("Dias>=80");
        RulesRepository.getInstance().insert(pressureRules);


      //  Rule



    }
    //methode used to create the first rules in our DB
    public void testInsertDB()
    {
        CustomRules rule =new CustomRules();
        rule.setCategory("cat1");
        rule.setVal_1_max((double)2);


        RulesRepository.getInstance().insert(rule);

        CustomRules myRules=RulesRepository.getInstance().getById(1);
        Log.d("Affichage_BD rules",myRules.getCategory());

        Measure measure=new Measure();
        measure.setCategory(Const.CATEGORY_GLUCOSE);
        measure.setTimeStamp(System.currentTimeMillis());
        measure.setValue1((double)33);

        MeasuresRepository.getInstance().insert(measure);

        Measure myMeasure=MeasuresRepository.getInstance().getById(1);
        Log.d("Affichage_BD measure",myMeasure.getCategory());

        Alertes alertes=new Alertes();
        alertes.setRule(myRules);
        alertes.setMeasure(myMeasure);
        AlertRepository.getINSTANCE().insert(alertes);

        Alertes myAlert=AlertRepository.getINSTANCE().getByIdWithRelations(1);

        List<Alertes>listAlert=AlertRepository.getINSTANCE().getAll();
        Log.d("Affichage_BD arraysize",listAlert.get(0).getId()+"");
        Log.d("Affichage_BD alert",myAlert.getMeasure().getCategory());
    }
    private void displayFirstData()
    {

    }
    private void testBetween()
    {
        //create 2 values
        Measure measure=new Measure();
        measure.setTimeStamp(10);
        measure.setCategory("cate");
        measure.setValue1(1.0);


        Measure measure2=new Measure();
        measure2.setTimeStamp(20);
        measure2.setCategory("cate");
        measure2.setValue1(1.0);


        Measure measure3=new Measure();
        measure3.setTimeStamp(30);
        measure3.setCategory("cate");
        measure3.setValue1(1.0);


        Measure measure4=new Measure();
        measure4.setTimeStamp(40);
        measure4.setCategory("cate");
        measure4.setValue1(1.0);

        Measure measure5=new Measure();
        measure5.setTimeStamp(50);
        measure5.setCategory("cate");
        measure5.setValue1(1.0);

        Log.d("PassageDansMethode,","passageDansMethode");
        MeasuresRepository.getInstance().insert(measure);
        MeasuresRepository.getInstance().insert(measure2);
        MeasuresRepository.getInstance().insert(measure3);
        MeasuresRepository.getInstance().insert(measure4);
        MeasuresRepository.getInstance().insert(measure5);

        //affichage des resultats

        List<Measure>list= MeasuresRepository.getInstance().getByCategoryWhereTimeStampBetween("cate",1,31);
        Log.d("AffichageResult",list.size()+"");
    }

    public void testShowRelationRules()
    {
        CustomRules custRules=RulesRepository.getInstance().getByCategory(Const.CATEGORY_GLUCOSE);
        Log.d("AffInMAINE",custRules.getCategory());
    }
    public void testAlertes()
    {

        List<Alertes>alertesList=AlertRepository.getINSTANCE().getAllByCategory(Const.CATEGORY_GLUCOSE);
        Log.d("affichageAlertSize",alertesList.size()+"");


    }
    public void testBetweenALert()
    {
        //create 2 values
        Measure measure=new Measure();
        measure.setTimeStamp(10);
        measure.setCategory(Const.CATEGORY_GLUCOSE);
        measure.setValue1(1.0);


        Measure measure2=new Measure();
        measure2.setTimeStamp(20);
        measure2.setCategory(Const.CATEGORY_GLUCOSE);
        measure2.setValue1(1.0);


        Measure measure3=new Measure();
        measure3.setTimeStamp(30);
        measure3.setCategory(Const.CATEGORY_GLUCOSE);
        measure3.setValue1(1.0);


        Measure measure4=new Measure();
        measure4.setTimeStamp(40);
        measure4.setCategory(Const.CATEGORY_GLUCOSE);
        measure4.setValue1(1.0);


        MeasuresRepository.getInstance().insert(measure);
        MeasuresRepository.getInstance().insert(measure2);
        MeasuresRepository.getInstance().insert(measure3);
        MeasuresRepository.getInstance().insert(measure4);

        //create the rules
        CustomRules rule= RulesRepository.getInstance().getByCategory(Const.CATEGORY_GLUCOSE);

        Alertes alertes=new Alertes();
        alertes.setMeasure(measure);
        AlertRepository.getINSTANCE().insert(alertes);


        //now we teste the repository

        //First one
      List<Alertes> alertGood=  AlertRepository.getINSTANCE().getAllByCategoryBetweenTimeStamp(Const.CATEGORY_GLUCOSE,0,30);
        List<Alertes>alertBGad=AlertRepository.getINSTANCE().getAllByCategoryBetweenTimeStamp(Const.CATEGORY_GLUCOSE,100,430);

        Log.d("SHOWSITEOF_Correct",alertGood.size()+"");
        Log.d("SHOWSITEOF_notCorrect",alertBGad.size()+"");
    }

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

    private void testDisplayAllMeasure()
    {
        List<Measure>measureList=MeasuresRepository.getInstance().getAll();

        for (Measure aMeasure : measureList)
        {
            Log.d("ShowingMeasure_BEGIN","*********************************************");
            Log.d("ShowingMeasure_TIME",aMeasure.getTimeStamp()+"");
            Log.d("ShowingMeasure_VALUE",aMeasure.getValue1()+"");
            Log.d("ShowingMeasure_CATEGORY",aMeasure.getCategory()+"");
            Log.d("ShowingMeasure_END","*********************************************");
        }
    }

    private void testGetFirstCategory()
    {
        List<Measure>measureList=MeasuresRepository.getInstance().getLastMeasure();
        for (Measure aMeasure : measureList)
        {
            Log.d("DisplayMeaInfo_Begin","*******************************************");
            Log.d("DisplayMeaInfo_category",aMeasure.getCategory());
            Log.d("DisplayMeaInfo_TimeStam",aMeasure.getTimeStamp()+"");
            Log.d("DisplayMeaInfo_Value1",aMeasure.getValue1()+"");
            Log.d("DisplayMeaInfo_ENDOF","*******************************************");

        }
    }


}
