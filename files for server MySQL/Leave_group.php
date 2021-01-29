<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";

$user_id = $_POST["user_id"];
$group_id = $_POST["group_id"];


$con = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement = mysqli_prepare($con, "DELETE FROM link_user_group WHERE user_id=? and group_id=? ");
mysqli_stmt_bind_param($statement, "ii", $user_id, $group_id);
mysqli_stmt_execute($statement);


$response = array();
$response["success"] = true;

echo json_encode($response);

?>