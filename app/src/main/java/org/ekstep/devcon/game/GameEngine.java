package org.ekstep.devcon.game;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ekstep.devcon.game.models.QuestionModel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Created by souvikmondal on 12/12/17.
 */

public class GameEngine {

    private static GameEngine engine;

    private LinkedList<QuestionModel> questionList;
    private QuestionModel currentQuestion;

    private GameEngine() {

    }

    public static final GameEngine getEngine() {
        if (engine == null) {
            engine = new GameEngine();
        }
        return engine;
    }

    public void init(Context context, OnGameInitiatedListener onGameInitiatedListener) {
        try {
            Map<String, LinkedList<QuestionModel>> treasureMap = parseJson(context);
            questionList = getRandomQuestionList(treasureMap);
            currentQuestion = questionList.poll();
            onGameInitiatedListener.onGameInitiated();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public QuestionModel getCurrentQuestion() {
        return currentQuestion;
    }

    public QuestionModel nextQuestion(int hashCode) throws GameException {
        if (currentQuestion.hashCode() == hashCode) {
            currentQuestion = questionList.poll();
            return currentQuestion;
        } else {
            throw new GameException("You have scanned a wrong QR code!! Please try with a valid QR code");
        }
    }

    private Map<String, LinkedList<QuestionModel>> parseJson(Context context) throws IOException {
        InputStreamReader reader = new InputStreamReader(
                context.getAssets().open("treasurehunt.json"));
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<Map<String, LinkedList<QuestionModel>>>() {
        }.getType();
        return gson.fromJson(reader, type);
    }

    private LinkedList<QuestionModel> getRandomQuestionList(Map<String, LinkedList<QuestionModel>> treasureMap) {
        int r = new Random().nextInt(treasureMap.size());
        Iterator iterator = treasureMap.keySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            LinkedList<QuestionModel> list = treasureMap.get(iterator.next());
            if (index == r) return list;
            index ++;
        }
        return null;
    }
}
