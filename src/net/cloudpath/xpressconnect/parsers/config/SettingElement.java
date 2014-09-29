package net.cloudpath.xpressconnect.parsers.config;

import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SettingElement
{
  public String additionalValue = null;
  public Boolean enforceAdditionalValue = Boolean.valueOf(false);
  public String helpLink = null;
  public String id = null;
  public String incorrectText = null;
  public String installArguments = null;
  public String installCabName = null;
  public String installText = null;
  public Boolean isRequired = Boolean.valueOf(true);
  private Logger mLogger = null;
  public String requiredValue = null;
  public String unenforcedValue = null;
  public String uninstallRegistryTitle = null;
  public String uninstallText = null;
  public String whenRepairedText = null;

  public SettingElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  public void endSettingElement(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString2 != "setting")
      parseSettingBlock(paramString2, paramString4);
  }

  public void parseSettingAttributes(Attributes paramAttributes)
    throws SAXException
  {
    if (paramAttributes == null);
    String str2;
    do
    {
      return;
      if (paramAttributes.getIndex("id") < 0)
        throw new SAXException("Got a <setting> tag that didn't contain an id!");
      this.id = paramAttributes.getValue("id");
      String str1 = paramAttributes.getValue("enforce_additional_value");
      if (str1 != null)
        this.enforceAdditionalValue = Boolean.valueOf(Util.getBoolFromString(str1));
      str2 = paramAttributes.getValue("is_required");
    }
    while (str2 == null);
    this.isRequired = Boolean.valueOf(Util.getBoolFromString(str2));
  }

  public void parseSettingBlock(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return;
    if (paramString1 == "incorrect_text")
    {
      this.incorrectText = paramString2;
      return;
    }
    if (paramString1 == "when_repaired_text")
    {
      this.whenRepairedText = paramString2;
      return;
    }
    if (paramString1 == "required_value")
    {
      this.requiredValue = paramString2;
      return;
    }
    if (paramString1 == "additional_value")
    {
      this.additionalValue = paramString2;
      return;
    }
    if (paramString1 == "unenforced_value")
    {
      this.unenforcedValue = paramString2;
      return;
    }
    if (paramString1 == "help_link")
    {
      this.helpLink = paramString2;
      return;
    }
    if (paramString1 == "install_text")
    {
      this.installText = paramString2;
      return;
    }
    if (paramString1 == "uninstall_text")
    {
      this.uninstallText = paramString2;
      return;
    }
    if (paramString1 == "uninstall_registry_title")
    {
      this.uninstallRegistryTitle = paramString2;
      return;
    }
    if (paramString1 == "install_cab_name")
    {
      this.installCabName = paramString2;
      return;
    }
    if (paramString1 == "install_arguments")
    {
      this.installArguments = paramString2;
      return;
    }
    Util.log(this.mLogger, "Unknown tag <" + paramString1 + "> found in <setting> block!");
  }

  public void startSettingElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 == "setting")
      parseSettingAttributes(paramAttributes);
  }
}