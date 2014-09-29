package net.cloudpath.xpressconnect.thread;

import android.net.wifi.WifiManager;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.GenerateIds;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.ConditionsElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemsElement;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.ProfilesElement;

public class PickNetworkThread extends ThreadBase
{
  public static final int ERR_NETWORK_NOT_FOUND = 1;
  public static final int ERR_PROFILE_NOT_FOUND = 2;
  public static final int NETWORK_FOUND;
  private ArrayList<NetworkItemElement> foundList = null;
  private Logger mLogger = null;
  private NetworkConfigParser mParser = null;
  private ThreadCom mThreadCom = null;
  private WifiManager mWifi = null;

  public PickNetworkThread(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, ThreadCom paramThreadCom, WifiManager paramWifiManager)
  {
    super(paramLogger);
    setName("PickNetwork thread.");
    this.mLogger = paramLogger;
    this.mParser = paramNetworkConfigParser;
    this.mWifi = paramWifiManager;
    this.mThreadCom = paramThreadCom;
    this.foundList = new ArrayList();
  }

  private boolean hasSingleWirelessNetwork()
  {
    int i = 0;
    if (findNetworks() != true)
      return false;
    Util.log(this.mLogger, "Done searching...");
    if (this.foundList == null)
      return false;
    for (int j = 0; j < this.foundList.size(); j++)
      if (!((NetworkItemElement)this.foundList.get(j)).isHidden.booleanValue())
        i++;
    if (i == 1)
    {
      Util.log(this.mLogger, "Found a single matching network.");
      this.mParser.selectedNetworkIdx = 0;
      this.mParser.selectedNetwork = ((NetworkItemElement)this.foundList.get(0));
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "There is only a single network, but offset 0 is null?");
        return false;
      }
      this.mParser.selectedProfile = this.mParser.selectedNetwork.getProfile();
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "No profile could be found for this network.");
        return false;
      }
      return true;
    }
    Util.log(this.mLogger, "More than one network found.. Asking user..");
    return false;
  }

  public boolean findNetworks()
  {
    return findNetworks(true);
  }

  public boolean findNetworks(boolean paramBoolean)
  {
    int i = 0;
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser is null in findNetworks()!");
      return false;
    }
    if (this.mParser.networks == null)
    {
      Util.log(this.mLogger, "No networks availble in findNetworks()!");
      return false;
    }
    Util.log(this.mLogger, "Searching network nodes. (Size : " + this.mParser.networks.networkItemsElements.size() + ")");
    while (i < this.mParser.networks.networkItemsElements.size())
    {
      if ((paramBoolean) && (shouldDie()))
        return false;
      NetworkItemsElement localNetworkItemsElement = (NetworkItemsElement)this.mParser.networks.networkItemsElements.get(i);
      Util.log(this.mLogger, "Looking in network items elements list.");
      int j = 0;
      while (j < localNetworkItemsElement.networks.size())
      {
        if ((paramBoolean) && (shouldDie()))
          return false;
        NetworkItemElement localNetworkItemElement = (NetworkItemElement)localNetworkItemsElement.networks.get(j);
        Util.log(this.mLogger, "Looking at : " + localNetworkItemElement.name);
        if (localNetworkItemElement.profiles == null)
        {
          Util.log(this.mLogger, "No useful profile information.  Skipping.");
        }
        else
        {
          ArrayList localArrayList = localNetworkItemElement.profiles.profiles;
          int k = 0;
          if (localArrayList == null)
          {
            Util.log(this.mLogger, "No profiles in the list.  Skipping.");
            continue;
            Util.log(this.mLogger, "Checking profile : " + ((ProfileElement)localNetworkItemElement.profiles.profiles.get(k)).name);
            if (((ProfileElement)localNetworkItemElement.profiles.profiles.get(k)).conditions.conditionsAreTrue())
            {
              Util.log(this.mLogger, "Found network match : " + localNetworkItemElement.name);
              if (this.foundList != null)
                break label410;
              Util.log(this.mLogger, "foundList is null in PickNetworkThread()!");
            }
          }
          else
          {
            while (true)
            {
              k++;
              if (k >= localNetworkItemElement.profiles.profiles.size())
                break label435;
              if ((!paramBoolean) || (!shouldDie()))
                break;
              return false;
              label410: if (!this.foundList.contains(localNetworkItemElement))
                this.foundList.add(localNetworkItemElement);
            }
            label435: j++;
          }
        }
      }
      i++;
    }
    return true;
  }

  public ArrayList<NetworkItemElement> getFoundList()
  {
    return this.foundList;
  }

  public void resume(ThreadCom paramThreadCom)
  {
    this.mThreadCom = paramThreadCom;
    unlock();
  }

  public void run()
  {
    super.run();
    GenerateIds localGenerateIds = new GenerateIds();
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser not bound in PickNetworkThread!");
      spinLock();
      if (this.mThreadCom != null)
        this.mThreadCom.doFailure(Boolean.valueOf(false), 21, "Parser not bound in PickNetworkThread!");
      unlock();
    }
    label358: 
    while (true)
    {
      return;
      if (this.mParser.savedConfigInfo == null)
      {
        Util.log(this.mLogger, "No saved config info in PickNetworkThread!");
        spinLock();
        if (this.mThreadCom != null)
          this.mThreadCom.doFailure(Boolean.valueOf(false), 21, "Saved config info not bound in PickNetworkThread!");
        unlock();
        return;
      }
      if (this.mParser.savedConfigInfo.clientId == null)
      {
        spinLock();
        this.mParser.savedConfigInfo.clientId = localGenerateIds.getClientId(this.mWifi);
        Util.log(this.mLogger, "Client ID : " + this.mParser.savedConfigInfo.clientId);
        unlock();
      }
      if (this.mParser.savedConfigInfo.sessionId == null)
      {
        spinLock();
        this.mParser.savedConfigInfo.sessionId = localGenerateIds.getSessionId();
        Util.log(this.mLogger, "Session ID : " + this.mParser.savedConfigInfo.sessionId);
        unlock();
      }
      if (!shouldDie())
        if (hasSingleWirelessNetwork())
        {
          if (!shouldDie())
          {
            spinLock();
            if (this.mThreadCom != null)
              this.mThreadCom.doSuccess();
            unlock();
          }
        }
        else
          while (true)
          {
            if (shouldDie())
              break label358;
            spinLock();
            Util.log(this.mLogger, "** Thread done.");
            unlock();
            return;
            if (shouldDie())
              break;
            spinLock();
            if (this.mThreadCom != null)
              this.mThreadCom.doFailure(Boolean.valueOf(false), -10, null);
            unlock();
          }
    }
  }

  public int selectNetworkByName(String paramString)
  {
    for (int i = 0; i < this.foundList.size(); i++)
    {
      if (shouldDie())
        return 1;
      if ((this.foundList.get(i) != null) && (((NetworkItemElement)this.foundList.get(i)).name != null) && (((NetworkItemElement)this.foundList.get(i)).name.equals(paramString)))
      {
        this.mParser.selectedNetworkIdx = i;
        this.mParser.selectedNetwork = ((NetworkItemElement)this.foundList.get(i));
        if (this.mParser.selectedNetwork == null)
          return 1;
        this.mParser.selectedProfile = this.mParser.selectedNetwork.getProfile();
        if (this.mParser.selectedProfile == null)
          return 2;
        return 0;
      }
    }
    return 1;
  }
}