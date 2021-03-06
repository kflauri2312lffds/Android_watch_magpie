package hevs.aislab.magpie.watch.listener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;


/**
 * Used to subscribe to data send by the watch. In this cas, it's measure data
 */

public class Listener_data_measure extends WearableListenerService {

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
                ArrayList<DataMap>containerList=dataMap.getDataMapArrayList(Const.KEY_MEASURE_DATA);

                //extract data and save each value
                for (DataMap aData : containerList)
                {
                    Measure measure=extractMeasureFromDataMap(aData);
                    MeasuresRepository.getInstance().insertOrReplace(measure);
                }

                // Broadcast DataMap contents to wearable activity for display

                //Prepare data to send to the activity
                Bundle bundle=new Bundle();
                bundle.putString(Const.KEY_MESSAGE_TYPE,getString(R.string.sync_measures));
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
     * @param dataMap data map filled with measure information
     * @return the measure object created via the datamap
     */
    private Measure extractMeasureFromDataMap(DataMap dataMap)
    {
        Measure measure=new Measure();
        measure.setId(dataMap.getLong(Const.KEY_MEASURE_ID));
        measure.setCategory(dataMap.getString(Const.KEY_MEASURE_CATEGORY));
        measure.setTimeStamp(dataMap.getLong(Const.KEY_MEASURE_TIMESTAMP));
        measure.setValue1(dataMap.getDouble(Const.KEY_MEASURE_VALUE1));
        Double measure2=null;
        //the 10000 is identifed as a null value
        if (dataMap.getDouble(Const.KEY_MEASURE_VALUE2)!=Const.NULL_IDENTIFIER)
            measure2=dataMap.getDouble(Const.KEY_MEASURE_VALUE2);
        measure.setValue2(measure2);
        return measure;
    }

}

