package hevs.aislab.magpie.watch.notification;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import hevs.aislab.magpie.watch.R;

/**
 * Created by teuft on 11.07.2017.
 */

 public class CustomToast {

    private static CustomToast INSTANCE;


    private CustomToast () {}

    public static CustomToast getInstance()
    {
        if (INSTANCE==null)
            INSTANCE=new CustomToast();
        return INSTANCE;
    }

    Toast toast;

    public void  confirmToast(final String text, final Activity activity)
    {
        View layout = initView(activity);

        //define the icon that will be displayed
        int icon= R.drawable.notif_success;
        //define the border and the background color that will be displayed
        Drawable border=activity.getResources().getDrawable(R.drawable.border_green);
        //sho the toast
        showToast(text, activity, layout, toast, icon,border);
    }
    public void warningToast(final String text, final Activity activity)
    {
        View layout = initView(activity);

        //define the icon that will be displayed
        int icon=R.drawable.notif_warning;
        //define the border and the background color that will be displayed
        Drawable border=activity.getResources().getDrawable(R.drawable.border_yellow);
        //sho the toast
        showToast(text, activity, layout, toast, icon,border);
    }

    public void errorTOast(final String text, final Activity activity)
    {
        View layout = initView(activity);

        //define the icon that will be displayed
        int icon=R.drawable.notif_error;
        //define the border and the background color that will be displayed
        Drawable border=activity.getResources().getDrawable(R.drawable.border_red);
        //sho the toast
        showToast(text, activity, layout, toast, icon,border);
    }

    private View initView(Activity activity) {
        //cancel the display of the toast (if a toast is displayed)
        if (toast != null)
            toast.cancel();

        View layout = getView(activity);
        createToast(activity, layout);

        return layout;
    }


    @NonNull
    private void createToast(Activity activity, View layout) {
        //Creating the Toast object
        toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout

    }

    private View getView(Activity activity) {
        //Creating the LayoutInflater instance
        LayoutInflater li = activity.getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        return li.inflate(R.layout.toast_confirm, (ViewGroup) activity.findViewById(R.id.toast_confirm));
    }

    private void showToast(final String text, final Activity activity, final View layout, final Toast toast, final int icon, final Drawable border) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.show();
                ((TextView) layout.findViewById(R.id.custom_toast_message)).setText(text);
                ImageButton imageButton=(ImageButton) layout.findViewById(R.id.img_button_toast);
                imageButton.setImageResource(icon);
                layout.setBackground(border);
            }
        });
    }




}
