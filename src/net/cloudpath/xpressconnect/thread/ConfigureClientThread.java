package net.cloudpath.xpressconnect.thread;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.List;
import net.cloudpath.xpressconnect.ConfigCheck;
import net.cloudpath.xpressconnect.CreateNewConfiguration;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.WifiIoctl;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class ConfigureClientThread extends ThreadBase
{
  private static final int WIFI_ENABLE_ATTEMPTS = 30;
  private static final boolean mDumpConfigSig;
  private Context mContext = null;
  private FailureReason mFailureReason = null;
  private GetGlobals mGlobals = null;
  private Logger mLogger = null;
  private Activity mParent = null;
  private NetworkConfigParser mParser = null;
  private ThreadCom mThreadCom = null;
  private WifiManager mWifi = null;
  protected CreateNewConfiguration newConfig = null;

  public ConfigureClientThread(ThreadCom paramThreadCom, NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, FailureReason paramFailureReason, WifiManager paramWifiManager, Activity paramActivity, GetGlobals paramGetGlobals)
  {
    this(paramThreadCom, paramNetworkConfigParser, paramLogger, paramFailureReason, paramWifiManager, paramActivity, paramGetGlobals, paramActivity);
  }

  public ConfigureClientThread(ThreadCom paramThreadCom, NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, FailureReason paramFailureReason, WifiManager paramWifiManager, Activity paramActivity, GetGlobals paramGetGlobals, Context paramContext)
  {
    super(paramLogger);
    setName("ConfigureClient thread.");
    this.mFailureReason = paramFailureReason;
    this.mThreadCom = paramThreadCom;
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mParent = paramActivity;
    this.mGlobals = paramGetGlobals;
    this.mContext = paramContext;
    if (this.mParser == null)
      Util.log(this.mLogger, "!!!!! Parser is null in ConfigureClientThread()!");
    do
    {
      do
      {
        return;
        this.mWifi = paramWifiManager;
      }
      while ((this.mWifi == null) || (this.mWifi.getConnectionInfo() == null));
      this.mParser.originalSsid = this.mWifi.getConnectionInfo().getSSID();
    }
    while (this.mParser.originalSsid == null);
    Util.log(this.mLogger, "Original network is : " + this.mParser.originalSsid);
  }

  private boolean disableConflicting()
  {
    String[] arrayOfString = null;
    WifiIoctl localWifiIoctl = new WifiIoctl(this.mWifi);
    if (this.mWifi == null)
    {
      Util.log(this.mLogger, "wifi context is null in disableConflicting().");
      return false;
    }
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser is not bound in disableConflicting()!");
      return false;
    }
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "No network selected in disableConflicting()!");
      return false;
    }
    String str = Util.getSSID(this.mLogger, this.mParser);
    if (str == null)
    {
      Util.log(this.mLogger, "Target SSID is null.");
      return false;
    }
    Util.log(this.mLogger, "Our target SSID is : " + str);
    List localList = this.mWifi.getConfiguredNetworks();
    if (localList == null)
    {
      Util.log(this.mLogger, "No networks were found.  No networks will conflict.");
      return true;
    }
    if (this.mParser.selectedNetwork.ssidConflict == 2)
    {
      Util.log(this.mLogger, "Configured not to do anything with conflicting SSIDs.  Moving on..");
      return true;
    }
    Util.log(this.mLogger, "Conflicting networks setting is '" + this.mParser.selectedNetwork.ssidConflictsStr + "'.");
    if ((this.mParser.selectedNetwork.ssidConflictsStr == null) || (this.mParser.selectedNetwork.ssidConflictsStr.equals("*")))
      Util.log(this.mLogger, "Conflicting networks setting is *.");
    boolean bool;
    int i;
    while (true)
    {
      bool = thereCanBeOnlyOne(localList, str);
      i = 0;
      if (i >= localList.size())
        break label1328;
      if (!shouldDie())
        break;
      return false;
      if (this.mParser.selectedNetwork.ssidConflictsStr == null)
        return true;
      arrayOfString = this.mParser.selectedNetwork.ssidConflictsStr.split(";");
    }
    WifiConfigurationProxy localWifiConfigurationProxy;
    while (true)
    {
      try
      {
        localWifiConfigurationProxy = new WifiConfigurationProxy((WifiConfiguration)localList.get(i), this.mLogger);
        if (this.mParser.savedConfigInfo.targetNetId != localWifiConfigurationProxy.getNetworkId())
          break label536;
        if (localWifiConfigurationProxy.getSsid() != null)
        {
          Util.log(this.mLogger, "Skipping id = " + localWifiConfigurationProxy.getNetworkId() + " ssid = " + localWifiConfigurationProxy.getSsid() + " because it is our new target.");
          i++;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Util.log(this.mLogger, "IllegalArgumentException in disableConflicting()!");
        localIllegalArgumentException.printStackTrace();
        return false;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        Util.log(this.mLogger, "NoSuchFieldException in disableConflicting()!");
        localNoSuchFieldException.printStackTrace();
        return false;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        Util.log(this.mLogger, "ClassNotFoundException in disableConflicting()!");
        localClassNotFoundException.printStackTrace();
        return false;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        Util.log(this.mLogger, "IllegalAccessException in disableConflicting()!");
        localIllegalAccessException.printStackTrace();
        return false;
      }
      Util.log(this.mLogger, "Skipping id = " + localWifiConfigurationProxy.getNetworkId() + " with NULL ssid because it is our new target.");
      continue;
      label536: if (localWifiConfigurationProxy != null)
        if (localWifiConfigurationProxy.getSsid() == null)
        {
          Util.log(this.mLogger, "SSID to check is null.   Skipping.");
        }
        else if ((localWifiConfigurationProxy.getSsid().contentEquals(str)) || (localWifiConfigurationProxy.getSsid().contentEquals("\"" + str + "\"")))
        {
          Util.log(this.mLogger, "Deleting duplicate ssid instance.");
          if (!localWifiIoctl.deleteSsid(this.mParent, this.mLogger, localWifiConfigurationProxy.getNetworkId(), localWifiConfigurationProxy.getSsid()))
          {
            Util.log(this.mLogger, "Couldn't delete duplicate SSID.");
            return false;
          }
          Util.log(this.mLogger, "Back from delete.");
        }
        else
        {
          if (arrayOfString != null)
            break label1013;
          if (this.mParser.originalSsid != null)
            if ((localWifiConfigurationProxy.getSsid().contentEquals("\"" + this.mParser.originalSsid + "\"")) || (localWifiConfigurationProxy.getSsid().contentEquals(this.mParser.originalSsid)))
            {
              Util.log(this.mLogger, "(1) Found conflicting SSID '" + this.mParser.originalSsid + "' (" + localWifiConfigurationProxy.getNetworkId() + ").");
              if ((bool) && ((localWifiConfigurationProxy.getSsid().contentEquals("\"" + str + "\"")) || (localWifiConfigurationProxy.getSsid().contentEquals(str))))
              {
                Util.log(this.mLogger, "Hit Android ID numbering bug.  Working around it.");
                this.mParser.savedConfigInfo.targetNetId = localWifiConfigurationProxy.getNetworkId();
              }
            }
            else
            {
              switch (this.mParser.selectedNetwork.ssidConflict)
              {
              default:
                Util.log(this.mLogger, "Unknown conflicting SSID action of " + this.mParser.selectedNetwork.ssidConflict);
              case 2:
                Util.log(this.mLogger, "Done with conflicts.");
                break;
              case 0:
              case 1:
                Util.log(this.mLogger, "Delete conflicting ssid.");
                if (!localWifiIoctl.deleteSsid(this.mParent, this.mLogger, localWifiConfigurationProxy.getNetworkId(), localWifiConfigurationProxy.getSsid()))
                {
                  Util.log(this.mLogger, "Unable to delete duplicate SSID.");
                  return false;
                }
                Util.log(this.mLogger, "Done.");
              }
            }
        }
    }
    label1013: int j = 0;
    label1016: if (j < arrayOfString.length)
    {
      if (shouldDie())
        return false;
      if ((localWifiConfigurationProxy.getSsid().contentEquals("\"" + arrayOfString[j] + "\"")) || (localWifiConfigurationProxy.getSsid().contentEquals(arrayOfString[j])))
      {
        Util.log(this.mLogger, "(2) Found conflicting SSID '" + arrayOfString[j] + "' (" + localWifiConfigurationProxy.getNetworkId() + ").");
        if ((!bool) || ((!localWifiConfigurationProxy.getSsid().contentEquals("\"" + str + "\"")) && (!localWifiConfigurationProxy.getSsid().contentEquals(str))))
          break label1216;
        Util.log(this.mLogger, "Hit Android ID numbering bug.  Working around it.");
        this.mParser.savedConfigInfo.targetNetId = localWifiConfigurationProxy.getNetworkId();
      }
    }
    label1216: 
    do
      while (true)
      {
        j++;
        break label1016;
        break;
        switch (this.mParser.selectedNetwork.ssidConflict)
        {
        case 2:
        default:
          Util.log(this.mLogger, "Unknown conflicting SSID action of " + this.mParser.selectedNetwork.ssidConflict);
        case 0:
        case 1:
        }
      }
    while (localWifiIoctl.deleteSsid(this.mParent, this.mLogger, localWifiConfigurationProxy.getNetworkId(), localWifiConfigurationProxy.getSsid()));
    Util.log(this.mLogger, "Unable to delete SSID.");
    return false;
    label1328: if (!this.mWifi.saveConfiguration())
      Util.log(this.mLogger, "Unable to save configuration following deletion of duplicates.  Will attempt to continue anyway.");
    return true;
  }

  private boolean showFinalWebSite()
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "mParser is invalid in showFinalWebSite()!");
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

  protected boolean configureOnly()
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "mParser is invalid in configureOnly()!");
      return false;
    }
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "No network selected in configureOnly()!");
      return false;
    }
    switch (this.mParser.selectedNetwork.migrationBehavior)
    {
    default:
      return false;
    case 0:
      this.mParser.savedConfigInfo.migrationMethod = 0;
      return true;
    case 1:
      this.mParser.savedConfigInfo.migrationMethod = 1;
      return false;
    case 2:
    }
    if (isSsidInList() == true)
    {
      this.mParser.savedConfigInfo.migrationMethod = 1;
      return false;
    }
    this.mParser.savedConfigInfo.migrationMethod = 0;
    return true;
  }

  public boolean enableWifi()
  {
    int i = 0;
    if (this.mWifi == null)
    {
      Util.log(this.mLogger, "wifi context was null in needsConfigurationUpdates().");
      return false;
    }
    if (this.mFailureReason == null)
    {
      Util.log(this.mLogger, "mFailureReason was null in enableWifi().");
      return false;
    }
    this.mFailureReason.setFailReason(FailureReason.useWarningIcon, null);
    if (this.mWifi.getWifiState() != 3)
    {
      this.mWifi.setWifiEnabled(true);
      while (true)
        if ((i < 30) && (this.mWifi.getWifiState() != 3))
        {
          if (Testing.skipWireless)
          {
            Util.log(this.mLogger, "Testing, will pretend wireless is enabled.");
            return true;
          }
          Util.log(this.mLogger, "No wireless enabled.  Sleeping 1 second.");
          try
          {
            Thread.sleep(1000L);
            i++;
          }
          catch (InterruptedException localInterruptedException)
          {
            while (true)
            {
              Util.log(this.mLogger, "Failed to sleep for 1 second.  Wifi state will likely break!");
              localInterruptedException.printStackTrace();
            }
          }
        }
      if (this.mWifi.getWifiState() != 3)
      {
        ParcelHelper localParcelHelper = new ParcelHelper("", this.mContext);
        this.mFailureReason.setFailReason(FailureReason.useWarningIcon, this.mContext.getString(localParcelHelper.getIdentifier("xpc_cant_enable_wifi", "string")), 4);
        Util.log(this.mLogger, "Couldn't enable the wifi interface.");
        return false;
      }
    }
    return true;
  }

  protected void findActiveNet()
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "Parser is null in findActiveNet().");
    String str;
    while (true)
    {
      return;
      if (this.mParser.originalSsid == null)
      {
        Util.log(this.mLogger, "No original SSID recorded.  Won't be able to find the active network.");
        return;
      }
      int i = 0;
      str = this.mWifi.getConnectionInfo().getSSID();
      if ((str == null) && (i < 15))
        try
        {
          Thread.sleep(1000L);
          if (!shouldDie())
          {
            str = this.mWifi.getConnectionInfo().getSSID();
            i++;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
            localInterruptedException.printStackTrace();
        }
    }
    if (str == null)
    {
      Util.log(this.mLogger, "********* Unable to determine currently connected network!!!  It won't get deleted!");
      return;
    }
    this.mParser.originalSsid = str;
    Util.log(this.mLogger, "Active SSID is : " + this.mParser.originalSsid);
  }

  protected int getHighestPriority(List<WifiConfiguration> paramList)
  {
    int i = 0;
    if (paramList == null)
      return -1;
    for (int j = 0; j < paramList.size(); j++)
    {
      if (shouldDie())
        return -1;
      if (((WifiConfiguration)paramList.get(j)).priority > i)
        i = ((WifiConfiguration)paramList.get(j)).priority;
    }
    return i;
  }

  protected int getTargetNetId(List<WifiConfiguration> paramList, String paramString)
  {
    if ((paramList == null) || (paramString == null))
      return -1;
    for (int i = 0; i < paramList.size(); i++)
    {
      if (shouldDie())
        return -1;
      try
      {
        WifiConfigurationProxy localWifiConfigurationProxy = new WifiConfigurationProxy((WifiConfiguration)paramList.get(i), this.mLogger);
        if ((localWifiConfigurationProxy != null) && (localWifiConfigurationProxy.getSsid() != null) && ((localWifiConfigurationProxy.getSsid().contentEquals(paramString)) || (localWifiConfigurationProxy.getSsid().contains("\"" + paramString + "\""))))
        {
          Util.log(this.mLogger, "Found target SSID at " + localWifiConfigurationProxy.getNetworkId() + ".");
          return localWifiConfigurationProxy.getNetworkId();
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Util.log(this.mLogger, "IllegalArgumentException in getTargetNetId()!");
        localIllegalArgumentException.printStackTrace();
        return -1;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        Util.log(this.mLogger, "NoSuchFieldException in getTargetNetId()!");
        localNoSuchFieldException.printStackTrace();
        return -1;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        Util.log(this.mLogger, "ClassNotFoundException in getTargetNetId()!");
        localClassNotFoundException.printStackTrace();
        return -1;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        Util.log(this.mLogger, "IllegalAccessException in getTargetNetId()!");
        localIllegalAccessException.printStackTrace();
        return -1;
      }
    }
    return -1;
  }

  public ThreadCom getThreadCom()
  {
    return this.mThreadCom;
  }

  protected boolean isSsidInList()
  {
    String str = Util.getSSID(this.mLogger, this.mParser);
    if (str == null)
      return false;
    List localList = this.mWifi.getScanResults();
    for (int i = 0; i < localList.size(); i++)
    {
      if (shouldDie())
        return false;
      if ((((ScanResult)localList.get(i)).SSID != null) && (((ScanResult)localList.get(i)).SSID.contentEquals(str)))
      {
        Util.log(this.mLogger, "Found target SSID in scan list.  Will migrate.");
        return true;
      }
    }
    Util.log(this.mLogger, "Target SSID not found in scan list.  Will only configure.");
    return false;
  }

  public void resume(ThreadCom paramThreadCom, Activity paramActivity)
  {
    this.mThreadCom = paramThreadCom;
    this.mParent = paramActivity;
    if (this.newConfig != null)
      this.newConfig.resume(paramActivity);
  }

  public void run()
  {
    int i = 0;
    super.run();
    if (shouldDie());
    label589: 
    do
    {
      do
      {
        do
        {
          do
          {
            do
            {
              return;
              findActiveNet();
            }
            while (shouldDie());
            spinLock();
            if (this.mThreadCom != null)
              this.mThreadCom.update("Checking your current configuration.");
            unlock();
          }
          while (shouldDie());
          spinLock();
          if ((!configureOnly()) && (!enableWifi()))
          {
            if ((this.mFailureReason != null) && (!Util.stringIsEmpty(this.mFailureReason.getFailureString())))
            {
              if (this.mThreadCom != null)
                this.mThreadCom.doFailure(Boolean.valueOf(false), 21, null);
              unlock();
              return;
            }
            if (this.mThreadCom != null)
              this.mThreadCom.doFailure(Boolean.valueOf(false), 15, null);
            unlock();
            return;
          }
          unlock();
        }
        while (shouldDie());
        spinLock();
        if (this.mThreadCom != null)
          this.mThreadCom.update("Configuring your new network.");
        unlock();
        while (true)
        {
          if (i != 0)
            break label589;
          if (shouldDie())
            break;
          spinLock();
          this.mParser.savedConfigInfo.higestPriority = getHighestPriority(this.mWifi.getConfiguredNetworks());
          this.mParser.savedConfigInfo.targetNetId = getTargetNetId(this.mWifi.getConfiguredNetworks(), Util.getSSID(this.mLogger, this.mParser));
          unlock();
          this.newConfig = new CreateNewConfiguration(this.mParser, this.mLogger, this.mFailureReason, this.mParent, this.mWifi, this, this.mGlobals, this.mContext);
          if (!this.newConfig.buildConfiguration())
          {
            Util.log(this.mLogger, "Failed to create new configuration!");
            if (this.mThreadCom != null)
              this.mThreadCom.doFailure(Boolean.valueOf(false), this.newConfig.getFailReason(), null);
            this.newConfig = null;
            return;
          }
          this.newConfig = null;
          if (shouldDie())
            break;
          if ((!validate(this.mParser.savedConfigInfo.targetNetId)) && (validate(-1 + this.mParser.savedConfigInfo.targetNetId)))
          {
            SavedConfigInfo localSavedConfigInfo = this.mParser.savedConfigInfo;
            localSavedConfigInfo.targetNetId = (-1 + localSavedConfigInfo.targetNetId);
          }
          Util.log(this.mLogger, "Target network should be : " + this.mParser.savedConfigInfo.targetNetId);
          if (shouldDie())
            break;
          spinLock();
          if (!disableConflicting())
          {
            if (this.mThreadCom == null)
              break;
            this.mThreadCom.doFailure(Boolean.valueOf(false), 15, "Unable to delete conflicting network configurations.");
            return;
          }
          unlock();
          if (shouldDie())
            break;
          if (!validate(this.mParser.savedConfigInfo.targetNetId))
          {
            Util.log(this.mLogger, "Triggered Android delete bug.");
            if (1 < 0)
            {
              Util.log(this.mLogger, "Will try again.");
              (0 + 1);
              continue;
            }
            Util.log(this.mLogger, "Just outright failed.");
            if (this.mThreadCom == null)
              break;
            this.mThreadCom.doFailure(Boolean.valueOf(false), 15, "Unable to delete conflicting network configurations.");
            return;
          }
          i = 1;
        }
      }
      while (shouldDie());
      if (configureOnly())
      {
        Util.log(this.mLogger, "Client is configured to only configure, and not migrate.");
        if (showFinalWebSite())
        {
          showSite();
          spinLock();
          if (this.mThreadCom != null)
            this.mThreadCom.doSuccess();
          unlock();
          return;
        }
      }
    }
    while (shouldDie());
    spinLock();
    if (this.mThreadCom != null)
      this.mThreadCom.doSuccess();
    unlock();
  }

  protected void showSite()
  {
    showSite(new WebInterface(this.mLogger));
  }

  protected void showSite(WebInterface paramWebInterface)
  {
    if ((this.mParser == null) || (this.mParser.selectedNetwork == null) || (this.mParser.selectedNetwork.endpointURL == null))
      Util.log(this.mLogger, "Invalid input data in showSite()!");
    while (true)
    {
      return;
      try
      {
        MapVariables localMapVariables1 = new MapVariables(this.mParser, this.mLogger);
        localMapVariables2 = localMapVariables1;
        String[] arrayOfString = this.mParser.selectedNetwork.endpointURL.split("\\?");
        if (!shouldDie())
          if (arrayOfString.length >= 2)
          {
            if (localMapVariables2 != null)
              arrayOfString[1] = localMapVariables2.varMap(arrayOfString[1], true, this.mWifi);
            spinLock();
            paramWebInterface.openWebPage(this.mParent, arrayOfString[0] + "?" + arrayOfString[1]);
            unlock();
            return;
          }
      }
      catch (Exception localException)
      {
        while (true)
        {
          localException.printStackTrace();
          Util.log(this.mLogger, "Unable to create variable mapper in showSite()!");
          MapVariables localMapVariables2 = null;
        }
        spinLock();
        paramWebInterface.openWebPage(this.mParent, this.mParser.selectedNetwork.endpointURL);
        unlock();
      }
    }
  }

  protected boolean thereCanBeOnlyOne(List<WifiConfiguration> paramList, String paramString)
  {
    int i = 0;
    if ((paramList == null) || (paramString == null))
    {
      Util.log(this.mLogger, "Nothing to search for!");
      return false;
    }
    for (int j = 0; j < paramList.size(); j++)
    {
      if (shouldDie())
        return false;
      if ((paramList.get(j) != null) && (((WifiConfiguration)paramList.get(j)).SSID != null) && ((((WifiConfiguration)paramList.get(j)).SSID.contentEquals("\"" + paramString + "\"")) || (((WifiConfiguration)paramList.get(j)).SSID.contentEquals(paramString))))
        i++;
    }
    if (i == 0)
      Util.log(this.mLogger, "Somehow the network we created got lost in the shuffle.  Not good!");
    return i == 1;
  }

  protected boolean validate(int paramInt)
  {
    int i = -1;
    while (true)
    {
      try
      {
        List localList = this.mWifi.getConfiguredNetworks();
        if (localList == null)
        {
          return false;
          if (j < localList.size())
          {
            if (shouldDie())
              return false;
            if (((WifiConfiguration)localList.get(j)).networkId == paramInt)
              i = j;
          }
          else
          {
            if (i >= 0)
              continue;
            Util.log(this.mLogger, "Unable to validate network.  The configurtion doesn't seem to exist.");
            return false;
          }
          j++;
          continue;
          try
          {
            WifiConfigurationProxy localWifiConfigurationProxy = new WifiConfigurationProxy((WifiConfiguration)localList.get(i), this.mLogger);
            return new ConfigCheck(this.mParser, this.mLogger, localWifiConfigurationProxy, this.mFailureReason, this.mParent).ssidSettingsAreCorrect();
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            Util.log(this.mLogger, "IllegalArgumentException in validate()!");
            localIllegalArgumentException.printStackTrace();
            return false;
          }
          catch (NoSuchFieldException localNoSuchFieldException)
          {
            Util.log(this.mLogger, "NoSuchFieldException in validate()!");
            localNoSuchFieldException.printStackTrace();
            return false;
          }
          catch (ClassNotFoundException localClassNotFoundException)
          {
            Util.log(this.mLogger, "ClassNotFoundException in validate()!");
            localClassNotFoundException.printStackTrace();
            return false;
          }
          catch (IllegalAccessException localIllegalAccessException)
          {
            Util.log(this.mLogger, "IllegalAccessException in validate()!");
            localIllegalAccessException.printStackTrace();
            return false;
          }
        }
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        Util.log(this.mLogger, "Invalid index to the configuration.  " + paramInt + " is not the index we are looking for!");
        return false;
      }
      int j = 0;
    }
  }
}