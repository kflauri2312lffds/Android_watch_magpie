package hevs.aislab.magpie.watch.agents;

import android.content.Context;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.behavior.Behavior;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.event.MagpieEvent;
import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.threads.DisplayGUI;

/**
 * Created by teuft on 17.06.2017.
 */

public class StepBehaviour extends Behavior {

    public StepBehaviour(Context context, MagpieAgent agent, int priority) {
        setContext(context);
        setAgent(agent);
        setPriority(priority);
    }

    @Override
    public void action(MagpieEvent event) {
        LogicTupleEvent lte = (LogicTupleEvent) event;
        final double steps = Double.parseDouble(lte.getArguments().get(0));

        //write the measure in the database
        Measure measure=new Measure();
        measure.setValue1(steps);
        measure.setCategory(Const.CATEGORY_STEP);
        measure.setTimeStamp(event.getTimestamp());

        MeasuresRepository.getInstance().insert(measure);

        //SET THE VALUE ON THE GUI
        HomeActivity context=((HomeActivity)getContext());
        Runnable threadGUI=new Thread(new DisplayGUI(context, Const.CATEGORY_STEP,steps));
        context.runOnUiThread(threadGUI);

        //TODO: DEFINE THE RULES FOR THE STEPS

    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_STEP);
    }
}
