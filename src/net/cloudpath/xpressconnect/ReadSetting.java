package net.cloudpath.xpressconnect;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.util.Iterator;
import java.util.List;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;

public class ReadSetting
{
  protected WifiConfigurationProxy mFoundNetwork = null;
  protected Logger mLogger = null;
  protected NetworkConfigParser mParser = null;
  protected WifiManager mWifi = null;

  public ReadSetting(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, WifiManager paramWifiManager)
  {
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mWifi = paramWifiManager;
  }

  public boolean desiredNetworkConfigured()
  {
    if ((this.mWifi == null) || (this.mWifi.getConnectionInfo() == null))
    {
      Util.log(this.mLogger, "System wifi data is invalid.  Assuming we aren't connected to a network.");
      this.mFoundNetwork = null;
    }
    do
    {
      return false;
      this.mFoundNetwork = getNetwork(this.mWifi.getConnectionInfo().getSSID());
    }
    while (this.mFoundNetwork == null);
    return true;
  }

  protected String getCurrentEapType()
  {
    if (this.mFoundNetwork == null)
      return "none";
    return this.mFoundNetwork.getEap();
  }

  protected String getCurrentOuterId()
  {
    if (this.mFoundNetwork == null)
      return "none";
    return this.mFoundNetwork.getAnonId();
  }

  protected String getCurrentPeapInner()
  {
    return getCurrentTtlsInner();
  }

  protected String getCurrentRootCa()
  {
    if (this.mFoundNetwork == null)
      return "none";
    if ((this.mFoundNetwork.getCaCert() != null) && (!this.mFoundNetwork.getCaCert().equals("")))
      return "in use";
    return "not used";
  }

  protected String getCurrentSsid()
  {
    if ((this.mWifi != null) && (this.mWifi.getConnectionInfo() != null))
      return this.mWifi.getConnectionInfo().getSSID();
    return "none";
  }

  protected String getCurrentTtlsInner()
  {
    if (this.mFoundNetwork == null)
      return "none";
    return this.mFoundNetwork.getPhase2();
  }

  protected String getCurrentValidateCert()
  {
    if (this.mFoundNetwork == null)
      return "none";
    if ((this.mFoundNetwork.getCaCert() != null) && (!this.mFoundNetwork.getCaCert().equals("")))
      return "1";
    return "0";
  }

  protected WifiConfigurationProxy getNetwork(String paramString)
  {
    List localList = this.mWifi.getConfiguredNetworks();
    if ((localList == null) || (localList.size() <= 0))
    {
      Util.log(this.mLogger, "No networks in the network list.  (So the network isn't found.)");
      return null;
    }
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser isn't bound in getNetwork()!");
      return null;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile is selected in getNetwork()!");
      return null;
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      WifiConfiguration localWifiConfiguration = (WifiConfiguration)localIterator.next();
      try
      {
        WifiConfigurationProxy localWifiConfigurationProxy = new WifiConfigurationProxy(localWifiConfiguration, this.mLogger);
        if (localWifiConfigurationProxy.getSsid().equals("\"" + paramString + "\""))
          return localWifiConfigurationProxy;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Util.log(this.mLogger, "IllegalArgumentException in getNetwork()!");
        localIllegalArgumentException.printStackTrace();
        return null;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        Util.log(this.mLogger, "NoSuchFieldException in getNetwork()!");
        localNoSuchFieldException.printStackTrace();
        return null;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        Util.log(this.mLogger, "ClassNotFoundException in getNetwork()!");
        localClassNotFoundException.printStackTrace();
        return null;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        Util.log(this.mLogger, "IllegalAccessException in getNetwork()!");
        localIllegalAccessException.printStackTrace();
        return null;
      }
    }
    return null;
  }

  public String getSetting(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "unknown setting " + paramInt;
    case 65001:
      return getCurrentEapType();
    case 40015:
      return getCurrentOuterId();
    case 65003:
      return getCurrentPeapInner();
    case 61301:
      return getCurrentRootCa();
    case 40001:
      return getCurrentSsid();
    case 65002:
      return getCurrentTtlsInner();
    case 65000:
      return getCurrentValidateCert();
    case 61306:
    }
    return "0";
  }
}