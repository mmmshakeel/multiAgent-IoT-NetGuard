import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class MonitoringAgent extends BaseAgent {
    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new TickerBehaviour(this, 10000) {
            @Override
            protected void onTick() {
                monitorNetworkHealth();
            }
        });
    }

    private void monitorNetworkHealth() {
        System.out.println("Monitoring MAS system health");

        if (checkNetworkHealth()) {
            notifyCommunicationAgent();
        }
    }

    private void notifyCommunicationAgent() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("CommunicationAgent", AID.ISLOCALNAME));
        msg.setContent("NETWORK_UNHEALTHY");
        send(msg);
    }

    private boolean checkNetworkHealth() {
        // a random function of the network health
        // Todo: implement actual network health check
        Random random = new Random();
        int simulatedNetHealth = random.nextInt(10);

        return simulatedNetHealth > 8;
    }

    // Monitoring the jade network
}
