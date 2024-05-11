import jade.core.behaviours.CyclicBehaviour;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class CollectionAgent extends BaseAgent {
    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:5000/predict"))
                        .header("Content-Type", "application/json")
                        .POST(BodyPublishers.ofString("{\"data\": {\"features\":[83007316.82262105, 1885.4, 180.72, 177.3948932388561, 0.0, 0.0, 20.52, 180.72, 156.4, 16.89, 16.883240031949594, 16.883240031949594, 10.79298722812946, 328.3251870229355, 0.02, 0.01, 65.73, 141.55, 7.624943986471866, 0.0, 0.19, 0.0, 9.5, 1.0, 0.0, 0.0]}}"))
                        .build();

                try {
                    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                    System.out.println("Response status code: " + response.statusCode());
                    System.out.println("Response body: " + response.body());

                    if (response.statusCode() == 200) {
                        String prediction = response.body();
                        prediction = prediction.replace("[", "").replace("]", "").replace("'", "").trim();

                        if (prediction.equals("DDoS")) {
                            System.out.println("DDoS detected!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                block(2000);
            }
        });
    }

    // Listens to the network traffic and utilises the ML model to determine the network behaviour
}
