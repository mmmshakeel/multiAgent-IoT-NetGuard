FROM openjdk:11-jdk-slim

WORKDIR /app

COPY . /app

# Install wget and unzip
RUN apt-get update && apt-get install -y wget unzip

# Set JADE environment variables
ENV JADE_HOME /app/lib
ENV CLASSPATH $JADE_HOME/jade.jar:$JADE_HOME/commons-csv-1.9.0.jar:$JADE_HOME/json-20210307.jar:.

# Make port 1099 available to the world outside this container
EXPOSE 1099
