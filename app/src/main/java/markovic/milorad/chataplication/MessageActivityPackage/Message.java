package markovic.milorad.chataplication.MessageActivityPackage;

public class Message {
    private String messageText;
    private int sender_id;
    private int receiver_id;
    private int id;
    private int color;
    private int pos;

    public Message(String s, int c, int p, int id, int s_id, int r_id) {
        this.messageText = s;
        this.color = c;
        this.pos = p;
        this.sender_id = s_id;
        this.receiver_id = r_id;
        this.id = id;
    }

    public int getSender_id() {

        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}