package activeacademy.com.kittenbot.entities;

/**
 * Created by Elena on 7/2/2017.
 */

public class Message {
    String body;
    boolean isMine;

    public Message(String body, boolean isMine){
        this.body = body;
        this.isMine = isMine;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
