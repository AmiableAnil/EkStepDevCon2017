package org.ekstep.devcon.game;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ekstep.devcon.game.models.QuestionModel;
import org.ekstep.devcon.util.Constant;
import org.ekstep.devcon.util.PreferenceUtil;

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

    public static void initGame(Context context, OnGameInitiatedListener onGameInitiatedListener) throws GameException {
        engine = new GameEngine(context, onGameInitiatedListener);
        if (engine.timeOver) throw new GameException("Time over!! You can't play the game again!!");
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

    public boolean isCorrect(String answer) {
        String[] answerArray;

        if (currentQuestion.getAnswer().contains("/")) {
            answerArray = currentQuestion.getAnswer().split("/");
        } else {
            answerArray = new String[1];
            answerArray[0] = currentQuestion.getAnswer();
        }

        boolean isCorrect = false;

        for (String ans : answerArray) {
            if (ans.trim().equalsIgnoreCase(answer.trim())) {
                isCorrect = true;
                break;
            }
        }

        if (isCorrect) {
            currentQuestion = questionList.poll();

            if (currentQuestion == null) {
                mCallback.gameCompleted();
            } else {
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
            String key = iterator.next();
            LinkedList<QuestionModel> list = treasureMap.get(key);
            PreferenceUtil.getInstance().setStringValue(Constant.KEY_SET_VALUE, key);
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
