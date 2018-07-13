<?php
	set_time_limit(0);
	
	if(!isset($_GET["t"])){
		echo "0";
		exit();
	}

	include("./function.php");
	require_once("../wp-load.php");
	
	try{
		$args = array(
			'posts_per_page'=>-1,
			'numberposts'=>-1,
			'orderby' => 'post_modified',
			'order' => 'desc',
			'post_type' => "page",
			'date_query' =>
				array(
					array(
						'column' => "post_modified",
						'after' => dedata($_GET["t"]),
					),
				),
			);

		$posts = get_posts($args);
		$result = "1";
		
		if(count($posts) == 0){
			echo "0";
			exit();
		}
		
		foreach($posts as $post){
			$id = $post->ID;
			
			$except = array(6350, 835, 	344, 895, 355, 218, 1467, 1043,2140, 3960,893,1048, 1096, 4073,3130,1045,4594, 4658);
			
			$check = 0;
			
			for($i = 0; $i < count($except); $i++){
				if($id == $except[$i]){
					$check = 1;
				}
			}

			if($check == 1){
				continue;
			}

			$result .= ";";
			$title = encode($post->post_title);
			$father = $post->post_parent;
			$active = status($post->post_status);
			$url = encode(get_post_permalink($id));//encode($post->post_content);//encode(get_post_permalink($id)); // encode(file_get_contents(get_post_permalink($id)));//encode($post->post_content);
			$post = endate($post->post_modified);
	
			if($father == 0){
				$array = array("9999".$id, $title, $id, $active, $url, "0", $post);
				
				$result .= implode("_-_-_", $array).";";
			}
			
			$array = array($id, $title, $father, $active, $url, "0", $post);
			
			$result .= implode("_-_-_", $array);
			
			//$result = get_metadata($id, "aioseop_description",true)."--".get_post_meta($post->ID,'seo_description',true)."--".get_post_meta($post->ID,'_headspace_description',true)."--".get_post_meta($post->ID, '_yoast_wpseo_metadesc', true);
			
		}
		
		if($result == "1"){
			echo "0";
			exit();
		}
		
		
		file_put_contents("dump_categorises.txt", $result);
		
		echo "1";
		file_put_contents("last_time.txt", date("Ym"));
		exit();
	}catch(Exception $e){
		//echo $e.getMessage(); //todo sil
		echo "0";
		exit();
	}
	


?>
