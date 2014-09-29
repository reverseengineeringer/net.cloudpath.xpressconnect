package net.cloudpath.xpressconnect;

import net.cloudpath.xpressconnect.logger.Logger;

public class CredentialRegexHandler
{
  private Logger mLogger = null;

  private String doRegexReplace(String paramString1, String paramString2)
  {
    String str1 = paramString1;
    Util.log(this.mLogger, "Regex is : " + paramString1);
    if (str1.startsWith("s/"))
      str1 = str1.substring(2);
    if (str1.startsWith("/"))
      str1 = str1.substring(1);
    boolean bool;
    if (str1.endsWith("/i"))
    {
      String str10 = str1.substring(0, -2 + str1.length());
      str1 = "(?i)" + str10;
      bool = false;
    }
    while (true)
    {
      if (str1.endsWith("/"))
        str1 = str1.substring(0, -1 + str1.length());
      Util.log(this.mLogger, "Stripped regex : " + str1 + "  (isGlobal=" + bool + ")");
      String[] arrayOfString = str1.split("/");
      String str3;
      String str5;
      if (arrayOfString.length < 2)
      {
        Util.log(this.mLogger, "Splitting the regex in to components failed.  Regex was : " + paramString1 + ".  Using a blank.");
        str3 = arrayOfString[0];
        str5 = "";
        label223: if ((str5.endsWith("\\") == true) && (!str5.endsWith("\\\\")))
          str5 = str5 + "\\";
        if (!bool)
          break label605;
      }
      try
      {
        String str7 = paramString2.replaceAll(str3, str5);
        return str7;
        if (str1.endsWith("/gi"))
        {
          String str9 = str1.substring(0, -3 + str1.length());
          str1 = "(?i)" + str9;
          bool = true;
        }
        else if (str1.endsWith("/ig"))
        {
          String str8 = str1.substring(0, -3 + str1.length());
          str1 = "(?i)" + str8;
          bool = true;
        }
        else if (str1.endsWith("/g"))
        {
          str1 = str1.substring(0, -2 + str1.length());
          bool = true;
        }
        else
        {
          bool = false;
          continue;
          if (arrayOfString.length == 2)
          {
            str3 = arrayOfString[0];
            str5 = arrayOfString[1];
            break label223;
          }
          int i = 0;
          String str2 = arrayOfString[0];
          while ((arrayOfString[i].endsWith("\\")) && (i < arrayOfString.length))
          {
            str2 = str2 + "/" + arrayOfString[(i + 1)];
            i++;
          }
          str3 = str2.replace("\\", "");
          int j = i + 1;
          String str4 = arrayOfString[j];
          while ((arrayOfString[j].endsWith("\\")) && (j < arrayOfString.length))
          {
            str4 = str4 + "/" + arrayOfString[(j + 1)];
            j++;
          }
          str5 = str4.replace("\\", "");
          break label223;
          label605: String str6 = paramString2.replaceFirst(str3, str5);
          return str6;
        }
      }
      catch (Exception localException)
      {
      }
    }
    return paramString2;
  }

  private boolean suffixMatches(String paramString1, String paramString2)
  {
    String[] arrayOfString = paramString2.split(";");
    String str = paramString1.toUpperCase();
    for (int i = 0; ; i++)
    {
      int j = arrayOfString.length;
      boolean bool = false;
      if (i < j)
      {
        Util.log(this.mLogger, "Checking suffix '" + arrayOfString[i].toUpperCase() + "' against username.");
        if (str.endsWith(arrayOfString[i].toUpperCase()))
        {
          bool = true;
          Util.log(this.mLogger, "Found match.");
        }
      }
      else
      {
        return bool;
      }
    }
  }

  public String getCheckedUserName(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    String[] arrayOfString = null;
    if (paramString3 != null)
      arrayOfString = paramString3.split(";");
    if ((paramString1 == null) || (paramString1.length() == 0))
      return paramString2;
    if (!paramString1.equals(paramString2))
    {
      if (paramInt != 1)
        break label112;
      paramString1 = paramString1.toUpperCase();
    }
    while (true)
    {
      if ((paramString4 != null) && (paramString4.length() > 0))
        paramString1 = doRegexReplace(paramString4, paramString1);
      if ((arrayOfString != null) && (arrayOfString.length > 0) && (!suffixMatches(paramString1, paramString3)))
        paramString1 = paramString1 + arrayOfString[0];
      return paramString1;
      label112: if (paramInt == 2)
        paramString1 = paramString1.toLowerCase();
    }
  }

  public boolean regexIsValid(String paramString1, String paramString2)
  {
    if ((paramString2 == null) || (paramString2.length() <= 0));
    while (paramString1.matches(paramString2))
      return true;
    return false;
  }

  public void setLogger(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }
}