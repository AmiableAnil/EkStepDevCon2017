package org.ekstep.devcon.game;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
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

    public static final long GAME_TIME = 15 * 60 * 1000; // 10 minutes

    private static final String TAG = "GameEngine";

    private static GameEngine engine;

    private LinkedList<QuestionModel> questionList;
    private QuestionModel currentQuestion;

    private OnGameInitiatedListener mCallback;

    private boolean timeOver;

    private GameEngine(Context context, OnGameInitiatedListener onGameInitiatedListener) {
        init(context, onGameInitiatedListener);
        initGameTimer();
    }

    public static GameEngine getEngine() {
        if (engine == null) {
            throw new RuntimeException("call init() first");
        }
        return engine;
    }

    private void initGameTimer() {
        new CountDownTimer(GAME_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
                mCallback.timeLapse(millisUntilFinished / 1000);
            }

            public void onFinish() {
                mCallback.timeFinished();
                timeOver = true;
            }

        }.start();
    }

    public static void initGame(Context context, OnGameInitiatedListener onGameInitiatedListener) throws GameException {
        engine = new GameEngine(context, onGameInitiatedListener);
        if (engine.timeOver) throw new GameException("Time over!! You can't play the game again!!");
    }

    public boolean isCorrect(String answer) {
        if (currentQuestion.getAnswer().toLowerCase().trim().contains(answer.toLowerCase())) {
            currentQuestion = questionList.poll();

            if (currentQuestion == null) {
                mCallback.gameCompleted();
            } else {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "isCorrect: " + currentQuestion.hashCode());
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.nextHint(currentQuestion.getHint());
                    }
                }, 3000);
            }

            return true;
        } else {
            return false;
        }
    }

    public QuestionModel verifyQR(String hashCode) throws GameException {
        if (String.valueOf(currentQuestion.getQuestion()).equals(hashCode)) {
            mCallback.nextQuestion(currentQuestion);
            return currentQuestion;
        } else {
            throw new GameException("You have scanned a wrong QR code!! Please try with a valid QR code");
        }
    }

    public boolean isLastQuestion() {
        return questionList.isEmpty();
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
