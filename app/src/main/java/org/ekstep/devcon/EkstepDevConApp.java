package org.ekstep.devcon;

import android.app.Application;

import org.ekstep.devcon.util.Constant;
import org.ekstep.devcon.util.PreferenceUtil;
import org.ekstep.genieservices.GenieService;

/**
 * @author vinayagasundar
 */

public class EkstepDevConApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GenieService.init(this, "org.ekstep.devcon");
        PreferenceUtil.init(this, Constant.PREFS_EKSTEP_DEVCONAPP );
    }
}
