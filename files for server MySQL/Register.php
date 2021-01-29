<?php
    $DB_HOST = "localhost";
    $DB_NAME = "id13671230_loginforandroidappbeta";
    $DB_USER = "id13671230_calandapp";
    $DB_PASSWORD = "V_u2LO]V!0Z0ko_U";

    $email = $_POST["email"];
    $firstname = $_POST["firstname"];
    $lastname = strtoupper($_POST["lastname"]);
    $password = $_POST["password"];


    $con1 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

    $statement1 = mysqli_prepare($con1, "INSERT INTO user (email, firstname, lastname, password) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement1, "ssss", $email, $firstname, $lastname, $password);
    mysqli_stmt_execute($statement1);

    mysqli_close($con1);


    $con2 = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

    $statement2 = mysqli_prepare($con2, "SELECT user_id FROM user WHERE email = ? AND password = ?");
    mysqli_stmt_bind_param($statement2, "ss", $email, $password);
    mysqli_stmt_execute($statement2);
    
    mysqli_stmt_store_result($statement2);
    mysqli_stmt_bind_result($statement2, $user_id);


    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement2)){
        $response["success"] = true ;
        $response["user_id"] = $user_id;
    }

    echo json_encode($response);
?>
