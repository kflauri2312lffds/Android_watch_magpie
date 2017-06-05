package hevs.aislab.magpie.watch.agents;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.behavior.Behavior;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.event.MagpieEvent;
import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.MainActivity;
import hevs.aislab.magpie.watch.models.Rules;
import hevs.aislab.magpie.watch.repository.RulesRepository;

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


        List<Rules>pulseRules= RulesRepository.getInstance().getByCategory("pulse");


        for (final Rules aRules : pulseRules)
        {
            Log.d("foreachTestValue:",value+"");
            Log.d("foreachTestMin:",aRules.getMinValue()+"");
            Log.d("foreachTestMax:",aRules.getMaxValue()+"");

            if (value>=aRules.getMinValue() && value<aRules.getMaxValue())
            {

                ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       ((HomeActivity) getContext()).getFragmentHome().setSeverity(aRules.getCategory(),aRules.getSeverity());
                    }
                });
            }
        }


//        if (value > 90) {
//            ((MainActivity) getContext()).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String name = getAgent().getName();
//                    Toast.makeText(
//                            getContext(),
//                            "Agent '" + name + "' detected a high weight",
//                            Toast.LENGTH_LONG).show();
//                }
//            });
//        }
    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals("pulse");
    }
}
