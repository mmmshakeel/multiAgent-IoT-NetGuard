import jade.core.Agent;

public class BaseAgent extends Agent {
    protected void setup() {
        System.out.println("Hello! " + getAID().getName() + " is ready.");
    }

    protected void takeDown() {
        System.out.println("Goodbye! " + getAID().getName() + " terminating.");
    }
}
