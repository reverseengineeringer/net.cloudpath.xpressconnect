package net.cloudpath.xpressconnect;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import com.cloudpath.common.util.Md5;
import java.util.Date;
import java.util.Random;

public class GenerateIds
{
  public String getClientId(WifiManager paramWifiManager)
  {
    if (paramWifiManager == null)
    {
      Log.d("XPC", "wm is null in GenerateIds!");
      return "";
    }
    if (paramWifiManager.getConnectionInfo() == null)
    {
      Log.d("XPC", "Connection info is null in GenerateIds!");
      return "";
    }
    String str = paramWifiManager.getConnectionInfo().getMacAddress();
    return Md5.md5(str + Build.BOARD + Build.BRAND + Build.DEVICE + Build.FINGERPRINT + Build.ID + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT);
  }

  public String getSessionId()
  {
    long l = new Date().getTime();
    Random localRandom = new Random();
    return String.valueOf(l) + String.valueOf(localRandom.nextInt());
  }
}