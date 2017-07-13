package hevs.aislab.magpie.watch_library.lib;

/**
 * Used to validate number
 */

public class Validator {

    //define the allowed value range for each category


    //PRESSURE VALUE
    private static double minSystol=50;
    private static double maxSystol=200;
    private static double minDiastol=20;
    private static double maxDiastol=150;

    //GLUCOSE VALUE
    private static double minGlucose=1;
    private static double maxGlucose=30;

    //WEIGHT VALUE
    private static double minWeight=10;
    private static double maxWeight=300;

    //pulse value
    private static double minPulse=10;
    private static double maxPulse=220;

    //steps
    private static double minStep=0;
    private static double maxStep=20000;



    public static boolean isRuleValueValide(String category, Double ... value)
    {
        //if null, it's means the value is empty so we allow
        if (value==null)
            return true;
       return isEntryValueValide(category,value);
    }

    // USED TO VALIDATE THE VALUES ENTERED BY THE USER AND THE RULES
    public static boolean isEntryValueValide(String category, Double ... value )
    {
        switch (category)
        {
            case Const.CATEGORY_DIASTOL :
                return isValide(value[0],minDiastol,maxDiastol);

            case Const.CATEGORY_SYSTOL :
                return isValide(value[0],minSystol,maxSystol);

            case Const.CATEGORY_WEIGHT :
                return isValide(value[0],minWeight,maxWeight);

            case Const.CATEGORY_GLUCOSE :
                return isValide(value[0],minGlucose,maxGlucose);

            case Const.CATEGORY_PRESSURE :
                return isValide(value[0],minSystol,maxSystol) && isValide(value[1],minDiastol,maxDiastol);

            case Const.CATEGORY_STEP :
                   return isValide(value[0],minStep,maxStep);

            case Const.CATEGORY_PULSE :
                return isValide(value[0],minPulse,maxPulse);
            default:
                return true;
        }
    }

    private static boolean isValide(double value, double minValue, double maxValue)
    {
        return !(value<minValue || value>maxValue);
    }
}
