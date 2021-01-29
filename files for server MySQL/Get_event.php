<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";


$group_id = $_POST["group_id"];


$con = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement = mysqli_prepare($con, "SELECT event_id, organizer_id, group_id, event_moment, event_duration, description FROM event WHERE group_id=?");
mysqli_stmt_bind_param($statement, "i", $group_id);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $event_id, $organizer_id, $group_id, $event_moment, $event_duration, $description);


$group_detail = array();
$response = array();
$response["success"] = false;

$i = 1;
while(mysqli_stmt_fetch($statement)){
    $response["success"] = true;

    $group_detail = array(
        "event_id" => $event_id,
        "organizer_id" => $organizer_id,
        "group_id" => $group_id,
        "event_moment" => $event_moment,
        "event_duration" => $event_duration,
        "description" => $description
    );
    $response[$i]=$group_detail;
    $i=$i+1;
}

echo json_encode($response);

?>