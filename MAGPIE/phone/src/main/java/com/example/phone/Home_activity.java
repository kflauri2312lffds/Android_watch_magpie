package com.example.phone;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.phone.gui.Fragment_display_alertes;
import com.example.phone.gui.Fragment_display_measures;
import com.example.phone.gui.Fragment_display_settings;

public class Home_activity extends AppCompatActivity {

    //store all the fragment

    Fragment fragmentAlert;
    Fragment fragmentMeasures;
    Fragment fragmentSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbarMenu = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbarMenu);

        initView();
        displayFragmentMeasures();
    }


    private void initView()
    {
        fragmentAlert =new Fragment_display_alertes();
        fragmentMeasures=new Fragment_display_measures();
        fragmentSettings =new Fragment_display_settings();


        //add the fragment to the fragment manager
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentAlert,"alertFrag").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentMeasures,"measureFrag").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentSettings,"settingsFrag").commit();
    }



    //*****************ACTION BAR METHOD************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //show the display of alert
            case R.id.action_alert:
                displayFragmentAlertes();
                return true;

            //display all the measure
            case R.id.action_measure:
                displayFragmentMeasures();
                return true;


            //display the settings
            case R.id.action_settings:
                displayFragmentSettings();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayFragmentAlertes()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentMeasures).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentSettings).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentAlert).commit();
    }
    private void displayFragmentMeasures()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentSettings).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentAlert).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentMeasures).commit();
    }

    private void displayFragmentSettings()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentMeasures).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentAlert).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentSettings).commit();
    }




}
