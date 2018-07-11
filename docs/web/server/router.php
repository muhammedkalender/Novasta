<?php
	set_time_limit(0);

	if(!isset($_GET["url"])){
		echo "0";
		exit();
	}

	$url = deurl(explode("_t_", $_GET["url"])[0]);
	$type = explode("_t_", $_GET["url"])[1];

	if($type == "1"){
		echo encode(htmlCategorises(file_get_contents($url)));
	}else if($type == "2"){
		echo encode(htmlNews(file_get_contents($url)));
	}else if($type == "3"){
		echo encode(htmlReferences(file_get_contents($url)));
	}else{
		echo "0";
	}


?>