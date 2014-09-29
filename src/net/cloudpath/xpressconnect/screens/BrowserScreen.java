package net.cloudpath.xpressconnect.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.LoadConfigCallback;
import net.cloudpath.xpressconnect.MyWebViewClient;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.remote.GetConfigFromUrl;

public class BrowserScreen extends ScreenBase
  implements LoadConfigCallback
{
  protected WebView mWeb;
  protected MyWebViewClient mWebViewClient;
  protected String targetConfig = null;

  private void gotoFlowControl()
  {
    GotoScreen.flowControl(this, this.targetConfig, null, null, null);
  }

  public boolean loadConfigFromUrl(String paramString)
  {
    GetConfigFromUrl localGetConfigFromUrl = new GetConfigFromUrl(this.mLogger);
    if (localGetConfigFromUrl.findByUrl(paramString, true))
    {
      this.mGlobals.setXmlConfig(localGetConfigFromUrl.getPageResult());
      return true;
    }
    return false;
  }

  public void loadFromUrl(final String paramString)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        String str1 = paramString.replace("android.netconfig", "");
        String str2 = str1 + "network_config_android.xml";
        BrowserScreen.this.targetConfig = null;
        if (BrowserScreen.this.loadConfigFromUrl(str2))
        {
          BrowserScreen.this.targetConfig = str2;
          BrowserScreen.this.runOnUiThread(new Runnable()
          {
            public void run()
            {
              BrowserScreen.this.gotoFlowControl();
            }
          });
          return;
        }
        BrowserScreen.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            BrowserScreen.this.gotoFlowControl();
          }
        });
      }
    }).start();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt2 == 0)
      done(0);
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  @SuppressLint({"SetJavaScriptEnabled"})
  public void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_browser"));
    super.onCreate(paramBundle);
    this.mWeb = ((WebView)findViewById(this.mParcelHelper.getItemId("webView")));
    this.mWeb.getSettings().setJavaScriptEnabled(true);
    this.mWebViewClient = new MyWebViewClient();
    this.mWebViewClient.setConfigCallback(this);
    this.mWeb.setWebViewClient(this.mWebViewClient);
    this.mWeb.loadUrl(getIntent().getStringExtra("url"));
  }
}