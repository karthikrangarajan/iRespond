<?php

class Location extends CI_Controller{
	function __construct(){
		parent::__construct();
		$this->load->model('messagemodel');
		$this->load->library('curl');
	}
	
	function index(){
		$requests = $this->messagemodel->get_requests();
		
		foreach($requests as $request){
			$observer = $this->messagemodel->get_closest_observer($request);
			
			if(!($observer)){
				
				return;
			}
			$url = "http://maps.googleapis.com/maps/api/directions/xml?origin=".$observer["latitude"].",".$observer["longitude"]."&destination=".$request["latitude"].",".$request["longitude"]."&sensor=false";
			$directions = $this->curl->simple_get($url);
			
			$xml = simplexml_load_string($directions);
			$direction_response = $xml->DirectionResponse;
			$i = 0;
			$message_id = $this->messagemodel->get_message_id();
			foreach($xml->route->leg->step as $step){
				if($i==0){
					if($message_id<10){
						$message = "0".$message_id."000".$request['category_id'].$request['priority'];
					}
					else{
						$message = $message_id."000".$request['category_id'].$request['priority'];
					}
					$i++;
					$message_id++;
					$message='';
				}
				if($message_id<10){
					$message = "0".$message_id;
					if($i<10){
						$message = $message."0".$i;
					}
					else{
						$message = $message.$i;
					}
				}
				else{
					$message = $message_id;
					if($i<10){
						$message = $message."0".$i;
					}
					else{
						$message = $message.$i;
					}
				}				
				$message = $message."0";
				$message = $message.strip_tags($step->html_instructions)." (";
				$message = $message.$step->distance->text.")";
				$i++;
				$message_id++;
				$this->messagemodel->send_sms($observer["observer_number"],$message);
				$message='';
			}
			if($message_id<10){
				$message = "0".$message_id;
				if($i<10){
					$message = $message."0".$i;
				}
				else{
					$message = $message.$i;
				}
			}
			
			else{
				$message = $message_id;
				if($i<10){
					$message = $message."0".$i;
				}
				else{
					$message = $message.$i;
				}
			}
			
			$message=$message."1"."End of Instructions";
			$this->messagemodel->send_sms($observer["observer_number"],$message);	
			$message_id++;
			$this->messagemodel->set_message_id($message_id);	
			echo "<br />Next Request <br /><br />";
		}
		
	}
}