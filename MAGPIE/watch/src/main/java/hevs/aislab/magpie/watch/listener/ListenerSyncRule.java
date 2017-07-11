package hevs.aislab.magpie.watch.listener;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * this class will be the listener of the data send by the phone (rule)
 */

public class ListenerSyncRule extends WearableListenerService {


    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                //get the new rule send by the phone
                CustomRules rule= getRuleFromMap(dataMap);

                //save the rule in the database
                RulesRepository.getInstance().insertOrUpdate(rule);

                Log.d("RuleSync_Data","data receive in the watch");



                //send the value to the activty to change the display of bar


                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra(Const.KEY_BROADCASTdATA, dataMap.toBundle());
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

            }
        }
    }


    /**
     * This method will extract a rule from the data map
     * @param dataMap
     * @return
     */
    private CustomRules getRuleFromMap(DataMap dataMap)
    {
        CustomRules rule =new CustomRules();

        rule.setId(dataMap.getLong(Const.KEY_RULE_ID));
        rule.setCategory(dataMap.getString(Const.KEY_RULE_CATEGORY));

        rule.setConstraint_1(dataMap.getString(Const.KEY_RULE_CONSTRAINT1));
        rule.setConstraint_2(dataMap.getString(Const.KEY_RULE_CONSTRAINT2));
        rule.setConstraint_3(dataMap.getString(Const.KEY_RULE_CONSTRAINT3));



        rule.setVal_1_min(formatValue( dataMap.getDouble(Const.KEY_RULE_VAL1_MIN)));
        rule.setVal_1_max(formatValue(dataMap.getDouble(Const.KEY_RULE_VAL1_MAX)));
        rule.setVal__2_min(formatValue(dataMap.getDouble(Const.KEY_RULE_VAL2_MIN)));
        rule.setVal_2_max(formatValue(dataMap.getDouble(Const.KEY_RULE_VAL2_MAX)));

        Log.d("received_RUle_category",rule.getCategory());
        Log.d("received_RUle_const1",rule.getConstraint_1());
        Log.d("received_RUle_const2",rule.getConstraint_2());
        Log.d("received_RUle_Val1_min",rule.getVal_1_min()+"");
        Log.d("received_RUle_Val1_max",rule.getVal_1_max()+"");
        Log.d("received_RUle_Val2_min",rule.getVal__2_min()+"");
        Log.d("received_RUle_Val2_max",rule.getVal_2_max()+"");


        return rule;

    }

    public Double formatValue(Double value)
    {
        return value==Const.NULL_IDENTIFIER ? null : value;
    }
}




