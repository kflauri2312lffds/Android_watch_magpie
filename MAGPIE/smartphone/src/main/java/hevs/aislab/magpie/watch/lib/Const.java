package hevs.aislab.magpie.watch.lib;

/**
 * Created by teuft on 07.07.2017.
 */

public class Const {
    //store the path used to communicate with the watch. WARNING: DO NOT CHANGE THIS VALUE. OR IF YOU CHANGE IT, CHANGE IT
    //ALSO IN THE MANIFEST
    public static final String PATH_PUSH_MEASURE ="/push_data_measure";
    public static final String PATH_PUSH_RULE ="/push_data_rule";


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


}
