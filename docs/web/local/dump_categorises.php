<?php

	$dump = file_get_contents("dump_categorises.txt");

	$dump = explode(";", $dump);

	foreach ($dump as $line) {
		if($line == "1"){
			continue;
		}


		$line = explode("_-_-_", $line);

		echo "INSERT INTO categorises (ID, TITLE, FATHER, URL, ACTIVE) VALUES 
		(".$line[0].",'".$line[1]."',".$line[2].",'".$line[4]."',".$line[3].");<br>";
	}

?>