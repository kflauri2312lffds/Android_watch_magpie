package hevs.aislab.magpie.watch.threads;

import android.content.Context;
import android.util.Log;

import java.util.List;

import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;

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

        List<Measure>measureList= MeasuresRepository.getInstance().getAll();
        for (Measure aMeasure : measureList)
        {
            Log.d("measure_ BEGIN MEASURE","***************");
            Log.d("Measure_ category:",aMeasure.getCategory());
            Log.d("Measure_ value 1",aMeasure.getValue1()+"");
            Log.d("Measure_ value 2",aMeasure.getValue2()+"");
            Log.d("Measure_ TimeStamp",aMeasure.getTimeStamp()+"");
            Log.d("measure_ ENDOF MEASURE","***************");
        }
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
            context.getFragmentHome().setGlucoseValue(value[0]+"");
            return;
        }
        if (category.equals(Const.CATEGORY_PRESSURE))
        {
                                                        //we cast in it to remove the comma.
            context.getFragmentHome().setSystolValue(((int)value[0])+"");
            context.getFragmentHome().setDiastolValue((int)value[1]+"");
            return;
        }
        if (category.equals(Const.CATEGORY_STEP))
        {
            context.getFragmentHome().setStepValue((int)value[0]+"");
            return;
        }

    }
    private void updateStep()
    {

    }
    private void updateWeight()
    {

    }

    private void updateGlucose()
    {

    }
    private void updatePulse()
    {

    }
}
