<?php

require 'tropo.class.php';
 
$session = new Session();
$to = "+".$session->getParameters("number");
$message = $session->getParameters("message");
     
$tropo = new Tropo();
     
$tropo->call("$to", array('network'=>'SMS'));
$tropo->say("$message");

$tropo->renderJson();
?>