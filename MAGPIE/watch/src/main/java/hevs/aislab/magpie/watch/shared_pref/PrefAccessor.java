package hevs.aislab.magpie.watch.shared_pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;


/**
 *
 *
 * This class is used to directly access to the shared pref.
 * You can save in the shared pref via the methode "save" and get data via the "get" methode
 */
public class PrefAccessor {

    private static PrefAccessor INSTANCE;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static PrefAccessor getInstance()
    {
        if (INSTANCE==null)
            INSTANCE=new PrefAccessor();
        return INSTANCE;
    }


    private void init(Context context)
    {
         preferences = PreferenceManager.getDefaultSharedPreferences(context);

    }


    //save data in char pref
    public void save(Context context, String key, long aLong)
    {
        init(context);
        editor=preferences.edit();
        editor.putLong(key,aLong);
        editor.apply();
    }

    public void save(Context context, String key, String aString)
    {
        init(context);
        editor=preferences.edit();
        editor.putString(key,aString);
        editor.apply();
    }

    public void save(Context context, String key, boolean b)
    {
        init(context);
        editor=preferences.edit();
        editor.putBoolean(key,b);
        editor.apply();
    }


    public long getLong(Context context, String key)
    {
        init(context);
       return  preferences.getLong(key,0);
    }

    public String getString( Context context, String key)
    {
        init(context);
       return preferences.getString(key,-1+"");
    }

    public boolean getBoolean( Context context,String key)
    {
        init(context);
        return preferences.getBoolean(key,false);
    }


}
