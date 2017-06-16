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
 * Created by teuft on 05.06.2017.
 */

public class PulseBehaviour extends Behavior {

    public PulseBehaviour(Context context, MagpieAgent agent, int priority) {
        setContext(context);
        setAgent(agent);
        setPriority(priority);
    }

    @Override
    public void action(MagpieEvent event) {
        LogicTupleEvent lte = (LogicTupleEvent) event;
        final double value = Long.parseLong(lte.getArguments().get(0));


//        List<CustomRules>pulseRules= RulesRepository.getInstance().getByCategory("pulse");
//
//        for (final CustomRules aRule : pulseRules)
//        {
//            if (value>= aRule.getMinValue() && value< aRule.getMaxValue())
//            {
//                ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //set the value and the severity
//                        ((HomeActivity) getContext()).getFragmentHome().setSeverity(aRule.getCategory(), aRule.getSeverity());
//                        ((HomeActivity) getContext()).getFragmentHome().setPulseValue(((int)value)+"");
//                    }
//                });
//            }
//        }



    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_PULSE);
    }
}
