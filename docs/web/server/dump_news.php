<?php
	set_time_limit(0);
	
	if(!isset($_GET["t"])){
		echo "0";
		exit();
	}

	include("./function.php");
	require_once("../wp-load.php");
	
	try{
		$newsCat = 57; //todo
		
		//father == 27
		$args = array(
		'posts_per_page'=>-1, 
			'numberposts'=>-1,
			'orderby' => 'post_modified',
			'post_type' => 'post',
			'date_query' =>
				array(
					array(
						'column' => "post_modified",
						'after' => dedata($_GET["t"]),
					),
				),
		);

		$news = get_posts($args);
		$result = "1";
		
		if(count($news) == 0){
			echo "0;NOTHING";
			exit();
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
			$active = status($new->post_status);
			$image = encode(get_the_post_thumbnail_url($id)); //todo
			$post = endate($new->post_modified);
			$content = encode(get_post_permalink($id));//encode(file_get_contents(get_post_permalink($id)));// encode($new->post_content);
			
			$array = array($id, $title, $image, $content, $active, $date,"0", $post);
			
			$result .= implode("_-_-_", $array);
		}
		
		if($result == "1"){
			echo "0;NULL";
			exit();
		}
		
		file_put_contents("dump_news.txt", $result);
		
		echo "1";
		file_put_contents("last_time.txt", date("Ym"));
		exit();
	}catch(Exception $e){
		//echo $e.getMessage(); //todo sil
		echo "0";
		exit();
	}
	


?>
