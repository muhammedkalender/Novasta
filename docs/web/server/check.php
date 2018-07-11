<?php
	set_time_limit(0);

	require_once("function.php");

	function checkPages($DATE){
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
							'after' => $DATE,
						),
					),
				);
			
			$posts = get_posts($args);
		
			$result = "1";
		
			if(count($posts) == 0){
				return "0";
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
				$description = ""; //todo
				$father = $post->post_parent;
				$active = status($post->post_status);
				$image = encode(get_the_post_thumbnail_url($id)); //todo
				$faq = ""; //todo
				$content = encode(get_post_permalink($id));//encode($post->post_content);//encode(get_post_permalink($id)); // encode(file_get_contents(get_post_permalink($id)));//encode($post->post_content);
				$post = endate($post->post_modified);
			
				if($father == 0){
					$array = array("9999".$id, $title, $id, $active, $image, $faq, $content, $description, $post);
					$result .= implode("_-_-_", $array).";";
				}else{
					$page = file_get_contents(decode($content));
					$content = encode(htmlCategorises($page));
					$description = encode(metaDescription($page));
				}
			
				$array = array($id, $title, $father, $active, $image, $faq, $content, $description, $post);
			
				$result .= implode("_-_-_", $array);
			
				//$result = get_metadata($id, "aioseop_description",true)."--".get_post_meta($post->ID,'seo_description',true)."--".get_post_meta	($post->ID,'_headspace_description',true)."--".get_post_meta($post->ID, '_yoast_wpseo_metadesc', true);
			
			}
		
			if($result == "1"){
				return "0";
			}
		
			sleep(3);
		
			return $result;
		}catch(Exception $e){
			//echo $e.getMessage(); //todo sil
			return "0";
		}
	}

	function checkNews($DATE){
		try{
			$newsCat = 57; //todo
		
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
							'after' => $DATE,
						),
					),
				);
		
			$result = "1";
	
			$news = get_posts($args);
	
			if(count($news) == 0){
				return "0";
			}
		
			foreach($news as $new){
				$id = $new->ID;
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
			
				if(!$checkFather){
					continue;
				}
			
				$result .= ";";
			
				$title = encode($new->post_title);
				$date = $new->post_date;
				$description = ""; //todo
				$active = status($new->post_status);
				$image = encode(get_the_post_thumbnail_url($id)); //todo
				$post = endate($new->post_modified);
				$content = encode(get_post_permalink($id));//encode(file_get_contents(get_post_permalink($id)));// encode($new->post_content);
			
			
				$page = file_get_contents(get_post_permalink($id));
			
				$content = encode(htmlNews($page));
				$description = encode(metaDescription($page));
			
				$array = array($id, $title, $description, $image, $content, $active, $date, $post);
			
				$result .= implode("_-_-_", $array);
			}
		
			if($result == "1"){
				return "0";
			}
		
			return $result;
		}catch(Exception $e){
			//echo $e.getMessage(); //todo sil
			return "0";
		}
	}

	function checkReferences($DATE){
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
							'after' => $DATE,
						),
					),
				);
		
			$result = "1";
	
			$news = get_posts($args);
	
			if(count($news) == 0){
				return "0";
			}
	
			foreach($news as $new){
				$id = $new->ID;
				$category = get_the_category($id)[0]->cat_ID;
				$father = get_the_category($id)[0]->cat_ID;
				$newsCats = array(57, 142, 177, 20, 63, 78, 74, 91, 309, 64);
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
			
				$title = encode($new->post_title);
				$date = $new->post_date;
				$description = "";get_post_meta($new->$id, 'content', true);
				// get_post_meta($id, 'header', true);//get_post_meta($id, "og:description")[0]; //todo
				$active = status($new->post_status);
				$image = encode(get_the_post_thumbnail_url($id)); //todo
				$post = endate($new->post_modified);
				$content = encode(get_post_permalink($id)); //encode(file_get_contents(get_post_permalink($id))); //encode($new->post_content);
			
				//$result .= get_post_permalink($id);
			
				$page = file_get_contents(get_post_permalink($id));
				$content = encode(htmlReference($page));
				$description = encode(metaDescription($page));
				$array = array($id, $title, $description, $image, $content,$date, $active, $post);
			
				$result .= implode("_-_-_", $array);
			}
		
			if($result == "1"){
				return "0";
			}
		
			return $result;
		}catch(Exception $e){
			//echo $e.getMessage(); //todo sil
			return "0";
		}
	}

	$date = date("Ym")."01000000";

	$result = checkPages($date);
	file_put_contents("pages.txt", $result);

	$result = checkNews($date);
	file_put_contents("news.txt", $result);

	$result = checkReferences($date);
	file_put_contents("references.txt", $result);
?>