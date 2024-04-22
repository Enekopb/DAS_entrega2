<?php
        //Parametros conexion DB
        $servername = "db";
        $username = "admin";
        $password = "test";
        $dbname = "database";

        //ApiKey de FCM
        define ('FCM_APIKEY', "");

        // Crear conexión
        $conn = new mysqli($servername, $username, $password, $dbname);

        // Verificar la conexión
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }
?>
