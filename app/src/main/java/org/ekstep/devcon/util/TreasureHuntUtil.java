package org.ekstep.devcon.util;

import android.content.Context;

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
            Map<String, LinkedList<QuestionModel>> treasureMap = GsonUtil.fromJson(json,
                    (Type) new LinkedList<>());

            // Add Preference check.

            Set<String> keys = treasureMap.keySet();
            int index = new Random().nextInt(keys.size());

            String key = keys.toArray(new String[]{})[index];

            sQuestionModelList = treasureMap.get(key);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getJSONFromAsset(Context context) {
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

    public static LinkedList<QuestionModel> getQuestions(String jsonString, String setKey) {
        Map<String, List<QuestionModel>> treasureMap = GsonUtil.fromJson(jsonString, (Type) new LinkedHashMap<>());
        return (LinkedList<QuestionModel>) treasureMap.get(setKey);
    }

    public Map<String, List<QuestionModel>> getTreasureMap(String jsonString) {
        Map<String, List<QuestionModel>> treasureMap = new LinkedHashMap<>();
        treasureMap = GsonUtil.fromJson(jsonString, (Type) treasureMap);
        return treasureMap;
    }

    public static LinkedList<QuestionModel> getRandomQuestionList(String jsonString) {
        HashMap<String, List<QuestionModel>> treasureMap = GsonUtil.fromJson(jsonString, (Type) new HashMap<>());
        List<LinkedList<QuestionModel>> valuesList = new ArrayList<>((Collection<?
                extends LinkedList<QuestionModel>>) treasureMap.values());
        int index = new Random().nextInt(valuesList.size());
        return valuesList.get(index);
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
