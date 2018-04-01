package markovic.milorad.chataplication.MessageActivityPackage;

public class Message {
    String messageText;
    int color;
    int pos;

    Message(String s, int c, int p) {
        this.messageText = s;
        this.color = c;
        this.pos = p;
    }
}
