package net.cloudpath.xpressconnect.parsers.config;

import java.util.List;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ProfileElement
{
  public ConditionsElement conditions = null;
  private boolean inConditions = false;
  private boolean inSections = false;
  private Logger mLogger = null;
  public String name = null;
  public OptionsElement options = null;
  public Boolean postRunas = Boolean.valueOf(false);
  public String postTransitionScript = null;
  public Boolean preRunas = Boolean.valueOf(false);
  public String preTransitionScript = null;
  public SectionsElement sections = null;
  public int showPreCreds = 0;

  public ProfileElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  private void parseProfileAttributes(Attributes paramAttributes)
  {
    if (paramAttributes == null);
    String str3;
    do
    {
      return;
      String str1 = paramAttributes.getValue("pre_runas");
      if (str1 != null)
        this.preRunas = Boolean.valueOf(Util.getBoolFromString(str1));
      String str2 = paramAttributes.getValue("post_runas");
      if (str2 != null)
        this.postRunas = Boolean.valueOf(Util.getBoolFromString(str2));
      str3 = paramAttributes.getValue("show_pre_creds");
    }
    while (str3 == null);
    this.showPreCreds = Util.parseInt(str3, 0);
  }

  private void parseProfileBlock(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return;
    if (paramString1 == "name")
    {
      this.name = paramString2;
      return;
    }
    if (paramString1 == "pre_transition_script")
    {
      this.preTransitionScript = paramString2;
      return;
    }
    if (paramString1 == "post_transition_script")
    {
      this.postTransitionScript = paramString2;
      return;
    }
    Util.log(this.mLogger, "Unknown tag <" + paramString1 + "> found in <profile> block!");
  }

  public void endProfileElement(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if (paramString2 != "profile")
    {
      if ((paramString2 != "conditions") && (this.inConditions != true))
        break label61;
      if (this.conditions != null)
        break label37;
      Util.log(this.mLogger, "conditions is null in endProfileElement()!");
    }
    label37: label61: 
    do
    {
      do
      {
        return;
        this.conditions.endConditionsElement(paramString1, paramString2, paramString3, paramString4);
      }
      while (paramString2 != "conditions");
      this.inConditions = false;
      return;
      if ((paramString2 != "sections") && (this.inSections != true))
        break;
      if (this.sections == null)
      {
        Util.log(this.mLogger, "sections is null in endProfileElement()!");
        return;
      }
      this.sections.endSectionsElement(paramString1, paramString2, paramString3, paramString4);
    }
    while (paramString2 != "sections");
    this.inSections = false;
    return;
    parseProfileBlock(paramString2, paramString4);
  }

  public SettingElement getSetting(int paramInt1, int paramInt2)
  {
    if (this.sections == null)
    {
      Util.log(this.mLogger, "sections is null in getSetting()!");
      return null;
    }
    SectionElement localSectionElement = this.sections.getSection(paramInt1);
    if (localSectionElement == null)
    {
      Util.log(this.mLogger, "Unable to find section by type!");
      return null;
    }
    return localSectionElement.getSetting(paramInt2);
  }

  public List<SettingElement> getSettingMulti(int paramInt1, int paramInt2)
  {
    if (this.sections == null)
    {
      Util.log(this.mLogger, "sections is null in getSettingMulti()!");
      return null;
    }
    SectionElement localSectionElement = this.sections.getSection(paramInt1);
    if (localSectionElement == null)
    {
      Util.log(this.mLogger, "Unable to find section by type!");
      return null;
    }
    return localSectionElement.getSettingMulti(paramInt2);
  }

  public void startProfileElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "profile")
    {
      if ((paramString2 == "conditions") || (this.inConditions == true))
      {
        this.inConditions = true;
        if (this.conditions == null)
          this.conditions = new ConditionsElement(this.mLogger);
        this.conditions.startConditionsElement(paramString1, paramString2, paramString3, paramAttributes);
      }
      while ((paramString2 != "sections") && (this.inSections != true))
        return;
      this.inSections = true;
      if (this.sections == null)
        this.sections = new SectionsElement(this.mLogger);
      this.sections.startSectionsElement(paramString1, paramString2, paramString3, paramAttributes);
      return;
    }
    parseProfileAttributes(paramAttributes);
  }
}