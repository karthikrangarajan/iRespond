<?php

class ParseSMS extends CI_Controller{
	function __construct(){
		parent::__construct();
		$this->load->library('session');
		$this->load->library('Curl');
		$this->load->model('messagemodel');
	}
	
	function index(){
		$this->messagemodel->read_messages();
	}
	
	
}
//This is a really fucking long message. I mean, really long. So long that SMS is split into two fucking things. Thats right, two fucking things. I am not running out of fucking ideas. Oh wait, I am not supposed to say fuck so much, its impolite. Or some fucking thing like that. Who gives a damn fuck, eh? I dont. Do you?
?>
