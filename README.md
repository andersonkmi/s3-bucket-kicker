# S3 bucket uploader tool (kicker)

This project is a simple S3 bucket uploader tool. It is a command line tool.

## Prerequisites

The following requirements are necessary:

- Java 11
- Gradle

## Build and test

In order to build and/test, run the following command:

```
$ gradle build
```

## Run the command line

In order to run the command line tool, use the following:

```
$ java -jar ./build/libs/aws-s3-kikker.jar --bucket org.codecraftlabs.kikker --prefix incoming --input-folder ../COVID-19/csse_covid_19_data/csse_covid_19_daily_reports --file-extension .csv
```