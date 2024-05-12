import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CommunicationAgent extends BaseAgent {
    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                String msgContent = msg.getContent();

                if (msg != null) {
                    if (msgContent.equals("DDOS_DETECTED")) {
                        System.out.println("Block network traffic command for network routers");
                    }
                }

                block(2000);
            }
        });
    }

    // Methods to send commands to network hardware
}
