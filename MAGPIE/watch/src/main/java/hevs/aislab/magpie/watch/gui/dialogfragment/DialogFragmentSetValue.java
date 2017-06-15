package hevs.aislab.magpie.watch.gui.dialogfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * this is the parent class to all Dialog Fragment to add values.
 */

public abstract class DialogFragmentSetValue extends DialogFragment {
    /**
     * This interface is designed to handle the communication between the fragment and the activity.
     * Each dialog Fragment can now call methode from activity  described in the Inteface
     */
    public interface IdialogToActivity
    {
         void sendValue(String category, String ... value);
    }
    protected View view;
    /**
     * This object will be able to call methode directly from activity, though the interface
     */
    protected IdialogToActivity homeActivity;

    abstract public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    abstract protected void createListenerSubmitButton(ImageButton submit);

    /**
     * This methode will create the ling between the activity and the fragment
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            homeActivity = (IdialogToActivity) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    /**
     * This methode have to be called inside the on Create view of the child
     * @param cancel : the cancel Image button, present in the layout
     */
    protected void createListenerCancelButton(ImageButton cancel)
    {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
