package com.poyster.buslinestops.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.poyster.buslinestops.client.TrafikLabClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class BusLineService {

    private static final Logger log = LogManager.getLogger(BusLineService.class);

    private final TrafikLabClient client;

    @Autowired
    public BusLineService(TrafikLabClient client) {
        this.client = client;
    }

    public void getBusLineStops() throws IOException {

        //Use the client to call the API and get the bus line stops
        String jsonResponse = client.getBusLineStops("jour", "BUS", "JourneyPatternPointOfLine");

        JsonArray resultArray = getJsonElements(jsonResponse);

        Map<String, Integer> lineNumberCounts = new HashMap<>();

        for (JsonElement resultElement : resultArray) {
            JsonObject resultObject = resultElement.getAsJsonObject();
            String lineNumber = resultObject.get("LineNumber").getAsString();
            lineNumberCounts.put(lineNumber, lineNumberCounts.getOrDefault(lineNumber, 0) + 1);
        }

        // Sort the bus lines based on number of bus stops

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(lineNumberCounts.entrySet());

        sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        List<Map.Entry<String, Integer>> top10Entries = sortedEntries.subList(0, Math.min(sortedEntries.size(), 10));

        // Get the bus line with most bus stops
        String firstRankedBusLine = top10Entries.get(0).getKey();

        // Log the top 10 bus lines with most bus stops
        logTopTenBusLinesBasedOnNumberOfStops(top10Entries);
        logBusStopNames(getBusStopNames(makeBusStopListForTopOneBusLine(resultArray, firstRankedBusLine)), firstRankedBusLine);
    }

    // This method returns a list of bus stop numbers for the bus line with most bus stops
    private List<String> makeBusStopListForTopOneBusLine(JsonArray resultArray, String firstRankedBusLine) {
        List<String> journeyPatternPointNumbers = new ArrayList<>();

        for (JsonElement element : resultArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String lineNumber = jsonObject.get("LineNumber").getAsString();

            if (lineNumber.equals(firstRankedBusLine)) {
                String journeyPatternPointNumber = jsonObject.get("JourneyPatternPointNumber").getAsString();
                journeyPatternPointNumbers.add(journeyPatternPointNumber);
            }
        }
        return journeyPatternPointNumbers;
    }

    private JsonArray getJsonElements(String jsonResponse) {
        Gson gson = new Gson();

        JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);
        return response.getAsJsonObject("ResponseData").getAsJsonArray("Result");
    }

    private List<String> getBusStopNames(List<String> stopPointNumbers) throws IOException {
        //Use the client to call the API and get the bus stop names
        String jsonResponse = client.getBusLineStopNames("stop", "BUS");

        JsonArray resultArray = getJsonElements(jsonResponse);

        List<String> busStopNames = new ArrayList<>();

        //Loop through the bus stops and add the names of the bus stops to the list
        for (JsonElement resultElement : resultArray) {
            JsonObject resultObject = resultElement.getAsJsonObject();
            String stopPointName = resultObject.get("StopPointName").getAsString();

            for (String stopPointNumber : stopPointNumbers) {
                if (resultObject.get("StopPointNumber").getAsString().equals(stopPointNumber))
                    busStopNames.add(stopPointName);
            }
        }
        // Remove duplicates
        return new ArrayList<>(new HashSet<>(busStopNames));
    }

    private void logBusStopNames(List<String> busStopNames, String firstRankedBusLine) {
        log.info("Names of All Bus Stops from Bus Line: " + firstRankedBusLine);
        log.info("---");
        for (String busStopName : busStopNames) {
            log.info(busStopName);
        }
        log.info("---");
    }

    private void logTopTenBusLinesBasedOnNumberOfStops(List<Map.Entry<String, Integer>> top10Entries) {
        log.info("Top 10 bus lines with most bus stops: ");
        log.info("---");
        for (var entry : top10Entries) {
            log.info("Bus Line Number: " + entry.getKey() + " | Number of bus stops: " + entry.getValue());
        }
        log.info("---");
    }

}