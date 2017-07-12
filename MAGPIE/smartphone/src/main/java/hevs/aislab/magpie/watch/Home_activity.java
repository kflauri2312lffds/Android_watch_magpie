package hevs.aislab.magpie.watch;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import hevs.aislab.magpie.watch.gui.fragments.Fragment_display_alertes;
import hevs.aislab.magpie.watch.gui.fragments.Fragment_display_measures;
import hevs.aislab.magpie.watch.gui.fragments.Fragment_display_settings;

public class Home_activity extends AppCompatActivity
        implements
        // implement methods
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        IactivityInterface{

    //store all the fragment

    private Fragment fragmentAlert;
    private Fragment fragmentMeasures;
    private Fragment fragmentSettings;

    //object we use to go through the data layer
    private GoogleApiClient googleClient;



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


        //INIT the googe client
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    // Connect to the data layer when the Activity starts
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

        //recreate the framgnet for the alert


        getSupportFragmentManager().beginTransaction().hide(fragmentMeasures).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentSettings).commit();
        getSupportFragmentManager().beginTransaction().remove(fragmentAlert).commit();
        fragmentAlert=new Fragment_display_alertes();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,fragmentAlert,"display_alertTag").commit();
        getSupportFragmentManager().beginTransaction().show(fragmentAlert).commit();

    }
    private void displayFragmentMeasures()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentSettings).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentAlert).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentMeasures).commit();

        //display all measure

    }

    private void displayFragmentSettings()
    {
        getSupportFragmentManager().beginTransaction().hide(fragmentMeasures).commit();
        getSupportFragmentManager().beginTransaction().hide(fragmentAlert).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentSettings).commit();
    }


    //**********************USED TO SEND MESSAGE TO THE WATCH**************************

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
    public GoogleApiClient getGoogleclient() {
        return googleClient;
    }
}
