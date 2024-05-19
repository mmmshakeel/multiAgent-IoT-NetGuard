import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class CollectionAgent extends BaseAgent {
    private String predictionServiceUrl = "http://127.0.0.1:5000/predict";
    private String networkEvents = "";

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    networkEvents = listenToTraffic();
                    HttpResponse<String> response = useMlModel(networkEvents);

                    assert response != null;
                    if (isDDoSDetected(response)) {
                        System.out.println("DDoS Detected. Inform Communication agent");

                        notifyCommunicationAgent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                block(5000);
            }
        });
    }

    /**
     * Todo: Update function to read from event datastore
     * Returns a hard coded network event for a DDoS scenarios
     * @return String - Network event record from an event datastore
     */
    private String listenToTraffic() {
        return "{\"data\": {\"features\":[83007316.82262105, 1885.4, 180.72, 177.3948932388561, 0.0, 0.0, 20.52, 180.72, 156.4, 16.89, 16.883240031949594, 16.883240031949594, 10.79298722812946, 328.3251870229355, 0.02, 0.01, 65.73, 141.55, 7.624943986471866, 0.0, 0.19, 0.0, 9.5, 1.0, 0.0, 0.0]}}";
    }

    private HttpResponse<String> useMlModel(String networkEventData) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(predictionServiceUrl))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(networkEventData))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isDDoSDetected(HttpResponse<String> response) {
        if (response.statusCode() == 200) {
            String prediction = response.body();
            prediction = prediction.replace("[", "").replace("]", "").replace("'", "").trim();

            return prediction.equals("DDoS");
        }

        return false;
    }

    private void notifyCommunicationAgent() {
        // Todo: Update the message to a template
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("CommunicationAgent", AID.ISLOCALNAME));
        msg.setContent("DDOS_DETECTED");
        send(msg);
    }
    // Listens to the network traffic and utilises the ML model to determine the network behaviour
}

