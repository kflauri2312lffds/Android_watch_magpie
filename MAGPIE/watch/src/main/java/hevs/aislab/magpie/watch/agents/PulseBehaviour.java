package hevs.aislab.magpie.watch.agents;

import android.content.Context;

import ch.hevs.aislab.magpie.agent.MagpieAgent;
import ch.hevs.aislab.magpie.behavior.Behavior;
import ch.hevs.aislab.magpie.event.LogicTupleEvent;
import ch.hevs.aislab.magpie.event.MagpieEvent;
import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.notification.NotificationGenerator;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
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
        final double value = Double.parseDouble(lte.getArguments().get(0));



        HomeActivity context=(HomeActivity)getContext();
        Thread threadGUI=new Thread(new DisplayGUI(context,Const.CATEGORY_PULSE,value));
        context.runOnUiThread(threadGUI);

        //INSERT THE VALUES IN THE DB
        Measure measure=new Measure();
        measure.setTimeStamp(event.getTimestamp());
        measure.setCategory(Const.CATEGORY_PULSE);
        measure.setValue1(value);
        MeasuresRepository.getInstance().insert(measure);

        //apply the simple rules for the pulse

        //get the rules related to pulse

        CustomRules pulseRules = RulesRepository.getInstance().getByCategory(Const.CATEGORY_PULSE);
        Double minValue=pulseRules.getVal_1_min();
        Double maxValue=pulseRules.getVal_1_max();
        //get the current value of the measure


        String messageAlert="";
        if (isGreaterThanMax(value,maxValue))
            messageAlert=getContext().getString(R.string.notification_high_pressure);
        else
            if (isSmallerThanMin(value,minValue))
                messageAlert=getContext().getString(R.string.notification_low_pulse);


        //no alert detected, we return
        if (messageAlert.equals(""))
            return;



        //launch the notification
        Thread notificationThread=new Thread(new NotificationGenerator(context,context.getString(R.string.category_pulse),messageAlert));
        context.runOnUiThread(notificationThread);
        //Create the alert in the database
        Alertes alertes=new Alertes();
        alertes.setRule(pulseRules);
        alertes.setMeasure(measure);
        AlertRepository.getINSTANCE().insert(alertes);


    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_PULSE);
    }
    private boolean isSmallerThanMin(double value, Double minValue)
    {
        //value not defined, so no alert
        if (minValue==null)
            return false;

        return  value<minValue;
    }
    private boolean isGreaterThanMax(double value, Double maxValue)
    {
        //value not defined, so no alert
        if (maxValue==null)
            return false;

        return  value>maxValue;
    }
}
