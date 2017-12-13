package org.ekstep.devcon.telemetry;

import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.telemetry.Impression;
import org.ekstep.genieservices.commons.bean.telemetry.Interact;
import org.ekstep.genieservices.commons.bean.telemetry.Log;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.bean.telemetry.Visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 12/13/2017.
 */

public class TelemetryBuilder {

    public static Log buildLogEvent(String pageId, String type, String message, Map<String, Object> params) {
        Log.Builder log = new Log.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            log.addParam(entry.getKey(), entry.getValue()).pageId(pageId).type(type).level(Log.Level.INFO).message(message);
        }
        return log.build();
    }

    public static Interact buildInteractEvent(InteractionType type, String subType, String pageId, String resourceId) {
        Interact interact = new Interact.Builder().interactionType(type).subType(subType).pageId(pageId).resourceId(resourceId).build();
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
