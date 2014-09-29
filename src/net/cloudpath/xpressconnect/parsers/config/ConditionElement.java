package net.cloudpath.xpressconnect.parsers.config;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConditionElement
{
  public String key;
  private Logger mLogger = null;
  Map<String, Integer> mVerMap = new HashMap();
  public String oper;
  public String value;

  public ConditionElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.mVerMap = AndroidVersion.getVersionMap();
  }

  private boolean matchEquals()
  {
    return matchEquals(new AndroidVersion(this.mLogger), Build.VERSION.SDK_INT);
  }

  private boolean matchGreaterOrEqual()
  {
    return matchGreaterOrEqual(new AndroidVersion(this.mLogger), Build.VERSION.SDK_INT);
  }

  @SuppressLint({"DefaultLocale"})
  private boolean matchIntType()
  {
    if ((this.oper == null) || (this.value == null))
    {
      Util.log(this.mLogger, "No interface match because either oper or value is null.  (Or both)");
      return false;
    }
    if (!this.oper.toLowerCase().equals("eq"))
    {
      Util.log(this.mLogger, "Got a request to check an interface type that wasn't an 'eq'!  Check your config!");
      return false;
    }
    if (!this.value.toLowerCase().equals("2"))
    {
      Util.log(this.mLogger, "Got a request to check for a non-wireless interface, when that isn't supported!");
      return false;
    }
    return true;
  }

  private boolean matchOs()
  {
    if ((this.oper == null) || (this.value == null))
      Util.log(this.mLogger, "No OS match because either oper or value is null.  (Or both)");
    do
    {
      return false;
      if (!this.oper.toLowerCase().equals("eq"))
      {
        Util.log(this.mLogger, "Android doesn't understand how to match an OS name using anything except 'eq'!");
        return false;
      }
    }
    while (!this.value.toLowerCase().equals("android"));
    return true;
  }

  @SuppressLint({"DefaultLocale"})
  private boolean matchVersion()
  {
    if ((this.oper == null) || (this.value == null))
    {
      Util.log(this.mLogger, "No version match because either oper or value is null.  (Or both)");
      return false;
    }
    if (this.oper.toLowerCase().equals("eq"))
      return matchEquals();
    if (this.oper.toLowerCase().equals("ge"))
      return matchGreaterOrEqual();
    Util.log(this.mLogger, "Android doesn't support matching versions with the '" + this.oper + "' operator.");
    return false;
  }

  public void endConditionElement(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString2 == "condition")
      parseConditionBlock(paramString2, paramString4);
  }

  public boolean isKnownVersion()
  {
    return isKnownVersion(Build.VERSION.SDK_INT);
  }

  public boolean isKnownVersion(int paramInt)
  {
    Iterator localIterator = this.mVerMap.entrySet().iterator();
    while (localIterator.hasNext())
      if (paramInt == ((Integer)((Map.Entry)localIterator.next()).getValue()).intValue())
        return true;
    return false;
  }

  public boolean matchEquals(AndroidVersion paramAndroidVersion, int paramInt)
  {
    String str = paramAndroidVersion.parseAndroidVerString(this.value);
    Iterator localIterator = this.mVerMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if ((paramAndroidVersion.parseAndroidVerString((String)localEntry.getKey()).equals(str)) && (paramInt == ((Integer)localEntry.getValue()).intValue()))
      {
        Util.log(this.mLogger, "!!!! Found a version match!");
        return true;
      }
    }
    return false;
  }

  public boolean matchGreaterOrEqual(AndroidVersion paramAndroidVersion, int paramInt)
  {
    Float localFloat = Float.valueOf(Float.parseFloat(paramAndroidVersion.parseAndroidVerString(this.value)));
    int i = -1;
    Iterator localIterator = this.mVerMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (Float.valueOf(Float.parseFloat(paramAndroidVersion.parseAndroidVerString((String)localEntry.getKey()))).equals(localFloat))
        if (i == -1)
          i = ((Integer)localEntry.getValue()).intValue();
        else if (((Integer)localEntry.getValue()).intValue() < i)
          i = ((Integer)localEntry.getValue()).intValue();
    }
    return paramInt >= i;
  }

  @SuppressLint({"DefaultLocale"})
  public boolean matches()
  {
    if (this.key == null)
    {
      Util.log(this.mLogger, "Unable to check matches because 'key' is null!");
      return false;
    }
    if (this.key.toLowerCase().equals("os"))
      return matchOs();
    if (this.key.toLowerCase().equals("version"))
      return matchVersion();
    if (this.key.toLowerCase().equals("type"))
      return matchIntType();
    Util.log(this.mLogger, "Unknown condition name '" + this.key + "'.  Returning false!");
    return false;
  }

  public void parseConditionAttributes(Attributes paramAttributes)
  {
    this.key = paramAttributes.getValue("key");
    this.oper = paramAttributes.getValue("operator");
  }

  public void parseConditionBlock(String paramString1, String paramString2)
  {
    if (paramString1 != "condition")
      Util.log(this.mLogger, "Unknown tag <" + paramString1 + "> in <condition> block!");
    this.value = paramString2;
  }

  public void startConditionElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    parseConditionAttributes(paramAttributes);
  }
}