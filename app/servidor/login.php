<?php
if(isset($_POST['usuario']) && isset($_POST['contrasena'])){
        // Include the necessary files
        require_once "configuracion.php";
        require_once "validate.php";
        // Call validate, pass form data as parameter and store the returned value
        $usuario = validate($_POST['usuario']);
        $password = validate($_POST['contrasena']);
        // Create the SQL query string
        $sql = "SELECT * FROM usuarios WHERE usuario=?";

        // Prepare statement
        $stmt = $conn->prepare($sql);

        // Bind parameters
        $stmt->bind_param("s", $usuario);

        // Execute statement
        $stmt->execute();

        // Get result
        $result = $stmt->get_result();

        // If number of rows returned is greater than 0 (that is, if the record is found), we'll verify the password
        if($result->num_rows > 0){
                // Fetch data from the database
                $row = $result->fetch_assoc();

                // Verify password
                if(password_verify($password, $row['contrasena'])){
                    echo "success";
                } else {
                    echo "failure";
                }
        } else {
                // If no record is found, print "failure"
                echo "failure";
        }

        // Close statement
        $stmt->close();
}
?>