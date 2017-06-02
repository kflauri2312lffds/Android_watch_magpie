package hevs.aislab.magpie.watch;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import ch.hevs.aislab.magpie.android.MagpieActivityWatch;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;

/**
 * Created by teuft on 31.05.2017.
 * this class will handle all other fragment related the the apps
 */

//implement the listener for the sensors
public class HomeActivity extends MagpieActivityWatch implements SensorEventListener {

    //handle the voice variable
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //init the graphical element




    }
    //------------VOICE RECOGNITION HANDLER------------------

    //event for the button voice
    public void voiceRecognition(View view)
    {
        displaySpeechRecognizer();
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    //handle the voice result
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0).toLowerCase();

            //action based on the r

            //get the texte

            //create the possiblity and handle multi language
            String glucose=getString(R.string.voice_glucose);

            if (spokenText.equals(glucose))
            {
                displayAddGlucose();
            }


            else
            {
                Toast.makeText(this, getString(R.string.voice_not_found), Toast.LENGTH_SHORT).show();
            }




        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void displayAddGlucose()
    {

    }

    //-----------MAGPIE METHODE-------------
    @Override
    public void onEnvironmentConnected() {

    }

    @Override
    public void onAlertProduced(LogicTupleEvent alert) {

    }

    //------------SENSORS METHODE--------------

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
