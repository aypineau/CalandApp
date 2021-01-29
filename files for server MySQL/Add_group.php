<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";

$group_name = $_POST["group_name"];


$con1 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement1 = mysqli_prepare($con1, "SELECT group_id FROM grp WHERE group_name = ?");
mysqli_stmt_bind_param($statement1, "s", $group_name);
mysqli_stmt_execute($statement1);

mysqli_stmt_store_result($statement1);
mysqli_stmt_bind_result($statement1, $group_id);

mysqli_close($con1);


$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement1)){
    $response["success"] = true ;
    $response["group_id"] = $group_id;
}


if ($response["success"] == false):


    $con2 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

    $statement2 = mysqli_prepare($con2, "INSERT INTO grp (group_name) VALUES (?)");
    mysqli_stmt_bind_param($statement2, "s", $group_name);
    mysqli_stmt_execute($statement2);

    mysqli_close($con2);


    $con3 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

    $statement3 = mysqli_prepare($con3, "SELECT group_id FROM grp WHERE group_name = ?");
    mysqli_stmt_bind_param($statement3, "s", $group_name);
    mysqli_stmt_execute($statement3);

    mysqli_stmt_store_result($statement3);
    mysqli_stmt_bind_result($statement3, $group_id);


    $response["success"] = false;

    while(mysqli_stmt_fetch($statement3)){
        $response["success"] = true ;
        $response["group_id"] = $group_id;
    }
endif;

echo json_encode($response);

?>