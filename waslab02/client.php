<?php

$URI = 'http://localhost:8080/waslab02/wall.php';

$postdata = '<?xml version="1.0"?><tweet><author>Test Author</author><text>Test Text</text></tweet>';

$opts = array('http' =>
    array(
        'method'  => 'PUT',
        'header'  => 'Content-type: text/xml',
        'content' => $postdata
    )
);

$context = stream_context_create($opts);
$resp = file_get_contents($URI, false, $context);
echo $resp;

echo "\n";

$opts = array('http' =>
    array(
        'method'  => 'DELETE',
        'header'  => 'Content-type: text/xml'
    )
);

$context = stream_context_create($opts);
$resp = file_get_contents($URI . '?twid=10', false, $context);
echo $resp;

echo "\n";

$resp = file_get_contents($URI);
echo $http_response_header[0], "\n"; // Print the first HTTP response header
$tweets = new SimpleXMLElement($resp);
foreach ($tweets->tweet as $tweet) {
    $id = (string) $tweet["id"];
    $author = $tweet->author;
    $text = $tweet->text;
    $time = $tweet->time;
    echo "[tweet #" . $id . "] " . $author . ": " . $text . ". [" . $time . "]\n";
}

?>
