package org.ekstep.devcon;

import android.app.Application;

import org.ekstep.devcon.telemetry.TelemetryOperation;
import org.ekstep.devcon.util.Constant;
import org.ekstep.devcon.util.PreferenceUtil;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.async.GenieAsyncService;
import org.ekstep.genieservices.async.SyncService;

import static org.ekstep.genieservices.GenieService.getAsyncService;

/**
 * @author vinayagasundar
 */

public class EkstepDevConApp extends Application {

    public static GenieService getGenieSdkInstance() {
        return GenieService.getService();
    }

    public static GenieAsyncService getGenieAsyncService() {
        return getAsyncService();
    }

    public static SyncService getSyncService() {
        return getAsyncService().getSyncService();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GenieService.init(this, "org.ekstep.devcon");
        PreferenceUtil.init(this, Constant.PREFS_EKSTEP_DEVCONAPP );
        TelemetryOperation.startSyncingTelemetry();
    }
}
