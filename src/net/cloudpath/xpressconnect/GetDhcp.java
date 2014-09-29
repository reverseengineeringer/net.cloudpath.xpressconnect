package net.cloudpath.xpressconnect;

import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.remote.RemoteUpload;
import net.cloudpath.xpressconnect.remote.WebInterface;
import net.cloudpath.xpressconnect.thread.ThreadBase;
import net.cloudpath.xpressconnect.thread.ThreadCom;

public class GetDhcp
{
  private static final int SUCCESSFUL_CONNECTION = 20000;
  private static final int SUCCESS_WEB_CHECK_FAIL = 99100;
  private FailureReason mFailure = null;
  private int mFailureId = -3;
  private Logger mLogger = null;
  private Context mParent;
  private NetworkConfigParser mParser = null;
  private ThreadBase mThread = null;
  private ThreadCom mThreadCom = null;
  private WifiManager mWifi = null;

  public GetDhcp(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, FailureReason paramFailureReason, Context paramContext, WifiManager paramWifiManager, ThreadCom paramThreadCom, ThreadBase paramThreadBase)
  {
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mParent = paramContext;
    this.mFailure = paramFailureReason;
    this.mThreadCom = paramThreadCom;
    this.mThread = paramThreadBase;
    this.mWifi = paramWifiManager;
  }

  public boolean getDhcp()
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this.mParent);
    if (gotDhcpAddress())
    {
      if (this.mParser == null)
      {
        Util.log(this.mLogger, "No parser bound in getDhcp()!");
        return false;
      }
      if (this.mThread.shouldDie())
        return false;
      Util.log(this.mLogger, "Got IP address : " + this.mParser.ipAddress);
      if (!ipIsValid(this.mParser.ipAddress))
      {
        Util.log(this.mLogger, "IP address isn't valid.  Returning error.");
        return false;
      }
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "No network selected in getDhcp()!");
        return false;
      }
      if (this.mThread.shouldDie())
        return false;
      if (this.mParser.selectedNetwork.validationURL != null)
      {
        Util.log(this.mLogger, "Checking validation URL...");
        this.mThreadCom.update(this.mParent.getResources().getString(localParcelHelper.getIdentifier("xpc_checking_website", "string")));
        this.mThreadCom.changeTab(4);
        if (this.mThread.shouldDie())
          return false;
        if (new WebInterface(this.mLogger).checkWebPage(this.mParser.selectedNetwork.validationURL, this.mParser.selectedNetwork.validationgrep, true) != 200)
        {
          if (this.mFailure != null)
            this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mParent.getResources().getString(localParcelHelper.getIdentifier("xpc_web_site_check_failed", "string")), 10);
          this.mFailureId = 33;
          if ((0x4 & this.mParser.selectedNetwork.remoteUpload) == 4)
          {
            if (new RemoteUpload(this.mLogger, this.mParent).uploadSessionState(this.mParser.savedConfigInfo.username, this.mParser.savedConfigInfo.password, this.mParser.networks.licensee, "key", this.mParser.savedConfigInfo.clientId, this.mParser.savedConfigInfo.sessionId, 99100, this.mParser.networks.server_address))
              break label404;
            Util.log(this.mLogger, "Unable to upload session state!");
          }
          while (true)
          {
            return false;
            label404: Util.log(this.mLogger, "Final state uploaded.");
          }
        }
      }
      if (((0x4 & this.mParser.selectedNetwork.remoteUpload) == 4) && (!new RemoteUpload(this.mLogger, this.mParent).uploadSessionState(this.mParser.savedConfigInfo.username, this.mParser.savedConfigInfo.password, this.mParser.networks.licensee, "key", this.mParser.savedConfigInfo.clientId, this.mParser.savedConfigInfo.sessionId, 20000, this.mParser.networks.server_address)))
        Util.log(this.mLogger, "Unable to upload session state!");
      return true;
    }
    if (this.mFailure != null)
    {
      if (this.mParser == null)
        break label566;
      this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mParser.getOptionById(404), 3);
    }
    while (true)
    {
      return false;
      label566: this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mParent.getResources().getString(localParcelHelper.getIdentifier("xpc_cant_get_ip", "string")), 3);
    }
  }

  public int getFailureId()
  {
    return this.mFailureId;
  }

  protected boolean gotDhcpAddress()
  {
    int i = 0;
    int j = 0;
    if (this.mWifi == null)
    {
      Util.log(this.mLogger, "No wifi context in gotDhcpAddress()!");
      return false;
    }
    WifiInfo localWifiInfo = this.mWifi.getConnectionInfo();
    while (true)
      if (j == 0)
      {
        if (this.mThread.shouldDie())
          break;
        if ((localWifiInfo.getIpAddress() != 0) && (ipIsValid(Util.intToIp(localWifiInfo.getIpAddress()))))
        {
          Util.log(this.mLogger, "IP address is valid.  Moving on.");
          j = 1;
          continue;
        }
        if (WifiInfo.getDetailedStateOf(localWifiInfo.getSupplicantState()) != NetworkInfo.DetailedState.OBTAINING_IPADDR)
        {
          Util.log(this.mLogger, "Left the 'obtaining IP state'.");
          j = 1;
          continue;
        }
        try
        {
          Thread.sleep(500L);
          localWifiInfo = this.mWifi.getConnectionInfo();
          i++;
          if (i > 240)
          {
            Util.log(this.mLogger, "Timed out waiting for an IP address.");
            j = 1;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
          {
            Util.log(this.mLogger, "Unable to sleep in DHCP test thread!");
            localInterruptedException.printStackTrace();
          }
        }
      }
    if (localWifiInfo.getIpAddress() == 0)
    {
      Util.log(this.mLogger, "No valid address returned.");
      return false;
    }
    Util.log(this.mLogger, "Raw IP address : " + localWifiInfo.getIpAddress());
    if (this.mParser != null)
      this.mParser.ipAddress = Util.intToIp(localWifiInfo.getIpAddress());
    return true;
  }

  protected boolean ipIsValid(String paramString)
  {
    if (paramString == null);
    do
    {
      do
        return false;
      while (paramString.split("\\.").length != 4);
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
      if (!paramString.startsWith("169.254"))
        break;
      Util.log(this.mLogger, "Got a link local address returned.");
    }
    while (this.mFailure == null);
    ParcelHelper localParcelHelper = new ParcelHelper("", this.mParent);
    this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mParent.getResources().getString(localParcelHelper.getIdentifier("xpc_success_bad_ip", "string")), 3);
    return false;
    return true;
  }

  public void updateSupState(SupplicantState paramSupplicantState)
  {
  }
}