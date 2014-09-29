package net.cloudpath.xpressconnect.parsers.config;

import com.cloudpath.common.util.Xmlable;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetworkConfigParser extends DefaultHandler
  implements Xmlable
{
  private boolean inNetwork = false;
  public String ipAddress = null;
  public NetworkElement networks = null;
  public String originalSsid = null;
  public SavedConfigInfo savedConfigInfo = null;
  public NetworkItemElement selectedNetwork = null;
  public int selectedNetworkIdx = -1;
  public ProfileElement selectedProfile = null;
  private StringBuilder text = new StringBuilder();

  public NetworkConfigParser(Logger paramLogger)
  {
    this.networks = new NetworkElement(paramLogger);
    this.savedConfigInfo = new SavedConfigInfo();
  }

  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    this.text.append(paramArrayOfChar, paramInt1, paramInt2);
  }

  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    if (this.networks.getInMessage())
      this.text.append("</" + paramString2 + ">");
    if (paramString2 == "checksum")
      this.networks.checksum = this.text.toString();
    if ((paramString2 == "network") || (this.inNetwork == true))
    {
      if (paramString2 == "network")
        this.inNetwork = false;
      this.networks.endNetworkElement(paramString1, paramString2, paramString3, this.text.toString());
    }
  }

  public String getConfigName()
  {
    return this.networks.name;
  }

  public String getOptionById(int paramInt)
  {
    if (this.networks.options == null)
      return null;
    return this.networks.options.getOptionById(paramInt);
  }

  public String getXml()
  {
    return null;
  }

  public void parseFromString(String paramString)
    throws Exception
  {
    if ((paramString == null) || (paramString == ""))
      throw new Exception("No data available to parse.  Please verify that you have network connectivity.");
    SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
    if (localSAXParserFactory != null)
      localSAXParserFactory.newSAXParser().parse(new ByteArrayInputStream(paramString.getBytes()), this);
  }

  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (!this.networks.getInMessage())
      this.text.setLength(0);
    while (true)
    {
      if ((paramString2 == "network") || (this.inNetwork == true))
      {
        this.inNetwork = true;
        if (this.networks != null)
          this.networks.startNetworkElement(paramString1, paramString2, paramString3, paramAttributes);
      }
      return;
      this.text.append("<" + paramString2 + ">");
    }
  }
}