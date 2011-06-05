<?php

class MessageModel extends CI_Model{
	function __construct(){
		parent::__construct();
		$this->load->database();
		$this->convert=array("0"=>0,"1"=>1,"2"=>2,"3"=>3,"4"=>4,"5"=>5,"6"=>6,"7"=>7,"8"=>8,"9"=>9,"A"=>10,"B"=>11,"C"=>12,"D"=>13,"E"=>14,"F"=>15,"G"=>16,"H"=>17,"I"=>18,"J"=>19,"K"=>20,"L"=>21,"M"=>22,"N"=>23,"O"=>24,"P"=>25,"Q"=>26,"R"=>27,"S"=>28,"T"=>29,"U"=>30,"V"=>31,"W"=>32,"X"=>33,"Y"=>34,"Z"=>35,"a"=>10,"b"=>11,"c"=>12,"d"=>13,"e"=>14,"f"=>15,"g"=>16,"h"=>17,"i"=>18,"j"=>19,"k"=>20,"l"=>21,"m"=>22,"n"=>23,"o"=>24,"p"=>25,"q"=>26,"r"=>27,"s"=>28,"t"=>29,"u"=>30,"v"=>31,"w"=>32,"x"=>33,"y"=>34,"z"=>35," "=>0,"."=>0);
	}
	
	function read_messages(){
		$sql = "select * from received_messages where message_read=0";
		$query = $this->db->query($sql);
		if($query->num_rows()==0){
			return false;
		}
		
		
		foreach($query->result() as $row){
			$request = array();
			$id = $row->received_message_id;
			if($row->message_text[0]=='0'&&$row->message_text[1]=='3'){
				$request_id = ($this->convert[$row->message_text[2]])*pow(36,2)+($this->convert[$row->message_text[3]])*36+($this->convert[$row->message_text[4]]);
				$sql = "update requests set picked=true where request_id=$request_id";
				$this->db->query($sql);
				return;
			}
			if($row->message_text[1]=='1'){
				$latitude=(($this->convert[$row->message_text[11]])*pow(36,4))+(($this->convert[$row->message_text[12]])*pow(36,3))+(($this->convert[$row->message_text[13]])*pow(36,2))+(($this->convert[$row->message_text[14]])*pow(36,1))+(($this->convert[$row->message_text[15]])*pow(36,0));
				$latitude=$latitude/100000; 
				if($row->message_text[10]=='-'){
					$request["latitude"]=0-$latitude;
				}
				else{
					$request["latitude"]=$latitude;
				}
				$longitude=(($this->convert[$row->message_text[17]])*pow(36,4))+(($this->convert[$row->message_text[18]])*pow(36,3))+(($this->convert[$row->message_text[19]])*pow(36,2))+(($this->convert[$row->message_text[20]])*pow(36,1))+(($this->convert[$row->message_text[21]])*pow(36,0));
				$longitude=$longitude/100000; 
				if($row->message_text[16]=='-'){
					$request["longitude"]=0-$longitude;
				}
				else{
					$request["longitude"]=$longitude;
				}
				$observer_number = $row->calling_phone;	
				$sql = "update observers set latitude=?, longitude=? where observer_number=\"$observer_number\"";
				$this->db->query($sql,array($request["latitude"],$request["longitude"]));
				$id = $row->received_message_id;
				$sql = "update received_messages set message_read=1 where received_message_id=$id";
				$this->db->query($sql);
					
				return;	
			}
			//$request["type"] = $row->message_text[1];
			$request["priority"] = $row->message_text[2];
			$request["category_id"] = $row->message_text[3];
			$request["timestamp"] = (($this->convert[$row->message_text[4]])*pow(36,5))+(($this->convert[$row->message_text[5]])*pow(36,4))+(($this->convert[$row->message_text[6]])*pow(36,3))+(($this->convert[$row->message_text[7]])*pow(36,2))+(($this->convert[$row->message_text[8]])*pow(36,1))+(($this->convert[$row->message_text[9]])*pow(36,0));
			$latitude=(($this->convert[$row->message_text[11]])*pow(36,4))+(($this->convert[$row->message_text[12]])*pow(36,3))+(($this->convert[$row->message_text[13]])*pow(36,2))+(($this->convert[$row->message_text[14]])*pow(36,1))+(($this->convert[$row->message_text[15]])*pow(36,0));
			$latitude=$latitude/100000; 
			if($row->message_text[10]=='-'){
				$request["latitude"]=0-$latitude;
			}
			else{
				$request["latitude"]=$latitude;
			}
			$longitude=(($this->convert[$row->message_text[17]])*pow(36,4))+(($this->convert[$row->message_text[18]])*pow(36,3))+(($this->convert[$row->message_text[19]])*pow(36,2))+(($this->convert[$row->message_text[20]])*pow(36,1))+(($this->convert[$row->message_text[21]])*pow(36,0));
			$longitude=$longitude/100000; 
			if($row->message_text[16]=='-'){
				$request["longitude"]=0-$longitude;
			}
			else{
				$request["longitude"]=$longitude;
			}
			$request["message"]=substr($row->message_text,22,136);
			$request["caller_no"] = $row->calling_phone;
			$this->db->insert("requests",$request);
			
			$sql = "update received_messages set message_read=1 where received_message_id=$id";
			$this->db->query($sql);
			$id=0;
		}
	
	}

	function get_requests(){
		$sql="select * from requests where sent=0";
		$query = $this->db->query($sql);
		$requests = array();
		$i = 0;
		
		foreach($query->result() as $row){
			$requests[$i]["request_id"] = $row->request_id;
			$requests[$i]["caller_no"] = $row->caller_no;
			$requests[$i]["timestamp"] = $row->timestamp;
			$requests[$i]["priority"] = $row->priority;
			$requests[$i]["latitude"] = $row->latitude;
			$requests[$i]["longitude"] = $row->longitude;
			$requests[$i]["message"] = $row->message;
			$requests[$i]["category_id"] = $row->category_id;
			$i++;
		}
		return $requests;
	}
	
	function calculate_distance($source_x,$source_y,$destination_x,$destination_y){
		return sqrt(pow(($destination_x-$source_x),2) + pow(($destination_y-$source_y),2)); 
	}
	
	function get_closest_observer($request){
		$caller_number = $request["caller_no"];
		$sql = "select * from observers where observer_number<>\"caller_number\" and processing_request=0";
		$query = $this->db->query($sql);
		
		if($query->num_rows()==0){
			return false;
		}
		$observer = array();
		$min = 9999;
		foreach($query->result() as $row){
			$min_new = $this->calculate_distance($request["latitude"],$row->latitude,$request["longitude"],$row->longitude);
			if($min_new < $min){
				$min = $min_new;
				$observer["observer_id"] = $row->observer_id;
				$observer["observer_number"] = $row->observer_number;
				$observer["latitude"] = $row->latitude;
				$observer["longitude"] = $row->longitude;
			}
		}
		$sql = "update observers set processing_request=1 where observer_id=".$observer["observer_id"];
		$this->db->query($sql);
		return $observer;
	}

	function send_sms($number,$message){
		$this->curl->simple_post('https://api.tropo.com/1.0/sessions', array('action'=>'create','token'=>'02e0dc20b689044c86761d6d3228283d63478b541f8f963942328042422bfa5819b607e81af8a02b5af7e863','message'=>$message,'number'=>$number));
	}
	
	function get_message_id(){
		$sql = "select dummy_row from dummy";
		$query = $this->db->query($sql);
		
		$row = $query->row();
		return $row->dummy_row;
	}
	
	function set_message_id($id){
		$sql = "update dummy set dummy_row=$id";
		$this->db->query($sql);
	}
	

}
?>


