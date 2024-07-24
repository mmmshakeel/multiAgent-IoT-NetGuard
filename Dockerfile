FROM openjdk:11-jdk-slim

WORKDIR /app

COPY . /app

# Install wget and unzip
RUN apt-get update && apt-get install -y wget unzip

# Set JADE environment variables
ENV JADE_HOME /app/lib
ENV CLASSPATH $JADE_HOME/jade.jar:.

# Make port 1099 available to the world outside this container
EXPOSE 1099

# Define environment variable
ENV NAME World
