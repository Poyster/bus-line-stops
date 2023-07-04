package com.poyster.buslinestops;

import com.poyster.buslinestops.service.BusLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class BusLineStopsApplication {

    private final BusLineService busLineService;

    @Autowired
    public BusLineStopsApplication(BusLineService busLineService) {
        this.busLineService = busLineService;
    }

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(BusLineStopsApplication.class, args);

        BusLineStopsApplication application = context.getBean(BusLineStopsApplication.class);

        application.startService();

        context.close();
    }

    public void startService() throws IOException {
        busLineService.getBusLineStops();
    }

}
