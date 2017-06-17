package hevs.aislab.magpie.watch.threads;

import android.hardware.Sensor;

/**
 * Created by teuft on 17.06.2017.
 */

public interface IhomeActivity {

    void registerSensor(Sensor sensor, int sensorsType);
    void unregisterSensors(Sensor sensor);
    void processPulse();

}
