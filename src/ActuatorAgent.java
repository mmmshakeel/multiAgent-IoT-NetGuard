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
                            updateDataStore();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                block(2000);
            }
        });
    }

    /**
     * Todo: Update this function to update a datastore (MySQL database)
     */
    private void updateDataStore() {
        System.out.println("Update datastore with DDoS event");
    }
    // Methods to update the data store
}
