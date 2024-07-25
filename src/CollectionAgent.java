import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CollectionAgent extends BaseAgent {
    private String predictionServiceUrl = "http://predict-service-nb-model:5000/predict";
    private String csvFilePath = "/app/data/generated_traffic.csv";

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    JSONArray networkEvents = listenToTraffic();
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

    private JSONArray listenToTraffic() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            CSVParser records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            List<CSVRecord> batch = new ArrayList<>();
            int batchSize = 100;

            for (CSVRecord record : records) {
                batch.add(record);
                if (batch.size() == batchSize) {
                    break;
                }
            }

            if (batch.isEmpty()) {
                throw new IOException("No data found in the CSV file");
            }

            // Select first, middle, and last record from the batch
            int size = batch.size();
            CSVRecord firstRecord = batch.get(0);
            CSVRecord middleRecord = batch.get(size / 2);
            CSVRecord lastRecord = batch.get(size - 1);

            // Convert selected records to JSON
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(convertToJSON(firstRecord));
            jsonArray.put(convertToJSON(middleRecord));
            jsonArray.put(convertToJSON(lastRecord));

            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private JSONObject convertToJSON(CSVRecord record) {
        JSONObject json = new JSONObject();
        JSONArray features = new JSONArray();

        for (String value : record) {
            features.put(Double.parseDouble(value));
        }

        json.put("data", new JSONObject().put("features", features));
        return json;
    }

    private HttpResponse<String> useMlModel(JSONArray networkEventData) {
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 0; i < networkEventData.length(); i++) {
            JSONObject event = networkEventData.getJSONObject(i);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(predictionServiceUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(event.toString()))
                    .build();

            try {
                return client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("CommunicationAgent", AID.ISLOCALNAME));
        msg.setContent("DDOS_DETECTED");
        send(msg);
    }
}
