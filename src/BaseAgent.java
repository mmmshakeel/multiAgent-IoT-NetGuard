import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class BaseAgent extends Agent {
    protected void setup() {
        System.out.println("Hello! " + getAID().getName() + " is ready.");
    }

    protected void takeDown() {
        System.out.println("Goodbye! " + getAID().getName() + " terminating.");
    }

    protected void sendMessageToAgent(String agentName, String msgFlag, String msgContent) {
        Message message = new Message(msgFlag, msgContent);

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));

        String msgString = message.toJson().toString();
        msg.setContent(msgString);

        send(msg);
    }

    protected Message getMessageFromAgent(ACLMessage msg) {
        if (msg != null) {
            String msgString = msg.getContent();
            System.out.println(msgString);
            JSONObject msgContent = new JSONObject(msgString);

            return Message.fromJson(msgContent);
        }

        return null;
    }
}
