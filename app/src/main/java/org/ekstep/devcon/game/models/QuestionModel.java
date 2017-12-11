package org.ekstep.devcon.game.models;

import java.util.List;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public class QuestionModel {
    private String question;
    private int answer;
    private String hint;
    private List<Option> options;

    @Override
    public int hashCode() {
        return question.hashCode();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
