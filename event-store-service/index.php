<?php
include 'config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);

    if (!empty($data)) {
        $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        $stmt = $conn->prepare("INSERT INTO events_detection (IAT, Tot_sum, Tot_size, AVG, flow_duration, Magnitue, Header_Length, Max, Min, Protocol_Type, Rate, Srate, Radius, Covariance, rst_count, urg_count, Duration, Weight, Std, ICMP, Variance, ack_flag_number, Number, UDP, syn_count, fin_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("dddddddsddddddiiiiidiiidiii",
            $data['IAT'], $data['Tot_sum'], $data['Tot_size'], $data['AVG'], $data['flow_duration'], $data['Magnitue'],
            $data['Header_Length'], $data['Max'], $data['Min'], $data['Protocol_Type'], $data['Rate'], $data['Srate'],
            $data['Radius'], $data['Covariance'], $data['rst_count'], $data['urg_count'], $data['Duration'],
            $data['Weight'], $data['Std'], $data['ICMP'], $data['Variance'], $data['ack_flag_number'], $data['Number'],
            $data['UDP'], $data['syn_count'], $data['fin_count']
        );

        if ($stmt->execute()) {
            echo json_encode(["status" => "success"]);
        } else {
            echo json_encode(["status" => "error", "message" => $stmt->error]);
        }

        $stmt->close();
        $conn->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Invalid data"]);
    }
}
