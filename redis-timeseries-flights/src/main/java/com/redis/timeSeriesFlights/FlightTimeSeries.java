package com.redis.timeSeriesFlights;

import java.util.*;
import com.redislabs.redistimeseries.*;
import redis.clients.jedis.exceptions.JedisDataException;

public class FlightTimeSeries {
    private Integer flightNumber;
    private String origin;
    private String destination;

    public FlightTimeSeries(Integer flightNumber, String origin, String destination) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
    }

    public void startTimeSeries() {
        RedisTimeSeries rts = new RedisTimeSeries("localhost", 6379);

        Map<String, String> labels = new HashMap<>();
        labels.put("type", "flight");
        labels.put("flightNum", this.flightNumber.toString());
        labels.put("orig", this.origin);
        labels.put("dest", this.destination);

        System.out.println(String.format("Inserting TS data for Flight # %d from %s to %s", this.flightNumber, this.origin, this.destination));

        Random rand = new Random(); //instance of random class
        int upperbound = 30;
        while (true) {
            //generate random values from 0-30
            int interval = rand.nextInt(upperbound);
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // NOTE - add a value of 1 to indicate a missed flight
            rts.add("cpu1", System.currentTimeMillis()/1000 /* time sec */, 1.0);
            System.out.println(String.format("Missed flight for Flight # %d", this.flightNumber));
        }
    }
}
