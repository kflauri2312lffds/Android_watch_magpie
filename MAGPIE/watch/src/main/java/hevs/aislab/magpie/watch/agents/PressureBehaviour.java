package hevs.aislab.magpie.watch.agents;

import android.content.Context;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.behavior.Behavior;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.event.MagpieEvent;
import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.threads.DisplayGUI;

/**
 * Created by teuft on 16.06.2017.
 */

public class PressureBehaviour extends Behavior {

    public PressureBehaviour(Context context, MagpieAgent agent, int priority) {
        setContext(context);
        setAgent(agent);
        setPriority(priority);
    }

    @Override
    public void action(MagpieEvent event) {
        LogicTupleEvent lte = (LogicTupleEvent) event;
        final double valueSystol = Double.parseDouble(lte.getArguments().get(0));
        final double valueDiastol = Double.parseDouble(lte.getArguments().get(1));



        //set the value on the gui
        HomeActivity context=((HomeActivity)getContext());
        Runnable threadGUI=new Thread(new DisplayGUI(context, Const.CATEGORY_PRESSURE,valueSystol,valueDiastol));
        context.runOnUiThread(threadGUI);

        //TODO: DEFINE THE RULES FOR THE PRESSURE
    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_PRESSURE);
    }
}
