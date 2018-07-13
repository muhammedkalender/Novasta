<?php
	set_time_limit(0);

	function search($content , $start, $end){
		$content = ' ' . $content;
		$ini = strpos($content, $start);
	
		if($ini == 0){
			return '';
		}
	
		$ini += strlen($start);
		$len = strpos($content, $end, $ini);

		return substr($content, $ini, $len);
	}

	function endata($DATA){
		$DATA = str_replace("'", "___!SINGLE_QUOTES!___", $DATA);
    	$DATA = str_replace("\"", "___!QUOTES!___", $DATA);
    	$DATA = str_replace("<", "___!LESS!___", $DATA);
    	$DATA = str_replace(">", "___!MORE!___", $DATA);
    	$DATA = str_replace("#", "___!HASTAG!___", $DATA);
   		$DATA = str_replace(";", "___!SEMICOLON!___", $DATA);
    	$DATA = str_replace(":", "___!TWO_POINT!___", $DATA);
		$DATA = str_replace("--", "___!DOUBLE_TRE!___", $DATA);
		$DATA = str_replace("&quot;", "___!QUOTES_ALT!___", $DATA);
		$DATA = str_replace("&#039;", "___!SINGLE_QUOTES_ALT!___", $DATA);
	
		return $DATA;
	}

	function dedata($DATA){
		$DATA = str_replace("___!SINGLE_QUOTES!___","'",  $DATA);
		$DATA = str_replace("___!QUOTES!___","\"", $DATA);
		$DATA = str_replace("___!LESS!___","<", $DATA);
		$DATA = str_replace("___!MORE!___",">", $DATA);
		$DATA = str_replace("___!HASTAG!___","#", $DATA);
		$DATA = str_replace("___!SEMICOLON!___",";", $DATA);
		$DATA = str_replace("___!TWO_POINT!___",":", $DATA);
		$DATA = str_replace("___!DOUBLE_TRE!___", "--", $DATA);
		$DATA = str_replace("___!QUOTES_ALT!___", "&quot;", $DATA);
		$DATA = str_replace("___!SINGLE_QUOTES_ALT!___", "&#039;", $DATA);

		return $DATE;
	}

	function endate($DATE){
		$date = substr($DATE, 0, 4);
		$date .= substr($DATE, 5, 2);
		$date .= substr($DATE, 8, 2);
		$date .= substr($DATE, 11, 2);
		$date .= substr($DATE, 14, 2);
		$date .= substr($DATE, 17, 2);

		return $date;
	}

	function dedate($DATE){
		$date = substr($DATE, - 14, 4)."-";
		$date .= substr($DATE, - 10, 2)."-";
		$date .= substr($DATE, - 8, 2)." ";
		$date .= substr($DATE, - 6, 2).":";
		$date .= substr($DATE, - 4, 2).":";
		$date .= substr($datE, - 2, 2);
		
		return $date;
	}

	function status($STATUS){
		if($STATUS == "publish"){
			return 1;
		}else{
			return 0;
		}
	}

	function enurl($DATA){
		$DATA = str_replace("?", "_SORU_", $DATA);
		$DATA = str_replace("&", "_AND_", $DATA);
		$DATA = str_replace("=", "_EQUAL_", $DATA);

		return $DATA;
	}

	function deurl($DATA){
		$DATA = str_replace("_SORU_", "?", $DATA);
		$DATA = str_replace("_AND_", "&", $DATA);
		$DATA = str_replace("_EQUAL_", "=", $DATA);

		return $DATA;
	}

	function htmlDescription($DATA){
		return str_replace('" />', "", search($DATA, '<meta name="description"  content="', '" />'));
	}

	function htmlImage($DATA){
		return search($DATA, '<meta property="og:image" content="', '" />');
	}

	function encode($text){
	$text = str_replace("'", "___!SINGLE_QUOTES!___", $text);
	
    $text = str_replace("\"", "___!QUOTES!___", $text);

    $text = str_replace("<", "___!LESS!___", $text);

    $text = str_replace(">", "___!MORE!___", $text);

    $text = str_replace("#", "___!HASTAG!___", $text);

    $text = str_replace(";", "___!SEMICOLON!___", $text);

    $text = str_replace(":", "___!TWO_POINT!___", $text);
	
	
	return $text;
}

function decode($text){
	$text = str_replace("___!SINGLE_QUOTES!___","'",  $text);
	
    $text = str_replace( "___!QUOTES!___","\"", $text);

    $text = str_replace( "___!LESS!___","<", $text);

    $text = str_replace( "___!MORE!___",">", $text);

    $text = str_replace( "___!HASTAG!___","#", $text);

    $text = str_replace( "___!SEMICOLON!___",";", $text);

    $text = str_replace( "___!TWO_POINT!___",":", $text);
	
	return $text;

}
	
	function htmlCategorises($DATA){
		$result = "";
	
		$between = search($DATA, "<div id='after_full_slider_1'", "<div class='container_wrap footer_color'");

		if($between == ""){
			$between = search($DATA, "<main", "<article");
	
			if($between == ""){
				$between = "<div id='main' class='all_colors' data-scroll-offset='0'><div class='main_color container_wrap_first container_wrap sidebar_right'><div class='container'><main".search($DATA, "<main", "<aside");
			}else{
				$between = "<div id='main' class='all_colors' data-scroll-offset='0'><div class='main_color container_wrap_first container_wrap sidebar_right'><div class='container'><main".$between;
			}
		}else{
			$between = "<div id='after_full_slider_1'".$between;
		}

		$between = str_replace("<div class='container_wrap footer_color".search($between, "<div class='container_wrap footer_color", "<footer")," ",$between);
		$between = str_replace("<aside class".search($between, "<aside class", "</aside"), " ", $between); 
		$between = str_replace(search($between, "av-special-heading av-special-heading-h2  blockquote modern-quote modern-centered  avia-builder-el-21  el_after_av_one_fi", "avia-slideshow-arrows avia-slideshow-controls"), " ", $between); 
		$between = str_replace(search($between, "avia-slideshow-arrows avia-slideshow-controls", "</div"), " ", $between); 
		$between = str_replace(search($between, "<div class='avia-content-slider-inner", "</div></div>"), " ", $between); 
		$between = str_replace("av-special-heading-tag ",'" style="display:none;"', $between);
		//echo search($between, "<aside class", "</aside");

		$result .= $between;

		return $result;
	}

	function htmlNews($DATA){
		$result = "";
		$result .= "<div class='stretch_full container_wrap alternate_color dark_bg_color title_container'>";
		$result .= "<div class='container_wrap container_wrap_first main_color sidebar_right'>";
		$result .= "<div class='container template-blog template-single-blog'>";
		$result .= "<main class='content units av-content-small alpha  av-blog-meta-author-disabled av-blog-meta-comments-disabled av-blog-meta-html-info-disabled av-blog-meta-tag-disabled' role='main' itemscope='itemscope' itemtype='https://schema.org/Blog'>";

		$between = search($DATA, "<article class='post-entry p", "</span></span></span>");
		$between = str_replace(search($between, "av-share-box", "/div>"), " ", $between);

		//$between
		$result .= "<article class='post-entry p".$between."</span></span></span>";
	
		return $result;
		//echo "<article class='post-entry p". $between."</article>";
	}

	function htmlReferences($DATA){
		$result = "";

		$result .= "<div id='main' class='all_colors' data-scroll-offset='0'>";
		$result .= "<div class='container_wrap container_wrap_first main_color sidebar_right'>";
		$result .= "<div class='container template-blog template-single-blog'>";

		$DATA = str_replace("<aside class".search($DATA, "<aside class", "</aside"), " ", $DATA); 
		$DATA = search($DATA, "<main", "/main>");

		$result .= "<main";
		$result .= str_replace(search($DATA, "av-share-box", "/div>"), " ", $DATA);

		return $result;
	}

?>