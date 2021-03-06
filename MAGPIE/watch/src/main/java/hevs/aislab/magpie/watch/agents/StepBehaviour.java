package hevs.aislab.magpie.watch.agents;

import android.content.Context;


import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.behavior.Behavior;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.event.MagpieEvent;

import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * Steps agent that will take care of the glucose data and trigger an alert
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

        //write the measure in the database, only 1 time a day

        Measure measure=new Measure();
        measure.setValue1(steps);
        measure.setCategory(Const.CATEGORY_STEP);
        measure.setTimeStamp(event.getTimestamp());
        MeasuresRepository.getInstance().insert(measure);


        //We do not need to set the value in GUI her


        //APPLY THE RULES FOR THE STEP

        //Get the rules
        CustomRules stepRules= RulesRepository.getInstance().getByCategory(Const.CATEGORY_STEP);

        //get min and max value allowed, based on the rules
        double minValue=stepRules.getVal_1_min();
        double maxValue=stepRules.getVal_1_max();

        //now we apply the rules

        //no alert trigered if the steps are in the range
        if (steps>minValue && steps<maxValue)
            return;

        //insert the alere in the db, but we don't display
        Alertes alertes=new Alertes();
        alertes.setRule(stepRules);
        alertes.setMeasure(measure);
        AlertRepository.getINSTANCE().insert(alertes);


    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_STEP);
    }
}
