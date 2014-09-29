package net.cloudpath.xpressconnect.parsers.config;

import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ProfilesElement
{
  private ProfileElement activeProfile = null;
  boolean inOptions = false;
  boolean inProfile = false;
  Logger mLogger = null;
  public OptionsElement options = null;
  public ArrayList<ProfileElement> profiles = null;

  public ProfilesElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.profiles = new ArrayList();
  }

  public void endProfilesElement(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if (paramString2 != "profiles")
    {
      if ((paramString2 != "profile") && (this.inProfile != true))
        break label85;
      if (this.activeProfile != null)
        break label37;
      Util.log(this.mLogger, "activeProfile is null in endProfilesElement!");
    }
    label37: label85: 
    do
    {
      do
      {
        do
        {
          return;
          this.activeProfile.endProfileElement(paramString1, paramString2, paramString3, paramString4);
        }
        while (paramString2 != "profile");
        if (this.profiles != null)
          this.profiles.add(this.activeProfile);
        this.activeProfile = null;
        this.inProfile = false;
        return;
      }
      while ((paramString2 != "options") && (this.inOptions != true));
      if (this.options == null)
      {
        Util.log(this.mLogger, "options is null in endProfilesElement!");
        return;
      }
      this.options.endOptionsElement(paramString1, paramString2, paramString3, paramString4);
    }
    while (paramString2 != "options");
    this.inOptions = false;
  }

  public ProfileElement getThisOsProfile()
  {
    for (int i = 0; i < this.profiles.size(); i++)
      if (((ProfileElement)this.profiles.get(i)).conditions.conditionsAreTrue())
        return (ProfileElement)this.profiles.get(i);
    return null;
  }

  public void startProfilesElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "profiles")
    {
      if ((paramString2 == "profile") || (this.inProfile == true))
      {
        this.inProfile = true;
        if (this.activeProfile == null)
          this.activeProfile = new ProfileElement(this.mLogger);
        this.activeProfile.startProfileElement(paramString1, paramString2, paramString3, paramAttributes);
      }
    }
    else
      return;
    if ((paramString2 == "options") || (this.inOptions == true))
    {
      this.inOptions = true;
      if (this.options == null)
        this.options = new OptionsElement(this.mLogger);
      this.options.startOptionsElement(paramString1, paramString2, paramString3, paramAttributes);
      return;
    }
    Util.log(this.mLogger, "Unknown tag in <profiles> block.  (Tag was : " + paramString2 + ")");
  }
}