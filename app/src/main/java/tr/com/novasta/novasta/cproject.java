package tr.com.novasta.novasta;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cproject {

    String htmlnews(String pdata) {
        try {
            String patternstart = "<main";
            String patternend = "<aside";// "</main>";

            Pattern pattern = Pattern.compile(Pattern.quote(patternstart) + "(.*?)" + Pattern.quote(patternend));

            Matcher matcher = pattern.matcher(pdata);

            if (matcher.find()) {
                String sum = "<body id='top' class='post-template-default single single-post postid-6731 single-format-standard stretched open_sans arial-websave _arial' itemscope='itemscope' itemtype='https://schema.org/WebPage'>";
                sum += "<div id='wrap_all'>";
                sum += "<div id='main' class='all_colors' data-scroll-offset='0'>";
                sum += "<div class='container_wrap container_wrap_first main_color sidebar_right'>";
                sum += "<div class='container template-blog template-single-blog'>";

                sum += matcher.group(0);

                sum += "</div></div></div></div></body></html>";

                return sum;
            }

            return pdata;
        } catch (Exception e) {
            clib.err(5200, e);
            return pdata;
        }
    }

    String htmlreferences(String pdata) {
        try {
            String patternstart = "<main";
            String patternend = "</main>";

            Pattern pattern = Pattern.compile(Pattern.quote(patternstart) + "(.*?)" + Pattern.quote(patternend));

            Matcher matcher = pattern.matcher(pdata);

            if (matcher.find()) {
                String sum = "<body id='top' class='post-template-default single single-post postid-6731 single-format-standard stretched open_sans arial-websave _arial' itemscope='itemscope' itemtype='https://schema.org/WebPage'>";
                sum += "<div id='wrap_all'>";
                sum += "<div id='main' class='all_colors' data-scroll-offset='0'>";
                sum += "<div class='container_wrap container_wrap_first main_color sidebar_right'>";
                sum += "<div class='container'>";
                sum += "<div class='container template-blog template-single-blog'>";

                sum += matcher.group(0);

                sum += "</div></div></div></div></div></body></html>";

                return sum;
            }

            return pdata;
        } catch (Exception e) {
            clib.err(5100, e);
            return pdata;
        }
    }

    String htmlcategorises(String pdata) {
        try {

            Log.e("INFO_cAT", "G1");

            String result = clib.search("<main", "</main>", pdata);

            if (!result.equals("")) {
                Log.e("INFO_cAT", "2G");
                String sum = "<body id='top' class='page-template-default page page-id-958 page-parent stretched open_sans arial-websave _arial' itemscope='itemscope' itemtype='https://schema.org/WebPage'>";
                sum += "<div id='wrap_all'>";
                sum += "<div id='main' class='all_colors' data-scroll-offset='0'>";
                sum += "<div id='after_full_slider_1' class='main_color av_default_container_wrap container_wrap sidebar_right'>";
                sum += "<div class='container'>";
                //   sum += "<div class='template-page content  av-content-small alpha units'>";

                sum += result;

                sum += "</div></div></div></div></div></body></html>";

                return sum;
            }Log.e("asda",pdata+"-");

            Log.e("INFO_cAT", "G3");

            return pdata;
        } catch (Exception e) {
            clib.err(3000, e);
            return pdata;
        }
    }

    String date(String pdate) {
        try {
            String year = pdate.substring(0, 4);
            String month = pdate.substring(5, 7);
            String day = pdate.substring(8, 10);

            return (day + "-" + month + "-" + year);
        } catch (Exception e) {
            clib.err(258, e);
            return pdate;
        }
    }

    String metadescription(String pdata) {
        try {
            //todo

            String search = clib.search("e", "a", pdata);

            if (!search.equals("")) {
                //result = result.replaceAll("<meta name=\"twitter:description\" content=\"", "");
                //result = result.replaceAll("\" />", "");

                return search;
            }

            return "";
        } catch (Exception e) {
            clib.err(2100, e);
            return "";
        }
    }

    String metaimage(String pdata) {
        try {
            String patternstart = "<meta property='og:image' content='";
            String patternend = "' />";

            Pattern pattern = Pattern.compile(Pattern.quote(patternstart) + "(.*?)" + Pattern.quote(patternend));

            Matcher matcher = pattern.matcher(pdata);

            if (matcher.find()) {
                String result = matcher.group(1);
                result = result.replaceAll("<meta property='og:image' content='", "");
                result = result.replaceAll("' />", "");

                return result;
            }

            return "";
        } catch (Exception e) {
            clib.err(2000, e);
            return "";
        }
    }
}
