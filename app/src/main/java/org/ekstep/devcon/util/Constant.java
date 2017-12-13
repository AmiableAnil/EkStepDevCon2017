package org.ekstep.devcon.util;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public interface Constant {
    String PREFS_EKSTEP_DEVCONAPP = "prefs_ekstep_devconapp";
    String KEY_SET_VALUE = "key_set";
    String BUNDLE_KEY_SCREEN_NUM = "screen_number";
    String BUNDLE_KEY_STALL_NAME = "stall_name";


    //telemetry constants
    String GAME_FIRST_LAUNCH_TIME = "game_first_launch_time";
    String TREASURE_FIRST_LAUNCH_TIME = "treasure_first_launch_time";
    String FILE_SIZE = "SizeOfFileInKB";
    String SYNC_INTERVAL = "syncInterval";
    String SYNC_MODE = "mode";
    String SYNC_MODE_FORCED = "forced";
    String SYNC_TYPE_DEFAULT = "default";
    String DEFAULT_SYNC_TYPE_AND_INTERVAL = "{\"default\":{\"syncInterval\":5, \"mode\":\"auto\"},\"0\":{\"syncInterval\":5, \"mode\":\"forced\"}}";
    String QUESTION = "que";
    String EXPECTED_ANSWER = "expectedAnswer";
    String GIVEN_ANSWER = "givenAnswer";
}
