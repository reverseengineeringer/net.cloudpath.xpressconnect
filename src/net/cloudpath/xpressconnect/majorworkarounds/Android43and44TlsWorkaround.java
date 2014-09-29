package net.cloudpath.xpressconnect.majorworkarounds;

import android.net.wifi.WifiManager;
import java.util.BitSet;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;

public class Android43and44TlsWorkaround
{
  protected Logger mLogger = null;

  public Android43and44TlsWorkaround(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  private boolean createFakeTargetSsidConfig(NetworkConfigParser paramNetworkConfigParser, WifiManager paramWifiManager)
  {
    String str = Util.getSSID(this.mLogger, paramNetworkConfigParser);
    try
    {
      WifiConfigurationProxy localWifiConfigurationProxy = new WifiConfigurationProxy(null, this.mLogger);
      localWifiConfigurationProxy.setSsid("\"" + str + "\"");
      BitSet localBitSet = new BitSet();
      localBitSet.set(3);
      localBitSet.set(2);
      localWifiConfigurationProxy.setAllowedKeyManagement(localBitSet);
      localWifiConfigurationProxy.setEap("PEAP");
      localWifiConfigurationProxy.setId("test");
      localWifiConfigurationProxy.setPassword("test");
      localWifiConfigurationProxy.setPhase2("auth=mschapv2");
      if (paramWifiManager.addNetwork(localWifiConfigurationProxy.getWifiConfig()) < 0)
      {
        Util.log(this.mLogger, "Unable to create workaround network in createFakeTargetSsidConfig()!");
        return false;
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
      Util.log(this.mLogger, "Illegal argument exception in the TLS workaround for Android 4.3 and 4.4.");
      return false;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      localNoSuchFieldException.printStackTrace();
      Util.log(this.mLogger, "No such field exception in the TLS workaround for Android 4.3 and 4.4.");
      return false;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
      Util.log(this.mLogger, "Class not found exception in the TLS workaround for Android 4.3 and 4.4.");
      return false;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
      Util.log(this.mLogger, "Illegal access exception in the TLS workaround for Android 4.3 and 4.4");
      return false;
    }
    if (!targetSsidConfigExists(paramNetworkConfigParser, paramWifiManager))
    {
      Util.log(this.mLogger, "API reported our network was created, but secondary check indicated otherwise!  Something is broken!");
      return false;
    }
    return true;
  }

  private boolean targetSsidConfigExists(NetworkConfigParser paramNetworkConfigParser, WifiManager paramWifiManager)
  {
    String str = Util.getSSID(this.mLogger, paramNetworkConfigParser);
    try
    {
      WifiConfigurationProxy localWifiConfigurationProxy = WifiConfigurationProxy.findNetworkBySsid(str, paramWifiManager, this.mLogger);
      boolean bool = false;
      if (localWifiConfigurationProxy != null)
        bool = true;
      return bool;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
      Util.log(this.mLogger, "Illegal argument exception in the TLS workaround for Android 4.3 & 4.4.");
      return false;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      localNoSuchFieldException.printStackTrace();
      Util.log(this.mLogger, "No such field exception in the TLS workaround for Android 4.3 & 4.4.");
      return false;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
      Util.log(this.mLogger, "Class not found exception in the TLS workaround for Android 4.3 & 4.4.");
      return false;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
      Util.log(this.mLogger, "Illegal access exception in the TLS workaround for Android 4.3 & 4.4.");
    }
    return false;
  }

  public boolean doWorkaround(NetworkConfigParser paramNetworkConfigParser, WifiManager paramWifiManager)
  {
    if (targetSsidConfigExists(paramNetworkConfigParser, paramWifiManager) == true)
      return true;
    return createFakeTargetSsidConfig(paramNetworkConfigParser, paramWifiManager);
  }
}