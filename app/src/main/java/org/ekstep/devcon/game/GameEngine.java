package org.ekstep.devcon.game;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ekstep.devcon.BuildConfig;
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

    private static final String TAG = "GameEngine";

    private static GameEngine engine;

    private LinkedList<QuestionModel> questionList;
    private QuestionModel currentQuestion;

    private OnGameInitiatedListener mCallback;

    private GameEngine(Context context, OnGameInitiatedListener onGameInitiatedListener) {
        init(context, onGameInitiatedListener);
    }

    public static GameEngine getEngine() {
        if (engine == null) {
            throw new RuntimeException("call init() first");
        }
        return engine;
    }

    public static void initGame(Context context, OnGameInitiatedListener onGameInitiatedListener) {
        engine = new GameEngine(context, onGameInitiatedListener);
    }

    public boolean isCorrect(int answerId) {
        if (currentQuestion.getAnswer() == answerId) {
            currentQuestion = questionList.poll();

            if (currentQuestion == null) {
                mCallback.gameCompleted();
            } else {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "isCorrect: " + currentQuestion.hashCode());
                }

                mCallback.nextHint(currentQuestion.getHint());
            }

            return true;
        } else {
            return false;
        }
    }

    public QuestionModel verifyQR(String hashCode) throws GameException {
        if (String.valueOf(currentQuestion.hashCode()).equals(hashCode)) {
            mCallback.nextQuestion(currentQuestion);
            return currentQuestion;
        } else {
            throw new GameException("You have scanned a wrong QR code!! Please try with a valid QR code");
        }
    }

    public boolean isLastQuestion() {
        return questionList.isEmpty();
    }

    public boolean isWinner() {
        return isLastQuestion();
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
        Iterator<String> iterator = treasureMap.keySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            LinkedList<QuestionModel> list = treasureMap.get(iterator.next());
            if (index == r) return list;
            index++;
        }
        return null;
    }

    private void init(Context context, OnGameInitiatedListener onGameInitiatedListener) {
        try {
            mCallback = onGameInitiatedListener;
            Map<String, LinkedList<QuestionModel>> treasureMap = parseJson(context);
            questionList = getRandomQuestionList(treasureMap);
            currentQuestion = questionList.poll();
            mCallback.onGameInitiated();
            mCallback.nextQuestion(currentQuestion);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
