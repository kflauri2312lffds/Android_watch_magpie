package hevs.aislab.magpie.watch.listener;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import hevs.aislab.magpie.watch.lib.Const;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

/**
 * Created by teuft on 07.07.2017.
 */

public class Listener_data_measure extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("dataReceiveMA","fsda");
        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            Log.v("myTag", "DataMap received on watch: " + DataMapItem.fromDataItem(event.getDataItem()).getDataMap());
            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
//                if (path.equals(Const.PATH_PUSH_MEASURE)) {}
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                //get the data array list
                ArrayList<DataMap>containerList=dataMap.getDataMapArrayList(Const.KEY_MEASURE_DATA);
                Log.d("size_of_data_recive",containerList.size()+"");

                //extract data and save each value
                for (DataMap aData : containerList)
                {
                    Measure measure=extractMeasureFromDataMap(aData);
                    MeasuresRepository.getInstance().insertOrReplace(measure);
                }
                Log.d("sizeofDBMeasure=",MeasuresRepository.getInstance().getAll().size()+"");



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

    private Measure extractMeasureFromDataMap(DataMap dataMap)
    {
        Measure measure=new Measure();
        measure.setId(dataMap.getLong(Const.KEY_MEASURE_ID));
        measure.setCategory(dataMap.getString(Const.KEY_MEASURE_CATEGORY));
        measure.setTimeStamp(dataMap.getLong(Const.KEY_MEASURE_TIMESTAMP));
        measure.setValue1(dataMap.getDouble(Const.KEY_MEASURE_VALUE1));
        Double measure2=null;
        if (dataMap.getDouble(Const.KEY_MEASURE_VALUE2)!=10000.0)
            measure2=dataMap.getDouble(Const.KEY_MEASURE_VALUE2);
        measure.setValue2(measure2);
        return measure;
    }

}
