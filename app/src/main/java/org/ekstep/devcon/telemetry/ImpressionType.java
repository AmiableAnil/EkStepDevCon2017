package org.ekstep.devcon.telemetry;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Sneha on 11/22/2017.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({ImpressionType.LIST, ImpressionType.DETAIL, ImpressionType.VIEW,
        ImpressionType.EDIT, ImpressionType.WORKFLOW, ImpressionType.SEARCH})
public @interface ImpressionType {

    String LIST = "List";
    String DETAIL = "Detail";
    String VIEW = "View";
    String EDIT = "Edit";
    String WORKFLOW = "Workflow";
    String SEARCH = "Search";
}
