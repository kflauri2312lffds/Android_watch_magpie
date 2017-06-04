package hevs.aislab.magpie.watch.threads;

import java.util.HashMap;

/**
 * Created by teuft on 04.06.2017.
 */

public class SensorsThreadLifecircle implements Runnable {


    //set the time for the start and // STOP


    //activate this sensors during this time
    private int activationSensorsSecond;
    //desactivate the sensor during this time
    private int desactivationSensorsSecond;




    public SensorsThreadLifecircle(int activationSensorsSecond, int desactivationSensorsSecond)
    {
        this.activationSensorsSecond=activationSensorsSecond;
        this.desactivationSensorsSecond=desactivationSensorsSecond;
    }

    @Override
    public void run() {

    }
}
