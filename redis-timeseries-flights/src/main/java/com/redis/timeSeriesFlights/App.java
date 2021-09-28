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

        System.out.println( "Hello World!" );
        //timeSeriesTest();
        FlightTimeSeries fts1 = new FlightTimeSeries(1011, "DFW", "LGA");
        fts1.startTimeSeries();

        // read flights from csv
        List<FlightTimeSeries> flights = new ArrayList<FlightTimeSeries>();
        //parsing a CSV file into Scanner class constructor
        Scanner sc = null;
        try {
            //sc = new Scanner(new File("src/main/resources/flight-data.csv"));
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/flight-data.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //sc.useDelimiter(",");   //sets the delimiter pattern

        while (sc.hasNext())  //returns a boolean value
        {
            String[] flightInfo = sc.next().split(" ");
            flights.add(new FlightTimeSeries(Integer.valueOf(flightInfo[0]), flightInfo[1], flightInfo[2]));
            System.out.print(sc.next());  //find and returns the next complete token from this scanner
        }
        sc.close();  //closes the scanner

        flights.forEach((f) -> {
            f.startTimeSeries();
        });
    }




    /* some flight numbers
     * 1011 DFW -> LGA
     * 1043 BOS -> MIA
     * 1147 PHX -> MIA
     * 1089 AUS -> JFK
     * 2913 AUS -> DFW
     * 2492 MIA -> JFK
     * 2130 BOS -> LGA
     * 1887 PHX -> DFW
     */
    private static void timeSeriesTest() {
        RedisTimeSeries rts = new RedisTimeSeries("localhost", 6379);
   
        Map<String, String> labels = new HashMap<>();
        labels.put("country", "US");
        labels.put("cores", "8");
        try {
            rts.create("cpu1", 60 * 10 /*10min*/, labels);
            rts.create("cpu1-avg", 60*10 /*10min*/, null);
            rts.createRule("cpu1", Aggregation.AVG, 60 /*1min*/, "cpu1-avg");
        } catch (JedisDataException e) {
            System.out.println("Key already exists. Continue...");
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        rts.add("cpu1", System.currentTimeMillis()/1000 /* time sec */, 80.0);
        
        // Get all the timeseries in US in the last 10min average per min  
        Range[] result = rts.mrange(System.currentTimeMillis()/1000 - 10*60, System.currentTimeMillis()/1000, Aggregation.AVG, 60, "country=US");
        for (Range r : result) {
            for (Value v : r.getValues()) {
                System.out.println(v);
            }
        }
        System.out.println(result.toString());
    }
}
