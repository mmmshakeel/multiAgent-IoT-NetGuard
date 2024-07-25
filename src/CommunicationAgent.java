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

                if (msg != null) {
                    String msgContent = msg.getContent();
                    if (msgContent.equals("DDOS_DETECTED")) {

                        System.out.println("");
                        System.out.println("===========");
                        System.out.println("DDOS DETECTED!");
                        System.out.println("===========");
                        System.out.println("");

                        notifyActuatorAgent();
                        notifyNetworkRouters();
                    }

                    if (msgContent.equals("NETWORK_UNHEALTHY")) {

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
        // Todo: Update the message to a template
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
