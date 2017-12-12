package org.ekstep.devcon.game;

import org.ekstep.devcon.game.models.QuestionModel;

/**
 * Created by souvikmondal on 11/12/17.
 */

public interface OnGameInitiatedListener {

    void onGameInitiated();

    void nextHint(String hint);

    void nextQuestion(QuestionModel questionModel);

    void gameCompleted();

    void timeFinished();

    void timeLapse(long timeRemainingInSeconds);

}
