package tr.com.novasta.novasta;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class clib {
    public static Context context;

    public static void err(int ERR_ID, Exception E) {
        try {
            Log.e("ERR_" + ERR_ID, E.getMessage() + "");
            //todoFirebaseCrash.report(E);
        } catch (Exception ex) {

        }
    }


    public static boolean glide(String URL, ImageView VIEW) {
        try {
            Glide.with(context).load(URL).into(VIEW);
            return true;
        } catch (Exception e) {
            err(2596, e);
            return false;
        }
    }

    public static void err(int ERR_ID, Exception E, String DESC) {
        try {
            Log.e("ERR_" + ERR_ID, E.getMessage() + "-" + DESC);
            //todo FirebaseCrash.report(E);
        } catch (Exception ex) {

        }
    }

    public static void err(int ERR_ID, OutOfMemoryError E) {
        try {
            Log.e("ERR_OOM_" + ERR_ID, E.getMessage() + "");
            //todo   FirebaseCrash.report(E);
        } catch (Exception ex) {

        }
    }

    public static int value(int RES_ID, int DEFAULT) {
        try {
            return context.getResources().getInteger(RES_ID);
        } catch (Exception e) {
            err(40487, e);
            return DEFAULT;
        }
    }

    public static int value(int RES_ID, int DEFAULT, boolean isDimen) {
        try {
            return context.getResources().getDimensionPixelSize(RES_ID);
        } catch (Exception e) {
            err(40487, e);
            return DEFAULT;
        }
    }

    public static String value(int RES_ID, String DEFAULT) {
        try {
            return context.getResources().getString(RES_ID);
        } catch (Exception e) {
            err(4099, e);
            return DEFAULT;
        }
    }

    public static String value(int RES_ID, String DEFAULT, String SUM) {
        try {
            return context.getResources().getString(RES_ID, SUM);
        } catch (Exception e) {
            err(40, e);
            return DEFAULT;
        }
    }

    public static String urlencode(String TEXT) {
        try {
            TEXT = TEXT.replace("&", "_AND_");
            TEXT = TEXT.replace("=", "_EQUAL_");
            TEXT = TEXT.replace("?", "_SORU_");


            return TEXT;
        } catch (Exception e) {
            err(2569, e);
            return TEXT;
        }
    }

    public static String encode(String TEXT) {
        try {
            if (TEXT == null) {
                TEXT = "";
            }

            String result = TEXT.replace("'", "___!SINGLE_QUOTES!___");

            result = result.replaceAll("\"", "___!QUOTES!___");
            result = result.replaceAll("<", "___!LESS!___");
            result = result.replaceAll(">", "___!MORE!___");
            result = result.replaceAll("#", "___!HASTAG!___");
            result = result.replaceAll(";", "___!SEMICOLON!___");
            result = result.replaceAll(":", "___!TWO_POINT!___");
            result = result.replaceAll("--", "___!DOUBLE_TRE!___");
            result = result.replaceAll("&quot;", "___!QUOTES_ALT!___");
            result = result.replaceAll("&#039;", "___!SINGLE_QUOTES_ALT!___");

            return result;
        } catch (Exception e) {
            err(910, e);
            return TEXT;
        }
    }

    public static String decode(String TEXT) {
        try {
            if (TEXT == null) {
                TEXT = "";
            }

            String result = TEXT.replace("___!SINGLE_QUOTES!___", "'");
            result = result.replaceAll("___!QUOTES!___", "\"");
            result = result.replaceAll("___!LESS!___", "<");
            result = result.replaceAll("___!MORE!___", ">");
            result = result.replaceAll("___!HASTAG!___", "#");
            result = result.replaceAll("___!SEMICOLON!___", ";");
            result = result.replaceAll("___!TWO_POINT!___", ":");
            result = result.replaceAll("___!DOUBLE_TRE!___", "--");
            result = result.replaceAll("___!QUOTES_ALT!___", "&quot;");
            result = result.replaceAll("___!SINGLE_QUOTES_ALT!___", "&#039;");

            return result;
        } catch (Exception e) {
            err(911, e);
            return TEXT;
        }
    }


    public static int config(String TAG, int DEFAULT) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            return sharedPreferences.getInt(TAG, DEFAULT);
        } catch (Exception e) {
            err(563, e);
            return DEFAULT;
        }
    }

    public static String webData(String SURL) {
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(SURL);

            InputStream is = url.openStream();  // throws an IOException

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            return result;
        } catch (MalformedURLException e) {
            err(900, e);
            return "";
        } catch (IOException e) {
            err(901, e);
            return "";
        } catch (Exception e) {
            err(902, e);
            return "";
        }
    }

    public static boolean config(String TAG, int VALUE, boolean isEdit) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(TAG, VALUE);
            editor.apply();
            return true;
        } catch (Exception e) {
            err(5666, e);
            return false;
        }
    }

    public static String read(String PATH) {
        try {
            String text = "";

            InputStream inputStream = context.getAssets().open(PATH);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            text = new String(buffer);


            return text;
        } catch (OutOfMemoryError e) {
            err(8633, e);
            return "";
        } catch (FileNotFoundException e) {
            err(8663, e);
            return "";
        } catch (Exception e) {
            err(8632, e);
            return "";
        }
    }
}
