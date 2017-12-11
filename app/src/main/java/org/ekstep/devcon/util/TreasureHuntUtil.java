package org.ekstep.devcon.util;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import org.ekstep.devcon.game.models.QuestionModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public class TreasureHuntUtil {
    private static LinkedList<QuestionModel> sQuestionModelList;


    public static void init(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("treasurehunt.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Type type = new TypeToken<Map<String, LinkedList<QuestionModel>>>() {
             }.getType();
            Map<String, LinkedList<QuestionModel>> treasureMap = GsonUtil.fromJson(json,
                    type);

            String key = PreferenceUtil.getInstance().getStringValue(Constant.KEY_SET, null);
            if (key == null) {
                Set<String> keys = treasureMap.keySet();
                int index = new Random().nextInt(keys.size());
                key = keys.toArray(new String[]{})[index];
            }

            // add set key in prefs
            PreferenceUtil.getInstance().setStringValue(Constant.KEY_SET, key);
            sQuestionModelList = treasureMap.get(key);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static QuestionModel getQuestion() {
        return sQuestionModelList.poll();
    }
}
