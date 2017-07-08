package hevs.aislab.magpie.watch.listener;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

import hevs.aislab.magpie.watch.lib.Const;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;

/**
 * Created by teuft on 08.07.2017.
 */

public class Listener_data_rule extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        DataMap dataMap;
        for (DataEvent event : dataEvents) {

            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
//                if (path.equals(Const.PATH_PUSH_MEASURE)) {}
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                //get the data array list
                ArrayList<DataMap> containerList=dataMap.getDataMapArrayList(Const.KEY_MEASURE_DATA);
                Log.d("size_data_rules",containerList.size()+"");

                //extract data and save each value
                for (DataMap aData : containerList)
                {
                    CustomRules rule= extractRulesFromDataMap(aData);
                    RulesRepository.getInstance().insert(rule);
                }

                // Broadcast DataMap contents to wearable activity for display
                // The content has the golf hole number and distances to the front,
                // middle and back pin placements.

                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra(Const.KEY_MEASURE_CATEGORY, dataMap.toBundle().getString(Const.KEY_MEASURE_CATEGORY));


                //LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

            }
        }
    }

    private CustomRules extractRulesFromDataMap(DataMap dataMap)
    {
        CustomRules rule=new CustomRules();

        rule.setId(dataMap.getLong(Const.KEY_RULE_ID));
        rule.setCategory(dataMap.getString(Const.KEY_RULE_CATEGORY));


        rule.setVal_1_min(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL1_MIN)));
        rule.setVal_1_max(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL1_MAX)));
        rule.setVal__2_min(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL2_MIN)));
        rule.setVal_2_max(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL2_MAX)));

        rule.setConstraint_1(dataMap.getString(Const.KEY_RULE_CONSTRAINT1));
        rule.setConstraint_2(dataMap.getString(Const.KEY_RULE_CONSTRAINT2));
        rule.setConstraint_3(dataMap.getString(Const.KEY_RULE_CONSTRAINT3));

        return rule;
    }
    private Double formatNumber(Double value)
    {
        return value== -10000 ? null : value;
    }

}