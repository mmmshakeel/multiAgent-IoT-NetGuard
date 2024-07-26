import org.json.JSONObject;

import java.io.Serializable;

public class Message implements Serializable {
    private String msgFlag;
    private JSONObject msgContent;

    public Message(String msgFlag, JSONObject msgContent) {
        this.msgFlag = msgFlag;
        this.msgContent = msgContent;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public JSONObject getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(JSONObject msgContent) {
        this.msgContent = msgContent;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("msgFlag", msgFlag);
        json.put("msgContent", msgContent);
        return json;
    }

    public static Message fromJson(JSONObject json) {
        return new Message(json.getString("msgFlag"), json.getJSONObject("msgContent"));
    }
}
