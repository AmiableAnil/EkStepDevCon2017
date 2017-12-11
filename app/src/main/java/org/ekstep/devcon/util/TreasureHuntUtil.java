package org.ekstep.devcon.util;

import android.content.Context;

import org.ekstep.devcon.model.QuestionModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public class TreasureHuntUtil {
    private List<QuestionModel> questionModelList;

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("treasurehunt.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public Map<String, List<QuestionModel>> getTreasureMap(String jsonString) {
        Map<String, List<QuestionModel>> treasureMap = new LinkedHashMap<>();
        treasureMap = GsonUtil.fromJson(jsonString, (Type) treasureMap);
        return treasureMap;
    }
}
