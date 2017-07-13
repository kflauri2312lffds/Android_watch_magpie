package hevs.aislab.magpie.watch_library.lib;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * This class will be used to manage the date format based on time stamp
 */
public class DateFormater {

    private static DateFormater INSTANCE;

    private DateFormater() {
    }

    public static DateFormater getInstance() {
        if (INSTANCE == null)
            INSTANCE = new DateFormater();
        return INSTANCE;
    }

    /**
     * get the date time in the following format
     * @param timeStamp
     * @return
     */
    public String getDateTime(long timeStamp){
        timeStamp/=1000;
        String dateFormat="dd-MM-yy HH:mm";
        return getDate(timeStamp,dateFormat);
    }

    public String getDateByFormat(long timeStamp,String dateformat)
    {
        timeStamp/=1000;
        return getDate(timeStamp,dateformat);
    }

    /**
     * Get the current date in the following format: dd_MMM_YY
     * @param timeStamp
     * @return
     */
    public String getDate(long timeStamp)
    {
        timeStamp/=1000;
        String dateFormat="dd-MMM-yy";
        return getDate(timeStamp,dateFormat);
    }

    public String getDate_DDMM(long timeStamp)
    {
        timeStamp/=1000;
        String dateFormat="d-M";
        return getDate(timeStamp,dateFormat);
    }

    private String getDate(long timeStamp, String dateFormat)
    {

        DateFormat objFormatter = new SimpleDateFormat(dateFormat);
        String timezone= Calendar.getInstance().getTimeZone().getDefault().getDisplayName();
        objFormatter.setTimeZone(TimeZone.getTimeZone(timezone));

        Calendar objCalendar =
                Calendar.getInstance(TimeZone.getTimeZone(timezone));
        objCalendar.setTimeInMillis(timeStamp*1000);
        String formatedDate= objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return formatedDate;
    }


}
