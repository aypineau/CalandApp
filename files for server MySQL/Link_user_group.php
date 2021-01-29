<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";

$user_id = $_POST["user_id"];
$group_id = $_POST["group_id"];


$con1 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement1 = mysqli_prepare($con1, "SELECT user_id, group_id FROM link_user_group WHERE user_id=? and group_id=?");
mysqli_stmt_bind_param($statement1, "ii", $user_id, $group_id);
mysqli_stmt_execute($statement1);

mysqli_stmt_store_result($statement1);
mysqli_stmt_bind_result($statement1, $user_id_answer, $group_id_answer);

mysqli_close($con1);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement1)){
    $response["success"] = true ;
}

if ($response["success"] == false):

    $con2 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

    $statement2 = mysqli_prepare($con2, "INSERT INTO link_user_group (user_id, group_id ) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement2, "ii", $user_id, $group_id);
    mysqli_stmt_execute($statement2);

    mysqli_close($con2);

    $response = array();
    $response["success"] = true;

endif;

echo json_encode($response);


?>