package hevs.aislab.magpie.watch.agents;

import android.content.Context;

import java.util.List;
import java.util.Objects;

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

        //TODO CHCKER QUE C'EST EN ORDRE
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
        long endTimeStamp=measure.getTimeStamp();
        long startTimeSTamp=endTimeStamp-weightRules.getTimeWindow();

        //check if an alert in the timewindows aldready exist. If yes, we leave
        if (AlertRepository.getINSTANCE().getAllByCategoryBetweenTimeStamp(Const.CATEGORY_WEIGHT,startTimeSTamp,endTimeStamp).size() >=1)
            return;
        //get the measure related to the weight inside the timewindow
        List<Measure>weightsMeasures=MeasuresRepository.getInstance().getByCategoryWhereTimeStampBetween(Const.CATEGORY_WEIGHT,startTimeSTamp,endTimeStamp);
        //if no more than 2 values, we return
        if (weightsMeasures.size()<=1)
            return;
        /* now we check the  weight variation based on our mesure. We will check all existing measure between the timestamp.
        The timestamp is 7 days. max weight variation is 1%. Min weight variation is 2%.
        */


       //we take the current weight of the measure
        double actualWeight=measure.getValue1();

        //save the weight fluctuation in a variable
        String message="";

        //Get the previous measure
            Measure previousMeasure=weightsMeasures.get(weightsMeasures.size()-2);
            double previousWeight=previousMeasure.getValue1();
            //the minimal value allowed otherwise, it won't pass the test
            double minValueAllowed =previousWeight*(weightRules.getVal__2_min()/100);
            //idem for the max value
            double maxValueAllowed=previousWeight*(weightRules.getVal_2_max()/100);

            if (actualWeight>=maxValueAllowed)
                message=context.getString(R.string.notification_high_weight);
            else
                if (actualWeight<=minValueAllowed)
                    message=  context.getString(R.string.notification_low_weight);

            //if no alert detected, we return
            if (message.equals(""))
                return;

        //we launch the notification
        NotificationGenerator notificationGenerator=new NotificationGenerator(context,"Weight",message);
        context.runOnUiThread(notificationGenerator);

        //we create the alert and insert it in the database
        //TODO ADD THE VARIATION IN THE MEASURE

        Alertes weightAlert=new Alertes();
        weightAlert.setMeasure(measure);
        weightAlert.setRule(weightRules);
        AlertRepository.getINSTANCE().insert(weightAlert);

    }
    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_WEIGHT);
    }
}
