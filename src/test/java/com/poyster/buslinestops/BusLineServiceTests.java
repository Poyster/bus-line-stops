package com.poyster.buslinestops;

import com.poyster.buslinestops.client.TrafikLabClient;
import com.poyster.buslinestops.service.BusLineService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusLineServiceTests {

    @Mock
    private TrafikLabClient trafikLabClient;

    @InjectMocks
    private BusLineService busLineService;

    private final PrintStream standardOut = System.out;
    private final ConsoleOutputCatcher outputCatcher = new ConsoleOutputCatcher();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputCatcher));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void getBusLineStops() throws IOException {
        String busLineStopsJson = "{\"StatusCode\":0,\"Message\":null,\"ExecutionTime\":0,\"ResponseData\":{\"Version\":\"2023-06-29 00:13\",\"Type\":\"JourneyPatternPointOnLine\",\"Result\":[{\"LineNumber\":\"1\",\"DirectionCode\":\"1\",\"JourneyPatternPointNumber\":\"10001\",\"LastModifiedUtcDateTime\":\"2022-02-15 00:00:00.000\",\"ExistsFromDate\":\"2022-02-15 00:00:00.000\"},{\"LineNumber\":\"1\",\"DirectionCode\":\"1\",\"JourneyPatternPointNumber\":\"10012\",\"LastModifiedUtcDateTime\":\"2023-03-07 00:00:00.000\",\"ExistsFromDate\":\"2023-03-07 00:00:00.000\"}]}}";
        when(trafikLabClient.getBusLineStops("jour", "BUS", "JourneyPatternPointOfLine")).thenReturn(busLineStopsJson);

        String busLineStopNamesJson = "{\"StatusCode\":0,\"Message\":null,\"ExecutionTime\":404,\"ResponseData\":{\"Version\":\"2023-06-29 00:13\",\"Type\":\"StopPoint\",\"Result\":[{\"StopPointNumber\":\"10001\",\"StopPointName\":\"Stadshagsplan\",\"StopAreaNumber\":\"10001\",\"LocationNorthingCoordinate\":\"59.3373571967995\",\"LocationEastingCoordinate\":\"18.0214674159693\",\"ZoneShortName\":\"A\",\"StopAreaTypeCode\":\"BUSTERM\",\"LastModifiedUtcDateTime\":\"2022-10-28 00:00:00.000\",\"ExistsFromDate\":\"2022-10-28 00:00:00.000\"}]}}";
        when(trafikLabClient.getBusLineStopNames("stop", "BUS")).thenReturn(busLineStopNamesJson);

        busLineService.getBusLineStops();

        assertThat(outputCatcher.getOutput()).contains("Bus Line Number: 1 | Number of bus stops: 2");
        assertThat(outputCatcher.getOutput()).contains("Stadshagsplan");
    }

}
