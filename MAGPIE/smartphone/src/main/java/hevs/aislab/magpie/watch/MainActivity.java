package hevs.aislab.magpie.watch;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch_library.lib.Const;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();

        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
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


    //*************************used to get message from service

    //receive data from the listener service
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getBundleExtra(Const.KEY_MEASURE_CATEGORY);
            Toast.makeText(context, "MessageReceive: ", Toast.LENGTH_SHORT).show();
        }
    }




}
