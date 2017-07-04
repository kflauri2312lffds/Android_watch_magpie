package hevs.aislab.magpie.watch.gui;

/**
 * Created by teuft on 04.07.2017.
 */


/**
 * This interface will be used to make the link between the fragment and the activity
 */
    public interface IdialogToActivity
    {
        void sendValue(String category, String ... value);
    }