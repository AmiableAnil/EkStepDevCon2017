package org.ekstep.devcon.game.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public class QuestionModel implements Parcelable {
    private String question;
    private int answer;
    private String hint;
    private List<Option> options;

    public QuestionModel() {

    }

    protected QuestionModel(Parcel in) {
        question = in.readString();
        answer = in.readInt();
        hint = in.readString();
        options = in.createTypedArrayList(Option.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeInt(answer);
        dest.writeString(hint);
        dest.writeTypedList(options);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };

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
