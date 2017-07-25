package hevs.aislab.magpie.watch_library.lib;

/**
 * This class is used to store constants for the whole application
 */

public class Const {
    //warning: do not change the value of theses constants (category). Theses constantes are used to locate ressources. If you change it, you will have to change the name of some
    //ressources!
    public static final String CATEGORY_GLUCOSE="glucose";
    public static final String CATEGORY_PULSE="pulse";
    public static final String CATEGORY_WEIGHT="weight";
    public static final String CATEGORY_PRESSURE="pressure";
    public static final String CATEGORY_STEP="step";

    public static final String CATEGORY_SYSTOL="systol";
    public static final String CATEGORY_DIASTOL="diastol";


    //VALUE
    public static final String VALUE_Value_1Min="Value_1Min";
    public static final String VALUE_Value_1Max="Value_1Max";
    public static final String VALUE_Value_2Min="Value_2Min";
    public static final String VALUE_Value_2Max="Value_2Max";

    //KEY FOR THE SHARED PREF
    public static final String KEY_CURRENT_STEP="steps_counter";
    public static final String KEY_DATE_STEP="day_step_date";

    //store the path used to communicate with the watch. WARNING: DO NOT CHANGE THIS VALUE. OR IF YOU CHANGE IT, CHANGE IT
    //ALSO IN THE MANIFEST
    public static final String PATH_PUSH_MEASURE ="/push_data_measure";
    public static final String PATH_PUSH_RULE ="/push_data_rule";
    public static final String PATH_PUSH_ALERT="/push_data_alert";

    public static final String PATH_SYNC_RULE="/sync_rules";

    //KEY TO IDENTIFY THE DATA SEND
    public static final String KEY_MEASURE_DATA="measure_data";
    public static final String KEY_CURRENTTIMESTAMP="current_timestamp";
    public static final String KEY_MEASURE_ID="measure_id";
    public static final String KEY_MEASURE_CATEGORY="measure_category";
    public static final String KEY_MEASURE_TIMESTAMP="measure_timestamp";
    public static final String KEY_MEASURE_VALUE1="measure_value1";
    public static final String KEY_MEASURE_VALUE2="measure_value2";


    //RULES KEY
    public static final String KEY_RULE_ID="Rules_id";
    public static final String KEY_RULE_CATEGORY="rules_category";
    public static final String KEY_RULE_CONSTRAINT1="rules_constraint1";
    public static final String KEY_RULE_CONSTRAINT2="rules_constraint2";
    public static final String KEY_RULE_CONSTRAINT3="rules_Constraint3";
    public static final String KEY_RULE_VAL1_MIN="rule_val1_min";
    public static final String KEY_RULE_VAL1_MAX="rule_val1_max";
    public static final String KEY_RULE_VAL2_MIN="rule_val2_min";
    public static final String KEY_RULE_VAL2_MAX="rule_val2_max";

    //ALERTES KEY
    public static final String KEY_ALERT_ID="alert_id";

    //broadcast manager key
    public static final String KEY_BROADCASTdATA ="RULE_BROADCAST_DATA";
    public static final String TYPE_RULE="type_rule";


    //FLOAT THAT WILL BE IDENTFIED AS A NULL VALUE. Used when we send information from the watch to the phone
    public static final double NULL_IDENTIFIER=-1000000;

    //USED TO SEND INFORMATION THOUTH BUNDLE
    public static final String KEY_MESSAGE_TYPE="message_type";
    public static final String BUNDLE_DATA="data";




}
