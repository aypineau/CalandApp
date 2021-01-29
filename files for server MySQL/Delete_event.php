<?php

$DB_HOST = "localhost";
$DB_NAME = "id13671230_loginforandroidappbeta";
$DB_USER = "id13671230_calandapp";
$DB_PASSWORD = "V_u2LO]V!0Z0ko_U";


$event_id = $_POST["event_id"];


$mysqli = new mysqli($DB_HOST , $DB_USER , $DB_PASSWORD, $DB_NAME);

/* Vérification de la connexion */
if (mysqli_connect_errno()) {
    printf("Échec de la connexion : %s\n", mysqli_connect_error());
    exit();
}

$stmt = $mysqli->prepare("DELETE FROM event WHERE event_id=?");
$stmt->bind_param('i', $event_id );


/* Exécution de la requête */
$stmt->execute();

/* Fermeture du traitement */
$stmt->close();

/* Fermeture de la connexion */
$mysqli->close();


$response = array();
$response["success"] = true;


echo json_encode($response);

?>