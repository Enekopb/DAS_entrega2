<?php
//Recoger tokens servidor
require_once "configuracion.php";
$sql = "SELECT * FROM usuarios";
$stmt = $conn->prepare($sql);
if (!$stmt) {
    die('Error en la consulta: ' . $conn->error); 
}
$stmt->execute();
$result = $stmt->get_result();

$tokens = array(); 

// Recorremos todos los resultados y almacenamos los tokens
while ($fila = $result->fetch_assoc()) {
    $tokens[] = $fila['token'];
}
$conn->close();

// Datos del mensaje a enviar
$msg = array(
	'registration_ids'=> $tokens,
	'data' => array(
		"title" => "Título del mensaje",
		"body" => "Cuerpo del mensaje")
);

$msgJSON = json_encode($msg);

$cabecera = array(
    'Authorization: key=AAAAoOJl1kQ:APA91bGRTEkpF1LQ8rh2o7NBrP_x-0jSJNPYtXgQdxmzNsoj0zBtFbzgMEsQyrUiMGDYK7uINE9HaC16DwBVL2HPONcPM40-PtbtXSIby7E2Bpg5v3ljZ2YVX1Y4U9_GNlns8t9Jo7wv',
    'Content-Type: application/json'
);

// Realizar la solicitud POST a la API de FCM
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $cabecera);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, $msgJSON);

// Ejecutar la solicitud y obtener la respuesta
$result = curl_exec($ch);
if ($result === false) {
    die('Error al enviar el mensaje: ' . curl_error($ch));
}

if (curl_errno($ch)) { 
	print curl_error($ch); 
} 


// Cerrar la conexión
curl_close($ch);

// Mostrar la respuesta
echo 'Mensaje enviado con éxito: ' . $result;
?>