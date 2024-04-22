<?php
        require_once "configuracion.php";

        $nombre = $_POST['nombre'];
        $accion = $_POST['accion'];

        if ($accion == 'subir'){
                $imagenBase64 = $_POST['imagen'];
                // Decodificar la imagen base64
                $imagen = base64_decode($imagenBase64);

                // Guardar la imagen en el servidor
                $rutaImagen = './imagenes/' . $nombre . '.jpg';
                file_put_contents($rutaImagen, $imagen);

                $sql = "UPDATE usuarios SET foto='$rutaImagen' WHERE usuario='$nombre'";
                                $resultado = $conn->query($sql);

                if ($resultado) {
                        echo "success";
                } else {
                        echo "failure";
                }

                $conn->close();
        } else if($accion == 'cargar'){
                // Recuperar la imagen base64 de la base de datos
        $sql = "SELECT * FROM usuarios WHERE usuario=?";

        // Prepare statement
        $stmt = $conn->prepare($sql);

        // Bind parameters
        $stmt->bind_param("s", $nombre);

        // Execute statement
        $stmt->execute();

        // Get result
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $fila = $result->fetch_assoc();
            $rutaImagen = $fila['foto'];

            // Leer el contenido del archivo de la ruta
                $imagenContenido = file_get_contents($rutaImagen);

                if ($imagenContenido !== false) {
                        // Codificar la imagen en base64
                        $imagenBase64 = base64_encode($imagenContenido);

                        // Devolver la imagen base64 como respuesta
                        echo $imagenBase64;
                } else {
                        echo "failure"; // Error al leer el archivo
                }
        } else {
                echo "failure"; // No se encontró la imagen en la base de datos
        }
        $conn->close();
        }
?>