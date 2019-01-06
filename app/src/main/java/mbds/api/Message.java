package mbds.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("alreadyReturned")
    @Expose
    private Boolean alreadyReturned;

    public Message(int id, String author, String msg, String dateCreated) {
        this.id = id;
        this.author = author;
        this.msg = msg;
        this.dateCreated = dateCreated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getAlreadyReturned() {
        return alreadyReturned;
    }

    public void setAlreadyReturned(Boolean alreadyReturned) {
        this.alreadyReturned = alreadyReturned;
    }

    public String toString(){
        return "id="+id+" author="+author+" msg="+msg+" date="+dateCreated+" alreadyReturned="+alreadyReturned;
    }

}