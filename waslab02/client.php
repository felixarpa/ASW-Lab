<?php

$URI = 'http://localhost:8080/waslab02/wall.php';
$resp = file_get_contents($URI);
echo $http_response_header[0], "\n"; // Print the first HTTP response header
//echo $resp;  // Print HTTP response body
$tweets = new SimpleXMLElement($resp);
foreach ($tweets->tweet as $tweet) {
    $id = (string) $tweet["id"];
    $author = $tweet->author;
    $text = $tweet->text;
    $time = $tweet->time;
    echo "[tweet #" . $id . "] " . $author . ": " . $text . ". [" . $time . "]\n";
    // [tweet #7] Donald: When somebody challenges you, fight back. Be brutal, be tough. [2017-02-22T16:55:58+00:00]
    
}

?>
