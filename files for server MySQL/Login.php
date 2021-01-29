<?php
    $DB_HOST = "localhost";
    $DB_NAME = "id13671230_loginforandroidappbeta";
    $DB_USER = "id13671230_calandapp";
    $DB_PASSWORD = "V_u2LO]V!0Z0ko_U";

    $email = $_POST["email"];
    $password = $_POST["password"];


    $con = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME);

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE email = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $email, $password);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $user_id, $email, $firstname, $lastname, $password);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true ;
        $response["user_id"] = $user_id;
        $response["email"] = $email;
        $response["firstname"] = $firstname;
        $response["lastname"] = $lastname;
    }

    echo json_encode($response);
?>
