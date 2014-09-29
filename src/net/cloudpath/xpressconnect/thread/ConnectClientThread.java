package net.cloudpath.xpressconnect.thread;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import net.cloudpath.xpressconnect.ConnectToNetwork;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.GetDhcp;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class ConnectClientThread extends ThreadBase
{
  private GetDhcp dhcp = null;
  private FailureReason mFailureReason = null;
  private IntentFilter mIntentFilter = null;
  private Logger mLogger = null;
  private Activity mParent = null;
  private NetworkConfigParser mParser = null;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      String str1 = paramAnonymousIntent.getAction();
      if (str1 == null)
        return;
      int j;
      if (str1.equals("android.net.wifi.WIFI_STATE_CHANGED"))
      {
        j = paramAnonymousIntent.getIntExtra("wifi_state", 4);
        switch (j)
        {
        default:
          Util.log(ConnectClientThread.this.mLogger, "Wifi state is unknown.");
        case 1:
        case 0:
        case 3:
        case 2:
        }
      }
      while (true)
      {
        if (ConnectClientThread.this.newConnection != null)
          ConnectClientThread.this.newConnection.updateWifiState(j);
        if (str1.equals("android.net.wifi.supplicant.STATE_CHANGE"))
        {
          SupplicantState localSupplicantState = (SupplicantState)paramAnonymousIntent.getParcelableExtra("newState");
          Util.log(ConnectClientThread.this.mLogger, "State Change (Normal state) : " + localSupplicantState.toString());
          Util.log(ConnectClientThread.this.mLogger, "State Change (Detailed) : " + WifiInfo.getDetailedStateOf(localSupplicantState).toString());
          if (ConnectClientThread.this.newConnection != null)
            ConnectClientThread.this.newConnection.updateSupState(localSupplicantState);
          if (ConnectClientThread.this.dhcp != null)
            ConnectClientThread.this.dhcp.updateSupState(localSupplicantState);
        }
        if (!str1.equals("android.net.wifi.supplicant.INFO"))
          break;
        int i = paramAnonymousIntent.getIntExtra("supplicantInfoId", -1);
        String str2 = paramAnonymousIntent.getStringExtra("supplicantInfoString");
        Util.log(ConnectClientThread.this.mLogger, "Got INFO message : type=" + i + " msg='" + str2 + "'");
        switch (i)
        {
        case 1:
        case 2:
        case 3:
        }
        return;
        Util.log(ConnectClientThread.this.mLogger, "Wifi is disabled.");
        continue;
        Util.log(ConnectClientThread.this.mLogger, "Wifi is disabling.");
        continue;
        Util.log(ConnectClientThread.this.mLogger, "Wifi is enabled.");
        continue;
        Util.log(ConnectClientThread.this.mLogger, "Wifi is enabling.");
      }
    }
  };
  private SystemLogMonitor mSysLogMon = null;
  private ThreadCom mThreadCom = null;
  private WifiManager mWifi = null;
  private ConnectToNetwork newConnection = null;

  public ConnectClientThread(ThreadCom paramThreadCom, NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, FailureReason paramFailureReason, Activity paramActivity, SystemLogMonitor paramSystemLogMonitor, WifiManager paramWifiManager)
  {
    super(paramLogger);
    setName("ConnectClient thread.");
    this.mFailureReason = paramFailureReason;
    this.mThreadCom = paramThreadCom;
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mParent = paramActivity;
    this.mSysLogMon = paramSystemLogMonitor;
    this.mWifi = paramWifiManager;
  }

  private void registerListener()
  {
    Util.log(this.mLogger, "Registering broadcast listener.");
    this.mIntentFilter = new IntentFilter();
    this.mIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
    this.mIntentFilter.addAction("android.net.wifi.SCAN_RESULTS");
    this.mIntentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
    this.mIntentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
    this.mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
    this.mIntentFilter.addAction("android.net.wifi.RSSI_CHANGED");
    this.mIntentFilter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
    this.mIntentFilter.addAction("android.net.wifi.supplicant.INFO");
    if (this.mParent != null)
      this.mParent.registerReceiver(this.mReceiver, this.mIntentFilter);
  }

  private boolean showFinalWebSite()
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "mParser is null in showFinalWebSite()!");
    do
    {
      return false;
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "No network selected in showFinalWebSite()!");
        return false;
      }
    }
    while ((this.mParser.selectedNetwork.endpointURL == null) || (this.mParser.selectedNetwork.endpointURL.length() <= 0) || (this.mParser.selectedNetwork.endpointURL.equals("http://")));
    return true;
  }

  private void showSite()
  {
    WebInterface localWebInterface = new WebInterface(this.mLogger);
    try
    {
      MapVariables localMapVariables1 = new MapVariables(this.mParser, this.mLogger);
      localMapVariables2 = localMapVariables1;
      arrayOfString = this.mParser.selectedNetwork.endpointURL.split("\\?");
      if (shouldDie())
        return;
    }
    catch (Exception localException)
    {
      MapVariables localMapVariables2;
      String[] arrayOfString;
      while (true)
      {
        localException.printStackTrace();
        Util.log(this.mLogger, "Unable to create variable mapper in showSite()!");
        localMapVariables2 = null;
      }
      if (arrayOfString.length >= 2)
      {
        if (localMapVariables2 != null)
          arrayOfString[1] = localMapVariables2.varMap(arrayOfString[1], true, this.mWifi);
        spinLock();
        localWebInterface.openWebPage(this.mParent, arrayOfString[0] + "?" + arrayOfString[1]);
        unlock();
        return;
      }
      spinLock();
      localWebInterface.openWebPage(this.mParent, this.mParser.selectedNetwork.endpointURL);
      unlock();
    }
  }

  private void unregisterListener()
  {
    if ((this.mParent != null) && (this.mReceiver != null));
    try
    {
      this.mParent.unregisterReceiver(this.mReceiver);
      return;
    }
    catch (Exception localException)
    {
      Util.log(this.mLogger, "Unable to unregister receiver.  Ignoring.  (Reason : " + localException.getLocalizedMessage() + ")");
    }
  }

  public void pause()
  {
    Util.log(this.mLogger, "Disconnected broadcast listener.");
    if (this.mReceiver == null)
      return;
    if (this.mParent != null);
    try
    {
      this.mParent.unregisterReceiver(this.mReceiver);
      this.mReceiver = null;
      return;
    }
    catch (Exception localException)
    {
      while (true)
        Util.log(this.mLogger, "Unable to unregister receiver.  Ignoring.  (Reason : " + localException.getLocalizedMessage() + ")");
    }
  }

  public void resume(ThreadCom paramThreadCom, Activity paramActivity)
  {
    this.mThreadCom = paramThreadCom;
    this.mParent = paramActivity;
    if (this.newConnection != null)
      this.newConnection.resume(paramActivity, this.mThreadCom);
    registerListener();
    unlock();
  }

  public void run()
  {
    super.run();
    if (this.mSysLogMon != null)
      this.mSysLogMon.reset();
    if (shouldDie())
      return;
    registerListener();
    if (shouldDie())
    {
      unregisterListener();
      return;
    }
    spinLock();
    if (this.mThreadCom != null)
      this.mThreadCom.update("Attempting to connect to the wireless network.");
    this.newConnection = new ConnectToNetwork(this.mParser, this.mLogger, this.mFailureReason, this.mParent, this.mSysLogMon, this.mWifi, this.mThreadCom);
    unlock();
    if (shouldDie())
    {
      unregisterListener();
      return;
    }
    int i = this.newConnection.connect(this);
    if (shouldDie())
    {
      unregisterListener();
      return;
    }
    if (i == 25)
    {
      spinLock();
      Util.log(this.mLogger, "Going back to change cert install method.");
      if (this.mThreadCom != null)
        this.mThreadCom.doFailure(Boolean.valueOf(false), i, null);
      unlock();
      unregisterListener();
      return;
    }
    if (shouldDie())
    {
      unregisterListener();
      return;
    }
    if (i != 1)
    {
      spinLock();
      Util.log(this.mLogger, "Failed to connect to the new network.");
      if (this.mThreadCom != null)
        this.mThreadCom.doFailure(Boolean.valueOf(false), i, null);
      unlock();
      unregisterListener();
      return;
    }
    if (shouldDie())
    {
      unregisterListener();
      return;
    }
    spinLock();
    if (this.mThreadCom != null)
      this.mThreadCom.update("Getting IP address.");
    this.dhcp = new GetDhcp(this.mParser, this.mLogger, this.mFailureReason, this.mParent, this.mWifi, this.mThreadCom, this);
    if (!this.dhcp.getDhcp())
    {
      if (shouldDie())
      {
        unregisterListener();
        return;
      }
      if (this.dhcp.getFailureId() == 33)
      {
        Util.log(this.mLogger, "Web site check failed.");
        if (this.mThreadCom != null)
          this.mThreadCom.doFailure(Boolean.valueOf(false), 33, null);
      }
      while (true)
      {
        unlock();
        unregisterListener();
        return;
        Util.log(this.mLogger, "Unable to get an IP address!");
        if (this.mThreadCom != null)
          this.mThreadCom.doFailure(Boolean.valueOf(false), 12, null);
      }
    }
    unlock();
    if (shouldDie())
    {
      unregisterListener();
      return;
    }
    if (showFinalWebSite())
    {
      showSite();
      spinLock();
      if (this.mThreadCom != null)
        this.mThreadCom.doSuccess();
      unlock();
      unregisterListener();
      return;
    }
    if (shouldDie())
    {
      unregisterListener();
      return;
    }
    spinLock();
    if (this.mThreadCom != null)
      this.mThreadCom.doSuccess();
    unlock();
    unregisterListener();
  }

  private class SupplicantInfo
  {
    public static final int INFO_EAP_PEER = 2;
    public static final int INFO_EAP_STATUS = 4;
    public static final int INFO_EAP_TLS_CERT_ERROR = 3;
    public static final int INFO_NOTIFICATION = 1;

    private SupplicantInfo()
    {
    }
  }
}