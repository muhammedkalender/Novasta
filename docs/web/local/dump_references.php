<?php

	$dump = file_get_contents("dump_references.txt");

	$dump = explode(";", $dump);

	foreach ($dump as $line) {
		if($line == "1"){
			continue;
		}


		$line = explode("_-_-_", $line);

		echo "INSERT INTO 'references' (ID, TITLE, URL, DATE, ACTIVE, IMAGE) VALUES (".$line[0].",'".$line[1]."','".$line[3]."','".$line[4]."',".$line[5].",'".$line[2]."');<br>";
	}

?>