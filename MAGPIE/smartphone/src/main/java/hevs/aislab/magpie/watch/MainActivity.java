package hevs.aislab.magpie.watch;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.gui.dialogFragment.DialogFragmentConfirmDelete;
import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.AlertesRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.watch_communication.MessageReceiver;

/**
 * Activty used to ask permission and init the DB
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();

        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
        askAllPermission();
    }

    /**
     * Used to launch the program
     * @param view
     */
    public void click_display_home_activity(View view)
    {
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);
    }

    /**
     * Listener of the buton. Used to clear the database
     */

    public void click_clearDB(View view)
    {
        //open confirm dialog to confirm action
        android.support.v4.app.DialogFragment fragmentConfirm=new DialogFragmentConfirmDelete();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentConfirm.show(fragmentTransaction,"tag");
    }




    private void askAllPermission()
    {
        //ask for all the permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                3);


    }

    /**
     * Used to init the data base
     */
    private void initDB() {
        //init the session
        Core.getInstance().setHelper(new DaoMaster.DevOpenHelper(this,"Prototype-db",null));
        Core.getInstance().setDb(Core.getInstance().getHelper().getWritableDatabase());
        Core.getInstance().setDaoMaster(new DaoMaster(Core.getInstance().getDb()));
        Core.getInstance().setDaoSession( (Core.getInstance().getDaoMaster().newSession()));
    }


}


