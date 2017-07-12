package hevs.aislab.magpie.watch_library.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by teuft on 09.07.2017.
 */

public class ButtonsManager {

    HashMap<String, ImageButton>buttonHashMap;
    Context context;
    public ButtonsManager(Context context)
    {
        this.context=context;
        buttonHashMap=new HashMap<>();
    }


    public ButtonsManager(Context context, HashMap<String, ImageButton> buttonHashMap) {
        this.context=context;
        this.buttonHashMap = buttonHashMap;
    }

    public HashMap<String, ImageButton> getButtonHashMap() {
        return buttonHashMap;
    }

    public void setButtonHashMap(HashMap<String, ImageButton> buttonHashMap) {
        this.buttonHashMap = buttonHashMap;
    }

    /**
     * set the button to red. Based on it's category
     */
    public void setButtonToRed(String category)
    {

        int id = context.getResources().getIdentifier(category+"_red", "drawable", context.getPackageName());
        Drawable img= ContextCompat.getDrawable(context, id);
        buttonHashMap.get(category).setImageDrawable(img);
    }

    /**
     * set all the button to  green
     */
    public void setAllButtonToGreen()
    {
        for (Map.Entry<String, ImageButton> entry : buttonHashMap.entrySet())
        {
            int id = context.getResources().getIdentifier(entry.getKey()+"_green", "drawable", context.getPackageName());
            Drawable img= ContextCompat.getDrawable(context, id);
            entry.getValue().setImageDrawable(img);
        }
    }

    public ImageButton getButtonByCategory(String category)
    {
        return buttonHashMap.get(category);
    }
    public void addButtonByCategory(String category,ImageButton imageButton)
    {
        buttonHashMap.put(category,imageButton);
    }




}
