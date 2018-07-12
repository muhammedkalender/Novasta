package tr.com.novasta.novasta;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView webView;
    ProgressDialog progressDialog;
    int lastType;
    int lastId;
    cdb db;
    cproject project;
    boolean redirect;
    RelativeLayout rlmain;

    RecyclerView rvMain, rvSearch;
    String[] elements = {

            "header_main",
            "header_meta",
            "full_slider_1",
            "footer",
            "footer",
            "socket",
            "scroll-top-link",
            "recommend_android"
    };

    String[] clases = {
            "av-special-heading av-special-heading-h2  blockquote modern-quote modern-centered  avia-builder-el-25  el_after_av_hr  el_before_av_hr",
            "sidebar sidebar_right smartphones_sidebar_active alpha units",
            "hr hr-custom hr-center hr-icon-no   avia-builder-el-26  el_after_av_heading  el_before_av_postslider",
            "hr hr-custom hr-left hr-icon-no   avia-builder-el-24  el_after_av_one_half  el_before_av_heading",
            //"av-special-heading-tag",
            "avia-content-slider avia-content-slider-active avia-content-slider1 avia-content-slider-even  avia-builder-el-27  el_after_av_hr  avia-builder-el-last",
            "stretch_full container_wrap alternate_color dark_bg_color title_container",
            "comment_container",
            "comment_meta_container",
            "related_posts clearfix av-related-style-tooltip",
            "related_posts clearfix av-related-style-tooltip",
            "entry-footer"
    };

    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Item> itemsSearch = new ArrayList<>();
    FrameLayout.LayoutParams paramsHide, paramsShow;
    View.OnClickListener listenerItem, listenerFather;

    List<Boolean> mapHide = new ArrayList<>();
    List<LinearLayout> mapFather = new ArrayList<>();

    List<Item> buildSearch(Cursor references, Cursor news, Cursor categorises) {
        try {
            ArrayList<Item> temp = new ArrayList<>();

            if (references.getCount() != 0) {
                while (references.moveToNext()) {
                    try {
                        int id = references.getInt(0);
                        String title = clib.decode(references.getString(1));
                        String image = clib.decode(references.getString(2).split("//__//")[0]);
                        String description = clib.decode(references.getString(3));
                        String date = clib.decode(references.getString(4));

                        temp.add(new Item(id, title, description, date, clib.value(R.string.reference, ""), image, 0));
                    } catch (OutOfMemoryError e) {
                        clib.err(101, e);
                    } catch (Exception e) {
                        clib.err(100, e);
                    }
                }
            }

            if (news.getCount() != 0) {
                while (news.moveToNext()) {
                    try {
                        int id = news.getInt(0);
                        String title = clib.decode(news.getString(1));
                        String image = clib.decode(news.getString(2));
                        String description = clib.decode(news.getString(3));
                        String date = clib.decode(news.getString(4));

                        temp.add(new Item(id, title, description, date, clib.value(R.string.news, ""), image, 1));

                    } catch (OutOfMemoryError e) {
                        clib.err(103, e);
                    } catch (Exception e) {
                        clib.err(102, e);
                    }
                }
            }

            if (categorises.getCount() != 0) {
                while (categorises.moveToNext()) {
                    try {
                        int id = categorises.getInt(0);

                        String title = clib.decode(categorises.getString(1));

                        String image = ""; // lib.decode(categorises.getString(2).split("//__//")[0]);

                        String description = clib.decode(categorises.getString(3));

                        String date = "";
                        String category = "";

                        if (db.categorises(id, "father", 0) != 0) {

                            category = db.categorises(db.categorises(id, "father", 0), "title", "");
                        }

                        //todo category +"/"+ title

                        temp.add(new Item(id, title, description, date, category, image, 2));

                    } catch (OutOfMemoryError e) {
                        clib.err(111, e);
                    } catch (Exception e) {
                        clib.err(110, e);
                    }
                }
            }
            return temp;
        } catch (OutOfMemoryError e) {
            clib.err(68, e);
            return new ArrayList<>();
        } catch (Exception e) {
            clib.err(67, e);
            return new ArrayList<>();
        }
    }

    List<Item> buildRecycler() {
        try {
            Cursor references = db.searchInReferences("");
            Cursor news = db.searchInNews("");

            List<Item> result = new ArrayList<>();

            for (int i = 0; i < references.getCount() + news.getCount(); i++) {
                if (i % 2 == 0) {
                    if (references.moveToNext()) {
                        try {
                            int id = references.getInt(0);
                            String title = clib.decode(references.getString(1));
                            String image = clib.decode(references.getString(2).split("//__//")[0]);
                            String description = clib.decode(references.getString(3));
                            String date = clib.decode(references.getString(4));

                            result.add(new Item(id, title, description, date, clib.value(R.string.reference, ""), image, 0));
                        } catch (OutOfMemoryError e) {
                            clib.err(1401, e);
                        } catch (Exception e) {
                            clib.err(1400, e);
                        }
                    } else {
                        try {
                            int id = news.getInt(0);
                            String title = clib.decode(news.getString(1));
                            String image = clib.decode(news.getString(2));

                            String description = clib.decode(news.getString(3));
                            String date = clib.decode(news.getString(4));

                            result.add(new Item(id, title, description, date, clib.value(R.string.news, ""), image, 1));

                        } catch (OutOfMemoryError e) {
                            clib.err(11203, e);
                        } catch (Exception e) {
                            clib.err(11202, e);
                        }
                    }
                } else {
                    if (news.moveToNext()) {
                        try {
                            int id = news.getInt(0);
                            String title = clib.decode(news.getString(1));
                            String image = clib.decode(news.getString(2));
                            String description = clib.decode(news.getString(3));
                            String date = clib.decode(news.getString(4));

                            result.add(new Item(id, title, description, date, clib.value(R.string.news, ""), image, 1));

                        } catch (OutOfMemoryError e) {
                            clib.err(1103, e);
                        } catch (Exception e) {
                            clib.err(1102, e);
                        }
                    } else {
                        try {
                            int id = references.getInt(0);
                            String title = clib.decode(references.getString(1));
                            String image = clib.decode(references.getString(2).split("//__//")[0]);
                            String description = clib.decode(references.getString(3));
                            String date = clib.decode(references.getString(4));

                            result.add(new Item(id, title, description, date, clib.value(R.string.reference, ""), image, 0));
                        } catch (OutOfMemoryError e) {
                            clib.err(1301, e);
                        } catch (Exception e) {
                            clib.err(1300, e);
                        }
                    }
                }
            }

            return result;
        } catch (Exception e) {
            clib.err(2596, e);
            return null;
        }
    }


    void search() {
        try {
            ((RelativeLayout) findViewById(R.id.rlSearch)).setVisibility(View.VISIBLE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(clib.context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            linearLayoutManager.scrollToPosition(0);


            final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(itemsSearch, new RecyclerAdapter.ItemListener() {
                @Override
                public void onItemClick(View v, int position) {
                    //todo
                    item(itemsSearch.get(position));
                }
            });

            if (rvSearch == null) {
                rvSearch = findViewById(R.id.rvSearch);
                rvSearch.setHasFixedSize(true);
                rvSearch.setLayoutManager(linearLayoutManager);

                rvSearch.setItemAnimator(new DefaultItemAnimator());
            }

            rvSearch.setVisibility(View.VISIBLE);
            rvSearch.bringToFront();

            final EditText etOnSearch = findViewById(R.id.etOnSearch);

            etOnSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if (etOnSearch.getText().length() < 3) {
                            return;
                        }


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.e("asda", "O6");
                                    String keyword = etOnSearch.getText().toString();

                                    List<Item> items = buildSearch(db.searchInReferences(keyword), db.searchInNews(keyword), db.searchInCategories(keyword));
                                    Log.e("asda", "O4");
                                    itemsSearch.clear();
                                    Log.e("asda", "O5");
                                    Log.e("asda", "O2");
                                    for (int i = 0; i < items.size(); i++) {
                                        itemsSearch.add(items.get(i));
                                    }
                                    Log.e("asda", "O1");
                                    if (items.size() == 0) {
                                        itemsSearch.add(new Item(0, clib.value(R.string.nothing_header, ""), clib.value(R.string.nothing, ""), " ", "", "", -1));
                                    }
                                    Log.e("asda", "O3");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                rvSearch.setAdapter(recyclerAdapter);
                                            } catch (Exception e) {
                                                clib.err(29653, e);
                                            }
                                        }
                                    });
                                    Log.e("asda", "O12");
                                } catch (Exception e) {
                                    clib.err(2596, e);
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        clib.err(2569, e);
                    }
                }
            });

        } catch (OutOfMemoryError e) {
            clib.err(64, e);
        } catch (Exception e) {
            clib.err(63, e);
        }
    }

    void buildMenu() {
        try {
            paramsHide = new FrameLayout.LayoutParams(-1, 0);
            paramsShow = new FrameLayout.LayoutParams(-1, clib.value(R.dimen.item_height, 0, true));

            listenerItem = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int id = Integer.parseInt(view.getTag().toString().split(",")[0]);
                        int type = Integer.parseInt(view.getTag().toString().split(",")[1]);

                        if (type == db.TYPE_CATEGORISES) {
                            //cat
                            loadcategorises(id);
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        } else if (type == db.TYPE_NEWS) {
                            //page
                            loadnews(id);
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        } else if (type == db.TYPE_REFERENCES) {
                            //ref
                            loadreferences(id);
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        }
                    } catch (Exception e) {
                        clib.err(1456, e);
                    }

                }
            };

            listenerFather = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int position = (int) view.getTag();
                        if (mapHide.get(position)) {
                            view.findViewById(R.id.tvTitle).setBackgroundColor(getResources().getColor(R.color.menu_background));

                            //mapFather.get(position).setLayoutParams(paramsShow);
                            mapFather.get(position).setVisibility(View.GONE);
                            mapHide.set(position, false);
                        } else {
                            view.findViewById(R.id.tvTitle).setBackgroundColor(getResources().getColor(R.color.menu_green));
                            mapFather.get(position).setVisibility(View.VISIBLE);//mapFather.get(position).setLayoutParams(paramsHide);
                            mapHide.set(position, true);
                        }


                    } catch (Exception e) {
                        clib.err(1456, e);
                    }
                }
            };

            LinearLayout llMenu = findViewById(R.id.llMenu);

            View tView = View.inflate(clib.context, R.layout.menu_father, null);
            tView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (((RelativeLayout) findViewById(R.id.rlSearch)) != null) {
                            ((RelativeLayout) findViewById(R.id.rlSearch)).setVisibility(View.INVISIBLE);
                        }


                        if (webView != null) {
                            findViewById(R.id.wvfather).setVisibility(View.INVISIBLE);
                            rvMain.setVisibility(View.VISIBLE);
                            rvMain.scrollToPosition(0);
                            // webView.setVisibility(View.INVISIBLE);
                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        }

                        rvMain.scrollToPosition(0);
                    } catch (Exception e) {
                        clib.err(14858, e);
                    }
                }
            });
            ((TextView) tView.findViewById(R.id.tvTitle)).setText(clib.value(R.string.main_menu, ""));
            llMenu.addView(tView);

            Cursor fathers = db.categorises();

            while (fathers.moveToNext()) {
                try {
                    int id = fathers.getInt(0);
                    String title = clib.decode(fathers.getString(2));

                    View view = View.inflate(clib.context, R.layout.menu_father, null);
                    ((TextView) view.findViewById(R.id.tvTitle)).setText(title);

                    LinearLayout father = (LinearLayout) view.findViewById(R.id.llFather);

                    mapFather.add(father);
                    mapHide.add(false);

                    Cursor temp = db.subCategorises(id);
                    view.setTag(mapFather.size() - 1);
                    view.setOnClickListener(listenerFather);

                    while (temp.moveToNext()) {
                        int subID = temp.getInt(0);
                        String subTitle = clib.decode(temp.getString(2));

                        View viewSub = View.inflate(clib.context, R.layout.menu_item, null);

                        ((TextView) viewSub).setText(subTitle);
                        viewSub.setTag(subID + "," + db.TYPE_CATEGORISES);
                        viewSub.setOnClickListener(listenerItem);

                        father.addView(viewSub);
                    }

                    ((LinearLayout) findViewById(R.id.llMenu)).addView(view);
                } catch (Exception e) {
                    clib.err(258, e);
                }
            }
        } catch (Exception e) {
            clib.err(360, e);
        }
    }


    void item(Item ITEM) {
        try {
            progressDialog.show();
            if (ITEM.isReference) {
                loadreferences(ITEM.id);
            } else if (ITEM.isBlog) {
                loadnews(ITEM.id);
            } else if (ITEM.isPage) {
                loadcategorises(ITEM.id);
            } else {

            }
        } catch (Exception e) {
            clib.err(24438, e);
        }
    }

    public boolean isInternetAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) clib.context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // test for connection
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            clib.err(1458, e);
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        clib.context = getApplicationContext();
        db = new cdb(clib.context);
        project = new cproject();

        if (clib.config("first_open", 0) == 0) {
            try {
                String sql = clib.read("db.sql");

                if (!sql.equals("")) {
                    String query[] = sql.split(";");

                    for (int i = 0; i < query.length; i++) {
                        db.exec(query[i]);
                    }
                }

                clib.config("first_open", 1, true);
            } catch (Exception e) {
                clib.err(6963, e);
            }
        }

        progressDialog = new ProgressDialog(Main.this);
        progressDialog.setMessage(clib.value(R.string.loading, ""));
        progressDialog.setCancelable(false);


        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, Sync.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            pendingIntent.send();

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 18000000, pendingIntent);
        } catch (PendingIntent.CanceledException e) {
            clib.err(9999, e);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(clib.context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);


        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(items, new RecyclerAdapter.ItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                //todo

                item(items.get(position));
            }
        });

        rvMain = findViewById(R.id.rcList);
        rvMain.setHasFixedSize(true);
        rvMain.setLayoutManager(linearLayoutManager);
        rvMain.setAdapter(recyclerAdapter);
        rvMain.setItemAnimator(new DefaultItemAnimator());

        Glide.with(clib.context).load(R.mipmap.ic_logo).into((ImageView) findViewById(R.id.ivLogo));

        findViewById(R.id.ivLogo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        List<Item> t = buildRecycler();//buildSearch(db.searchInReferences(""), db.searchInNews(""), db.searchInCategories(""));

        for (int i = 0; i < t.size(); i++) {
            items.add(t.get(i));
        }


        (findViewById(R.id.ivSearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    search();
                } catch (Exception e) {
                    clib.err(259, e);
                }
            }
        });

        (findViewById(R.id.ivMap)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //https://stackoverflow.com/a/2201999
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(db.company("adress_cor", "")));
                    startActivity(intent);
                    //if (!db.company("adress_cor", " ").equals("")) {
                    //  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(db.company("adress_cor", " ")));
                    // startActivity(browserIntent);
                    // todo   }
                } catch (Exception e) {
                    clib.err(90, e);
                }
            }
        });

        (findViewById(R.id.ivWP)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  PackageManager pm=getPackageManager();
                try {

                    /*    Intent waIntent = new Intent(Intent.ACTION_SEND);
                        waIntent.setType("text/plain");
                        String text = "YOUR TEXT HERE";

                        PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                        //Check if package exists or not. If not then code
                        //in catch block will be called
                        waIntent.setPackage("com.whatsapp");

                        waIntent.putExtra(Intent.EXTRA_TEXT, text);
                        //todo
                        startActivity(Intent.createChooser(waIntent, "Share with"));
*/

                    Intent browserIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + db.company("phone", "")));
                    startActivity(browserIntent);

                    //Uri uri = Uri.parse("smsto:" + db.company("phone", ""));
                    //Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    //i.setPackage("com.whatsapp");
                    //startActivity(Intent.createChooser(i, ""));
                } catch (Exception e) {
                    clib.err(996, e);
                }

            }
        });


        ((TextView) findViewById(R.id.tvHeader)).setText(clib.value(R.string.header, ""));
        clib.glide("file:///android_asset/web.jpg", (ImageView) findViewById(R.id.ivHeader));

        buildMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (((RelativeLayout) findViewById(R.id.rlSearch)).getVisibility() == View.VISIBLE) {
                ((RelativeLayout) findViewById(R.id.rlSearch)).setVisibility(View.INVISIBLE);
                rvMain.scrollToPosition(0);
                rvMain.setVisibility(View.VISIBLE);
            } else if (webView != null && webView.getVisibility() == View.VISIBLE) {
                //webView.setVisibility(View.VISIBLE);
                findViewById(R.id.wvfather).setVisibility(View.INVISIBLE);
                rvMain.scrollToPosition(0);
                rvMain.setVisibility(View.VISIBLE);
            } else {
                super.onBackPressed();
            }
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
                webView.getSettings().setAppCacheEnabled(true);
                webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 50); //50 MB
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.getSettings().setJavaScriptEnabled(true);
                //webView.addJavascriptInterface(new cjs(), "android");
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageFinished(final WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (redirect) {
                            return;
                        }

                        lastType = ptype;
                        lastId = pid;

                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < elements.length + clases.length; i++) {
                                        if (i < elements.length) {
                                            view.loadUrl("javascript:document.getElementById('" + elements[i] + "').remove();");
                                        } else {
                                            view.loadUrl("javascript:document.getElementsByClassName('" + clases[i - elements.length] + "')[0].remove();");
                                        }
                                    }
                                } catch (Exception e) {
                                    clib.err(2596, e);
                                }
                            }
                        });


/*
                    finished = true;
                    lastType = 1;
                    lastReferences = ID;


                    db.cacheReferences(ID, true);
                                     */

                     /*   for (int i = 0; i < elements.length + clases.length; i++) {
                            if (i < elements.length) {
                                view.loadUrl("javascript:document.getElementById('" + elements[i] + "').remove();");
                            } else {
                                view.loadUrl("javascript:document.getElementsByClassName('" + clases[i - elements.length] + "')[0].remove();");
                            }
                        }
*/
                        //todo todo todo cache image için cacheyi lsitle reism olmayanalrı sil :)

                        //webView.setVisibility(View.VISIBLE);
                        findViewById(R.id.wvfather).setVisibility(View.VISIBLE);
                        webView.bringToFront();
                        webView.scrollTo(0, 0);
                        rvMain.setVisibility(View.INVISIBLE);

                        if (((RelativeLayout) findViewById(R.id.rlSearch)).getVisibility() == View.VISIBLE) {
                            ((RelativeLayout) findViewById(R.id.rlSearch)).setVisibility(View.INVISIBLE);
                        }
                        db.cache(pid, ptype, true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);//Thread.sleep(1500);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                webView.setVisibility(View.VISIBLE);
                                                progressDialog.hide();
                                            } catch (Exception e) {
                                                clib.err(2157, e);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    clib.err(2563, e);
                                }
                            }
                        }).start();
                        ;


                        redirect = true;
                    }
                });
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //todo

                                if (!isInternetAvailable() && !db.cache(pid, ptype)) { //todo
                                    Snackbar.make(findViewById(R.id.rlArea), clib.value(R.string.no_cache, ""), Snackbar.LENGTH_LONG)
                                            .show();
                                    progressDialog.hide();
                                    return;
                                }
                                webView.setVisibility(View.INVISIBLE);
                                String data = pdata;

                                //if (data.length() > 4 && data.substring(0, 4).equals("http")) {
                                if (!db.cache(pid, ptype) && false) { //todo
                                    data = db.url(pid, ptype);
                                    Log.e("asda", "S4");
                                    if (data.equals("")) {
                                        //todo
                                    }

                                    data = clib.decode(clib.webData(data));

                                    if (ptype == db.TYPE_NEWS) {
                                        data = project.htmlnews(data);
                                    } else if (ptype == db.TYPE_REFERENCES) {
                                        data = project.htmlreferences(data);
                                    } else if (ptype == db.TYPE_CATEGORISES) {
                                        data = project.htmlcategorises(data);
                                    }

                                    if (db.content(pid, ptype, data)) {
                                        db.cache(pid, ptype, true);
                                    }

                                    db.description(pid, ptype, project.metadescription(data));
                                } else {
                                    data = clib.decode(data);
                                }
/*
                                String css = "<style>" + clib.read("default.css") + "</style>";
                                String jquery = "<script>" + clib.read("jquery.js") + "</script>";
                                String header = "";

                                if (ptype == db.TYPE_CATEGORISES) {
                                    header = clib.read("categories_header.html");
                                } else if (ptype == db.TYPE_NEWS) {
                                    header = clib.read("news_header.html");
                                } else if (ptype == db.TYPE_REFERENCES) {
                                    header = clib.read("reference_header.html");
                                }

                                data = css + jquery + header + data;*/
                                redirect = false;
                                webView.loadUrl(db.url(pid, ptype));
                                //  webView.loadDataWithBaseURL(ptype + "__" + pid, data, "text/html; charset=utf-8", "utf-8", ptype + "_cached_" + pid);
                            } catch (Exception e) {
                                clib.err(2000, e);
                            }
                        }
                    });
                }
            }).start();

        } catch (OutOfMemoryError e) {
            clib.err(1051, e);
        } catch (Exception e) {
            clib.err(1050, e);
        }
    }

    void loadreferences(int ID) {
        try {
            progressDialog.show();
            //todo
            String data = db.reference(ID, "content", "");
            loadData(data, ID, "", db.TYPE_REFERENCES);
        } catch (OutOfMemoryError e) {
            clib.err(1801, e);
        } catch (Exception e) {
            clib.err(1800, e);
        }
    }

    void loadcategorises(int ID) {
        try {
            progressDialog.show();
            //todo
            String data = db.categorises(ID, "content", "");
            loadData(data, ID, "", db.TYPE_CATEGORISES);
        } catch (OutOfMemoryError e) {
            clib.err(1501, e);
        } catch (Exception e) {
            clib.err(1500, e);
        }
    }

    void loadnews(int ID) {
        try {
            progressDialog.show();
            //todo
            String data = db.news(ID, "content", "");
            loadData(data, ID, "", db.TYPE_NEWS);
        } catch (OutOfMemoryError e) {
            clib.err(1001, e);
        } catch (Exception e) {
            clib.err(1000, e);
        }
    }
}
