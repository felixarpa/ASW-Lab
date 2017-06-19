<?php

$URI = 'http://localhost:8080/waslab02/wall.php';
$resp = file_get_contents($URI);
echo $http_response_header[0], "\n"; // Print the first HTTP response header
echo $resp;  // Print HTTP response body

?>