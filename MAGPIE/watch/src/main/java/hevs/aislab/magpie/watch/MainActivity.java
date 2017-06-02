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

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.DaoMaster;

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

        //ask for all the permission
       ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BODY_SENSORS},
                3);





    }

    // if user has set the permission, we change the activity
    @Override
    protected void onResume() {
        super.onResume();


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
        Core.getInstance().setDaoSession(Core.getInstance().getDaoMaster().newSession());
    }




//    @Override
//    public void onEnterAmbient(Bundle ambientDetails) {
//        super.onEnterAmbient(ambientDetails);
//        updateDisplay();
//    }
//
//    @Override
//    public void onUpdateAmbient() {
//        super.onUpdateAmbient();
//        updateDisplay();
//    }
//
//    @Override
//    public void onExitAmbient() {
//        updateDisplay();
//        super.onExitAmbient();
//    }
//
//    private void updateDisplay() {
//        if (isAmbient()) {
//
//
//            return;
//        }

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
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BODY_SENSORS},
                    3);
        }


    }




}
