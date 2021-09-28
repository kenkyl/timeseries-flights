# timeseries-flights
Using Redis TimeSeries to collect and aggregate flight related data.

## Dependencies
1. Maven (v. 3.6.3)
2. Java (v. 15.0.1)

## To Run Locally 

1. `mvn compile`
2. `mvn package`
3. `mvn install assembly:assembly`
4. `java -cp target/redis-timeseries-flights-1.0-SNAPSHOT-jar-with-dependencies.jar com.redis.timeSeriesFlights.App`