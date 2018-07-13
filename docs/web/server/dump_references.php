<?php
	set_time_limit(0);
	
	if(!isset($_GET["t"])){
		echo "0";
		exit();
	}

	include("./function.php");
	require_once("../wp-load.php");
	
	try{
		
		$newsCat = "57"; //todo
		
		//father == 27
		$args = array(
			'posts_per_page'=>-1, 
			'numberposts'=>-1,
			'orderby' => 'post_modified',
			'order' => 'desc',
			'post_type' => 'post',
			'date_query' =>
				array(
					array(
						'column' => "post_modified",
						'after' => dedata($_GET["t"]),
					),
				),
			
		);
		

		$references = get_posts($args);
		$result = "1";
		
		if(count($references) == 0){
			echo "0";
			exit();
		}
		
		foreach($references as $reference){
			$id = $reference->ID;
		
			$category = get_the_category($id)[0]->cat_ID;
			$father = get_the_category($id)[0]->cat_ID;
			
			$newsCats = array(57, 142, 177, 20, 63, 	78,74, 91,309,64);
			
			$checkFather = false;
			
			for($i = 0; $i < count($newsCats); $i++){
				if($category == $newsCats[$i]){
					$checkFather = true;
					break;
				}
			}
			
			if($checkFather){
				continue;
			}

			$result .= ";";
			$title = encode($reference->post_title);
			$active = status($reference->post_status);
			$url = encode(get_post_permalink($id));//encode($reference->post_content);//encode(get_post_permalink($id)); // encode(file_get_contents(get_post_permalink($id)));//encode($reference->post_content);
			$post = endate($reference->post_modified);
			$date = encode($reference->post_date);
			$image =  encode(get_the_post_thumbnail_url($id));
	
	
			$array = array($id, $title, $image, $url,$date,$active, "0", $post);
			
			$result .= implode("_-_-_", $array);
			
			//$result = get_metadata($id, "aioseop_description",true)."--".get_post_meta($reference->ID,'seo_description',true)."--".get_post_meta($reference->ID,'_headspace_description',true)."--".get_post_meta($reference->ID, '_yoast_wpseo_metadesc', true);
			
		}
		
		if($result == "1"){
			echo "0";
			exit();
		}
		
		file_put_contents("dump_references.txt", $result);
		
		echo "1";
		
		file_put_contents("last_time.txt", date("Ym"));
		exit();
	}catch(Exception $e){
		//echo $e.getMessage(); //todo sil
		echo "0";
		exit();
	}
	


?>
