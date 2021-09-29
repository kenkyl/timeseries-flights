package com.redis.timeSeriesFlights;

import com.redislabs.redistimeseries.*;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;
import java.io.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // read flights from csv
        List<FlightTimeSeries> flights = new ArrayList<FlightTimeSeries>();
        //parsing a CSV file into Scanner class constructor
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src/main/resources/flight-data.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String csvLine;
        while (true) {
            try {
                if (!((csvLine = br.readLine()) != null)) break;
                String[] flightInfo = csvLine.split(", ");
                flights.add(new FlightTimeSeries(Integer.valueOf(flightInfo[0]), flightInfo[1], flightInfo[2]));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // start each flight timeseries thread to generate data
        flights.forEach((f) -> {
            f.start();
        });
    }

    /* example queries:
     * TS.MRANGE - + WITHLABELS AGGREGATION SUM 6000 FILTER type=flight GROUPBY dest REDUCE SUM
     *
    */
}
