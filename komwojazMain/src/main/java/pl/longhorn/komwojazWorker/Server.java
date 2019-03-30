package pl.longhorn.komwojazWorker;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.Future;

public class Server {
    private boolean isManaged = false;
    //private boolean isBusy = false;
    private String adress;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Server(String adress) {
        this.adress = adress;
    }

    public Server() {
        Random random = new Random();
        int port = random.nextInt(500) + 8000;

        isManaged = true;
        adress = "http://localhost:" + port;

        try {
            Runtime.getRuntime().exec("java  -Dserver.port=" + port + " -jar " + KomwojazMainApplication.PATH_TO_WORKER_CLASS);
            System.out.println("Start worker with port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void examinate(InputData inputData) {
        try {
            Future<HttpResponse<JsonNode>> future = Unirest.post(adress + "/research")
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString(inputData))
                    .asJsonAsync(new Callback<JsonNode>() {

                        public void failed(UnirestException e) {
                            System.out.println("The request has failed" + e.getMessage());
                        }

                        public void completed(HttpResponse<JsonNode> response) {
                            int code = response.getStatus();
                            //Map<String, String> headers = response.getHeaders();
                            JsonNode body = response.getBody();
                            InputStream rawBody = response.getRawBody();
                            try {
                                InputData data = objectMapper.readValue(new String(response.getRawBody().readAllBytes()), InputData.class);
                                KomwojazMainApplication.addResults(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        public void cancelled() {
                            System.out.println("The request has been cancelled");
                        }

                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
