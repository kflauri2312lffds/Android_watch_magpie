package com.example.phone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.phone.db.Core;
import com.example.phone.models.DaoMaster;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();
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
}
