<?php
ini_set("soap.wsdl_cache_enabled","0");

try{

  $sClient = new SoapClient('http://www.webservicex.net/airport.asmx?WSDL');

  // Get the necessary parameters from the request
  // Use $sClient to call the operation GetWeather
  // echo the returned info as a JSON object

  header(':', true, 501); // Just remove this line to return the successful 
                          // HTTP-response status code 200.
  echo json_encode(array('Result' => 'Not implemented'));
  
}
catch(SoapFault $e){
  header(':', true, 500);
  echo json_encode($e);
}
?>
