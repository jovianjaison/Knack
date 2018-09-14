package joviananthony.knack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Web extends AppCompatActivity {
  private WebView mWebView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    Toast.makeText(getApplicationContext(), "Loading,Please wait...", Toast.LENGTH_LONG ).show();
    Bundle bundle = getIntent().getExtras();
    final String message=bundle.getString("WEBSITE");


    mWebView=findViewById(R.id.webview1);
    mWebView.getSettings().setJavaScriptEnabled(true);

    final Activity activity =this;

    mWebView.setWebViewClient(new WebViewClient());

    mWebView.loadUrl(message);
    mWebView.setWebViewClient(new Client());

  }
  class Client extends WebViewClient
  {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
      mWebView.loadUrl(url);
      return super.shouldOverrideUrlLoading(view, url);
    }
  }
}