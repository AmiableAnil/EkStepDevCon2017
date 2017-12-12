package org.ekstep.devcon.game.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Indraja Machani on 12/11/2017.
 */

public class Option implements Parcelable {
    private int id;
    private String optionText;

    public Option() {

    }

    protected Option(Parcel in) {
        id = in.readInt();
        optionText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(optionText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}
