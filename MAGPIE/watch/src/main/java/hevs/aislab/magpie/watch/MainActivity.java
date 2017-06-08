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
import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch.models.DaoSession;
import hevs.aislab.magpie.watch.models.Rules;
import hevs.aislab.magpie.watch.models.RulesDao;
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
            PrefAccessor.getInstance().save(this,"first",true);

        }



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

        ArrayList<Rules>rulesList=new ArrayList<Rules>();
        //Create each rule
        Rules rule1 = new Rules();
        rule1.setCategory("pulse");
        rule1.setDescription("very low");
        rule1.setMinValue(0);
        rule1.setMaxValue(30);

        rule1.setSeverity(3);

        Rules rule2 = new Rules();
        rule2.setCategory("pulse");
        rule2.setDescription("low");
        rule2.setMinValue(30);
        rule2.setMaxValue(50);
        rule2.setSeverity(0);

        Rules rule3 = new Rules();
        rule3.setCategory("pulse");
        rule3.setDescription("normal");
        rule3.setMinValue(50);
        rule3.setMaxValue(90);
        rule3.setSeverity(0);

        Rules rule4 = new Rules();
        rule4.setCategory("pulse");
        rule4.setDescription("high");
        rule4.setMinValue(90);
        rule4.setMaxValue(140);
        rule4.setSeverity(0);

        Rules rule5 = new Rules();
        rule5.setCategory("pulse");
        rule5.setDescription("Very high");
        rule5.setMinValue(140);
        rule5.setMaxValue(160);
        rule5.setSeverity(1);

        Rules rule6 = new Rules();
        rule6.setCategory("pulse");
        rule6.setDescription("too much! ");
        rule6.setMinValue(160);
        rule6.setMaxValue(220);
        rule6.setSeverity(3);


        rulesList.add(rule1);
        rulesList.add(rule2);
        rulesList.add(rule3);
        rulesList.add(rule4);
        rulesList.add(rule5);
        rulesList.add(rule6);

        for (Rules aRule: rulesList)
        {
            RulesRepository.getInstance().insert(aRule);
        }
    }



}
