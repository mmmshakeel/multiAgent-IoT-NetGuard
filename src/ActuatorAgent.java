import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ActuatorAgent extends BaseAgent {
    private String eventStoreServiceUrl = "http://event-store-service:80/";
//    private String eventStoreServiceUrl = "http://127.0.0.1:8080/";

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    Message message = getMessageFromAgent(msg);
                    JSONObject msgContent = message.getMsgContent();
                    System.out.println(msgContent);
                    System.out.println(message.getMsgFlag());

                    if (message.getMsgFlag().equals("DDOS_DETECTED")) {
                        JSONArray featuresArray = msgContent.getJSONArray("data");
                        updateDataStore(featuresArray);
                    }
                }

                block(2000);
            }
        });
    }

    private void updateDataStore(JSONArray featuresArray) {
        System.out.println("Update datastore with DDoS event");

        HttpClient client = HttpClient.newHttpClient();

        try {
            // Ensure the array has the correct number of elements
            if (featuresArray.length() < 26) {
                throw new IllegalArgumentException("Features array must have at least 26 elements.");
            }

            // Construct JSON string dynamically
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("IAT", featuresArray.getDouble(0));
            jsonObject.put("Tot_sum", featuresArray.getDouble(1));
            jsonObject.put("Tot_size", featuresArray.getDouble(2));
            jsonObject.put("AVG", featuresArray.getDouble(3));
            jsonObject.put("flow_duration", featuresArray.getDouble(4));
            jsonObject.put("Magnitue", featuresArray.getDouble(5));
            jsonObject.put("Header_Length", featuresArray.getDouble(6));
            jsonObject.put("Max", featuresArray.getDouble(7));
            jsonObject.put("Min", featuresArray.getDouble(8));
            jsonObject.put("Protocol_Type", featuresArray.getDouble(9));
            jsonObject.put("Rate", featuresArray.getDouble(10));
            jsonObject.put("Srate", featuresArray.getDouble(11));
            jsonObject.put("Radius", featuresArray.getDouble(12));
            jsonObject.put("Covariance", featuresArray.getDouble(13));
            jsonObject.put("rst_count", featuresArray.getDouble(14));
            jsonObject.put("urg_count", featuresArray.getDouble(15));
            jsonObject.put("Duration", featuresArray.getDouble(16));
            jsonObject.put("Weight", featuresArray.getDouble(17));
            jsonObject.put("Std", featuresArray.getDouble(18));
            jsonObject.put("ICMP", featuresArray.getDouble(19));
            jsonObject.put("Variance", featuresArray.getDouble(20));
            jsonObject.put("ack_flag_number", featuresArray.getDouble(21));
            jsonObject.put("Number", featuresArray.getDouble(22));
            jsonObject.put("UDP", featuresArray.getDouble(23));
            jsonObject.put("syn_count", featuresArray.getDouble(24));
            jsonObject.put("fin_count", featuresArray.getDouble(25));

            String jsonInputString = jsonObject.toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(eventStoreServiceUrl))
                    .header("Content-Type", "application/json; utf-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Datastore updated");
            } else {
                System.out.println("Failed to update datastore. HTTP response code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
