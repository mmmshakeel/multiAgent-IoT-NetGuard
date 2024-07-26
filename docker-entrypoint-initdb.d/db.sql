CREATE DATABASE IF NOT EXISTS jade_db;
USE jade_db;

CREATE TABLE IF NOT EXISTS events_detection (
    IAT DOUBLE,
    Tot_sum DOUBLE,
    Tot_size DOUBLE,
    AVG DOUBLE,
    flow_duration DOUBLE,
    Magnitue DOUBLE,
    Header_Length DOUBLE,
    Max DOUBLE,
    Min DOUBLE,
    Protocol_Type INT,
    Rate DOUBLE,
    Srate DOUBLE,
    Radius DOUBLE,
    Covariance DOUBLE,
    rst_count INT,
    urg_count INT,
    Duration DOUBLE,
    Weight DOUBLE,
    Std DOUBLE,
    ICMP DOUBLE,
    Variance DOUBLE,
    ack_flag_number INT,
    Number DOUBLE,
    UDP INT,
    syn_count INT,
    fin_count INT
);
