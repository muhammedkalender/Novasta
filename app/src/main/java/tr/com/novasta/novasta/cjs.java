package tr.com.novasta.novasta;

import android.util.Log;
import android.webkit.JavascriptInterface;

import java.lang.annotation.Annotation;

class cjs implements JavascriptInterface {
    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @JavascriptInterface
    public void result(String pdata){
        Log.e("asda",pdata+"-");
    }
}
