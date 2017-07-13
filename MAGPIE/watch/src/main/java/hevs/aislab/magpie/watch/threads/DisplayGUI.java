package hevs.aislab.magpie.watch.threads;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * this class will change value inside the GUI. Used in the agent.
 * Here an exemple of instanciation inside a Java Agent:
 *HomeActivity context=(HomeActivity)getContext();
 *Runnable threadGui= new DisplayGUI(Const.CATEGORY_GLUCOSE,value,context);
 *context.runOnUiThread(threadGui);
 */
public class DisplayGUI implements Runnable {



    private String category;
    private double[] value;
    private HomeActivity context;




    public DisplayGUI(HomeActivity context, String category,  double ... value)
    {
        this.category=category;
        this.value=value;
        this.context=context;
    }

    @Override
    public void run()
    {
        updateGUI();
    }

    //UPDATE INSIDE THE GUI
    private void updateGUI()
    {
        if (category.equals(Const.CATEGORY_GLUCOSE))
        {
            context.getFragmentHome().setGlucoseValue(value[0]);
            return;
        }

        if (category.equals(Const.CATEGORY_PRESSURE))
        {
                                                        //we cast in it to remove the comma.
            context.getFragmentHome().setSystolValue(value[0]);
            context.getFragmentHome().setDiastolValue(value[1]);
            return;
        }

        if (category.equals(Const.CATEGORY_STEP))
        {
            context.getFragmentHome().setStepValue(value[0]);
            return;
        }

        if (category.equals(Const.CATEGORY_PULSE))
        {
            context.getFragmentHome().setPulseValue(value[0]);
        }

        if (category.equals(Const.CATEGORY_WEIGHT))
        {
            context.getFragmentHome().setWeightValue(value[0],value[1]);
        }

    }

}
