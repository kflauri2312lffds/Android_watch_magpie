package hevs.aislab.magpie.watch.listener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * Used to subscribe to data send by the watch. In this cas, it's rules data
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
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                //get the data array list
                ArrayList<DataMap> containerList=dataMap.getDataMapArrayList(Const.KEY_MEASURE_DATA);
                //extract data and save each value
                for (DataMap aData : containerList)
                {
                    CustomRules rule= extractRulesFromDataMap(aData);
                    RulesRepository.getInstance().insertOrUpdate(rule);
                }

                // Broadcast DataMap contents to wearable activity for display

                //Prepare data to send to the activity
                Bundle bundle=new Bundle();
                bundle.putString(Const.KEY_MESSAGE_TYPE,getString(R.string.sync_rules));
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra(Const.BUNDLE_DATA, bundle);

                //send info to activity
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

            }
        }
    }

    /**
     *
     * @param dataMap the data map that contains rules information
     * @return the rules created though datamap
     */
    private CustomRules extractRulesFromDataMap(DataMap dataMap)
    {
        CustomRules rule=new CustomRules();

        rule.setId(dataMap.getLong(Const.KEY_RULE_ID));
        rule.setCategory(dataMap.getString(Const.KEY_RULE_CATEGORY));

        rule.setVal_1_min(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL1_MIN)));
        rule.setVal_1_max(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL1_MAX)));
        rule.setVal_2_min(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL2_MIN)));
        rule.setVal_2_max(formatNumber(dataMap.getDouble(Const.KEY_RULE_VAL2_MAX)));

        rule.setConstraint_1(dataMap.getString(Const.KEY_RULE_CONSTRAINT1));
        rule.setConstraint_2(dataMap.getString(Const.KEY_RULE_CONSTRAINT2));
        rule.setConstraint_3(dataMap.getString(Const.KEY_RULE_CONSTRAINT3));

        return rule;
    }

    /**
     *
     * @param value value we want to format. if it contains the identifier, it will be set to null
     * @return value formated
     */
    private Double formatNumber(Double value)
    {
        return value== Const.NULL_IDENTIFIER ? null : value;
    }

}
