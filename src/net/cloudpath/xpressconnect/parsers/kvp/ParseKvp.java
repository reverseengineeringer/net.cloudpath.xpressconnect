package net.cloudpath.xpressconnect.parsers.kvp;

import java.util.HashMap;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class ParseKvp
{
  private HashMap<String, String> kvps = new HashMap();
  private Logger mLogger = null;

  public ParseKvp(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  private void parseLine(String paramString)
  {
    if (paramString.contains("#"));
    for (String str = paramString.substring(0, paramString.indexOf("#")); !str.contains("="); str = paramString)
      return;
    String[] arrayOfString = str.trim().split("=");
    if (arrayOfString.length >= 2)
    {
      Util.log(this.mLogger, "Key : " + arrayOfString[0] + "    Value : " + arrayOfString[1]);
      this.kvps.put(arrayOfString[0], arrayOfString[1]);
      return;
    }
    Util.log(this.mLogger, "Key : " + arrayOfString[0] + "    Value : <empty>");
    this.kvps.put(arrayOfString[0], "");
  }

  public String getValueFor(String paramString)
  {
    if (this.kvps.containsKey(paramString))
    {
      Util.log(this.mLogger, "Returning value of '" + (String)this.kvps.get(paramString) + "' for key '" + paramString + "'.");
      return (String)this.kvps.get(paramString);
    }
    Util.log(this.mLogger, "Key '" + paramString + "' not found.");
    return null;
  }

  public boolean parse(String paramString)
  {
    String[] arrayOfString = paramString.split("\\n");
    for (int i = 0; i < arrayOfString.length; i++)
      parseLine(arrayOfString[i]);
    return true;
  }
}