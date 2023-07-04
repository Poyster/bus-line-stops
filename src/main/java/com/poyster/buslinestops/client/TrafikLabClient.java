package com.poyster.buslinestops.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrafikLabClient {
    private final RestTemplate restTemplate;

    @Value("${apiKey}")
    private String apiKey;

    @Value("${baseApiUrl}")
    private String baseApiUrl;

    public TrafikLabClient() {
        this.restTemplate = new RestTemplate();
    }

    public String getBusLineStops(String model, String transportModeCode, String type) {
        String apiUrl = baseApiUrl + apiKey + "&model=" + model + "&DefaultTransportModeCode=" + transportModeCode + "&type=" + type;
        return sendRequest(apiUrl);
    }

    public String getBusLineStopNames(String model, String transportModeCode) {
        String apiUrl = baseApiUrl + apiKey + "&model=" + model + "&DefaultTransportModeCode=" + transportModeCode;
        return sendRequest(apiUrl);
    }

    private String sendRequest(String apiUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_ENCODING, "deflate");
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<?> requestEntity = RequestEntity.get(apiUrl)
                .headers(headers)
                .build();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        return responseEntity.getBody();
    }
}
