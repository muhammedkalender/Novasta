<?php

	$dump = file_get_contents("dump_news.txt");

	$dump = explode(";", $dump);

	foreach ($dump as $line) {
		if($line == "1"){
			continue;
		}


		$line = explode("_-_-_", $line);

		echo "INSERT INTO news (ID, TITLE, DESCRIPTION, URL, DATE, ACTIVE, IMAGE) VALUES (".$line[0].",'".$line[1]."','".$line[2]."','".$line[4]."','".$line[6]."',".$line[5].",'".$line[3]."');<br>";
	}

?>