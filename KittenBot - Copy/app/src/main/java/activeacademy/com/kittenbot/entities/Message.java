package activeacademy.com.kittenbot.entities;


public class Message {
    private String body;
    private boolean isMine;

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

}
