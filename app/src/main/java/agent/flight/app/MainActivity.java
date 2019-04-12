package agent.flight.app;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import agent.fly.com.agent.R;


public class MainActivity extends Activity {

    private WebView agent;
    //  Button b1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        agent = (WebView) findViewById(R.id.agent);
        agent.setWebViewClient(new CustomWebViewClient());
        agent.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        }else {
            agent.loadUrl("http://flight-agent.ru/");
            WebSettings webSettings = agent.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        agent.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
       agent.restoreState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String url = agent.getUrl();
        Bundle out = new Bundle();
        agent.saveState(out);
        out.putString("url", url);
        onSaveInstanceState(out);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        }else {
            agent.reload();
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (agent.canGoBack()) {
                        agent.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private class CustomWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            } else {
                view.loadUrl(url);
            }
            return true;
        }
    }
}
