import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

public class BaseAgent extends Agent {
    protected void setup() {
        System.out.println("Hello! " + getAID().getName() + " is ready.");
    }

    protected void takeDown() {
        System.out.println("Goodbye! " + getAID().getName() + " terminating.");
    }

    protected void sendMessageToAgent(String agentName, String msgFlag, JSONArray data) {
        JSONObject msgContent = new JSONObject();
        msgContent.put("data", data);

        Message message = new Message(msgFlag, msgContent);

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));

        String msgString = message.toJson().toString();

        System.out.println(msgString);
        msg.setContent(msgString);

        send(msg);
    }

    protected Message getMessageFromAgent(ACLMessage msg) {
        try {
            String msgString = msg.getContent();
            JSONObject msgJson = new JSONObject(msgString);
            return Message.fromJson(msgJson);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to parse message content to JSON");  // Debugging statement
        }
        return null;
    }
}
