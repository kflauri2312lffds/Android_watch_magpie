package hevs.aislab.magpie.smartphone.listener;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import hevs.aislab.magpie.smartphone.lib.Const;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by teuft on 07.07.2017.
 */

public class Listener_data extends WearableListenerService {

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
                if (path.equals(Const.PATH_PUSH_DELETE_DATA)) {}
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();


                // Broadcast DataMap contents to wearable activity for display
                // The content has the golf hole number and distances to the front,
                // middle and back pin placements.

                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("datamap", dataMap.toBundle());


                Log.d("dataMapReceiveItem",dataMap.getString(Const.PATH_PUSH_DELETE_DATA));


                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

            }
        }
    }

}

