package hevs.aislab.magpie.watch.agents;

import android.content.Context;

import java.util.List;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.behavior.Behavior;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.event.MagpieEvent;
import hevs.aislab.magpie.watch.HomeActivity;

import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;

/**
 * Created by teuft on 12.06.2017.
 */

public class GlucoseBehaviour extends Behavior {

    RulesRepository rulesRepository;
    MeasuresRepository measuresRepository;

    public GlucoseBehaviour(Context context, MagpieAgent agent, int priority) {
        setContext(context);
        setAgent(agent);
        setPriority(priority);
    }

    @Override
    public void action(MagpieEvent event) {
        LogicTupleEvent lte = (LogicTupleEvent) event;
        final double value = Long.parseLong(lte.getArguments().get(0));


        List<CustomRules> pulseRules= RulesRepository.getInstance().getByCategory("pulse");


        // Write the measure in the DB. for now, I hardcoded it
        //TODO WRITE VALUE IN DB

        int hours=6;
        long TimeWindows=hours*60*60*1000;
        long currentTimeStamp=System.currentTimeMillis();
        long minTimeSTamp=currentTimeStamp-currentTimeStamp;

      //  List<Measures>


        // TODO: GET ALL EVENT IN THE DB THAT ARE IN BETWEEN CURRENT TIME STAMP AND MIN TIMESTAMP






            if (1==1)
            {
                ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //set the value and the severity

                    }
                });
            }

    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals("pulse");
    }
}
