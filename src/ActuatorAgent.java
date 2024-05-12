import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ActuatorAgent extends BaseAgent {
    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    ACLMessage msg = receive();

                    if (msg != null) {
                        String msgContent = msg.getContent();
                        if (msgContent.equals("DDOS_DETECTED")) {
                            System.out.println("Update datastore with DDoS event");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                block(2000);
            }
        });
    }

    // Methods to update the data store
}
