package hevs.aislab.magpie.watch.agents;

import android.content.Context;
import android.util.Log;

import java.util.List;

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
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch.notification.NotificationGenerator;
import hevs.aislab.magpie.watch.threads.DisplayGUI;

/**
 * Created by teuft on 12.06.2017.
 */

public class GlucoseBehaviour extends Behavior {

    public GlucoseBehaviour(Context context, MagpieAgent agent, int priority) {
        setContext(context);
        setAgent(agent);
        setPriority(priority);
    }

    @Override
    public void action(MagpieEvent event) {
        LogicTupleEvent lte = (LogicTupleEvent) event;
        Log.d("MessageVocal",lte.getArguments().get(0));
        final double value = Double.parseDouble(lte.getArguments().get(0));

        //add the values in the GUI
        HomeActivity context=(HomeActivity)getContext();
        Runnable threadGui= new DisplayGUI(context,Const.CATEGORY_GLUCOSE,value);
        context.runOnUiThread(threadGui);

        //ENTER THE GLUCOSE INTO THE DB
        Measure measure=new Measure();
        insertInDB(lte, value, measure);

        //GET THE RULE RELATED TO THE GLUCOSE
        CustomRules rules =RulesRepository.getInstance().getByCategory(Const.CATEGORY_GLUCOSE);

        //GET THE BEGIN TIME STAMP AND THE END TIME STAMP
        Long endTimeSTamp=measure.getTimeStamp();
        Long startTimeStamp=endTimeSTamp-rules.getTimeWindow();

        //check if an alert exist aldready in the timestamp
//        List<Alertes>alertesList=AlertRepository.getINSTANCE().getAllByCategoryBetweenTimeStamp(Const.CATEGORY_GLUCOSE,startTimeStamp,endTimeSTamp);
//        if (alertesList.size()!=0)
//            return;

        //now we query the db to find all event inside the timestamp
        List<Measure>measureList= MeasuresRepository.getInstance().getByCategoryWhereTimeStampBetween(Const.CATEGORY_GLUCOSE,startTimeStamp,endTimeSTamp);
        //we need to have at least 2 glucose measure to be able to make a comparison
        if (measureList.size()<=1)
            return;

        Log.d("InfoRulesAgent","Measure size greater than 1");
        //CHECK IF THERE IS A MEASURE THAT IS BELOW 3.8 AND TAKE IT. iT'S ORDER BY TIMESTAMP, SO WE START IN THE ARRAY
        int counter=0;
        Measure firstMeasure=null;
        for (counter=0;counter<measureList.size();counter++) {
            if (measureList.get(counter).getValue1()<=rules.getVal_1_min())
            {
                //we keep the measure to compare with measure that come later
                firstMeasure=measureList.get(counter);
                break;
            }
        }

       //if null: no measure below 3.8 so we don't check further
        if (firstMeasure==null)
            return;

        Log.d("InfoRulesAgent","First loop");
        Measure secondeMeasure=null;
        //now we will compare measure that comme later in the time stamp is higher than the max (in our case, it's 8.0
        for (int k=counter;k<measureList.size();k++)
        {
            if (measureList.get(k).getValue1()>=rules.getVal_2_max())
            {
                secondeMeasure=measureList.get(k);
                break;
            }
        }

        //if no value >=8 come after the value <=3.8, there is no alert so we don't write it in the db.
        if (secondeMeasure==null)
            return;

        //now, we check if an alert exist in the time stamp.
        //todo put it in the first position
        Log.d("InfoRulesAgent","Seconde measure is not null");

        //launch the notification
        Thread notificationThread=new Thread(new NotificationGenerator(context,context.getString(R.string.category_glucose),context.getString(R.string.notification_high_glucose)));
        context.runOnUiThread(notificationThread);

        //there is no alert existing, so we create one and we link it with the measure
        Alertes alertes=new Alertes();
        alertes.setMeasure(measure);
        alertes.setRule(rules);
        AlertRepository.getINSTANCE().insert(alertes);

        Log.d("InfoRulesAgent","Alert has been trigered");
        //TODO DISPLAY THE ALERT OF RULES

    }



    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_GLUCOSE);
    }

    /**
     * Create the measure and enter it in the database
     * @param lte
     * @param value
     * @param measure
     */
    private void insertInDB(LogicTupleEvent lte, double value, Measure measure) {
        measure.setValue1(value);
        measure.setCategory(Const.CATEGORY_GLUCOSE);
        measure.setTimeStamp(lte.getTimestamp());
        MeasuresRepository.getInstance().insert(measure);
    }

}
