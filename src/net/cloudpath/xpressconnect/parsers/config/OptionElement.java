package net.cloudpath.xpressconnect.parsers.config;

import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class OptionElement
{
  public String id;
  private Logger mLogger = null;
  public String value;

  public OptionElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  public void endOptionElement(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString2 == null)
      return;
    if (paramString2 != "option");
    parseOptionBlock(paramString2, paramString4);
  }

  public void parseOptionAttributes(Attributes paramAttributes)
    throws SAXException
  {
    if (paramAttributes == null)
      return;
    if (paramAttributes.getIndex("id") < 0)
      throw new SAXException("Got an <option> tag that didn't contain an id!");
    this.id = paramAttributes.getValue("id");
  }

  public void parseOptionBlock(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return;
    if (paramString1 != "option")
      Util.log(this.mLogger, "Unknown tag <" + paramString1 + "> in <options> block!");
    this.value = paramString2;
  }

  public void startOptionElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 == null);
    while (paramString2 != "option")
      return;
    parseOptionAttributes(paramAttributes);
  }
}