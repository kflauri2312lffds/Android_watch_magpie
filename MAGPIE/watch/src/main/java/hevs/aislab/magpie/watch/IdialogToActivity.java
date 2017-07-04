package hevs.aislab.magpie.watch;

/**
 * Created by teuft on 04.07.2017.
 */


import ch.hevs.aislab.magpie.support.Rule;
import hevs.aislab.magpie.watch.gui.FragmentDisplayAlertes;
import hevs.aislab.magpie.watch.gui.FragmentHome;
import hevs.aislab.magpie.watch.gui.FragmentSettings;
import hevs.aislab.magpie.watch.models.CustomRules;

/**
 * This interface will be used to make the link between the fragment and the activity
 */
    public interface IdialogToActivity
    {
        void sendValue(String category, String ... value);

        /**
         * used to order to the fragment that the display are should be updated
         * @param rules
         */
        void updateBarArea(CustomRules rules);
    }