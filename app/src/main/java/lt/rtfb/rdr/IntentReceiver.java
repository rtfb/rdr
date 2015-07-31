package lt.rtfb.rdr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class IntentReceiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Let's display the progress in the activity title bar, like the
        // browser app does.
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir);
        Intent i = getIntent();
        String action = i.getAction();
        String type = i.getType();
        String sharedText = i.getStringExtra(Intent.EXTRA_TEXT);
        android.util.Log.i("zzz", action);
        android.util.Log.i("zzz", type);
        android.util.Log.i("zzz", sharedText);

        final WebView webview = (WebView) findViewById(R.id.main_webview);
        webview.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
            public void onPageFinished(WebView view, String url) {
                String js = "javascript:(function() {"
    + "function isScriptNode(node) {"
    + "    return node.nodeType === Node.ELEMENT_NODE && node.nodeName === \"SCRIPT\";"
    + "}"
    + "function isWhiteSpaceOnly(node) {"
    + "    return node.nodeValue.replace(/[\\n\\t ]+/, \"\") === \"\";" // Note: escapes in regexp!
    + "}"
    + "function getAllTextNodes(elem) {"
    + "    var filter = NodeFilter.SHOW_TEXT,"
    + "        walker = document.createTreeWalker(elem, filter, null, false),"
    + "        arr = [],"
    + "        node;"
    + "    while (walker.nextNode()) {"
    + "        node = walker.currentNode;"
    + "        if (node.parentNode && isScriptNode(node.parentNode)) {"
    + "            continue;"
    + "        }"
    + "        if (node.isElementContentWhitespace || isWhiteSpaceOnly(node)) {"
    + "            continue;"
    + "        }"
    + "        arr.push(node.nodeValue);" // Note: changed 'node' to 'node.nodeValue'
    + "    }"
    + "    return arr;"
    + "}"
    + "Android.passData(getAllTextNodes(document.body));"
    + "})();";
                view.loadUrl(js);
            }
        });
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");

        webview.loadUrl(sharedText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
