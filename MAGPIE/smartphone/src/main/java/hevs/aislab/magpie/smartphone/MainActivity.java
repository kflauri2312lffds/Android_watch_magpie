package hevs.aislab.magpie.smartphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import hevs.aislab.magpie.smartphone.R;
import hevs.aislab.magpie.smartphone.db.Core;
import hevs.aislab.magpie.smartphone.models.DaoMaster;
//import hevs.aislab.magpie.smartphone.models.DaoMaster;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();

        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

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
     * Used to init the data base
     */
    private void initDB() {
        //init the session
        Core.getInstance().setHelper(new DaoMaster.DevOpenHelper(this,"Prototype-db",null));
        Core.getInstance().setDb(Core.getInstance().getHelper().getWritableDatabase());
        Core.getInstance().setDaoMaster(new DaoMaster(Core.getInstance().getDb()));
        Core.getInstance().setDaoSession( (Core.getInstance().getDaoMaster().newSession()));
    }

    //receive data from the listener service
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getBundleExtra("datamap");
            Toast.makeText(context, "MessageReceive", Toast.LENGTH_SHORT).show();
        }
    }

}
