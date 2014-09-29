package net.cloudpath.xpressconnect.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class BadConfigScreen extends ScreenBase
  implements View.OnClickListener
{
  private Button mRetryButton = null;
  private Button mViewButton = null;
  private String selectedURL = null;

  public void onClick(View paramView)
  {
    if (paramView == this.mRetryButton)
    {
      Util.log(this.mLogger, "--- Leaving BadConfigScreen by way of retry button.");
      if ((this.mViewButton.getVisibility() == 4) && (this.mFailure != null))
        this.mFailure.setFailReason(FailureReason.useWarningIcon, "No configuration was available.  Please reconnect to the web site and try again.", 5);
      done(16);
    }
    while (paramView != this.mViewButton)
      return;
    if (new WebInterface(this.mLogger).openWebPage(this, this.selectedURL) == true)
    {
      Util.log(this.mLogger, "Launching web browser from BadConfigScreen.");
      return;
    }
    Util.log(this.mLogger, "!!!! Unable to launch web browser!");
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_bad_config"));
    super.onCreate(paramBundle);
    this.mRetryButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_RetryButton")));
    this.mRetryButton.setOnClickListener(this);
    this.mViewButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_ViewButton")));
    this.mViewButton.setOnClickListener(this);
    this.selectedURL = null;
    if (paramBundle != null)
    {
      this.selectedURL = paramBundle.getString("selectedURL");
      Log.d("XPC", "Selected URL : " + this.selectedURL);
    }
    while (true)
    {
      if (this.selectedURL == null)
        this.mViewButton.setVisibility(4);
      this.mShowAboutMenu = false;
      this.mShowLogsMenu = true;
      this.mShowHelpdeskMenu = false;
      this.mShowEmailLogsMenu = true;
      this.mShowStartOverMenu = true;
      this.mShowQuitMenu = true;
      return;
      if (getIntent() != null)
      {
        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null)
        {
          this.selectedURL = localBundle.getString("selected");
          Log.d("XPC", "Selected URL : " + this.selectedURL);
        }
      }
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("selectedURL", this.selectedURL);
    super.onSaveInstanceState(paramBundle);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing bad config screen.");
    if (haveTabs())
      setWelcomeActive();
    if (this.selectedURL == null)
      this.selectedURL = this.mGlobals.getLoadedFromUrl();
  }
}