package hevs.aislab.magpie.watch_library.lib;

/**
 * Used to format number and display the correct digit number based on the category
 */

public class NumberFormater  {


  private static NumberFormater INSTANCE;


    private NumberFormater (){}



    public static NumberFormater getInstance()
    {
        if (INSTANCE==null)
            INSTANCE=new NumberFormater();
        return INSTANCE;
    }
    public String formatWith1Digit(double number)
    {
        String format="%(.1f";
        return String.format( format,number);


    }
    public String formatWithNoDigit(double number)
    {
        String format="%(.0f";
        return String.format( format,number);
    }

    public String formatWith1Digit(String  number)
    {
        try
        {
            double numberDouble=Double.parseDouble(number);
            return formatWith1Digit(numberDouble);
        }
        catch (Exception ex)
        {
            return "";
        }
    }
    public String formatWithNoDigit(String number)
    {
        try
        {
            double numberDouble=Double.parseDouble(number);
            return formatWithNoDigit(numberDouble);
        }
        catch (Exception ex)
        {
            return "";
        }
    }



}
