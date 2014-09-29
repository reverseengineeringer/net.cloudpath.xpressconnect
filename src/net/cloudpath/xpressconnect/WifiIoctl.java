package net.cloudpath.xpressconnect;

import android.app.Activity;
import android.net.wifi.WifiManager;
import net.cloudpath.xpressconnect.logger.Logger;

public class WifiIoctl
{
  protected int mResult = -1;
  protected WifiManager mWifi = null;

  public WifiIoctl(WifiManager paramWifiManager)
  {
    this.mWifi = paramWifiManager;
  }

  public boolean deleteSsid(Activity paramActivity, final Logger paramLogger, final int paramInt, final String paramString)
  {
    this.mResult = -1;
    paramActivity.runOnUiThread(new Runnable()
    {
      public void run()
      {
        if (WifiIoctl.this.mWifi.removeNetwork(paramInt))
        {
          Util.log(paramLogger, "*** Duplicate SSID (" + paramInt + ") '" + paramString + "' deleted.");
          WifiIoctl.this.mResult = 1;
          return;
        }
        Util.log(paramLogger, "*** Unable to delete duplicate SSID (" + paramInt + ") '" + paramString + "'!!");
        WifiIoctl.this.mResult = 0;
      }
    });
    return getResult(paramLogger);
  }

  public boolean disableSsid(Activity paramActivity, final Logger paramLogger, final int paramInt, final String paramString)
  {
    this.mResult = -1;
    paramActivity.runOnUiThread(new Runnable()
    {
      public void run()
      {
        if (!WifiIoctl.this.mWifi.disableNetwork(paramInt))
        {
          Util.log(paramLogger, "Unable to disable network " + paramString + ".");
          WifiIoctl.this.mResult = 0;
          return;
        }
        Util.log(paramLogger, "Network " + paramString + " disabled.");
        WifiIoctl.this.mResult = 1;
      }
    });
    return getResult(paramLogger);
  }

  protected boolean getResult(Logger paramLogger)
  {
    int i = 0;
    while (true)
      if (this.mResult < 0)
        try
        {
          Thread.sleep(500L);
          i++;
          if (i > 60)
          {
            this.mResult = 0;
            Util.log(paramLogger, "Timed out waiting for UI thread response.");
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
            localInterruptedException.printStackTrace();
        }
    return this.mResult == 1;
  }

  public boolean saveConfig(Activity paramActivity, final Logger paramLogger)
  {
    this.mResult = -1;
    if (Testing.areTesting)
      return true;
    paramActivity.runOnUiThread(new Runnable()
    {
      public void run()
      {
        if (WifiIoctl.this.mWifi.saveConfiguration())
        {
          Util.log(paramLogger, "Saved configuration...");
          WifiIoctl.this.mResult = 1;
          return;
        }
        Util.log(paramLogger, "Unable to save configuration!!!");
        WifiIoctl.this.mResult = 0;
      }
    });
    return getResult(paramLogger);
  }
}