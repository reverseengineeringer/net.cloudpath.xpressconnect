package net.cloudpath.xpressconnect.parsers.config;

import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetworkItemsElement
{
  private NetworkItemElement activeNetwork = null;
  private boolean inNetworkItem = false;
  private Logger mLogger = null;
  public ArrayList<NetworkItemElement> networks = null;

  public NetworkItemsElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.networks = new ArrayList();
  }

  public void endNetworkItemsElement(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if ((paramString2 != "network_items") && (this.inNetworkItem == true))
    {
      this.activeNetwork.endNetworkItemElement(paramString1, paramString2, paramString3, paramString4);
      if (paramString2 == "network_item")
      {
        this.networks.add(this.activeNetwork);
        this.activeNetwork = null;
        this.inNetworkItem = false;
      }
    }
  }

  public boolean getInMessage()
  {
    if (this.activeNetwork == null)
      return false;
    return this.activeNetwork.getInMessage();
  }

  public void startNetworkItemsElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "network_items")
    {
      if ((paramString2 == "network_item") || (this.inNetworkItem == true))
      {
        this.inNetworkItem = true;
        if (this.activeNetwork == null)
          this.activeNetwork = new NetworkItemElement(this.mLogger);
        this.activeNetwork.startNetworkItemElement(paramString1, paramString2, paramString3, paramAttributes);
      }
    }
    else
      return;
    Util.log(this.mLogger, "Unknown tag <" + paramString2 + "> found in <network_items>!");
  }
}