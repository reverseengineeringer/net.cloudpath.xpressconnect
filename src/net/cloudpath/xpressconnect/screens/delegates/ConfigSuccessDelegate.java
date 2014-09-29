package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.Html;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class ConfigSuccessDelegate extends DelegateBase
{
  public static final int AMAZON_MARKETPLACE = 2;
  public static final int ANDROID_MARKETPLACE = 1;
  public static final int NO_MARKETPLACE;

  public ConfigSuccessDelegate(Context paramContext)
  {
    super(paramContext);
  }

  public int findMarket()
  {
    try
    {
      this.mContext.getPackageManager().getApplicationInfo("com.amazon.venezia", 0);
      Util.log(this.mLogger, "Has Amazon market.");
      return 2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException1)
    {
      try
      {
        this.mContext.getPackageManager().getApplicationInfo("com.android.vending", 0);
        Util.log(this.mLogger, "Has Google market.");
        return 1;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException2)
      {
      }
    }
    return 0;
  }

  public String getIpAddress(WifiManager paramWifiManager)
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser is null when attempting to get IP address line.");
      return "";
    }
    try
    {
      MapVariables localMapVariables1 = new MapVariables(this.mParser, this.mLogger);
      localMapVariables2 = localMapVariables1;
      if ((localMapVariables2 != null) && (localMapVariables2.varMap("${IP_ADDRESS}", false, paramWifiManager).length() > 0))
        return localMapVariables2.varMap("Your IP address is ${IP_ADDRESS}", false, paramWifiManager);
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        Util.log(this.mLogger, "Unable to mapVar in ConfigSuccess.");
        MapVariables localMapVariables2 = null;
      }
    }
    return "";
  }

  public void launchMarket(int paramInt)
  {
    switch (paramInt)
    {
    default:
    case 1:
    case 2:
    }
    WebInterface localWebInterface;
    do
    {
      return;
      this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=net.cloudpath.xpressconnect")));
      Util.log(this.mLogger, "*** User went to Android marketplace rating page.");
      return;
      localWebInterface = new WebInterface(this.mLogger);
      Util.log(this.mLogger, "*** User went to Amazon marketplace rating page.");
    }
    while (localWebInterface.openWebPage(this.mContext, "http://www.amazon.com/gp/mas/dl/android?p=net.cloudpath.xpressconnect"));
    Toast.makeText(this.mContext, "Unable to launch web browser.", 1).show();
  }

  public void setSmallStatus(TextView paramTextView)
  {
    if ((paramTextView == null) || (this.mParcelHelper == null))
    {
      Util.log(this.mLogger, "Invalid values passed in to setSmallStatus().");
      return;
    }
    if ((this.mParser == null) || (this.mParser.savedConfigInfo == null) || (this.mContext == null))
    {
      Util.log(this.mLogger, "Invalid context information in setSmallStatus().");
      return;
    }
    if (this.mParser.savedConfigInfo.mCertInstallWarning == null)
    {
      paramTextView.setText("");
      return;
    }
    paramTextView.setText(Html.fromHtml(this.mParser.savedConfigInfo.mCertInstallWarning), TextView.BufferType.SPANNABLE);
  }
}