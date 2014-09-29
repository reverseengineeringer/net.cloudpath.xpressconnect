package net.cloudpath.xpressconnect.nativeproxy;

import android.net.wifi.WifiConfiguration;
import android.os.Build.VERSION;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.BitSet;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class WifiConfigTemplate
{
  protected static boolean mStripValues = true;
  protected int mApiLevel = 0;
  protected Logger mLogger = null;
  protected WifiConfiguration mWifiConfig = null;

  public WifiConfigTemplate(WifiConfiguration paramWifiConfiguration, Logger paramLogger)
  {
    if (paramWifiConfiguration != null);
    for (this.mWifiConfig = paramWifiConfiguration; ; this.mWifiConfig = new WifiConfiguration())
    {
      this.mLogger = paramLogger;
      return;
    }
  }

  protected String forceUnquotedString(String paramString)
  {
    if (paramString == null)
      return null;
    return paramString.replace("\"", "");
  }

  public BitSet getAllowedAuthAlgorithms()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getAllowedAuthAlgoritms is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public BitSet getAllowedGroupCiphers()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setAllowedGroupCiphers is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public BitSet getAllowedKeyManagement()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getAllowedKeyManagement is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public BitSet getAllowedPairwiseCiphers()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getAllowedPairwiseCiphers is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public BitSet getAllowedProtocols()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getAllowedProtocols is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getAnonId()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getAnonId is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public int getApiLevel()
  {
    return this.mApiLevel;
  }

  public String getCaCert()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getCaCert is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public String getClientCertAlias()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getClientCert is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getClientPrivateKey()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getClientPrivateKey is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getEap()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getEap is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public String getEngine()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getEngine is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getEngineId()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getEngineId is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean getHiddenSsid()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getHiddenSsid is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getId()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getId is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public String getKeyId()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getKeyId is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public LinkPropertiesProxy getLinkProperties()
    throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, ClassNotFoundException
  {
    throw new IllegalAccessException("getLinkProperties() is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public int getNetworkId()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getNetworkId is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getPassword()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getPassword is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public String getPhase2()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getPhase2 is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public int getPriority()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getPriority is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getPsk()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getPsk is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  protected String getQuotedString(String paramString)
  {
    return getQuotedString(paramString, Build.VERSION.SDK_INT);
  }

  protected String getQuotedString(String paramString, int paramInt)
  {
    if (paramString == null)
      return null;
    if (paramInt >= 8)
      Util.log(this.mLogger, "Using Android 2.2+ quoting format.");
    for (String str = paramString; ; str = "\"" + paramString + "\"")
    {
      return str;
      Util.log(this.mLogger, "Using Android 2.1 quoting format.");
    }
  }

  public String getSsid()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getSsid is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public int getStatus()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getPriority is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public String getSubjectMatch()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getSubjectMatch is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  protected String getUnquotedString(String paramString)
  {
    return getUnquotedString(paramString, Build.VERSION.SDK_INT);
  }

  protected String getUnquotedString(String paramString, int paramInt)
  {
    if (paramInt >= 8)
    {
      Util.log(this.mLogger, "Stripping Android 2.2+ quoting format.");
      return paramString;
    }
    Util.log(this.mLogger, "Stripping Android 2.1 quoting format.");
    return forceUnquotedString(paramString);
  }

  public WifiConfiguration getWifiConfig()
  {
    return this.mWifiConfig;
  }

  public boolean setAllowedAuthAlgorithms(BitSet paramBitSet)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setAllowedAuthAlgoritms is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setAllowedGroupCiphers(BitSet paramBitSet)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setAllowedGroupCiphers is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setAllowedKeyManagement(BitSet paramBitSet)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setAllowedKeyManagement is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setAllowedPairwiseCiphers(BitSet paramBitSet)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setAllowedPairwiseCiphers is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setAllowedProtocols(BitSet paramBitSet)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setAllowedProtocols is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setAnonId(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setAnonId is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setCaCert(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setCaCert is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setCaCertificate(X509Certificate paramX509Certificate)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setCaCertificate is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setClientCert(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setClientCert is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setClientKeyEntry(PrivateKey paramPrivateKey, X509Certificate paramX509Certificate)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setClientKeyEntry is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setClientPrivateKey(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setClientPrivateKey is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setEap(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setEap is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setHiddenSsid(boolean paramBoolean)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setHiddenSsid is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setId(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setId is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setNetworkId(int paramInt)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setNetworkId is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setPassword(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setPassword is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setPhase2(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setPhase2 is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setPriority(int paramInt)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setPriority is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setProxyState(int paramInt)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setProxyState() is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public boolean setPsk(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setPsk is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setSsid(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setSsid is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setStatus(int paramInt)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("getPriority is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }

  public boolean setSubjectMatch(String paramString)
    throws IllegalAccessException
  {
    throw new IllegalAccessException("setSubjectMatch is not implemented for level " + this.mApiLevel + " of the wirelss API.");
  }

  public String toFilteredString()
    throws IllegalAccessException
  {
    throw new IllegalAccessException("toFilteredString is not implemented for level " + this.mApiLevel + " of the wireless API.");
  }
}