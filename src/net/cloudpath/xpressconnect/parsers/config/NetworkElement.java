package net.cloudpath.xpressconnect.parsers.config;

import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetworkElement
{
  public String checksum = null;
  public String contact_email = null;
  public String contact_im = null;
  public String contact_message = null;
  public String contact_name = null;
  public String contact_phone = null;
  public String contact_url = null;
  public String contractState = null;
  public String description = null;
  public String expiration = null;
  public int gui_flags = 0;
  boolean inContact = false;
  boolean inContent = false;
  boolean inNetworkItems = false;
  boolean inOptions = false;
  public String license = null;
  public String licensee = null;
  private Logger mLogger = null;
  public String name = null;
  private NetworkItemsElement networkElement = null;
  public ArrayList<NetworkItemsElement> networkItemsElements = null;
  public OptionsElement options = null;
  public String refresh_url = null;
  public String server_address = null;
  public String subnets = null;
  public String unsupported_os_message = null;
  public String unsupported_os_url = null;
  public String version = null;
  public String welcome_title = null;

  public NetworkElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.networkItemsElements = new ArrayList();
    this.options = new OptionsElement(paramLogger);
  }

  public void endContactElement(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if (this.inContact == true)
    {
      if (paramString2 == "contact")
        this.inContact = false;
    }
    else
      return;
    parseContactBlock(paramString2, paramString4);
  }

  public void endNetworkElement(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if (paramString2 != "network")
    {
      if ((paramString2 != "contact") && (this.inContact != true))
        break label41;
      if (paramString2 == "contact")
        this.inContact = false;
      endContactElement(paramString1, paramString2, paramString3, paramString4);
    }
    label41: 
    do
    {
      return;
      if ((paramString2 != "network_items") && (this.inNetworkItems != true))
        break;
      if (this.networkElement == null)
      {
        Util.log(this.mLogger, "networkElement was null in endNetworkElement()!");
        return;
      }
      this.networkElement.endNetworkItemsElement(paramString1, paramString2, paramString3, paramString4);
    }
    while (paramString2 != "network_items");
    this.networkItemsElements.add(this.networkElement);
    this.networkElement = null;
    this.inNetworkItems = false;
    return;
    if ((paramString2 == "options") || (this.inOptions == true))
    {
      if (paramString2 == "options")
        this.inOptions = false;
      if (this.options == null)
      {
        Util.log(this.mLogger, "options is null in endNetworkElement!");
        return;
      }
      this.options.endOptionsElement(paramString1, paramString2, paramString3, paramString4);
      return;
    }
    parseNetworkBlock(paramString2, paramString4);
  }

  public boolean getInMessage()
  {
    if (this.networkElement == null)
      return false;
    return this.networkElement.getInMessage();
  }

  public void parseContactBlock(String paramString1, String paramString2)
    throws SAXException
  {
    if (paramString1 == null)
      return;
    if (paramString1 == "message")
    {
      this.contact_message = paramString2;
      return;
    }
    if (paramString1 == "name")
    {
      this.contact_name = paramString2;
      return;
    }
    if (paramString1 == "email")
    {
      this.contact_email = paramString2;
      return;
    }
    if (paramString1 == "phone")
    {
      this.contact_phone = paramString2;
      return;
    }
    if (paramString1 == "url")
    {
      this.contact_url = paramString2;
      return;
    }
    if (paramString1 == "im")
    {
      this.contact_im = paramString2;
      return;
    }
    Util.log(this.mLogger, "Unknown tag <" + paramString1 + "> found in <contact> block!");
  }

  protected void parseNetworkAttributes(Attributes paramAttributes)
  {
    if (paramAttributes == null);
    String str;
    do
    {
      return;
      this.subnets = paramAttributes.getValue("subnets");
      this.expiration = paramAttributes.getValue("expiration");
      this.version = paramAttributes.getValue("version");
      this.contractState = paramAttributes.getValue("contract_state");
      str = paramAttributes.getValue("gui_flags");
    }
    while (str == null);
    this.gui_flags = Util.parseInt(str, 0);
  }

  protected void parseNetworkBlock(String paramString1, String paramString2)
    throws SAXException
  {
    if (paramString1 == null)
      return;
    if (paramString1 == "name")
    {
      this.name = paramString2;
      return;
    }
    if (paramString1 == "welcome_title")
    {
      this.welcome_title = paramString2;
      return;
    }
    if (paramString1 == "licensee")
    {
      this.licensee = paramString2;
      return;
    }
    if (paramString1 == "license")
    {
      this.license = paramString2;
      return;
    }
    if (paramString1 == "unsupported_os_message")
    {
      this.unsupported_os_message = paramString2;
      return;
    }
    if (paramString1 == "unsupported_os_url")
    {
      this.unsupported_os_url = paramString2;
      return;
    }
    if (paramString1 == "server_address")
    {
      this.server_address = paramString2;
      return;
    }
    Util.log(this.mLogger, "Unknown tag <" + paramString1 + "> found in <network> block!");
  }

  public void startContactElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (!this.inContact)
      this.inContact = true;
  }

  public void startNetworkElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "network")
    {
      if ((paramString2 == "contact") || (this.inContact == true))
        startContactElement(paramString1, paramString2, paramString3, paramAttributes);
      do
      {
        return;
        if ((paramString2 == "network_items") || (this.inNetworkItems == true))
        {
          this.inNetworkItems = true;
          if (this.networkElement == null)
            this.networkElement = new NetworkItemsElement(this.mLogger);
          this.networkElement.startNetworkItemsElement(paramString1, paramString2, paramString3, paramAttributes);
          return;
        }
      }
      while ((paramString2 != "options") && (this.inOptions != true));
      this.inOptions = true;
      if (this.options == null)
      {
        Util.log(this.mLogger, "options is not allocated in NetworkElement!");
        return;
      }
      this.options.startOptionsElement(paramString1, paramString2, paramString3, paramAttributes);
      return;
    }
    parseNetworkAttributes(paramAttributes);
  }
}