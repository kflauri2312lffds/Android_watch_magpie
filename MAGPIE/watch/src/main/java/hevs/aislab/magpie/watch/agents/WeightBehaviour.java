package hevs.aislab.magpie.watch.agents;

import android.content.Context;

import java.util.List;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.behavior.Behavior;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.event.MagpieEvent;
import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch.threads.DisplayGUI;

/**
 * Created by teuft on 17.06.2017.
 */

public class WeightBehaviour extends Behavior {

    public WeightBehaviour(Context context, MagpieAgent agent, int priority) {
        setContext(context);
        setAgent(agent);
        setPriority(priority);
    }

    @Override
    public void action(MagpieEvent event) {
        LogicTupleEvent lte = (LogicTupleEvent) event;
        final double value = Double.parseDouble(lte.getArguments().get(0));


        //TODO : IMPLEMENT THE PROLOG: RULES:  >1% AND <2%
        //write the measure in the database


        Measure measure=new Measure();
        measure.setValue1(value);
        measure.setCategory(Const.CATEGORY_WEIGHT);
        measure.setTimeStamp(event.getTimestamp());
        MeasuresRepository.getInstance().insert(measure);

        //SET THE VALUE ON THE GUI
        HomeActivity context=((HomeActivity)getContext());
        Runnable threadGUI=new Thread(new DisplayGUI(context, Const.CATEGORY_WEIGHT,value));
        context.runOnUiThread(threadGUI);



        //GET THE RULES RELATED ON THE WEIGHT
        CustomRules weightRules= RulesRepository.getInstance().getByCategory(Const.CATEGORY_WEIGHT);
        //DEFIN THE BEGIN AND THE END TIME STAMP, BASED ON THE WINDOWS
        long endTimeStamp=event.getTimestamp();
        long startTimeSTamp=event.getTimestamp()-weightRules.getTimeWindow();

        //check if an alert in the timewindows aldready exist
        if (AlertRepository.getINSTANCE().getAllByCategoryBetweenTimeStamp(Const.CATEGORY_WEIGHT,startTimeSTamp,endTimeStamp).size() >=1)
            return;

        //get the measure related to the weight inside the timewindow
        List<Measure>weightsMeasures=MeasuresRepository.getInstance().getByCategoryWhereTimeStampBetween(Const.CATEGORY_WEIGHT,startTimeSTamp,endTimeStamp);


    }
    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_WEIGHT);
    }
}
