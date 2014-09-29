package net.cloudpath.xpressconnect.thread;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class FindByDnsName extends FindByDns
{
  private Context mContext = null;
  private String mDomain = null;
  private String mIpAddress = null;

  public FindByDnsName(Logger paramLogger, Context paramContext)
  {
    super(paramLogger);
    this.mContext = paramContext;
  }

  private boolean findDomain(boolean paramBoolean)
  {
    if (gotIpAddress())
    {
      InetAddress localInetAddress;
      try
      {
        localInetAddress = InetAddress.getByName(this.mIpAddress);
        if (paramBoolean)
          Util.log(this.mLogger, "Host : " + localInetAddress.getHostName());
        if (!ipIsValid(localInetAddress.getCanonicalHostName()))
        {
          this.mDomain = getDomainPortion(localInetAddress.getCanonicalHostName());
          if (paramBoolean)
            Util.log(this.mLogger, "Domain portion : " + this.mDomain);
          if (this.mDomain == null)
            break label188;
          return true;
        }
      }
      catch (UnknownHostException localUnknownHostException)
      {
        localUnknownHostException.printStackTrace();
        Util.log(this.mLogger, "Unknown host exception attempting to resolve " + this.mIpAddress + " to hostname.");
        return false;
      }
      Util.log(this.mLogger, "Result, " + localInetAddress.getCanonicalHostName() + ", appears to be an IP address, or is invalid.  Discarding.");
    }
    while (true)
    {
      label188: return false;
      Util.log(this.mLogger, "No IP address set, can't search by DNS hints.");
    }
  }

  private boolean foundViaTxt()
  {
    if (Util.stringIsEmpty(this.mDomain))
    {
      Util.log(this.mLogger, "No domain information was set skipping finding by TXT.");
      return false;
    }
    String str1 = getTxtRecord("cloudpath-xpc." + this.mDomain);
    if (Util.stringIsEmpty(str1))
    {
      Util.log(this.mLogger, "No TXT record found for cloudpath-xpc");
      return false;
    }
    Util.log(this.mLogger, "TXT record : " + str1);
    String str2 = buildUrl(str1);
    Util.log(this.mLogger, "Mapped record : " + str2);
    this.mTargetUrl = str2;
    return true;
  }

  public boolean findByDnsName(boolean paramBoolean)
  {
    Util.log(this.mLogger, "... Trying by domain name ...");
    if (!findDomain(paramBoolean))
    {
      this.mTargetUrl = null;
      Util.log(this.mLogger, "Unable to determine DNS domain for this device.  Skipping other domain based attempts to find the configuration.");
      return false;
    }
    Util.log(this.mLogger, "... Trying by TXT lookup ...");
    if (foundViaTxt())
      return true;
    this.mTargetUrl = null;
    return false;
  }

  public String getDomainPortion(String paramString)
  {
    if (paramString == null)
      return null;
    for (int i = 0; ; i++)
    {
      int j = paramString.length();
      int k = 0;
      if (i < j)
      {
        if (paramString.charAt(i) == '.')
          k = i;
      }
      else
      {
        if (k == 0)
          break;
        return paramString.substring(k + 1);
      }
    }
  }

  public String getRawUrl()
  {
    if (Util.stringIsEmpty(this.mRawUrl))
      return null;
    if (this.mRawUrl.endsWith("/network_config_android.xml"))
      return this.mRawUrl.substring(0, this.mRawUrl.length() - "/network_config_android.xml".length());
    return this.mRawUrl;
  }

  protected boolean gotIpAddress()
  {
    if (this.mContext == null)
      return false;
    WifiManager localWifiManager = (WifiManager)this.mContext.getSystemService("wifi");
    if (localWifiManager == null)
    {
      Util.log(this.mLogger, "Unable to acquire wifi context while checking DNS name.");
      return false;
    }
    WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
    if ((localWifiInfo.getIpAddress() != 0) && (ipIsValid(Util.intToIp(localWifiInfo.getIpAddress()))))
    {
      Util.log(this.mLogger, "IP address is valid.  Moving on.");
      this.mIpAddress = Util.intToIp(localWifiInfo.getIpAddress());
      return true;
    }
    this.mIpAddress = null;
    return false;
  }

  protected boolean ipIsValid(String paramString)
  {
    if (paramString == null);
    while (paramString.split("\\.").length != 4)
      return false;
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = 0;
      for (int k = 0; k < ".1234567890".length(); k++)
        if (paramString.charAt(i) == ".1234567890".charAt(k))
          j = 1;
      if (j == 0)
      {
        Util.log(this.mLogger, "Invalid character in the IP address.");
        return false;
      }
    }
    if (paramString.startsWith("169.254"))
    {
      Util.log(this.mLogger, "Got a link local address returned.");
      return false;
    }
    return true;
  }
}