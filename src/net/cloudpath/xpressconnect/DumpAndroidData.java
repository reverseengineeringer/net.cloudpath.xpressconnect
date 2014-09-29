package net.cloudpath.xpressconnect;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import java.util.List;
import net.cloudpath.xpressconnect.logger.Logger;

public class DumpAndroidData
{
  private Logger mLogger = null;
  private WifiManager wifi = null;

  public DumpAndroidData(Logger paramLogger, WifiManager paramWifiManager)
  {
    this.mLogger = paramLogger;
    this.wifi = paramWifiManager;
    dumpAndroidData();
    dumpWirelessData();
  }

  private void dumpAndroidData()
  {
    Util.log(this.mLogger, " *****  Android Device Data  *****");
    Util.log(this.mLogger, " Board              : " + Build.BOARD);
    Util.log(this.mLogger, " Brand              : " + Build.BRAND);
    Util.log(this.mLogger, " ABI                : " + Build.CPU_ABI);
    Util.log(this.mLogger, " Device             : " + Build.DEVICE);
    Util.log(this.mLogger, " Display            : " + Build.DISPLAY);
    Util.log(this.mLogger, " Fingerprint        : " + Build.FINGERPRINT);
    Util.log(this.mLogger, " Host               : " + Build.HOST);
    Util.log(this.mLogger, " ID                 : " + Build.ID);
    Util.log(this.mLogger, " Manufacturer       : " + Build.MANUFACTURER);
    Util.log(this.mLogger, " Model              : " + Build.MODEL);
    Util.log(this.mLogger, " Product            : " + Build.PRODUCT);
    Util.log(this.mLogger, " Tags               : " + Build.TAGS);
    Util.log(this.mLogger, " Time               : " + Build.TIME);
    Util.log(this.mLogger, " Type               : " + Build.TYPE);
    Util.log(this.mLogger, " User               : " + Build.USER);
    Util.log(this.mLogger, " Codename           : " + Build.VERSION.CODENAME);
    Util.log(this.mLogger, " Incremental        : " + Build.VERSION.INCREMENTAL);
    Util.log(this.mLogger, " Release            : " + Build.VERSION.RELEASE);
    Util.log(this.mLogger, " SDK                : " + Build.VERSION.SDK_INT);
    Util.log(this.mLogger, " Dev. ID            : android_id");
    Util.log(this.mLogger, " Provisioned        : device_provisioned");
    Util.log(this.mLogger, " HTTP Proxy         : http_proxy");
    Util.log(this.mLogger, " Net. Pref.         : network_preference");
    Util.log(this.mLogger, " Wifi DHCP Retries  : wifi_max_dhcp_retry_count");
    Util.log(this.mLogger, " Wifi Avail Note    : wifi_networks_available_notification_on");
    Util.log(this.mLogger, " Wifi Avail Repeat  : wifi_networks_available_repeat_delay");
    Util.log(this.mLogger, " Wifi Open Kept     : wifi_num_open_networks_kept");
    Util.log(this.mLogger, " Wifi On            : wifi_on");
    Util.log(this.mLogger, " ***** End Android Device Data  *****");
  }

  private void dumpWirelessData()
  {
    Util.log(this.mLogger, "*******  Wireless Data ********");
    if (this.wifi == null)
      Util.log(this.mLogger, "Wifi manager is null.  SSID information will not be dumpped.");
    while (true)
    {
      return;
      List localList = this.wifi.getScanResults();
      if (localList == null)
      {
        Util.log(this.mLogger, "No SSID information available.");
        return;
      }
      if (localList.size() <= 0)
      {
        Util.log(this.mLogger, "No networks are configured!!!");
        return;
      }
      for (int i = 0; i < localList.size(); i++)
      {
        ScanResult localScanResult = (ScanResult)localList.get(i);
        if (localScanResult != null)
        {
          Util.log(this.mLogger, "  -----  Wireless Network  ----- ");
          if (localScanResult.SSID != null)
            Util.log(this.mLogger, "  SSID         : " + localScanResult.SSID);
          if (localScanResult.BSSID != null)
            Util.log(this.mLogger, "  BSSID        : " + localScanResult.BSSID);
          if (localScanResult.capabilities != null)
            Util.log(this.mLogger, "  Capabilities : " + localScanResult.capabilities);
          Util.log(this.mLogger, "  Freq.        : " + localScanResult.frequency);
          Util.log(this.mLogger, "  Level        : " + localScanResult.level);
        }
      }
    }
  }
}