package org.ekstep.devcon.model;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public class QuestionModel {
    private String id;
    private String question;
    private String answer;
    private String hint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

}
