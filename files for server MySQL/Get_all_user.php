<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";


$con = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

$statement = mysqli_prepare($con, "SELECT user_id, firstname, lastname FROM user");
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $user_id, $firstname, $lastname );

$user_detail = array();
$response = array();
$response["success"] = false;

$i = 1;
while(mysqli_stmt_fetch($statement)){
    $user_detail = array(
        "user_id" => $user_id,
        "firstname" => $firstname,
        "lastname" => $lastname
    );
    $response["success"] = true;
    $response[$i]=$user_detail;
    $i=$i+1;
}

echo json_encode($response);

?>