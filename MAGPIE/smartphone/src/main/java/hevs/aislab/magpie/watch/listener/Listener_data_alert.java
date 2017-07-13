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
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.repository.AlertesRepository;
import hevs.aislab.magpie.watch_library.lib.Const;


/**
 * Used to subscribe to data send by the watch. In this cas, it's alert data
 */
public class Listener_data_alert extends WearableListenerService {

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
                    Alertes alerte= extractAlertFromDatamap(aData);
                    AlertesRepository.getINSTANCE().insertOrReplace(alerte);
                }

                // Broadcast DataMap contents to wearable activity for display
                Bundle bundle=new Bundle();
                bundle.putString(Const.KEY_MESSAGE_TYPE,getString(R.string.sync_alertes));
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
     * @param dataMap data map that containt alert information
     * @return alert created via the datamap
     */
    private Alertes extractAlertFromDatamap(DataMap dataMap)
    {
        Alertes alerte=new Alertes();
        alerte.setId(dataMap.getLong(Const.KEY_ALERT_ID));
        alerte.setMeasure_id(dataMap.getLong(Const.KEY_MEASURE_ID));
        alerte.setRule_id(dataMap.getLong(Const.KEY_RULE_ID));
        return alerte;
    }

}
