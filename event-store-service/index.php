<?php
include 'config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);

    // Check if JSON decoding was successful
    if (json_last_error() === JSON_ERROR_NONE && !empty($data)) {
        $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        // Check database connection
        if ($conn->connect_error) {
            die(json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]));
        }

        // Prepare the SQL statement
        $stmt = $conn->prepare("INSERT INTO events_detection (IAT, Tot_sum, Tot_size, AVG, flow_duration, Magnitue, Header_Length, Max, Min, Protocol_Type, Rate, Srate, Radius, Covariance, rst_count, urg_count, Duration, Weight, Std, ICMP, Variance, ack_flag_number, Number, UDP, syn_count, fin_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Check if the statement was prepared successfully
        if ($stmt === false) {
            die(json_encode(["status" => "error", "message" => "Statement preparation failed: " . $conn->error]));
        }

        // Ensure all required fields are present in the data
        $requiredFields = ['IAT', 'Tot_sum', 'Tot_size', 'AVG', 'flow_duration', 'Magnitue', 'Header_Length', 'Max', 'Min', 'Protocol_Type', 'Rate', 'Srate', 'Radius', 'Covariance', 'rst_count', 'urg_count', 'Duration', 'Weight', 'Std', 'ICMP', 'Variance', 'ack_flag_number', 'Number', 'UDP', 'syn_count', 'fin_count'];
        foreach ($requiredFields as $field) {
            if (!isset($data[$field])) {
                die(json_encode(["status" => "error", "message" => "Missing field: $field"]));
            }
        }

        // Bind the parameters
        $stmt->bind_param("dddddddsddddddiiiiidiiidii",
            $data['IAT'], $data['Tot_sum'], $data['Tot_size'], $data['AVG'], $data['flow_duration'], $data['Magnitue'],
            $data['Header_Length'], $data['Max'], $data['Min'], $data['Protocol_Type'], $data['Rate'], $data['Srate'],
            $data['Radius'], $data['Covariance'], $data['rst_count'], $data['urg_count'], $data['Duration'],
            $data['Weight'], $data['Std'], $data['ICMP'], $data['Variance'], $data['ack_flag_number'], $data['Number'],
            $data['UDP'], $data['syn_count'], $data['fin_count']
        );

        // Execute the statement
        if ($stmt->execute()) {
            echo json_encode(["status" => "success"]);
        } else {
            echo json_encode(["status" => "error", "message" => $stmt->error]);
            error_log("Statement execution failed: " . $stmt->error); // Log the error
        }

        // Close the statement and connection
        $stmt->close();
        $conn->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Invalid JSON data"]);
        error_log("JSON decoding error: " . json_last_error_msg()); // Log the JSON error
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}