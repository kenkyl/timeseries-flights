package com.redis.timeSeriesFlights;

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
        System.out.println(String.format("Inserting TS data for Flight # %d from %s to %s", this.flightNumber, this.origin, this.destination));
    }

}
