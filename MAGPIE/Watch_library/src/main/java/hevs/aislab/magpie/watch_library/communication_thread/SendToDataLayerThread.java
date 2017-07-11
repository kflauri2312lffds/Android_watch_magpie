package hevs.aislab.magpie.watch_library.communication_thread;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by teuft on 07.07.2017.
 */


/**
 * This class will take care of the commnication between the watch on the phone. We will be able
 * to send information using this thread. First create a Datamap (containter to send object though the
 * data layer) send, create the the thread including the datatamap. This will be send directly to the other
 * device (phone if it's send from the watch, and watch if it's send from the phone
 */
public class SendToDataLayerThread extends Thread {
    String path;
    DataMap dataMap;
    GoogleApiClient googleClient;

    /**
     *
     * @param path: the identifiant used to send information
     * @param dataMap : the data we will send to the phone
     */
    public SendToDataLayerThread(GoogleApiClient googleClient, String path, DataMap dataMap)
    {
        this.path=path;
        this.dataMap=dataMap;
        this.googleClient=googleClient;
    }


    @Override
    public void run() {
        Log.d("RuleSync_Data","data send to the watch from the phone");
        // Construct a DataRequest and send over the data layer
        PutDataMapRequest putDMR = PutDataMapRequest.create(path);
        putDMR.getDataMap().putAll(dataMap);
        PutDataRequest request = putDMR.asPutDataRequest();
        DataApi.DataItemResult result = Wearable.DataApi.putDataItem(googleClient, request).await();
        if (result.getStatus().isSuccess()) {
            Log.v("myTag", "DataMap: " + dataMap + " sent successfully to data layer ");
        } else {
            // Log an error
            Log.v("myTag", "ERROR: failed to send DataMap to data layer");
        }

    }
}
