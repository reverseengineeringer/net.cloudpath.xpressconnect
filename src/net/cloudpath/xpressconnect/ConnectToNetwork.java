package net.cloudpath.xpressconnect;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.List;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.thread.ConnectClientThread;
import net.cloudpath.xpressconnect.thread.SystemLogMonitor;
import net.cloudpath.xpressconnect.thread.ThreadCom;

public class ConnectToNetwork
{
  private static final int CONNECT_TIMEOUT = 35;
  private static final int EXTENDED_CONNECT_TIMEOUT = 15;
  private boolean assocFailure = false;
  private boolean attemptingConnect = true;
  private boolean credsLikelyBad = false;
  private int currentWifiState = 4;
  private boolean keyFailure = false;
  private SupplicantState lastState = null;
  private boolean mConnectAttempted = false;
  private Context mContext = null;
  private FailureReason mFailure = null;
  private Logger mLogger = null;
  private NetworkConfigParser mParser = null;
  private SystemLogMonitor mSysLogMon = null;
  private ThreadCom mThreadCom = null;
  private boolean mWaitingForFinalFinish = false;
  private WifiManager mWifi = null;

  public ConnectToNetwork(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, FailureReason paramFailureReason, Context paramContext, SystemLogMonitor paramSystemLogMonitor, WifiManager paramWifiManager, ThreadCom paramThreadCom)
  {
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mFailure = paramFailureReason;
    this.mSysLogMon = paramSystemLogMonitor;
    this.mContext = paramContext;
    this.mThreadCom = paramThreadCom;
    this.mWifi = paramWifiManager;
    if (this.mWifi != null)
    {
      this.mWifi.setWifiEnabled(true);
      return;
    }
    Util.log(this.mLogger, "Wifi context was null in ConnectToNetwork()!");
  }

  private boolean areConnected()
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "Parser is null in monitorConnection()!");
    String str;
    WifiInfo localWifiInfo;
    do
    {
      do
      {
        return false;
        if (this.mParser.selectedProfile == null)
        {
          Util.log(this.mLogger, "No profile selected in monitorConnection()!");
          return false;
        }
        SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(1, 40001);
        if (localSettingElement == null)
        {
          Util.log(this.mLogger, "Unable to locate the SSID element!");
          return false;
        }
        if (localSettingElement.additionalValue == null)
        {
          Util.log(this.mLogger, "No SSID information in our configuration!?");
          return false;
        }
        String[] arrayOfString = localSettingElement.additionalValue.split(":");
        if (arrayOfString.length != 3)
        {
          Util.log(this.mLogger, "The SSID element provided in the configuration file is invalid!");
          return false;
        }
        str = arrayOfString[2];
        if (this.mWifi == null)
        {
          Util.log(this.mLogger, "Unable to get a handle to the Wifi service.");
          return false;
        }
        localWifiInfo = this.mWifi.getConnectionInfo();
      }
      while ((localWifiInfo == null) || (localWifiInfo.getSupplicantState() == null) || (((this.lastState == null) || (!this.lastState.equals(SupplicantState.COMPLETED))) && (localWifiInfo.getSupplicantState() != SupplicantState.COMPLETED)));
      Util.log(this.mLogger, "Supplicant indicates completion...  Check SSID.");
      if (localWifiInfo.getSSID() != null)
      {
        Util.log(this.mLogger, "SSID configured : " + stripQuotes(str));
        Util.log(this.mLogger, "SSID connected : " + stripQuotes(localWifiInfo.getSSID()));
      }
    }
    while ((localWifiInfo.getSSID() == null) || (!stripQuotes(localWifiInfo.getSSID()).contentEquals(stripQuotes(str))));
    Util.log(this.mLogger, "Successfully associated/authenticated to the network.");
    return true;
  }

  private boolean areConnectedStable(ConnectClientThread paramConnectClientThread)
  {
    if (!areConnected())
      return false;
    int i = 0;
    while (true)
      while (true)
      {
        if (i >= 30)
          break label69;
        if (paramConnectClientThread.shouldDie())
          break;
        try
        {
          Thread.sleep(100L);
          if (paramConnectClientThread.shouldDie())
            break;
          if (!areConnected())
          {
            Util.log(this.mLogger, "*-*-*-*-* Connection didn't stick!");
            return false;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
            localInterruptedException.printStackTrace();
          i++;
        }
      }
    label69: return true;
  }

  private int isConnectionFound(ConnectClientThread paramConnectClientThread)
  {
    if (this.mWifi == null)
    {
      Util.log(this.mLogger, "wifi context was null.");
      return -2;
    }
    List localList = this.mWifi.getConfiguredNetworks();
    if (localList == null)
    {
      Util.log(this.mLogger, "Networks list returned was null in isConnectionFound().");
      return -2;
    }
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser is null in isConnectionFound()");
      return -2;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile is selected in isConnectionFound()!");
      return -2;
    }
    Util.log(this.mLogger, "Your device currently has " + localList.size() + " wireless network(s) configured!");
    int i = 0;
    if (i < localList.size())
    {
      if (paramConnectClientThread.shouldDie())
        return -2;
      WifiConfigurationProxy localWifiConfigurationProxy;
      SettingElement localSettingElement;
      try
      {
        localWifiConfigurationProxy = new WifiConfigurationProxy((WifiConfiguration)localList.get(i), this.mLogger);
        localSettingElement = this.mParser.selectedProfile.getSetting(1, 40001);
        if (localSettingElement == null)
        {
          Util.log(this.mLogger, "Unable to locate the SSID element!");
          if (this.mFailure != null)
          {
            ParcelHelper localParcelHelper2 = new ParcelHelper("", this.mContext);
            this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(localParcelHelper2.getIdentifier("xpc_config_missing_ssid_element", "string")), 5);
          }
          return -2;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Util.log(this.mLogger, "IllegalArgumentException in isConnectionFound()!");
        localIllegalArgumentException.printStackTrace();
        return -2;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        Util.log(this.mLogger, "NoSuchFieldException in isConnectionFound()!");
        localNoSuchFieldException.printStackTrace();
        return -2;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        Util.log(this.mLogger, "ClassNotFoundException in isConnectionFound()!");
        localClassNotFoundException.printStackTrace();
        return -2;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        Util.log(this.mLogger, "IllegalAccessException in isConnectionFound()!");
        localIllegalAccessException.printStackTrace();
        return -2;
      }
      if (paramConnectClientThread.shouldDie())
        return -2;
      if (localSettingElement.additionalValue == null)
      {
        Util.log(this.mLogger, "No additional value defined for element in isConnectionFound()!");
        return -2;
      }
      String[] arrayOfString = localSettingElement.additionalValue.split(":");
      if (arrayOfString.length != 3)
      {
        Util.log(this.mLogger, "The SSID element provided in the configuration file is invalid!");
        if (this.mFailure != null)
        {
          ParcelHelper localParcelHelper1 = new ParcelHelper("", this.mContext);
          this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(localParcelHelper1.getIdentifier("xpc_config_missing_ssid_element", "string")), 5);
        }
        return -2;
      }
      if (paramConnectClientThread.shouldDie())
        return -2;
      if (localWifiConfigurationProxy.getSsid() == null)
        Util.log(this.mLogger, "No SSID specified in the configuration.");
      while (!localWifiConfigurationProxy.getSsid().equals("\"" + arrayOfString[2] + "\""))
      {
        i++;
        break;
      }
      return localWifiConfigurationProxy.getNetworkId();
    }
    return -1;
  }

  private int monitorConnection(ConnectClientThread paramConnectClientThread)
  {
    int i = 0;
    int j = 0;
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser is null in monitorConnection()!");
      return 15;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile selected in monitorConnection()!");
      return 15;
    }
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(1, 40001);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Unable to locate the SSID element!");
      if (this.mFailure != null)
      {
        ParcelHelper localParcelHelper3 = new ParcelHelper("", this.mContext);
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getString(localParcelHelper3.getIdentifier("xpc_config_missing_ssid_element", "string")), 5);
      }
      return 15;
    }
    if (localSettingElement.additionalValue == null)
    {
      Util.log(this.mLogger, "No SSID information in our configuration!?");
      return 15;
    }
    String[] arrayOfString = localSettingElement.additionalValue.split(":");
    if (arrayOfString.length != 3)
    {
      Util.log(this.mLogger, "The SSID element provided in the configuration file is invalid!");
      if (this.mFailure != null)
      {
        ParcelHelper localParcelHelper2 = new ParcelHelper("", this.mContext);
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getString(localParcelHelper2.getIdentifier("xpc_config_file_ssid_invalid", "string")), 5);
      }
      return 15;
    }
    String str = arrayOfString[2];
    if (this.mWifi == null)
    {
      Util.log(this.mLogger, "Unable to get a handle to the Wifi service.");
      if (this.mFailure != null)
      {
        ParcelHelper localParcelHelper1 = new ParcelHelper("", this.mContext);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getString(localParcelHelper1.getIdentifier("xpc_no_wifi_service", "string")), 4);
      }
      return 15;
    }
    WifiInfo localWifiInfo = this.mWifi.getConnectionInfo();
    while (true)
    {
      if ((j == 0) || (this.mWaitingForFinalFinish == true))
      {
        if (paramConnectClientThread.shouldDie())
          return -1;
        if (i >= 350)
        {
          j = 1;
          if ((this.lastState == SupplicantState.ASSOCIATED) || (this.lastState == SupplicantState.FOUR_WAY_HANDSHAKE) || (this.lastState == SupplicantState.GROUP_HANDSHAKE) || (this.lastState == SupplicantState.COMPLETED))
          {
            if (!this.mWaitingForFinalFinish)
              Util.log(this.mLogger, "Authentication is in process.  Waiting for an outcome.");
            this.mWaitingForFinalFinish = true;
            if (i >= 500)
              this.mWaitingForFinalFinish = false;
          }
        }
        if (!areConnectedStable(paramConnectClientThread));
      }
      else
      {
        this.mFailure.clearFailureReason();
        if (!areConnected())
          break;
        Util.log(this.mLogger, "We connected to the correct SSID.");
        return 1;
      }
      if (paramConnectClientThread.shouldDie())
        return -1;
      try
      {
        Thread.sleep(100L);
        if (paramConnectClientThread.shouldDie())
          return -1;
      }
      catch (InterruptedException localInterruptedException)
      {
        while (true)
        {
          Util.log(this.mLogger, "Unable to sleep in the thread while waiting to connect!");
          localInterruptedException.printStackTrace();
        }
        if (!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure))
        {
          if ((this.mSysLogMon.switchCertType()) && (this.mParser.savedConfigInfo.connectOnly != 1))
          {
            Util.log(this.mLogger, "!!!! Need to switch certificate type!");
            return 25;
          }
          if ((this.mSysLogMon.enableKeystore()) && (!this.mParser.savedConfigInfo.forceOpenKeystore) && (this.mParser.savedConfigInfo.connectOnly != 1))
          {
            Util.log(this.mLogger, "!!!! Need to enable keystore even though certs aren't in use!");
            return 27;
          }
        }
        localWifiInfo = this.mWifi.getConnectionInfo();
        i++;
      }
    }
    Util.log(this.mLogger, "The supplicant indicated the connection was complete, but we didn't end up on the network we expected.");
    Util.log(this.mLogger, "We connected to '" + stripQuotes(localWifiInfo.getSSID()) + "' when we wanted to connect to '" + stripQuotes(str) + "'.");
    this.mFailure.mCredsLikelyBad = this.credsLikelyBad;
    this.mFailure.mKeyFailure = this.keyFailure;
    this.mFailure.mAssocFailure = this.assocFailure;
    this.mFailure.mConnectionAttempted = this.mConnectAttempted;
    this.mFailure.mSSIDIsInScanList = ssidIsInScanList(paramConnectClientThread);
    this.mFailure.gatherFailData(this.mSysLogMon);
    this.mFailure.dumpVariables(this.mLogger);
    this.mFailure.getConnectFailureReason(this.mContext, this.mParser);
    return -3;
  }

  private boolean ssidIsInScanList(ConnectClientThread paramConnectClientThread)
  {
    List localList = this.mWifi.getScanResults();
    if (localList == null)
      Util.log(this.mLogger, "No SSID information available.");
    while (true)
    {
      return false;
      if ((this.mParser == null) || (this.mParser.selectedProfile == null))
      {
        Util.log(this.mLogger, "Parser data is null in ssidIsInScanList()!");
        return false;
      }
      SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(1, 40001);
      if (localSettingElement.additionalValue == null)
      {
        Util.log(this.mLogger, "No SSID information in our configuration!?");
        return false;
      }
      String[] arrayOfString = localSettingElement.additionalValue.split(":");
      if (arrayOfString.length != 3)
      {
        Util.log(this.mLogger, "The SSID element provided in the configuration file is invalid!");
        if (this.mFailure != null)
        {
          ParcelHelper localParcelHelper = new ParcelHelper("", this.mContext);
          this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getString(localParcelHelper.getIdentifier("xpc_config_file_ssid_invalid", "string")), 5);
          return false;
        }
      }
      else
      {
        String str = arrayOfString[2];
        for (int i = 0; (i < localList.size()) && (!paramConnectClientThread.shouldDie()); i++)
        {
          ScanResult localScanResult = (ScanResult)localList.get(i);
          if ((localScanResult != null) && (localScanResult.SSID.contentEquals(str)))
          {
            Util.log(this.mLogger, "Found matching SSID in the scan list!");
            return true;
          }
        }
      }
    }
  }

  public int connect(ConnectClientThread paramConnectClientThread)
  {
    int i = -10;
    if (this.mWifi == null)
    {
      Util.log(this.mLogger, "Unable to use wifi class.  Bailing out.");
      return 13;
    }
    Util.log(this.mLogger, "Wifi state is : " + this.mWifi.getWifiState());
    int j = isConnectionFound(paramConnectClientThread);
    if (j < 0)
    {
      if (!this.mWifi.isWifiEnabled())
        Util.log(this.mLogger, "Wireless isn't enabled!?");
      this.mFailure.mNoConnectionFound = true;
      Util.log(this.mLogger, "Unable to locate the configuration that we just created.  Something bad happened!!");
      this.mFailure.gatherFailData(this.mSysLogMon);
      this.mFailure.dumpVariables(this.mLogger);
      this.mFailure.getConnectFailureReason(this.mContext, this.mParser);
      return 13;
    }
    if (paramConnectClientThread.shouldDie())
      return -1;
    Util.log(this.mLogger, "Found network connection at id " + j);
    if (this.mWifi == null)
    {
      ParcelHelper localParcelHelper1 = new ParcelHelper("", this.mContext);
      this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getString(localParcelHelper1.getIdentifier("xpc_internal_error", "string")), 6);
      Util.log(this.mLogger, "wifi context was null in ConnectToNetwork::connect().");
      return 15;
    }
    if ((!this.mParser.savedConfigInfo.isHidden) && (!ssidIsInScanList(paramConnectClientThread)))
    {
      Util.log(this.mLogger, "!!!! The SSID wasn't found in the scan list (Starting scan) !!!!");
      if (!this.mWifi.startScan())
        Util.log(this.mLogger, "  Unable to start a scan.");
    }
    if (areConnected())
    {
      Util.log(this.mLogger, "Already connected to the correct network, moving on.");
      return 1;
    }
    int k = 0;
    if (k < 3)
    {
      if (paramConnectClientThread.shouldDie())
        return -1;
      if (k == 2)
      {
        int i2 = 0;
        while (true)
          if (i2 < 3)
          {
            if (paramConnectClientThread.shouldDie())
              return -1;
            if (!this.mWifi.enableNetwork(j, true))
            {
              Util.log(this.mLogger, "Unable to enableNetwork.  Waiting 10 seconds and trying again.  (Attempt " + (i2 + 1) + " of 3)");
              try
              {
                Thread.sleep(10000L);
                i2++;
              }
              catch (InterruptedException localInterruptedException3)
              {
                while (true)
                {
                  Util.log(this.mLogger, "Unable to sleep.");
                  localInterruptedException3.printStackTrace();
                }
              }
            }
          }
        if (i2 >= 3)
        {
          this.mFailure.mEnableNetworkFail = true;
          Util.log(this.mLogger, "Unable to request the network connection! (enableNetwork failed.)");
          Util.log(this.mLogger, "Looking to see if our network is in the list anymore.");
          int i3 = isConnectionFound(paramConnectClientThread);
          if (i3 >= 0)
            Util.log(this.mLogger, "Connection was found at id " + i3 + ".");
          while (true)
          {
            this.mFailure.gatherFailData(this.mSysLogMon);
            this.mFailure.dumpVariables(this.mLogger);
            this.mFailure.getConnectFailureReason(this.mContext, this.mParser);
            return 14;
            Util.log(this.mLogger, "Connection wasn't found in the list anymore.");
          }
        }
      }
      else if (!this.mWifi.enableNetwork(j, true))
      {
        Util.log(this.mLogger, "Unable to request the network connection! (enableNetwork failed.)");
        Util.log(this.mLogger, "Looking to see if our network is in the list anymore.");
        int i1 = isConnectionFound(paramConnectClientThread);
        if (i1 >= 0)
          Util.log(this.mLogger, "Connection was found at id " + i1 + ".");
        while (true)
        {
          this.mFailure.mEnableNetworkFail = true;
          this.mFailure.gatherFailData(this.mSysLogMon);
          this.mFailure.dumpVariables(this.mLogger);
          this.mFailure.getConnectFailureReason(this.mContext, this.mParser);
          return 14;
          Util.log(this.mLogger, "Connection wasn't found in the list anymore.");
        }
      }
      this.attemptingConnect = true;
      this.mFailure.isTerminal = false;
      Util.log(this.mLogger, "Connect attempt " + (k + 1) + " of 3.");
      i = monitorConnection(paramConnectClientThread);
      if (paramConnectClientThread.shouldDie())
        return -1;
      if (i != 1)
        break label850;
    }
    while (true)
    {
      this.attemptingConnect = false;
      if ((i == 1) || (areConnected()))
        break label1198;
      Util.log(this.mLogger, "The attempt to connect failed.");
      this.mFailure.gatherFailData(this.mSysLogMon);
      this.mFailure.dumpVariables(this.mLogger);
      this.mFailure.getConnectFailureReason(this.mContext, this.mParser);
      return this.mFailure.mResult;
      label850: if (i == -1)
        return -1;
      this.mFailure.gatherFailData(this.mSysLogMon);
      if (this.mFailure.isTerminal != true)
        break;
      Util.log(this.mLogger, "Failure is terminal.  Variable dump : ");
      this.mFailure.dumpVariables(this.mLogger);
    }
    if (i == 25)
      return 25;
    if (i == 27)
      return 27;
    int n;
    if (k == 1)
    {
      Util.log(this.mLogger, "Bouncing the interface to try to get it to connect.");
      if (!this.mWifi.setWifiEnabled(false))
        Util.log(this.mLogger, "Couldn't disable the wireless interface.  Perhaps it is locked?");
      for (int m = 0; ; m++)
      {
        if (m < 30)
        {
          if (paramConnectClientThread.shouldDie())
          {
            this.mWifi.setWifiEnabled(true);
            return -1;
          }
          try
          {
            Thread.sleep(1000L);
            if (paramConnectClientThread.shouldDie())
              return -1;
          }
          catch (InterruptedException localInterruptedException2)
          {
            while (true)
              localInterruptedException2.printStackTrace();
          }
          if (this.currentWifiState == 1)
            Util.log(this.mLogger, "Wifi interface has been disabled.  Moving on.");
        }
        else
        {
          if (this.mWifi.setWifiEnabled(true))
            break;
          Util.log(this.mLogger, "Couldn't enable the wireless interface!  Cannot continue.");
          ParcelHelper localParcelHelper2 = new ParcelHelper("", this.mContext);
          this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getString(localParcelHelper2.getIdentifier("xpc_cant_toggle_wifi", "string")), 4);
          return 14;
        }
        Util.log(this.mLogger, "Current state is : " + this.currentWifiState);
      }
      n = 0;
    }
    while (true)
      while (true)
      {
        if (n < 30)
          if (paramConnectClientThread.shouldDie())
            return -1;
        try
        {
          Thread.sleep(1000L);
          if (this.currentWifiState == 3)
          {
            Util.log(this.mLogger, "Wifi interface has been enabled.  Moving on.");
            k++;
          }
        }
        catch (InterruptedException localInterruptedException1)
        {
          while (true)
            localInterruptedException1.printStackTrace();
          n++;
        }
      }
    label1198: this.mFailure.clearFailureReason();
    return 1;
  }

  public void resume(Activity paramActivity, ThreadCom paramThreadCom)
  {
    this.mContext = paramActivity;
    this.mThreadCom = paramThreadCom;
  }

  protected String stripQuotes(String paramString)
  {
    if (paramString == null)
      return null;
    if ((paramString.startsWith("\"")) && (paramString.endsWith("\"")))
      return paramString.substring(1, -1 + paramString.length());
    return paramString;
  }

  public void updateSupState(SupplicantState paramSupplicantState)
  {
    if (paramSupplicantState == null)
      Util.log(this.mLogger, "Invalid state passed to updateSupState()!");
    while (!this.attemptingConnect)
      return;
    if ((paramSupplicantState == SupplicantState.ASSOCIATED) || (paramSupplicantState == SupplicantState.COMPLETED) || (paramSupplicantState == SupplicantState.FOUR_WAY_HANDSHAKE) || (paramSupplicantState == SupplicantState.GROUP_HANDSHAKE))
      this.mThreadCom.changeTab(3);
    while (true)
    {
      if ((this.lastState == SupplicantState.ASSOCIATED) && (WifiInfo.getDetailedStateOf(this.lastState) == NetworkInfo.DetailedState.CONNECTING) && (paramSupplicantState == SupplicantState.DISCONNECTED) && (WifiInfo.getDetailedStateOf(paramSupplicantState) == NetworkInfo.DetailedState.DISCONNECTED))
      {
        Util.log(this.mLogger, "Supplicant state indicated user's credentials may be bad.");
        this.credsLikelyBad = true;
      }
      if ((paramSupplicantState == SupplicantState.FOUR_WAY_HANDSHAKE) || (paramSupplicantState == SupplicantState.GROUP_HANDSHAKE))
      {
        Util.log(this.mLogger, "Users credentials are valid.");
        this.credsLikelyBad = false;
      }
      if (((this.lastState == SupplicantState.FOUR_WAY_HANDSHAKE) && (paramSupplicantState != SupplicantState.GROUP_HANDSHAKE)) || ((this.lastState == SupplicantState.FOUR_WAY_HANDSHAKE) && (paramSupplicantState != SupplicantState.COMPLETED)))
      {
        Util.log(this.mLogger, "Didn't transition from unicast keying to group keying.");
        this.keyFailure = true;
      }
      if ((this.lastState == SupplicantState.GROUP_HANDSHAKE) && (paramSupplicantState != SupplicantState.COMPLETED))
      {
        Util.log(this.mLogger, "Didn't finish the keying handshake.");
        this.keyFailure = true;
      }
      if (paramSupplicantState == SupplicantState.COMPLETED)
      {
        Util.log(this.mLogger, "Got keys successfully.");
        this.keyFailure = false;
      }
      if ((this.lastState == SupplicantState.ASSOCIATING) && (paramSupplicantState == SupplicantState.DISCONNECTED))
      {
        Util.log(this.mLogger, "Failed to associate.");
        this.assocFailure = true;
      }
      if (paramSupplicantState == SupplicantState.ASSOCIATING)
        this.mConnectAttempted = true;
      if (paramSupplicantState == SupplicantState.ASSOCIATED)
        this.assocFailure = false;
      if ((this.mWaitingForFinalFinish) && ((paramSupplicantState == SupplicantState.ASSOCIATING) || (paramSupplicantState == SupplicantState.DISCONNECTED) || (paramSupplicantState == SupplicantState.DORMANT) || (paramSupplicantState == SupplicantState.INACTIVE) || (paramSupplicantState == SupplicantState.INVALID) || (paramSupplicantState == SupplicantState.SCANNING) || (paramSupplicantState == SupplicantState.UNINITIALIZED)))
      {
        Util.log(this.mLogger, "Delayed authentication attempt failed.");
        this.mWaitingForFinalFinish = false;
      }
      this.lastState = paramSupplicantState;
      return;
      this.mThreadCom.changeTab(2);
    }
  }

  public void updateWifiState(int paramInt)
  {
    Util.log(this.mLogger, "Setting state to : " + paramInt);
    this.currentWifiState = paramInt;
  }
}