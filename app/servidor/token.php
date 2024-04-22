<?php
require_once "configuracion.php";

        $nombre = $_POST['nombre'];
        $token = $_POST['token'];
        $sql = "UPDATE usuarios SET token='$token' WHERE usuario='$nombre'";
        $resultado = $conn->query($sql);

        if ($resultado) {
                echo "success";
        } else {
                echo "failure";
        }
        $conn->close();
?>