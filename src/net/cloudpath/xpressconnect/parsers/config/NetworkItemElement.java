package net.cloudpath.xpressconnect.parsers.config;

import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetworkItemElement
{
  public String adminInfo = null;
  public int androidReporting = 0;
  public String authAssistance = null;
  public int completionBehavior = 1;
  public String defaultDomain = null;
  public String defaultPassword = null;
  public String defaultUsername = null;
  public String description = null;
  public int dhcpWait = 0;
  public Boolean disableDomain = Boolean.valueOf(true);
  public String endpointURL = null;
  public Boolean fixAutomatically = Boolean.valueOf(true);
  public Boolean forceNICSelection = Boolean.valueOf(false);
  private boolean inMessage = false;
  private boolean inOptions = false;
  private boolean inProfiles = false;
  public Boolean isDefault = Boolean.valueOf(false);
  public Boolean isHidden = Boolean.valueOf(false);
  public String loginStyle = "1";
  private Logger mLogger = null;
  public String messageElement = null;
  public int migrationBehavior = 1;
  public String name = null;
  public OptionsElement options = null;
  public String passwordDescription = null;
  public int preCredentialCase = 0;
  public String preCredentialHelpLinkName = null;
  public String preCredentialHelpLinkURL = null;
  public String preCredentialRegex = null;
  public String preCredentialReplacementRegex = null;
  public String preCredentialSuffix = null;
  public String preCredentialText = null;
  public ProfilesElement profiles = null;
  public int registrationBehavior = 0;
  public String registrationCookie = null;
  public String registrationUrl = null;
  public int remoteUpload = 0;
  public int ssidConflict = 0;
  public String ssidConflictsStr = null;
  public int submitBehavior = 1;
  public String usernameDescription = null;
  public String validationURL = null;
  public String validationgrep = null;

  public NetworkItemElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.profiles = new ProfilesElement(paramLogger);
  }

  public void endNetworkItemElement(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if (paramString2 != "network_item")
    {
      if (paramString2 != "message")
        break label39;
      this.messageElement = paramString4;
      this.messageElement = this.messageElement.replace("</message>", "");
      this.inMessage = false;
    }
    label39: 
    do
    {
      do
      {
        return;
        if ((paramString2 != "profiles") && (this.inProfiles != true))
          break;
        this.profiles.endProfilesElement(paramString1, paramString2, paramString3, paramString4);
      }
      while (paramString2 != "profiles");
      this.inProfiles = false;
      return;
      if ((paramString2 != "options") && (this.inOptions != true))
        break;
      if (this.options == null)
      {
        Util.log(this.mLogger, "options is null in endNetworkItemElement()!");
        return;
      }
      this.options.endOptionsElement(paramString1, paramString2, paramString3, paramString4);
    }
    while (paramString2 != "options");
    this.inOptions = false;
    return;
    parseNetworkItemBlock(paramString2, paramString4);
  }

  public boolean getInMessage()
  {
    return this.inMessage;
  }

  public ProfileElement getProfile()
  {
    return this.profiles.getThisOsProfile();
  }

  public void parseNetworkItemAttributes(Attributes paramAttributes)
  {
    if (paramAttributes == null);
    String str13;
    do
    {
      return;
      String str1 = paramAttributes.getValue("is_default");
      if (str1 != null)
        this.isDefault = Boolean.valueOf(Util.getBoolFromString(str1));
      String str2 = paramAttributes.getValue("is_hidden");
      if (str2 != null)
        this.isHidden = Boolean.valueOf(Util.getBoolFromString(str2));
      String str3 = paramAttributes.getValue("enable_remote_upload");
      if (str3 != null)
        this.remoteUpload = Util.parseInt(str3, 0);
      String str4 = paramAttributes.getValue("enable_android_reporting");
      if (str4 != null)
        this.androidReporting = Util.parseInt(str4, 0);
      String str5 = paramAttributes.getValue("fix_automatically");
      if (str5 != null)
        this.fixAutomatically = Boolean.valueOf(Util.getBoolFromString(str5));
      String str6 = paramAttributes.getValue("force_nic_selection");
      if (str6 != null)
        this.forceNICSelection = Boolean.valueOf(Util.getBoolFromString(str6));
      this.loginStyle = paramAttributes.getValue("login_style");
      String str7 = paramAttributes.getValue("completion_behavior");
      if (str7 != null)
        this.completionBehavior = Util.parseInt(str7, 1);
      String str8 = paramAttributes.getValue("disable_domain");
      if (str8 != null)
        this.disableDomain = Boolean.valueOf(Util.getBoolFromString(str8));
      String str9 = paramAttributes.getValue("submit_behavior");
      if (str9 != null)
        this.submitBehavior = Util.parseInt(str9, 1);
      String str10 = paramAttributes.getValue("pre_credential_case");
      if (str10 != null)
        this.preCredentialCase = Util.parseInt(str10, 0);
      String str11 = paramAttributes.getValue("conflict");
      if (str11 != null)
        this.ssidConflict = Util.parseInt(str11, 0);
      String str12 = paramAttributes.getValue("dhcp_wait");
      if (str12 != null)
        this.dhcpWait = Util.parseInt(str12, 0);
      str13 = paramAttributes.getValue("migration_behavior");
    }
    while (str13 == null);
    this.migrationBehavior = Util.parseInt(str13, 1);
  }

  public void parseNetworkItemBlock(String paramString1, String paramString2)
  {
    if (paramString1 == null);
    do
    {
      return;
      if (paramString1 == "name")
      {
        this.name = paramString2;
        return;
      }
      if (paramString1 == "description")
      {
        this.description = paramString2;
        return;
      }
      if (paramString1 == "validation_url")
      {
        this.validationURL = paramString2;
        return;
      }
      if (paramString1 == "validation_grep")
      {
        this.validationgrep = paramString2;
        return;
      }
      if (paramString1 == "endpoint_url")
      {
        this.endpointURL = paramString2;
        return;
      }
      if (paramString1 == "username_description")
      {
        this.usernameDescription = paramString2;
        return;
      }
      if (paramString1 == "default_username")
      {
        this.defaultUsername = paramString2;
        return;
      }
      if (paramString1 == "password_description")
      {
        this.passwordDescription = paramString2;
        return;
      }
      if (paramString1 == "default_password")
      {
        this.defaultPassword = paramString2;
        return;
      }
      if (paramString1 == "pre_credential_text")
      {
        this.preCredentialText = paramString2;
        return;
      }
      if (paramString1 == "pre_credential_link_name")
      {
        this.preCredentialHelpLinkName = paramString2;
        return;
      }
      if (paramString1 == "pre_credential_link_url")
      {
        this.preCredentialHelpLinkURL = paramString2;
        return;
      }
      if (paramString1 == "pre_credential_regex")
      {
        this.preCredentialRegex = paramString2;
        return;
      }
      if (paramString1 == "pre_credential_replacement_regex")
      {
        this.preCredentialReplacementRegex = paramString2;
        return;
      }
      if (paramString1 == "pre_credential_suffix")
      {
        this.preCredentialSuffix = paramString2;
        return;
      }
      if (paramString1 == "authentication_assistance")
      {
        this.authAssistance = paramString2;
        return;
      }
      if (paramString1 == "ssid_conflict")
      {
        this.ssidConflictsStr = paramString2;
        return;
      }
      if (paramString1 == "message")
      {
        this.messageElement = paramString2;
        return;
      }
      if (paramString1 == "admin_info")
      {
        this.adminInfo = paramString2;
        return;
      }
      if (paramString1 == "registration_url")
      {
        this.registrationUrl = paramString2;
        return;
      }
      if (paramString1 == "registration_behavior")
      {
        this.registrationBehavior = Util.parseInt(paramString2, 0);
        return;
      }
      if (paramString1 == "registration_cookie")
      {
        this.registrationCookie = paramString2;
        return;
      }
    }
    while (paramString1 == "default_domain");
    Util.log(this.mLogger, "Unknown tag <" + paramString1 + "> found in <network_item> block!");
  }

  public void startNetworkItemElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 == "network_item")
      parseNetworkItemAttributes(paramAttributes);
    do
    {
      return;
      if ((paramString2 == "profiles") || (this.inProfiles == true))
      {
        this.inProfiles = true;
        this.profiles.startProfilesElement(paramString1, paramString2, paramString3, paramAttributes);
        return;
      }
      if ((paramString2 == "options") || (this.inOptions == true))
      {
        this.inOptions = true;
        if (this.options == null)
          this.options = new OptionsElement(this.mLogger);
        this.options.startOptionsElement(paramString1, paramString2, paramString3, paramAttributes);
        return;
      }
    }
    while (paramString2 != "message");
    this.inMessage = true;
  }
}