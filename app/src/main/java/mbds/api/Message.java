package mbds.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {

    private int id;

    private String  author;

    @SerializedName("body")
    private String textMessage;

    private String dateCreated;

    public Message(int id, String author, String textMessage, String dateCreated) {
        this.id = id;
        this.author = author;
        this.textMessage = textMessage;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTextmessage() {
        return textMessage;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String toString(){
        return "id= " + this.id + " author= " + this.author + " date= " + this.dateCreated + "message= " +  this.textMessage;
    }
}
