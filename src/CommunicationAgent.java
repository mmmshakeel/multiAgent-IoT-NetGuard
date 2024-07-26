import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CommunicationAgent extends BaseAgent {
    private String agentName = "ActuatorAgent";
    private String[] networkRouters;

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                Message message = getMessageFromAgent(msg);

                if (msg != null) {

                    if (message.getMsgFlag().equals("DDOS_DETECTED")) {
                        String eventString = message.getMsgContent();

                        System.out.println("");
                        System.out.println("===========");
                        System.out.println("DDOS DETECTED!");
                        System.out.println(eventString);
                        System.out.println("===========");
                        System.out.println("");

                        notifyActuatorAgent();
                        notifyNetworkRouters();
                    }

                    if (message.getMsgFlag().equals("NETWORK_UNHEALTHY")) {

                        System.out.println("");
                        System.out.println("===========");
                        System.out.println("NETWORK UNHEALTHY");
                        System.out.println("===========");
                        System.out.println("");

                        notifyNetworkRouters();
                    }
                }

                block(2000);
            }
        });
    }

    private void notifyActuatorAgent() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
        msg.setContent("DDOS_DETECTED");
        send(msg);
    }

    private void notifyNetworkRouters() {
        System.out.println("Block network traffic command for network routers");
    }
    // Methods to send commands to network hardware
}
