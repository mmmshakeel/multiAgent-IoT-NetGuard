import org.json.JSONObject;

import java.io.Serializable;

public class Message implements Serializable {
    private String msgFlag;
    private String msgContent;

    public Message(String msgFlag, String msgContent) {
        this.msgFlag = msgFlag;
        this.msgContent = msgContent;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("msgFlag", msgFlag);
        json.put("msgContent", msgContent);
        return json;
    }

    public static Message fromJson(JSONObject json) {
        return new Message(json.getString("msgFlag"), json.getString("msgContent"));
    }
}
