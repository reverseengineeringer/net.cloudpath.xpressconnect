package net.cloudpath.xpressconnect.nativeproxy;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.BitSet;
import java.util.List;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class WifiConfigurationProxy
{
  protected static int mForceApiLevel = 0;
  protected Logger mLogger = null;
  protected WifiConfigTemplate mWifiConfigProxy = null;

  public WifiConfigurationProxy(WifiConfiguration paramWifiConfiguration, Logger paramLogger)
    throws IllegalArgumentException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException
  {
    this(paramWifiConfiguration, paramLogger, 0);
  }

  public WifiConfigurationProxy(WifiConfiguration paramWifiConfiguration, Logger paramLogger, int paramInt)
    throws IllegalArgumentException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException
  {
    this.mLogger = paramLogger;
    mForceApiLevel = paramInt;
    if (mForceApiLevel < 1)
      try
      {
        this.mWifiConfigProxy = new WifiConfigurationV2(paramWifiConfiguration, paramLogger);
        return;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        this.mWifiConfigProxy = new WifiConfigurationV1(paramWifiConfiguration, paramLogger);
        return;
      }
    switch (mForceApiLevel)
    {
    default:
    case 2:
    }
    for (this.mWifiConfigProxy = new WifiConfigurationV1(paramWifiConfiguration, paramLogger); ; this.mWifiConfigProxy = new WifiConfigurationV2(paramWifiConfiguration, paramLogger))
    {
      Util.log(this.mLogger, "Using wireless API level " + this.mWifiConfigProxy.getApiLevel());
      return;
    }
  }

  public WifiConfigurationProxy(WifiConfigTemplate paramWifiConfigTemplate)
  {
    this.mWifiConfigProxy = paramWifiConfigTemplate;
  }

  public static WifiConfigurationProxy findNetworkBySsid(String paramString, WifiManager paramWifiManager, Logger paramLogger)
    throws IllegalArgumentException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException
  {
    if (paramString == null)
    {
      Util.log(paramLogger, "Invalid SSID passed in to findNetworkBySsid()!");
      return null;
    }
    if (paramWifiManager == null)
    {
      Util.log(paramLogger, "Unable to connect to WifiManager.");
      return null;
    }
    List localList = paramWifiManager.getConfiguredNetworks();
    if (localList == null)
    {
      Util.log(paramLogger, "Unable to acquire a list of configured networks.  Is the wireless radio enabled?");
      return null;
    }
    int i = 0;
    if (i < localList.size())
    {
      if ((localList.get(i) == null) || (((WifiConfiguration)localList.get(i)).toString() == null));
      while ((!((WifiConfiguration)localList.get(i)).SSID.equals(paramString)) && (!((WifiConfiguration)localList.get(i)).SSID.equals("\"" + paramString + "\"")))
      {
        i++;
        break;
      }
      Util.log(paramLogger, "Found an SSID match at " + i + ".");
      return new WifiConfigurationProxy((WifiConfiguration)localList.get(i), paramLogger);
    }
    return null;
  }

  public BitSet getAllowedAuthAlgorithms()
  {
    try
    {
      BitSet localBitSet = this.mWifiConfigProxy.getAllowedAuthAlgorithms();
      return localBitSet;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getAllowedAuthAlgorithms()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public BitSet getAllowedGroupCiphers()
  {
    try
    {
      BitSet localBitSet = this.mWifiConfigProxy.getAllowedGroupCiphers();
      return localBitSet;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getAllowedGroupCiphers()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public BitSet getAllowedKeyManagement()
  {
    try
    {
      BitSet localBitSet = this.mWifiConfigProxy.getAllowedKeyManagement();
      return localBitSet;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getAllowedKeyManagement()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public BitSet getAllowedPairwiseCiphers()
  {
    try
    {
      BitSet localBitSet = this.mWifiConfigProxy.getAllowedPairwiseCiphers();
      return localBitSet;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getAllowedPairwiseCiphers()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public BitSet getAllowedProtocols()
  {
    try
    {
      BitSet localBitSet = this.mWifiConfigProxy.getAllowedProtocols();
      return localBitSet;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getAllowedProtocols()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getAnonId()
  {
    try
    {
      String str = this.mWifiConfigProxy.getAnonId();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getAnonId()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public int getApiLevel()
  {
    return this.mWifiConfigProxy.getApiLevel();
  }

  public String getCaCert()
  {
    try
    {
      String str = this.mWifiConfigProxy.getCaCert();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getCaCert()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getClientCertAlias()
  {
    try
    {
      String str = this.mWifiConfigProxy.getClientCertAlias();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getClientCertAlias()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getClientPrivateKey()
  {
    try
    {
      String str = this.mWifiConfigProxy.getClientPrivateKey();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getClientPrivateKey()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getEap()
  {
    try
    {
      String str = this.mWifiConfigProxy.getEap();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getEap()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getEngine()
  {
    try
    {
      String str = this.mWifiConfigProxy.getEngine();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getEngine()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getEngineId()
  {
    try
    {
      String str = this.mWifiConfigProxy.getEngineId();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getEngineId()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public boolean getHiddenSsid()
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.getHiddenSsid();
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getHiddenSsid()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public String getId()
  {
    try
    {
      String str = this.mWifiConfigProxy.getId();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getId()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getKeyId()
  {
    try
    {
      String str = this.mWifiConfigProxy.getKeyId();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getKeyId()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public LinkPropertiesProxy getLinkProperties()
  {
    try
    {
      LinkPropertiesProxy localLinkPropertiesProxy = this.mWifiConfigProxy.getLinkProperties();
      return localLinkPropertiesProxy;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      Util.log(this.mLogger, "IllegalArgumentException in getLinkProperties()!");
      localIllegalArgumentException.printStackTrace();
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      while (true)
      {
        Util.log(this.mLogger, "IllegalAccessException in getLinkProperties()!");
        localIllegalAccessException.printStackTrace();
      }
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      while (true)
      {
        Util.log(this.mLogger, "NoSuchFieldException in getLinkProperties()!");
        localNoSuchFieldException.printStackTrace();
      }
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      while (true)
      {
        Util.log(this.mLogger, "ClassNotFoundException in getLinkProperties()!");
        localClassNotFoundException.printStackTrace();
      }
    }
  }

  public int getNetworkId()
  {
    try
    {
      int i = this.mWifiConfigProxy.getNetworkId();
      return i;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getNetworkId()!");
      localIllegalAccessException.printStackTrace();
    }
    return -1;
  }

  public String getPassword()
  {
    try
    {
      String str = this.mWifiConfigProxy.getPassword();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getPassword()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getPhase2()
  {
    try
    {
      String str = this.mWifiConfigProxy.getPhase2();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getPhase2()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public int getPriority()
  {
    try
    {
      int i = this.mWifiConfigProxy.getPriority();
      return i;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getPriority()!");
      localIllegalAccessException.printStackTrace();
    }
    return -1;
  }

  public String getPsk()
  {
    try
    {
      String str = this.mWifiConfigProxy.getPsk();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getPsk()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public String getSsid()
  {
    try
    {
      String str = this.mWifiConfigProxy.getSsid();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getSsid()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public int getStatus()
  {
    try
    {
      int i = this.mWifiConfigProxy.getStatus();
      return i;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getStatus()!");
      localIllegalAccessException.printStackTrace();
    }
    return -1;
  }

  public String getSubjectMatch()
  {
    try
    {
      String str = this.mWifiConfigProxy.getSubjectMatch();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in getSubjectMatch()!");
      localIllegalAccessException.printStackTrace();
    }
    return null;
  }

  public WifiConfiguration getWifiConfig()
  {
    return this.mWifiConfigProxy.getWifiConfig();
  }

  public boolean setAllowedAuthAlgorithms(BitSet paramBitSet)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setAllowedAuthAlgorithms(paramBitSet);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setAllowedAuthAlgorithms()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setAllowedGroupCiphers(BitSet paramBitSet)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setAllowedGroupCiphers(paramBitSet);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setAllowedGroupCiphers()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setAllowedKeyManagement(BitSet paramBitSet)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setAllowedKeyManagement(paramBitSet);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setAllowedKeyManagement()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setAllowedPairwiseCiphers(BitSet paramBitSet)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setAllowedPairwiseCiphers(paramBitSet);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setAllowedPairwiseCiphers()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setAllowedProtocols(BitSet paramBitSet)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setAllowedProtocols(paramBitSet);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setAllowedProtocols()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setAnonId(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setAnonId(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setAnonId()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setCaCert(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setCaCert(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setCaCert()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setCaCertificate(X509Certificate paramX509Certificate)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setCaCertificate(paramX509Certificate);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "Unable to set CA certificate.");
    }
    return false;
  }

  public boolean setClientCert(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setClientCert(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setClientCert()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setClientKeyEntry(PrivateKey paramPrivateKey, X509Certificate paramX509Certificate)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setClientKeyEntry(paramPrivateKey, paramX509Certificate);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "Unable to set user certificate/key pair.");
    }
    return false;
  }

  public boolean setClientPrivateKey(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setClientPrivateKey(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setClientPrivateKey()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setEap(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setEap(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setEap()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setHiddenSsid(boolean paramBoolean)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setHiddenSsid(paramBoolean);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setHiddenSsid()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setId(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setId(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setId()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setNetworkId(int paramInt)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setNetworkId(paramInt);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setNetworkId()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setPassword(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setPassword(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setPassword()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setPhase2(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setPhase2(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setPhase2()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setPriority(int paramInt)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setPriority(paramInt);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setPriority()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setProxyState(int paramInt)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setProxyState(paramInt);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setProxyState()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setPsk(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setPsk("\"" + paramString + "\"");
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setPsk()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setSsid(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setSsid(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setSsid()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setStatus(int paramInt)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setStatus(paramInt);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setStatus()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public boolean setSubjectMatch(String paramString)
  {
    try
    {
      boolean bool = this.mWifiConfigProxy.setSubjectMatch(paramString);
      return bool;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in setSubjectMatch()!");
      localIllegalAccessException.printStackTrace();
    }
    return false;
  }

  public String toFilteredString()
  {
    try
    {
      String str = this.mWifiConfigProxy.toFilteredString();
      return str;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in toFilteredString()!");
      localIllegalAccessException.printStackTrace();
    }
    return "";
  }
}