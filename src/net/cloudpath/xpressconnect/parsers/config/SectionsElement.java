package net.cloudpath.xpressconnect.parsers.config;

import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SectionsElement
{
  private SectionElement activeSection = null;
  private boolean inSection = false;
  private Logger mLogger = null;
  public ArrayList<SectionElement> sections = null;

  public SectionsElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.sections = new ArrayList();
  }

  public void endSectionsElement(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString2 != "sections")
    {
      if ((this.inSection != true) && (paramString2 != "section"))
        break label85;
      if (this.activeSection != null)
        break label37;
      Util.log(this.mLogger, "activeSection is null in endSectionsElement()!");
    }
    label37: 
    do
    {
      return;
      this.activeSection.endSectionElement(paramString1, paramString2, paramString3, paramString4);
    }
    while (paramString2 != "section");
    if (this.sections != null)
      this.sections.add(this.activeSection);
    this.activeSection = null;
    this.inSection = false;
    return;
    label85: Util.log(this.mLogger, "Unknown tag <" + paramString2 + "> in <sections>.");
  }

  public SectionElement getSection(int paramInt)
  {
    for (int i = 0; i < this.sections.size(); i++)
      if (Integer.parseInt(((SectionElement)this.sections.get(i)).id) == paramInt)
        return (SectionElement)this.sections.get(i);
    Util.log(this.mLogger, "Unable to find Section by id!");
    return null;
  }

  public void startSectionsElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "sections")
    {
      if ((paramString2 == "section") || (this.inSection == true))
      {
        this.inSection = true;
        if (this.activeSection == null)
          this.activeSection = new SectionElement(this.mLogger);
        this.activeSection.startSectionElement(paramString1, paramString2, paramString3, paramAttributes);
      }
    }
    else
      return;
    Util.log(this.mLogger, "Unknown tag <" + paramString2 + "> found in <sections>!");
  }
}