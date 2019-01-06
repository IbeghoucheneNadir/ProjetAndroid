package mbds.api;

public class MessageEntry extends Message {

    private String contact;
    private String recipient;
    private long userID;

    public MessageEntry(int id, String author, String recipient, String contact, String textMessage, String dateCreated, long userID) {
        super(id, author, textMessage, dateCreated);
        this.recipient = recipient;
        this.contact = contact;
        this.userID = userID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContact() {
        return contact;

    }

    public long getUserID() {
        return userID;
    }
}
