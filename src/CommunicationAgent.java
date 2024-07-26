import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommunicationAgent extends BaseAgent {
    private String[] networkRouters;

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    Message message = getMessageFromAgent(msg);
                    if (message != null) {
                        if (message.getMsgFlag().equals("DDOS_DETECTED")) {
                            JSONObject eventContent = message.getMsgContent();

                            System.out.println("");
                            System.out.println("===========");
                            System.out.println("DDOS DETECTED!");
                            System.out.println(eventContent.toString());
                            System.out.println("===========");
                            System.out.println("");

                            notifyActuatorAgent(eventContent.getJSONArray("data"));
                            notifyNetworkRouters("DDOS");
                        }

                        if (message.getMsgFlag().equals("NETWORK_UNHEALTHY")) {

                            System.out.println("");
                            System.out.println("===========");
                            System.out.println("NETWORK UNHEALTHY");
                            System.out.println("===========");
                            System.out.println("");

                            notifyNetworkRouters("UNHEALTHY");
                        }
                    }
                }

                block(2000);
            }
        });
    }

    private void notifyActuatorAgent(JSONArray data) {
        sendMessageToAgent("ActuatorAgent", "DDOS_DETECTED", data);
    }

    private void notifyNetworkRouters(String blockType) {
        System.out.println(blockType + ": Block network traffic command for network routers");
    }
}
