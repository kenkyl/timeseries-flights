package com.redis.timeSeriesFlights;
//import io.lettuce.core.*;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import com.redislabs.redistimeseries.*;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        syncCommands.set("key", "Hello, Redis!");

        String result = syncCommands.get("key");

        System.out.println(result);

        connection.close();
        redisClient.shutdown();

        timeSeriesTest();
    }

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
            System.out.println(r);
        }
        System.out.println(result.toString());
    }
}
