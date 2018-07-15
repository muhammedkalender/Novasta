package tr.com.novasta.novasta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Sync extends BroadcastReceiver {
    public void references() {
        try {
                       /*
                        0 => TIME
                        1 => TITLE
                        1 => TITLE
                        2 => SITE
                        3 => GSM
                        4 => FAX
                        5 => ADRESS
                     */

            new Thread(new Runnable() {
                @Override
                public void run() {
                    cdb db = new cdb(clib.context);

                    long lastDate = db.last("LAST_UPDATE_references");
                    //String lastTime = lib.webData("https://novasta.com.tr/mobile.php?p=references_t_" + lastDate);
                    //String lastTime = clib.webData("https://novasta.com.tr/update/reference.txt");

                    if (lastDate == 0) {
                        Log.e("DB BOŞ DÖNDÜ", "DB BOŞ DÖNDÜ");
                        return;
                    }
                    String lastTime = clib.webData("https://novasta.com.tr/mobile/dump_references.txt");
                    if (lastTime.equals("")) {
                        Log.e("Olmadı", "be reyiz");
                        return;
                    } else {
                        String query[] = lastTime.split(";");

                        if (query[0].equals("1")) {
                        } else {
                            Log.e("UPDATE", "BİRŞEYLER YANLIŞ");
                            return;
                        }

                        for (int i = 1; i < query.length; i++) {
                            try {
                                String pieceOQuery[] = query[i].split("_-_-_");

                                //ID, TITLE, FATHER, ACTIVE, IMAGE, CONTENT, FAQS, DATE
                                if (pieceOQuery.length != 8) {
                                    Log.e("UPDATE_REFERENCES", "EKSIK-" + pieceOQuery.length);
                                    continue;
                                }

                                for (int x = 0; x < 8; x++) {
                                    pieceOQuery[x] = clib.encode(pieceOQuery[x]);
                                }

                                if (db.checkReferences(Integer.parseInt(pieceOQuery[0]))) {
                                    if (db.exec("UPDATE 'references' SET TITLE = '" + pieceOQuery[1] +
                                            "', IMAGE = '" + pieceOQuery[2] +
                                            "', URL = '" + pieceOQuery[3] +
                                            "', DATE = '" + pieceOQuery[4] +// "', DATE = '" + pieceOQuery[5].substring(0, pieceOQuery[5].length() - 8) +
                                            "', ACTIVE = " + pieceOQuery[5] +
                                            ", CACHE = " + pieceOQuery[6] +
                                            " WHERE ID =" + pieceOQuery[0])) {

                                        //if (db.last("LAST_UPDATE_references", Long.parseLong(pieceOQuery[7]))) {
                                          //  Log.e("UPDATE_REFERENCE", "OK");
                                         //   db.references(Integer.parseInt(pieceOQuery[0]));
                                       // } else {
                                         //   Log.e("UPDATE_REFERENCE", "PASS");
                                     //   }
                                    } else {
                                        Log.e("UPDATE", "SORUN 3");
                                    }
                                } else {
                                    if (db.exec("INSERT INTO 'references' " +
                                            "(ID, TITLE, IMAGE, URL, DATE, ACTIVE,CACHE) VALUES " +
                                            "(" + pieceOQuery[0] + ", '" + pieceOQuery[1] + "','" + pieceOQuery[2] + "','" + pieceOQuery[3] + "','" + pieceOQuery[4] + "'," + pieceOQuery[5] + "," + pieceOQuery[6]+ ")")) {
                                        // "(" + pieceOQuery[0] + ", '" + pieceOQuery[1] + "','" + pieceOQuery[2] + "','" + pieceOQuery[3] + "','" + pieceOQuery[4] + "','" + pieceOQuery[5].substring(0, pieceOQuery[5].length() - 8) + "'," + pieceOQuery[6] + "," + pieceOQuery[7] + ")")) {
                                     //   if (db.last("LAST_UPDATE_references", Long.parseLong(pieceOQuery[7]))) {
                                      //      Log.e("INSERT_REFERENCE", "OK");
                                       // }
                                    } else {
                                        Log.e("INSERT_REFERENCE", "PASS");
                                    }
                                }
                            } catch (Exception e) {
                                clib.err(58163, e);
                            }
                        }
                    }

                }
            }).start();
        } catch (Exception e) {
            clib.err(501, e);
        }

    }

    public void info() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cdb db = new cdb(clib.context);

                    long lastDate = db.last("LAST_UPDATE_info");
                    //String lastTime = clib.webData("https://widerspiel.com/check.php?i=info_t_" + lastDate);


                    if (lastDate == 0) {
                        Log.e("DB BOŞ DÖNDÜ", "DB BOŞ DÖNDÜ");
                        return;
                    }
                    String lastTime = clib.webData("https://novasta.com.tr/mobile/check_i"); //todo
                    if (lastTime.equals("")) {
                        Log.e("INFO", "UPDATE YOK");
                        return;
                    } else {
                        String query[] = lastTime.split(";");

                        if (query[0].equals("1")) {
                        } else {
                            Log.e("INFO_UPDATE", "HATALI ÇIKTI");
                            return;
                        }

                        for (int i = 1; i < query.length; i++) {
                            try {
                                String pieceOQuery[] = query[i].split("_-_-_");

                                //ID, TITLE, FATHER, ACTIVE, IMAGE, CONTENT, FAQS, DATE
                                if (pieceOQuery.length != 15) {
                                    Log.e("INFO_UPDATE", "EKSIK-" + pieceOQuery.length);
                                    continue;
                                }

                                for (int x = 0; x < 14; x++) {
                                    pieceOQuery[x] = clib.encode(pieceOQuery[x]);
                                }

                                for (int j = 0; j < 13; j += 2) {
                                    if (!db.company(pieceOQuery[j], pieceOQuery[j + 1], true)) {
                                        Log.e("UPDATE_INFO", pieceOQuery[j] + "--" + pieceOQuery[j + 1]);
                                    }
                                }

                               // db.last("LAST_UPDATE_info", Long.parseLong(pieceOQuery[14]));
                            } catch (Exception e) {
                                clib.err(58963, e);
                            }
                        }
                    }

                }
            }).start();
        } catch (Exception e) {
            clib.err(502, e);
        }

    }

    public void categorises() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("CAT", "START");

                        cdb db = new cdb(clib.context);

                        long lastDate = db.last("LAST_UPDATE_categorises");

                        //  String lastTime = lib.webData("http://192.168.1.35/catout.html");
                        //  String lastTime = lib.webData("https://novasta.com.tr/mobile.php?p=categories_t_" + lastDate);
                        //String lastTime = clib.webData("https://novasta.com.tr/update/cat.txt");

                        if (lastDate == 0) {
                            Log.e("CAT_DB_NULL", "N");
                            return;
                        }

                        String lastTime = clib.webData("https://novasta.com.tr/mobile/dump_categorises.txt");


                        if (lastTime.equals("")) {
                            Log.i("CAT", "NULL");
                            return;
                        } else {
                            String query[] = lastTime.split(";");

                            if (query[0].equals("1")) {
                            } else {
                                Log.e("AF", query[0]);
                                Log.e("CATEGORIESES_UPDATE", "FORMAT");
                                return;
                            }

                            for (int i = 1; i < query.length; i++) {
                                try {
                                    String pieceOQuery[] = query[i].split("_-_-_");

                                    //ID, TITLE, FATHER, ACTIVE, IMAGE, CONTENT, FAQS, DATE
                                    if (pieceOQuery.length != 7) {
                                        Log.e("UPDATE", "Bişeyler eksik" + pieceOQuery.length);
                                        continue;
                                    }

                                    for (int x = 0; x < 7; x++) {
                                        pieceOQuery[x] = clib.encode(pieceOQuery[x]);
                                    }

                                    if (db.checkCategorises(Integer.parseInt(pieceOQuery[0]))) {
                                        if (db.exec("UPDATE categorises SET TITLE = '" + pieceOQuery[1] +
                                                "', FATHER = " + pieceOQuery[2] +
                                                ", ACTIVE = " + pieceOQuery[3] +
                                                ", URL = '" + pieceOQuery[4] +
                                                "', CACHE = " + pieceOQuery[5] + " WHERE ID =" + pieceOQuery[0])) {
                                           // if (db.last("LAST_UPDATE_categorises", Long.parseLong(pieceOQuery[6]))) {
                                            //    Log.e("UPDATE", "BAŞARILI");
                                             //   db.categorises(Integer.parseInt(pieceOQuery[0]));
                                            //} else {
                                              //  Log.e("UPDATE", "SORUN 2");
                                           // }
                                        } else {
                                            Log.e("UPDATE", "SORUN 3");
                                        }
                                    } else {
                                        if (db.exec("INSERT INTO categorises " +
                                                "(ID, TITLE, FATHER, ACTIVE, URL, CACHE) VALUES " +
                                                "(" + pieceOQuery[0] + ", '" + pieceOQuery[1] + "'," + pieceOQuery[2] + "," + pieceOQuery[3] + ",'" + pieceOQuery[4] + "'," + pieceOQuery[5] + ")")) {
                                           // if (db.last("LAST_UPDATE_categorises", Long.parseLong(pieceOQuery[6]))) {
                                             //   Log.e("INSER", "BASARILI MI ?");
                                               // Log.e("conrt", pieceOQuery[6] + "");
                                            //}
                                        } else {
                                            Log.e("INSERT", "OLMADI BE REYIZZ");
                                        }
                                    }
                                } catch (Exception e) {
                                    clib.err(58963, e);
                                }
                            }
                        }
                    } catch (Exception e) {
                        clib.err(256, e);
                    }
                }
            }).start();
        } catch (Exception e) {
            clib.err(500, e);
        }
    }

    public void news() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cdb db = new cdb(clib.context);

                    long lastDate = db.last("LAST_UPDATE_news");
                    //   String lastTime = lib.webData("https://novasta.com.tr/mobile.php?p=news_t_" + lastDate);


                    if (lastDate == 0) {
                        Log.e("NEWS_DB_NULL", "BOŞ DÖNDÜ");
                        return;
                    }
                    String lastTime = clib.webData("https://novasta.com.tr/mobile/dump_news.txt");
                    if (lastTime.equals("")) {
                        Log.e("asdas", "a2");
                        return;
                    } else {
                        String query[] = lastTime.split(";");

                        if (query[0].equals("1")) {
                        } else {
                            Log.e("NEWS_UPDATE", "FORMAT UYMUYOR");
                            return;
                        }

                        for (int i = 1; i < query.length; i++) {
                            try {
                                String pieceOQuery[] = query[i].split("_-_-_");

                                //ID, TITLE, FATHER, ACTIVE, IMAGE, CONTENT, FAQS, DATE
                                if (pieceOQuery.length != 8) {
                                    Log.e("NEWS_UPDATE", "EKSIK" + pieceOQuery.length);
                                    continue;
                                }

                                for (int x = 0; x < 8; x++) {
                                    pieceOQuery[x] = clib.encode(pieceOQuery[x]);
                                }

                                if (db.checkNews(Integer.parseInt(pieceOQuery[0]))) {
                                    if (db.exec("UPDATE news SET TITLE = '" + pieceOQuery[1] +
                                            "', IMAGE = '" + pieceOQuery[2] +
                                            "', URL = '" + pieceOQuery[3] +
                                            "', ACTIVE = " + pieceOQuery[4] +
                                            //  ", DATE ='" + pieceOQuery[6].substring(0, pieceOQuery[6].length() - 8) +
                                            ", DATE ='" + pieceOQuery[5] +
                                            "', CACHE = " + pieceOQuery[6] + " WHERE ID =" + pieceOQuery[0])) {
                                        Log.e("date", pieceOQuery[7]);
                                       // if (db.last("LAST_UPDATE_news", Long.parseLong(pieceOQuery[7]))) {
                                         //   Log.e("UPDATE", "BAŞARILI");
                                           // db.news(Integer.parseInt(pieceOQuery[0]));
                                        //} else {
                                          //  Log.e("UPDATE", "SORUN 2");
                                       // }
                                    } else {
                                        Log.e("UPDATE", "SORUN 3");
                                    }
                                } else {
                                    if (db.exec("INSERT INTO news " +
                                            "(ID, TITLE, IMAGE, URL, ACTIVE, DATE, CACHE) VALUES " +
                                            "(" + pieceOQuery[0] + ", '" + pieceOQuery[1] + "','" + pieceOQuery[2] + "','" + pieceOQuery[3] + "','" + pieceOQuery[4] + "','" + pieceOQuery[5] + "','" + pieceOQuery[6] + "')")) {
                                        //"(" + pieceOQuery[0] + ", '" + pieceOQuery[1] + "','" + pieceOQuery[2] + "','" + pieceOQuery[3] + "','" + pieceOQuery[4] + "','" + pieceOQuery[5] + "','" + pieceOQuery[6].substring(0, pieceOQuery[6].length() - 8) + "'," + pieceOQuery[7] + ")")) {

                                     //   if (db.last("LAST_UPDATE_news", Long.parseLong(pieceOQuery[7]))) {
                                         //   Log.e("INSER", "BASARILI MI ?");
                                       // }
                                    } else {
                                        Log.e("INSERT", "OLMADI BE REYIZZ");
                                    }
                                }
                            } catch (Exception e) {
                                clib.err(368963, e);
                            }
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            clib.err(520, e);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            if(hour < 8){
                Log.e("SABAH ERKEN","ERKEN");
                return;
            }

            cdb db = new cdb(clib.context);

            int last = (int) db.last("LAST_UPDATE_last_time");

            Date date = new Date();

            ///https://stackoverflow.com/a/22819017

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+03:00"));

            int month = Integer.parseInt(simpleDateFormat.format(date));

            if (last == month) {
                Log.e("GUNCEL", "GUNCEL");
                return;
            } else {
                Log.e("asda", "GGL");
                try {
                    categorises();
                    news();
                   //todo info();
                    references();

                    db.last("LAST_UPDATE_last_time", month);
                    db.last("LAST_UPDATE_references",month);
                    db.last("LAST_UPDATE_categorises",month);
                    db.last("LAST_UPDATE_news",month);
                    //todo info
                } catch (Exception e) {
                    clib.err(2563, e);
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            clib.err(2568, e);
        }
    }
}