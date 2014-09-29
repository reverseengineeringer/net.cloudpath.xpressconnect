package net.cloudpath.xpressconnect.thread;

import android.net.wifi.WifiManager;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class PreConfigUrlThread extends ThreadBase
{
  private Logger mLogger = null;
  private NetworkConfigParser mParser = null;
  private ThreadCom mThreadCom = null;
  private WifiManager mWifi = null;

  public PreConfigUrlThread(ThreadCom paramThreadCom, NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, WifiManager paramWifiManager)
  {
    super(paramLogger);
    setName("PreConfigUrl thread.");
    this.mLogger = paramLogger;
    this.mThreadCom = paramThreadCom;
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mWifi = paramWifiManager;
  }

  public void run()
  {
    WebInterface localWebInterface = new WebInterface(this.mLogger);
    super.run();
    if (this.mParser == null)
      Util.log(this.mLogger, "Parser is null in PreConfigUrlThread!");
    label474: 
    do
    {
      return;
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "No network selected in PreConfigUrlThread!");
        return;
      }
      if (Util.stringIsEmpty(this.mParser.selectedNetwork.registrationUrl))
      {
        Util.log(this.mLogger, "Ended up in the pre config url thread when we shouldn't have been.  Dropping out..");
        spinLock();
        if (this.mThreadCom != null)
          this.mThreadCom.doSuccess();
        unlock();
        return;
      }
      spinLock();
      while (true)
      {
        MapVariables localMapVariables;
        String[] arrayOfString;
        try
        {
          localMapVariables = new MapVariables(this.mParser, this.mLogger);
          if (shouldDie())
            break;
          String str1 = this.mParser.selectedNetwork.registrationUrl;
          arrayOfString = str1.split("\\?");
          if (shouldDie())
            break;
          if (arrayOfString.length == 1)
          {
            localWebInterface.getWebPageMultiAuth(str1, "", this.mParser.savedConfigInfo.username, this.mParser.savedConfigInfo.password, true, this.mParser.selectedNetwork.registrationCookie, true);
            if (shouldDie())
              break;
            if ((this.mParser.selectedNetwork.registrationBehavior != 1) && (localWebInterface.resultCode != 401))
              break label474;
            Util.log(this.mLogger, "URL result code : " + localWebInterface.resultCode);
            if (localWebInterface.resultCode == 200)
              break label474;
            int i = localWebInterface.resultCode;
            String str2 = null;
            if (i == 401)
              str2 = this.mParser.getOptionById(416);
            if (str2 == null)
              str2 = localWebInterface.statusText + " (" + localWebInterface.resultCode + ")";
            if (shouldDie())
              break;
            if (this.mThreadCom != null)
              this.mThreadCom.doFailure(Boolean.valueOf(true), 18, str2);
            unlock();
            return;
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          Util.log(this.mLogger, "Failed to init MapVariables.");
          if (this.mThreadCom != null)
            this.mThreadCom.doFailure(Boolean.valueOf(false), 17, null);
          unlock();
          return;
        }
        localWebInterface.getWebPageMultiAuth(arrayOfString[0], localMapVariables.varMap(arrayOfString[1], false, this.mWifi), this.mParser.savedConfigInfo.username, this.mParser.savedConfigInfo.password, true, this.mParser.selectedNetwork.registrationCookie, true);
      }
    }
    while (shouldDie());
    if (this.mThreadCom != null)
      this.mThreadCom.doSuccess();
    unlock();
  }

  public void setThreadCom(ThreadCom paramThreadCom)
  {
    this.mThreadCom = paramThreadCom;
  }
}