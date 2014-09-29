package net.cloudpath.xpressconnect.parsers.config;

import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class OptionsElement
{
  public OptionElement activeOption = null;
  private boolean inOption = false;
  private Logger mLogger = null;
  public ArrayList<OptionElement> options = null;

  public OptionsElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.options = new ArrayList();
  }

  public void endOptionsElement(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString2 != "options")
    {
      if (this.activeOption != null)
        break label23;
      Util.log(this.mLogger, "activeOption is null!");
    }
    label23: 
    do
    {
      return;
      this.activeOption.endOptionElement(paramString1, paramString2, paramString3, paramString4);
    }
    while (paramString2 != "option");
    this.options.add(this.activeOption);
    this.activeOption = null;
  }

  public String getOptionById(int paramInt)
  {
    for (int i = 0; i < this.options.size(); i++)
    {
      OptionElement localOptionElement = (OptionElement)this.options.get(i);
      if (Util.parseInt(localOptionElement.id, -1) == paramInt)
        return localOptionElement.value;
    }
    return null;
  }

  public void startOptionsElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "options")
    {
      if ((paramString2 == "option") || (this.inOption == true))
      {
        this.inOption = true;
        if (this.activeOption == null)
          this.activeOption = new OptionElement(this.mLogger);
        this.activeOption.startOptionElement(paramString1, paramString2, paramString3, paramAttributes);
      }
    }
    else
      return;
    Util.log(this.mLogger, "Unknown tag <" + paramString2 + "> found in <options>!");
  }
}