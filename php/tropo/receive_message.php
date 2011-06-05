<?php

require('tropo.class.php');
$session = new Session();
$tropo = new Tropo();
$message = $session->getInitialText();
$from = $session->getFrom();
$from_number = $from["id"];
//$tropo->say($message);
$tropo->renderJson();

$sql = "insert into received_messages(message_text,message_read,calling_phone) values (\"$message\",false,\"$from_number\")";

$conn = mysql_connect("mysql.krangarajan.com","karthikra","th1sisthepassword!!");
mysql_select_db('irespond',$conn);

mysql_query($sql);

?>
