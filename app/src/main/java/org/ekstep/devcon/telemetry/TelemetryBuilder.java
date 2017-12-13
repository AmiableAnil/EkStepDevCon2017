package org.ekstep.devcon.telemetry;

import android.content.Context;
import android.text.TextUtils;

import org.ekstep.devcon.customview.Utils;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.ILocationInfo;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.telemetry.DeviceSpecification;
import org.ekstep.genieservices.commons.bean.telemetry.End;
import org.ekstep.genieservices.commons.bean.telemetry.Impression;
import org.ekstep.genieservices.commons.bean.telemetry.Interact;
import org.ekstep.genieservices.commons.bean.telemetry.Start;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.bean.telemetry.Visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 12/13/2017.
 */

public class TelemetryBuilder {

    /**
     * Start event
     *
     * @param context
     * @return
     */
    public static Start buildStartEvent(Context context) {

        DeviceSpecification deviceSpec = new DeviceSpecification();
        deviceSpec.setOs("Android " + org.ekstep.genieservices.utils.DeviceSpec.getOSVersion());
        deviceSpec.setMake(org.ekstep.genieservices.utils.DeviceSpec.getDeviceName());
        deviceSpec.setId(org.ekstep.genieservices.utils.DeviceSpec.getAndroidId(context));

        String internalMemory = Utils.bytesToHuman(org.ekstep.genieservices.utils.DeviceSpec.getTotalInternalMemorySize());
        if (!TextUtils.isEmpty(internalMemory)) {
            deviceSpec.setIdisk(Double.valueOf(internalMemory));
        }

        String externalMemory = Utils.bytesToHuman(org.ekstep.genieservices.utils.DeviceSpec.getTotalExternalMemorySize());
        if (!TextUtils.isEmpty(externalMemory)) {
            deviceSpec.setEdisk(Double.valueOf(externalMemory));
        }

        String screenSize = org.ekstep.genieservices.utils.DeviceSpec.getScreenInfoinInch(context);
        if (!TextUtils.isEmpty(screenSize)) {
            deviceSpec.setScrn(Double.valueOf(screenSize));
        }

        String[] cameraInfo = org.ekstep.genieservices.utils.DeviceSpec.getCameraInfo(context);
        String camera = "";
        if (cameraInfo != null) {
            camera = TextUtils.join(",", cameraInfo);
        }
        deviceSpec.setCamera(camera);

        deviceSpec.setCpu(org.ekstep.genieservices.utils.DeviceSpec.getCpuInfo());
        deviceSpec.setSims(-1);

        ILocationInfo locationInfo = GenieService.getService().getLocationInfo();

        Start start = new Start.Builder()
                .deviceSpec(deviceSpec)
                .loc(locationInfo.getLocation())
                .pageid("Splash")
                .type("app")
                .build();

        return start;
    }

    /**
     * End event.
     *
     * @return
     */
    public static End buildEndEvent() {
        long timeInSeconds = 0;
        String genieStartTime = "" + System.currentTimeMillis();

        if (!TextUtils.isEmpty(genieStartTime)) {
            long timeDifference = System.currentTimeMillis() - Long.valueOf(genieStartTime);
            timeInSeconds = (timeDifference / 1000);
        }

        End end = new End.Builder()
                .duration(timeInSeconds)
                .pageid("Genie-Home")
                .type("app")
                .build();

        return end;
    }

    public static Interact buildInteractEvent(InteractionType type, String subType, String pageId, String resourceId) {
        Interact interact = new Interact.Builder().interactionType(type).subType(subType).pageId(pageId).resourceId(resourceId).build();
        return interact;
    }

    public static Telemetry buildInteractEvent(InteractionType type, String subType, String pageId, Map<String, Object> value, String id) {
        List valuesList = new ArrayList();
        valuesList.add(value);
        Interact interact = new Interact.Builder().interactionType(type).subType(subType).pageId(pageId).resourceId(id).values(valuesList).build();
        return interact;
    }

    public static Telemetry buildInteractEvent(InteractionType type, String subType, String pageId, String id, Map<String, String> value) {
        List valuesList = new ArrayList();
        valuesList.add(value);
        Interact interact = new Interact.Builder().interactionType(type).subType(subType).pageId(pageId).resourceId(id).values(valuesList).build();
        return interact;
    }

    public static Telemetry buildImpressionEvent(String pageId, String type, String subType) {
        Impression impression = new Impression.Builder().pageId(pageId).type(type).subType(subType).build();
        return impression;
    }

    public static Telemetry buildImpressionEvent(String pageId, String type, String subType, String id, String objType) {
        Visit visit = new Visit(id, type);
        Impression impression = new Impression.Builder().pageId(pageId).type(type).subType(subType).addVisit(visit).build();
        return impression;
    }
}
