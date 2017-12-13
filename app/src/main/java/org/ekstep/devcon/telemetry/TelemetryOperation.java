package org.ekstep.devcon.telemetry;

import android.util.Log;

import org.ekstep.devcon.EkstepDevConApp;
import org.ekstep.devcon.util.Constant;
import org.ekstep.devcon.util.PreferenceUtil;
import org.ekstep.genieservices.async.TelemetryService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SyncStat;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sneha.
 */

public class TelemetryOperation {
    private static final String TAG = TelemetryOperation.class.getSimpleName();
    private static boolean mIsSyncInProgress = false;
    private static String mSyncType = "auto";
    private static long mSyncInterval;
    private static ScheduledExecutorService mExecutor;
    private static int mInitialDelay = 2;

    private static HashMap<String, Object> getFileSizeMap(SyncStat syncStat) {
        HashMap<String, Object> map = new HashMap<>();
        if (syncStat != null && syncStat.getSyncedFileSize() != null) {
            map.put(Constant.FILE_SIZE, syncStat.getSyncedFileSize());
        }
        return map;
    }

    /**
     * This method starts syncing telemetry with the specified time
     * <p>
     * Default Sync time is 30secs
     * Default Sync mode is auto
     */
    public static void startSyncingTelemetry() {
        shutDownSchedulers();
        mExecutor = Executors.newScheduledThreadPool(1);

        setSyncTypeAndInterval();
        mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!mIsSyncInProgress) {
                    syncBasedOnNumberOfUnsyncedEvents();
                }
            }
        }, getInitialDelay(), mSyncInterval, TimeUnit.SECONDS);
    }

    private static void setSyncTypeAndInterval() {
        final String diffInDays;
        Map<String, Map<String, String>> eventMap;
        String telemetrySyncInterval = getTelemetrySyncInterval();

        if (PreferenceUtil.getGameFirstLaunchTime() == -1) {
            diffInDays = "0";
        } else {
            diffInDays = String.valueOf(DateUtil.getTimeDifferenceInDays(PreferenceUtil.getGameFirstLaunchTime(), System.currentTimeMillis()));
        }

        // On day-0 the sync type will be always forced with sync interval set to 30 seconds
        try {
            eventMap = GsonUtil.fromJson(telemetrySyncInterval, Map.class);
            if (eventMap.containsKey(diffInDays)) {
                mSyncInterval = (long) Double.parseDouble(String.valueOf(eventMap.get(diffInDays).get(Constant.SYNC_INTERVAL)));
                mSyncType = eventMap.get(diffInDays).get(Constant.SYNC_MODE);
            } else {
                mSyncInterval = (long) Double.parseDouble(String.valueOf(eventMap.get(Constant.SYNC_TYPE_DEFAULT).get(Constant.SYNC_INTERVAL)));
                mSyncType = eventMap.get(Constant.SYNC_TYPE_DEFAULT).get(Constant.SYNC_MODE);
            }
        } catch (Exception e) {
            eventMap = GsonUtil.fromJson(Constant.DEFAULT_SYNC_TYPE_AND_INTERVAL, Map.class);
            mSyncInterval = (long) Double.parseDouble(String.valueOf(eventMap.get(Constant.SYNC_TYPE_DEFAULT).get(Constant.SYNC_INTERVAL)));
            mSyncType = eventMap.get(Constant.SYNC_TYPE_DEFAULT).get(Constant.SYNC_MODE);
        }
    }

    /**
     * This method checks the number of unsynced events and only if the unsynced events are more than 3 from last synct time,
     * then it syncs
     */
    private static void syncBasedOnNumberOfUnsyncedEvents() {
        final TelemetryService telemetryService = EkstepDevConApp.getGenieSdkInstance().getAsyncService().getTelemetryService();
        telemetryService.getTelemetryStat(new IResponseHandler<TelemetryStat>() {
            @Override
            public void onSuccess(GenieResponse<TelemetryStat> genieResponse) {
                TelemetryStat telemetryStat = genieResponse.getResult();
                if (telemetryStat != null) {
                    if (telemetryStat.getUnSyncedEventCount() >= 3) {
                        Log.e(TAG + "@startSyncingTelemetry", "Called!");
                        mIsSyncInProgress = true;
                        if (Constant.SYNC_MODE_FORCED.equalsIgnoreCase(mSyncType)) {
                            manualSync();
                        } else {
                            autoSync("TreasureHunt-TelemetrySync");
                        }
                    }
                }
            }

            @Override
            public void onError(GenieResponse<TelemetryStat> genieResponse) {

            }
        });
    }

    private static void autoSync(String stageId) {
        if (canSync(EkstepDevConApp.getGenieSdkInstance().getConnectionInfo())) {

            TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(InteractionType.TOUCH, "AutoSync-Initiated", stageId));
            EkstepDevConApp.getSyncService().sync(new IResponseHandler<SyncStat>() {
                @Override
                public void onSuccess(GenieResponse<SyncStat> genieResponse) {
                    mIsSyncInProgress = false;

                    TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(InteractionType.OTHER, "AutoSync-Success", "TreasureHunt-TelemetrySync", getFileSizeMap(genieResponse.getResult())));
                }

                @Override
                public void onError(GenieResponse<SyncStat> genieResponse) {
                    mIsSyncInProgress = false;
                }
            });
        }
    }

    private static void manualSync() {

        TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(InteractionType.TOUCH, "ManualSync-Initiated", "TreasureHunt-TelemetrySync"));
        EkstepDevConApp.getGenieAsyncService().getSyncService().sync(new IResponseHandler<SyncStat>() {
            @Override
            public void onSuccess(GenieResponse<SyncStat> genieResponse) {
                mIsSyncInProgress = false;

                TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(InteractionType.OTHER, "ManualSync-Success", "TreasureHunt-TelemetrySync", getFileSizeMap(genieResponse.getResult())));
            }

            @Override
            public void onError(GenieResponse<SyncStat> genieResponse) {
                mIsSyncInProgress = false;
            }
        });
    }

    /**
     * This method shuts down all the threads
     */
    public static void shutDownSchedulers() {
        if (mExecutor != null) {
            mExecutor.shutdown();
        }
    }

    public static int getInitialDelay() {
        return mInitialDelay;
    }

    public static void setInitialDelay(int delay) {
        mInitialDelay = delay;
    }

    /**
     * @return telemetrySyncInterval json string
     */
    public static String getTelemetrySyncInterval() {
        return Constant.DEFAULT_SYNC_TYPE_AND_INTERVAL;
    }

    public static boolean canSync(IConnectionInfo connectionInfo) {
        return connectionInfo.isConnected();
    }
}