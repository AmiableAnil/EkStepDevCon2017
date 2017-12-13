package org.ekstep.devcon.telemetry;

import android.util.Log;

import org.ekstep.devcon.EkstepDevConApp;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

/**
 *Created by Sneha
 */
public class TelemetryHandler {

    private static final String TAG = TelemetryHandler.class.getSimpleName();

    public static void saveTelemetry(Telemetry event, IResponseHandler handler) {
        EkstepDevConApp.getGenieSdkInstance().getAsyncService().getTelemetryService().saveTelemetry(event, handler);
    }

    public static void saveTelemetry(Telemetry event) {
        EkstepDevConApp.getGenieSdkInstance().getAsyncService().getTelemetryService().saveTelemetry(event, new IResponseHandler<Void>() {
            @Override
            public void onSuccess(GenieResponse<Void> genieResponse) {
                Log.i(TAG, "TelemetryEvent sent successfully");
            }

            @Override
            public void onError(GenieResponse<Void> genieResponse) {
                Log.e(TAG, "TelemetryEvent sending Failed");
            }
        });
    }

}
