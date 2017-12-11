package org.ekstep.devcon.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.ekstep.devcon.model.QuestionModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public class TreasureHuntUtil {
    private static final String TAG = "TreasureHuntUtil";

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


            // TODO: 11/12/17 Remove code later
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LinkedList<QuestionModel> questionModels = sQuestionModelList;
                    for(QuestionModel questionModel : questionModels) {
                        Log.i(TAG, "run: " + questionModel.hashCode());
                    }
                }
            }).start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static QuestionModel getQuestion(int questionId) {
        for (QuestionModel question : sQuestionModelList) {
            if (question.hashCode() == questionId) {
                return question;
            }
        }
        return null;
    }
}
