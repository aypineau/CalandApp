<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";

$user_id = $_POST["user_id"];
$group_id = $_POST["group_id"];
$event_moment = $_POST["event_moment"];
$event_duration = $_POST["event_duration"];
$description = $_POST["description"];


$con1 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement1 = mysqli_prepare($con1, "INSERT INTO event (organizer_id, group_id, event_moment, event_duration, description ) VALUES (?, ?, ?, ?, ?)");
mysqli_stmt_bind_param($statement1, 'iisss', $user_id, $group_id , $event_moment, $event_duration, $description);
mysqli_stmt_execute($statement1);

mysqli_close($con1);


$con2 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement2 = mysqli_prepare($con2, "SELECT event_id FROM event WHERE organizer_id=? AND group_id=? AND event_moment=? AND event_duration=? AND description=?");
mysqli_stmt_bind_param($statement2, 'iisss', $user_id, $group_id, $event_moment, $event_duration, $description);
mysqli_stmt_execute($statement2);

mysqli_stmt_store_result($statement2);
mysqli_stmt_bind_result($statement2, $event_id);


$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement2)){
    $response["success"] = true ;
    $response["event_id"] = $event_id;
}

echo json_encode($response);

?>