package lt.rtfb.rdr;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


public class WebAppInterface {
    Context mContext;

    // Instantiate the interface and set the context
    WebAppInterface(Context c) {
        mContext = c;
    }

    // Show a toast from the web page
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void passData(String []data) {
        android.util.Log.i("zzz", "Len: " + data.length);
        for (String d : data) {
            if (d == null) {
                android.util.Log.i("zzz", "null");
            } else {
                android.util.Log.i("zzz", "'" + d + "'");
            }
        }
    }
}
