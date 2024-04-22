<?php
if(isset($_POST['usuario']) && isset($_POST['contrasena'])){
    // Include the necessary files
    require_once "configuracion.php";
    //require_once "validate.php";
    // Call validate, pass form data as parameter and store the returned value
    $usuario = $_POST['usuario'];
    $password = $_POST['contrasena'];

    // Hash de la contraseña usando password_hash()
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    // Crear la consulta SQL preparada para evitar la inyección de SQL
    $sql = "INSERT INTO usuarios (usuario, contrasena) VALUES (?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ss", $usuario, $hashed_password);

    // Ejecutar la consulta
    if ($stmt->execute()) {
        echo "success";
    } else {
        echo "failure";
    }

    // Cerrar la conexión
    $stmt->close();
    $conn->close();
}
?>