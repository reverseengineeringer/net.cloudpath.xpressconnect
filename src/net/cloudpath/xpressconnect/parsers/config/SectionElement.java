package net.cloudpath.xpressconnect.parsers.config;

import java.util.ArrayList;
import java.util.List;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SectionElement
{
  private SettingElement activeSetting = null;
  public String id;
  private boolean inSetting = false;
  private Logger mLogger = null;
  public ArrayList<SettingElement> settings;

  public SectionElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.settings = new ArrayList();
  }

  public void endSectionElement(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString2 != "section")
    {
      if ((paramString2 != "setting") && (this.inSetting != true))
        break label61;
      this.activeSetting.endSettingElement(paramString1, paramString2, paramString3, paramString4);
      if (paramString2 == "setting")
      {
        this.inSetting = false;
        this.settings.add(this.activeSetting);
        this.activeSetting = null;
      }
    }
    return;
    label61: Util.log(this.mLogger, "Got an unknown tag <" + paramString2 + "> in the <section> block!");
  }

  public SettingElement getSetting(int paramInt)
  {
    int i = 0;
    if (i < this.settings.size())
    {
      if (this.settings.get(i) == null)
        Util.log(this.mLogger, "Settings item is null!?");
      label68: 
      do
        while (true)
        {
          i++;
          break;
          if (((SettingElement)this.settings.get(i)).id != null)
            break label68;
          Util.log(this.mLogger, "Settings id is null!?");
        }
      while (Util.parseInt(((SettingElement)this.settings.get(i)).id, -1) != paramInt);
      return (SettingElement)this.settings.get(i);
    }
    return null;
  }

  public List<SettingElement> getSettingMulti(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.clear();
    int i = 0;
    if (i < this.settings.size())
    {
      if (this.settings.get(i) == null)
        Util.log(this.mLogger, "Settings item is null!?");
      while (true)
      {
        i++;
        break;
        if (((SettingElement)this.settings.get(i)).id == null)
          Util.log(this.mLogger, "Settings id is null!?");
        else if (Util.parseInt(((SettingElement)this.settings.get(i)).id, -1) == paramInt)
          localArrayList.add(this.settings.get(i));
      }
    }
    if (localArrayList.size() == 0)
      localArrayList = null;
    return localArrayList;
  }

  public void parseSectionAttributes(Attributes paramAttributes)
    throws SAXException
  {
    if (paramAttributes == null)
      return;
    if (paramAttributes.getIndex("id") < 0)
      throw new SAXException("Got a <section> tag that didn't contain an id!");
    this.id = paramAttributes.getValue("id");
  }

  public void startSectionElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "section")
    {
      if ((paramString2 == "setting") || (this.inSetting == true))
      {
        this.inSetting = true;
        if (this.activeSetting == null)
          this.activeSetting = new SettingElement(this.mLogger);
        this.activeSetting.startSettingElement(paramString1, paramString2, paramString3, paramAttributes);
        return;
      }
      Util.log(this.mLogger, "Unable to parse the attributes from the <section> name!");
      return;
    }
    parseSectionAttributes(paramAttributes);
  }
}