package com.example.tanishqsaluja.beep.Receiver;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by ansh on 8/2/18.
 */

public class MessageCustomStructure implements Parcelable{
    private String sender,message;

    public MessageCustomStructure() {
    }

    public MessageCustomStructure(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    protected MessageCustomStructure(Parcel in) {
        sender = in.readString();
        message = in.readString();
    }

    public static final Creator<MessageCustomStructure> CREATOR = new Creator<MessageCustomStructure>() {
        @Override
        public MessageCustomStructure createFromParcel(Parcel in) {
            return new MessageCustomStructure(in);
        }

        @Override
        public MessageCustomStructure[] newArray(int size) {
            return new MessageCustomStructure[size];
        }
    };

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageCustomStructure=================={\n" +
                "sender=" + sender + '\n' +
                ", message=" + message + '\n' +
                "\n}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(message);
    }
}
