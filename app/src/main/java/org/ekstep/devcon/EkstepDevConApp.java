package org.ekstep.devcon;

import android.app.Application;

import org.ekstep.devcon.util.TreasureHuntUtil;

/**
 * @author vinayagasundar
 */

public class EkstepDevConApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TreasureHuntUtil.init(this);
    }
}
