package net.cloudpath.xpressconnect.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.ServiceBoundCallback;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;

public class ShowMessage extends ScreenBase
  implements ServiceBoundCallback
{
  private String mMessageIn = null;
  private WebView mWebView = null;
  private Button okayButton = null;

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving ShowMessage because user pressed back.");
    done(-2);
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_show_message"));
    super.onCreate(paramBundle);
    if (haveTabs())
      setWelcomeActive();
    this.mWebView = ((WebView)findViewById(this.mParcelHelper.getItemId("xpc_WebView")));
    this.okayButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_OkayButton")));
    this.okayButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (ShowMessage.this.mMessageIn != null)
        {
          ShowMessage.this.quit();
          return;
        }
        ShowMessage.this.done(0);
      }
    });
    Intent localIntent = getIntent();
    if (localIntent != null)
    {
      this.mMessageIn = localIntent.getStringExtra("static_message");
      if (this.mMessageIn != null)
        this.mWebView.loadData(this.mMessageIn, "text/html", null);
    }
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
      Log.d("XPC", "Unable to bind to local service!\n");
    do
    {
      return;
      super.onServiceBind(paramEncapsulationService);
      if (this.mParser == null)
        Util.log(this.mLogger, "Config was not properly bound!");
      Util.log(this.mLogger, "+++ Showing ShowMessage.");
      if (haveTabs())
        setWelcomeActive();
    }
    while (this.mMessageIn != null);
    try
    {
      MapVariables localMapVariables1 = new MapVariables(this.mParser, this.mLogger);
      localMapVariables2 = localMapVariables1;
      if ((localMapVariables2 == null) || (this.mParser == null) || (this.mParser.selectedNetwork == null))
      {
        this.mWebView.loadData("There was an error getting information from the configuration file.", "text/html", null);
        return;
      }
    }
    catch (Exception localException)
    {
      MapVariables localMapVariables2;
      while (true)
      {
        localException.printStackTrace();
        Util.log(this.mLogger, "Error : Failed to create variable mapper.");
        localMapVariables2 = null;
      }
      this.mWebView.loadData(localMapVariables2.varMap(this.mParser.selectedNetwork.messageElement, false, this.mWifi), "text/html", null);
    }
  }
}