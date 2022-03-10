# Calculations

## Metrics

The metrics class is an enum that references each provided metric to calculate. Each enum will have a corresponding producer function to create a new instance of the that calculation class. A producer was used so that many calculations of the same type can be ran at once using this method.

### Metric

The metric interface is provides two functions:

```java
//Generates an overall value for campaign data 
public Function<Campaign, Object> overall();

//Provides a set of graph points to be plotted
public Function<Bundle, HashSet<Point2D>> overTime();
```

Each of these methods returns a type Function. This was because it would allow us to pass the corresponding function as a value to allow greater generalisation when performing a calculation as seen in Calculator.java

```java
switch (func) {
    case OVERALL:
        function = metric.getMetric().overall();
        break;
    ...
```

## Calculator

The Calculator class is the interface with which the Model class will perform calculations. To perform a calculation it will run:

```java
public Future<Object> runCalculation(Campaign campaign, Metrics metric, MetricFunction func) 
```

This will create an instance of the Calculation class which is an instance of Callable. We then use the ExecutorService class to run the calculation on its own thread and return a Future class.

The idea is that the View section will be given this Future class and can display some loading message while the calculation is ongoing.