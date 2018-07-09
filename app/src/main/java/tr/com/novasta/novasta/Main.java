package tr.com.novasta.novasta;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView webView;
    ProgressDialog progressDialog;

    cdb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
 /*
   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        clib.context = getApplicationContext();
        db = new cdb(clib.context);

        progressDialog = new ProgressDialog(Main.this);
        progressDialog.setMessage(clib.value(R.string.loading, ""));
        progressDialog.setCancelable(false);

        loadNews(1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void loadData(final String pdata, final int pid, final String purl, final int ptype) {
        try {
            if (webView == null) {
                webView = findViewById(R.id.wvbrowser);
                webView.getSettings().setAppCacheMaxSize(1024 * 50); //50 MB
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setScrollX(0);
                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageFinished(WebView view, String url) {
                                    /*super.onPageFinished(view, url);

                                    for (int i = 0; i < elements.length + clases.length; i++) {
                        if (i < elements.length) {
                            view.loadUrl("javascript:document.getElementById('" + elements[i] + "').remove();");
                        } else {
                            view.loadUrl("javascript:document.getElementsByClassName('" + clases[i - elements.length] + "')[0].remove();");
                        }
                    }

                    finished = true;
                    lastType = 1;
                    lastReferences = ID;


                    db.cacheReferences(ID, true);
                                     */
                        webView.setScrollX(0);
                        webView.setVisibility(View.VISIBLE);
                        webView.bringToFront();
                        progressDialog.hide();

                        db.cache(pid, ptype);
                    }
                });
            }

            webView.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //todo

                        String data = pdata;

                        if (data.substring(0, 4).equals("http")) {
                            data = clib.decode(clib.webData("https://www.novasta.com.tr/mobile/router.php?url=" + clib.urlencode(data))+"_t_"+ptype);
                        } else {
                            data = clib.decode(data);
                        }

                        String css = "<style>" + clib.read("default.css") + "</style>";
                        String jquery = "<script>" + clib.read("jquery.js") + "</script>";
                        String header = "";

                        if (ptype == db.TYPE_CATEGORISES) {
                            header = clib.read("categorises_header.html");
                        } else if (ptype == db.TYPE_NEWS) {
                            header = clib.read("news_header.html");
                        } else if (ptype == db.TYPE_REFERENCES) {
                            header = clib.read("reference_header.html");
                        }

                        data = css + jquery + header + data;

                        webView.loadDataWithBaseURL(ptype + "__" + pid, data, "text/html; charset=utf-8", "utf-8", ptype + "_cached_" + pid);
                    } catch (
                            Exception e)

                    {
                        clib.err(2000, e);
                    }
                }
            });
            //todoLog.e("asda","a");
            Log.e("asda", "a8");
        } catch (
                OutOfMemoryError e)

        {
            clib.err(1051, e);
        } catch (
                Exception e)

        {
            clib.err(1050, e);
        }

    }

    void loadNews(int ID) {
        try {
            progressDialog.show();
            //todo


            String data = ("http://google.com");
            loadData(data, 0, "", 0);
        } catch (OutOfMemoryError e) {
            clib.err(1001, e);
        } catch (Exception e) {
            clib.err(1000, e);
        }
    }
}
