package hevs.aislab.magpie.watch.threads;

import android.hardware.Sensor;
import android.util.Log;

/**
 * This thread is designed to handle the lifecyrcle of sensors.
 * We can the thread simplye by settings time the sensors will listen (ActivationSensorsSecond)
 * and the time the sensor will sleep (desactivationSensorSecond.
 *
 */

public class SensorsThreadLifecircle extends Thread {

    IhomeActivity context;
    //activate this sensors during this time
    private int enabledTimeInMillisec;
    //desactivate the sensor during this time
    private int disabledTimeInMillisec;

    private Sensor sensor;

  volatile  private boolean isRunning=true;




    public SensorsThreadLifecircle(IhomeActivity context, Sensor sensor, int enabledTimeInMillisec, int disabledTimeInMillisec)
    {
        this.context=context;
        this.sensor=sensor;
        this.enabledTimeInMillisec = enabledTimeInMillisec;
        this.disabledTimeInMillisec = disabledTimeInMillisec;
    }

    @Override
    public void run() {

        while(isRunning)
        {
            try
            {

                context.registerSensor(sensor,Sensor.TYPE_HEART_RATE);
                Log.d("ThreadSensors","Sensors are activated");
                Thread.sleep(enabledTimeInMillisec);
                if (!isRunning)
                    return;
                context.processPulse();
                context.unregisterSensors(sensor);
                Log.d("ThreadSensors","Sensors are DISACTIVATED");
                Thread.sleep(disabledTimeInMillisec);
            }
            catch (InterruptedException ex)
            {
                Log.d("ThreadSensors","Error");
            }

        }

    }

    /**
     * use this methode to stop the thread and destroy it
     */
    public void cancel()
    {
        Log.d("DestroyHasBennCalled","Cancel method has been called");

        isRunning=false;
    }

}
