package net.cloudpath.xpressconnect;

import android.os.Build;

public class DeviceBlacklist
{
  public static final int DEVICE_SUPPORTED = 0;
  public static final int ENTERPRISE_NOT_SUPPORTED = 1;

  public int deviceIsUsable(String paramString1, String paramString2)
  {
    return 0;
  }

  public int thisDeviceIsUsable()
  {
    return deviceIsUsable(Build.MODEL, Build.ID);
  }
}