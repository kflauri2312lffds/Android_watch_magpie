package hevs.aislab.magpie.watch.watch_communication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * this class is used by activty to subscribe to a service (Listener service ) and make
 * the communication between the service and the activity
 */

public class MessageReceiver extends BroadcastReceiver {

    Activity activity;
    public MessageReceiver(Activity activity)
    {
        this.activity=activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //get the message
        Bundle bundle=intent.getBundleExtra(Const.BUNDLE_DATA);
        if (bundle==null)
            return;
        //get the string associed with the bundle
        String message=bundle.getString(Const.KEY_MESSAGE_TYPE);
        if (message==null)
            return;
        CustomToast.getInstance().confirmToast(message,activity);


    }
}
