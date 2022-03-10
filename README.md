
# Ad Auction Dashboard

The Ad Auction Dashboard is a tool that will allow clients to review the success
of their ad campaign by viewing graphical represenations and overviews of data
collected about the campaign.

## Run Locally

To run the application locally:

```bash
  git clone https://git.soton.ac.uk/tcs1g20/ad-auction-dashboard.git
  cd ad-auction-dashboard
  gradle run
```

To run with command line arguments use:

```bash
  gradle run --args='arg1 arg2 ...'
```

You can create and run fat JAR using:

```bash
  gradle jar
  java -jar ./app/build/libs/app.jar
```

> NOTE: If you do not have Gradle installed use ./gradlew instead of gradle

## Running Tests

To run tests, run the following command:

```bash
  gradle clean test
```

> NOTE: If you do not have Gradle installed use ./gradlew instead of gradle

## Tech Stack

**Language:** Java 17 \
**Build Tool:** Gradle 

*Libraries:* \
**GUI:** JavaFX 17 \
**Testing:** JUnit 5 \
**Logging:** Log4j 2


## Authors

- Thomas Smith - **tcs1g20**
- Ishaipiriyan Karunakularatnam - **ik3g20**
- Ahmed Mohammed - **am6g20**
- Tianyi Xu - **tx1g20**
- Hristiyan Georgiev - **hhg1u20**
- Daniel Battersby - **djb1u20**
