package net.cloudpath.xpressconnect.screens;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.screens.delegates.ConfigSuccessDelegate;

public class ConfigSuccess extends ConfigResultBase
{
  private ConfigSuccessDelegate mDelegate = null;

  public void onClick(View paramView)
  {
    if (paramView == this.rateMe)
      this.mDelegate.launchMarket(this.mDelegate.findMarket());
    super.onClick(paramView);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mDelegate = new ConfigSuccessDelegate(this);
    this.changeCredsButton.setVisibility(8);
    this.retryButton.setVisibility(8);
    this.successImage.setImageResource(this.mParcelHelper.getDrawableId("xpc_success"));
    this.rateMe.setOnClickListener(this);
    if (this.mDelegate.findMarket() == 0)
      this.rateMe.setVisibility(8);
    if (Build.BRAND.toLowerCase().contentEquals("nook"))
      this.rateMe.setVisibility(4);
    this.mShowStartOverMenu = false;
    this.mFinalResult = 1;
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
      Log.d("XPC", "Unable to bind to the local service!");
    do
    {
      return;
      super.onServiceBind(paramEncapsulationService);
      if (this.mParser == null)
        Util.log(this.mLogger, "Config was not properly bound!");
      Util.log(this.mLogger, "+++ Showing ConfigSuccess screen.");
      if (this.mGlobals.getRunningAsLibrary() == true)
        this.rateMe.setVisibility(4);
      if (this.mDelegate != null)
        this.mDelegate.setRequiredSettings(this.mLogger, this.mParser);
      if ((this.ipAddress != null) && (this.mDelegate != null) && (this.mWifi != null))
        this.ipAddress.setText(this.mDelegate.getIpAddress(this.mWifi));
      if ((this.finalStatus != null) && (this.mParcelHelper != null))
        this.finalStatus.setText(this.mParcelHelper.getIdentifier("xpc_congrats_connected", "string"));
    }
    while (this.mDelegate == null);
    this.mDelegate.setSmallStatus(this.smallStatus);
  }
}