package net.cloudpath.xpressconnect.checks;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import java.io.File;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class LocalFileHandler
{
  private Context mAct;
  private Logger mLogger;

  public LocalFileHandler(Context paramContext, Logger paramLogger)
  {
    this.mAct = paramContext;
    this.mLogger = paramLogger;
  }

  private boolean isAttributeSet(String paramString, Character paramCharacter, int paramInt)
  {
    String str = getFileAttributes(paramString);
    if ((str == null) || (str.contentEquals("")))
      this.mLogger.addLine("Unable to determine file rights for : " + paramString);
    while (str.charAt(paramInt) != paramCharacter.charValue())
      return false;
    return true;
  }

  public static void setWorldReadable(String paramString)
  {
    Util.cmdExec("chmod o+r " + paramString);
  }

  protected String getFileAttributes(String paramString)
  {
    if (paramString == null)
      return "filepath was null";
    String str = Util.cmdExec("ls -l " + paramString);
    Util.log(this.mLogger, "ls result : " + str);
    return str.split(" ")[0];
  }

  public String getPathToAppDirectory()
  {
    String str;
    try
    {
      str = this.mAct.getPackageManager().getPackageInfo(this.mAct.getPackageName(), 0).packageName;
      if (str == null)
        return null;
    }
    catch (Exception localException)
    {
      while (true)
      {
        Util.log(this.mLogger, "Exception getting local app name.   Exception : " + localException.getMessage());
        str = null;
      }
    }
    return Environment.getDataDirectory().toString() + "/data/" + str;
  }

  public boolean isGroupExecutable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('x'), 6);
  }

  public boolean isGroupReadable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('r'), 4);
  }

  public boolean isGroupWritable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('w'), 5);
  }

  public boolean isUserExecutable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('x'), 3);
  }

  public boolean isUserReadable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('r'), 1);
  }

  public boolean isUserWritable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('w'), 2);
  }

  public boolean isWorldExecutable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('x'), 9);
  }

  public boolean isWorldReadable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('r'), 7);
  }

  public boolean isWorldWritable(String paramString)
  {
    return isAttributeSet(paramString, Character.valueOf('w'), 8);
  }
}