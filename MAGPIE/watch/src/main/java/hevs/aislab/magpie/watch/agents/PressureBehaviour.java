package hevs.aislab.magpie.watch.agents;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
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
import hevs.aislab.magpie.watch.threads.CreateNotification;
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

        //write the new measure in the database
        Measure measure=new Measure();
        measure.setValue1(valueSystol);
        measure.setValue2(valueDiastol);
        measure.setCategory(Const.CATEGORY_PRESSURE);
        measure.setTimeStamp(event.getTimestamp());
        MeasuresRepository.getInstance().insert(measure);
        //set the value on the gui
        HomeActivity context=((HomeActivity)getContext());
        Runnable threadGUI=new Thread(new DisplayGUI(context, Const.CATEGORY_PRESSURE,valueSystol,valueDiastol));
        context.runOnUiThread(threadGUI);


        //APPLY THE RULES BASED ON THE PROLOG RULES
        //get the rules related to blood pressure
        CustomRules pressureRules= RulesRepository.getInstance().getByCategory(Const.CATEGORY_PRESSURE);

        //define the start and end timeStamp based on the timewindow

        long endTimeStamp=event.getTimestamp();
        long startTimeStamp=endTimeStamp-pressureRules.getTimeWindow();

        //WE CHECK IF AN ALERT EXIST FOR THE TIMESTAMP AND THE CATEGORY. If yes, we don't triger a new alert. If no, we trigger a new alert.
        List<Alertes>alertesList=  AlertRepository.getINSTANCE().getAllByCategoryBetweenTimeStamp(Const.CATEGORY_PRESSURE,startTimeStamp,endTimeStamp);
        if (alertesList.size()!=0)
            return;


        //get measure in db  between the timestamp
        List<Measure>measureList= MeasuresRepository.getInstance().getByCategoryWhereTimeStampBetween(Const.CATEGORY_PRESSURE,startTimeStamp,endTimeStamp);


        Log.d("pressureAlertStatus","check measure size in the database");
        if (measureList.size()<=1)
            return;

        // GET ALL MEASURE THAT: SYS >=130 && DIAS>=80
        List<Measure>severMeasure=new ArrayList<Measure>();

        for (Measure aMeasure : measureList)
        { //                                           130                                                    80
            if (aMeasure.getValue1()>=pressureRules.getVal_1_max() && aMeasure.getValue2()>=pressureRules.getVal_2_max())
            {
                severMeasure.add(aMeasure);
            }
        }
        Log.d("pressureAlertStatus","check the sever measure size");
        if (severMeasure.size()<=1)
            return;

        //launch the notification
        Thread notificationThread=new Thread(new CreateNotification(context,context.getString(R.string.category_pressure),context.getString(R.string.notification_high_pressure)));
        context.runOnUiThread(notificationThread);

        Alertes alertes=new Alertes();
        alertes.setRule(pressureRules);
        alertes.setMeasure(measure);
        AlertRepository.getINSTANCE().insert(alertes);




        Log.d("pressureAlertStatus","AlertHasBen trigered");



    }

    @Override
    public boolean isTriggered(MagpieEvent event) {
        LogicTupleEvent condition = (LogicTupleEvent) event;
        return condition.getName().equals(Const.CATEGORY_PRESSURE);
    }
}
