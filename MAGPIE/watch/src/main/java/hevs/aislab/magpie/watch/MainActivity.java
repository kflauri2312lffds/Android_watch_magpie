package hevs.aislab.magpie.watch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.magpie.support.Rule;
import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch.shared_pref.PrefAccessor;

/**
 * This class will be used mainly to init the db and ask for the permission
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the data base
        initDB();
        askAllPermission();

        //test if it's the first time we open the apps
        boolean hasBeenInit=PrefAccessor.getInstance().getBoolean(this,"first");

       if (!hasBeenInit)
       {
            createFirstRules();
           // createFirstRules();
            PrefAccessor.getInstance().save(this,"first",true);

       }

        displayFirstData();


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
    //methode used to create the first rules in our DB
    public void createFirstRules()
    {
        CustomRules rule =new CustomRules();
        rule.setCategory("cat1");
        rule.setVal_1_max((double)2);


            RulesRepository.getInstance().insert(rule);



        CustomRules myRules=RulesRepository.getInstance().getById(1);
        Log.d("Affichage_BD rules",myRules.getCategory());

        Measure measure=new Measure();
        measure.setCategory("cateMeasure");
        measure.setTimeStamp(System.currentTimeMillis());
        measure.setValue((double)33);

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
        CustomRules myRules=RulesRepository.getInstance().getById(1);
        Log.d("Affichage_BD rules",myRules.getCategory());

        Measure myMeasure=MeasuresRepository.getInstance().getById(1);
        Log.d("Affichage_BD measure",myMeasure.getCategory());

        Alertes myAlert=AlertRepository.getINSTANCE().getByIdWithRelations(1);
        Log.d("Affichage_BD alert",myAlert.getMeasure().getValue()+"");
    }
}
