<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";


$user_id = $_POST["user_id"];


$con = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement = mysqli_prepare($con, "SELECT grp.group_id, grp.group_name FROM grp LEFT JOIN link_user_group ON link_user_group.group_id = grp.group_id WHERE link_user_group.user_id=?");
mysqli_stmt_bind_param($statement, "i", $user_id);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $group_id, $group_name);

$group_detail = array();
$response = array();
$response["success"] = false;

$i = 1;
while(mysqli_stmt_fetch($statement)){
    $group_detail = array(
        "group_id" => $group_id,
        "group_name" => $group_name
    );
    $response["success"] = true;
    $response[$i]=$group_detail;
    $i=$i+1;
}

echo json_encode($response);

?>