package net.cloudpath.xpressconnect.checks;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.io.PrintStream;
import java.util.List;

public class AppDetection
{
  public boolean isAppInstalledByClass(PackageManager paramPackageManager, String paramString)
  {
    if ((paramString == null) || (paramPackageManager == null))
      return true;
    Intent localIntent = new Intent("android.intent.action.MAIN", null);
    localIntent.addCategory("android.intent.category.LAUNCHER");
    List localList = paramPackageManager.queryIntentActivities(localIntent, 0);
    for (int i = 0; ; i++)
    {
      if (i >= localList.size())
        break label118;
      ResolveInfo localResolveInfo = (ResolveInfo)localList.get(i);
      System.out.println("Apps : " + localResolveInfo.activityInfo.packageName);
      if (paramString.contentEquals(localResolveInfo.activityInfo.packageName))
        break;
    }
    label118: return false;
  }

  public boolean isAppInstalledByName(PackageManager paramPackageManager, String paramString)
  {
    if ((paramString == null) || (paramPackageManager == null))
      return true;
    Intent localIntent = new Intent("android.intent.action.MAIN", null);
    localIntent.addCategory("android.intent.category.LAUNCHER");
    List localList = paramPackageManager.queryIntentActivities(localIntent, 0);
    for (int i = 0; ; i++)
    {
      if (i >= localList.size())
        break label136;
      ResolveInfo localResolveInfo = (ResolveInfo)localList.get(i);
      System.out.println("Apps : " + localResolveInfo.activityInfo.applicationInfo.loadLabel(paramPackageManager).toString());
      if (paramString.contentEquals(localResolveInfo.activityInfo.applicationInfo.loadLabel(paramPackageManager).toString()))
        break;
    }
    label136: return false;
  }
}