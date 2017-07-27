package hevs.aislab.magpie.watch.phone_communication;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;
import java.util.List;


import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch_library.communication_thread.SendToDataLayerThread;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * this thread will process and prepare data for the phone. It will push all database (measures, alert, rules ) to the phone
 */

public class PushMeasureThread extends Thread {
    private int counter = 0;
    //max size of data is 200 entry so the target device will have time to proceed.
    private int maxSize = 200;
    private int index = 0;
    private int sleepTime=3000;

    GoogleApiClient googleClient;
    public PushMeasureThread(GoogleApiClient googleClient)
    {
        this.googleClient=googleClient;
    }

    @Override
    public void run() {
        pushMeasure();
        pushRules();
        pushAlert();

    }

    private void pushMeasure() {

          counter = 0;
        //max size of data is 200 entry so the target device will have time to proceed.
          maxSize = 200;
          int index = 0;
        //get all the measure
        List<Measure> allMeasure = MeasuresRepository.getInstance().getAll();
        //prepare the data container
        int datasize=allMeasure.size();

        while (index < datasize) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {

            }

            DataMap data_container = new DataMap();
            //create the arrayList that will contains all data
            ArrayList<DataMap> containerList = new ArrayList<>();

            for (int i = index; i < datasize; i++) {
                index++;
                counter++;
                Measure aMeasure = allMeasure.get(i);
                DataMap data = generateDataMap(aMeasure);
                containerList.add(data);
                if (counter >= maxSize) {
                    counter = 0;
                    break;
                }
            }

            //add the list to the data_container
            data_container.putDataMapArrayList(Const.KEY_MEASURE_DATA, containerList);
            //send the data container to the data layer
            new SendToDataLayerThread(googleClient, Const.PATH_PUSH_MEASURE, data_container).start();
        }
    }

    /**
     * Pushes the rules to the smartphon
     */
    private void pushRules()
    {
        counter = 0;
        maxSize = 200;
        int index = 0;
        sleepTime=1000;

        //get all the measure
        List<CustomRules> rulesList = RulesRepository.getInstance().getAll();
        //prepare the data container
        int datasize=rulesList.size();

        while (index < datasize) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {

            }

            DataMap data_container = new DataMap();
            //create the arrayList that will contains all data
            ArrayList<DataMap> containerList = new ArrayList<>();

            for (int i = index; i < datasize; i++) {
                index++;
                counter++;
                CustomRules aRule = rulesList.get(i);
                DataMap data = generateDataMap(aRule);
                containerList.add(data);
                if (counter >= maxSize) {
                    counter = 0;
                    break;
                }
            }
            //add the list to the data_container
            data_container.putDataMapArrayList(Const.KEY_MEASURE_DATA, containerList);
            //send the data container to the data layer
            new SendToDataLayerThread(googleClient, Const.PATH_PUSH_RULE, data_container).start();
        }
    }

    private void pushAlert()
    {
        counter = 0;
        maxSize = 200;
        int index = 0;
        sleepTime=3000;


        //get all the measure
        List<Alertes> alertList = AlertRepository.getINSTANCE().getAll();
        //prepare the data container
        int datasize=alertList.size();


        while (index < datasize) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {

            }

            DataMap data_container = new DataMap();
            //create the arrayList that will contains all data
            ArrayList<DataMap> containerList = new ArrayList<>();

            for (int i = index; i < datasize; i++) {
                index++;
                counter++;
                Alertes anAlert= alertList.get(i);
                DataMap data = generateDataMap(anAlert);
                containerList.add(data);
                if (counter >= maxSize) {
                    counter = 0;
                    break;
                }
            }
            //add the list to the data_container
            data_container.putDataMapArrayList(Const.KEY_MEASURE_DATA, containerList);
            //send the data container to the data layer
            new SendToDataLayerThread(googleClient, Const.PATH_PUSH_ALERT, data_container).start();
        }
    }


    private DataMap generateDataMap(Measure aMeasure) {
        //create a set of data with information of the measure
        DataMap data=new DataMap();
        data.putLong(Const.KEY_CURRENTTIMESTAMP,System.currentTimeMillis());
        data.putLong(Const.KEY_MEASURE_ID,aMeasure.getId());
        data.putString(Const.KEY_MEASURE_CATEGORY,aMeasure.getCategory());
        data.putLong(Const.KEY_MEASURE_TIMESTAMP,aMeasure.getTimeStamp());
        data.putDouble(Const.KEY_MEASURE_VALUE1,aMeasure.getValue1());
        //handle possible null value
        data.putDouble(Const.KEY_MEASURE_VALUE2,formatValue(aMeasure.getValue2()));
        //insert the data into the list
        return data;
    }

    private DataMap generateDataMap(CustomRules aRule)
    {
        DataMap dataMap=new DataMap();
        dataMap.putLong(Const.KEY_CURRENTTIMESTAMP,System.currentTimeMillis());
        dataMap.putLong(Const.KEY_RULE_ID,aRule.getId());
        dataMap.putString(Const.KEY_RULE_CATEGORY,aRule.getCategory());
        dataMap.putString(Const.KEY_RULE_CONSTRAINT1,aRule.getConstraint_1());
        dataMap.putString(Const.KEY_RULE_CONSTRAINT2,aRule.getConstraint_2());
        dataMap.putString(Const.KEY_RULE_CONSTRAINT3,aRule.getConstraint_3());

        //if null, we send the number -100000. We will have to check on the other side
        dataMap.putDouble(Const.KEY_RULE_VAL1_MIN, formatValue(aRule.getVal_1_min()));
        dataMap.putDouble(Const.KEY_RULE_VAL1_MAX,formatValue(aRule.getVal_1_max()));
        dataMap.putDouble(Const.KEY_RULE_VAL2_MIN,formatValue(aRule.getVal_2_min()));
        dataMap.putDouble(Const.KEY_RULE_VAL2_MAX, formatValue(aRule.getVal_2_max()));

        return dataMap;

    }
    private DataMap generateDataMap(Alertes anAlert)
    {
        DataMap dataMap=new DataMap();
        dataMap.putLong(Const.KEY_CURRENTTIMESTAMP,System.currentTimeMillis());
        dataMap.putLong(Const.KEY_ALERT_ID,anAlert.getId());
        dataMap.putLong(Const.KEY_MEASURE_ID,anAlert.getMeasure_id());
        dataMap.putLong(Const.KEY_RULE_ID,anAlert.getRule_id());

        return dataMap;

    }
    //used to format the value and send a number if it's null
    private double formatValue(Double value)
    {
        return value==null ? Const.NULL_IDENTIFIER : value;
    }

}
