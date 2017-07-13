package hevs.aislab.magpie.watch.threads;

import android.hardware.Sensor;

/**
 * Used to make the link between the fragment and the activity
 */

public interface IhomeActivity {

    void registerSensor(Sensor sensor, int sensorsType);
    void unregisterSensors(Sensor sensor);
    void processPulse();

}
