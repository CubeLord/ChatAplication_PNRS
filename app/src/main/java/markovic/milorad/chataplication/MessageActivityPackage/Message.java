package markovic.milorad.chataplication.MessageActivityPackage;

public class Message {
    private String messageText;
    private int color;
    private int pos;

    Message(String s, int c, int p) {
        this.messageText = s;
        this.color = c;
        this.pos = p;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getColor() {
        return color;
    }

    public int getPos() {
        return pos;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
