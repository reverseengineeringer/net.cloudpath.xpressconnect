package net.cloudpath.xpressconnect.remoteservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakefulIntentService extends IntentService
{
  public static final String LOCK_NAME_LOCAL = "net.cloudpath.xpressconnect.remoteservice.Local";
  public static final String LOCK_NAME_STATIC = "net.cloudpath.xpressconnect.remoteservice.Static";
  private static PowerManager.WakeLock lockStatic = null;
  private PowerManager.WakeLock lockLocal = null;

  public WakefulIntentService(String paramString)
  {
    super(paramString);
  }

  public static void acquireStaticLock(Context paramContext)
  {
    getLock(paramContext).acquire();
  }

  private static PowerManager.WakeLock getLock(Context paramContext)
  {
    PowerManager.WakeLock localWakeLock;
    if (paramContext == null)
      localWakeLock = null;
    while (true)
    {
      return localWakeLock;
      try
      {
        if (lockStatic == null)
        {
          lockStatic = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "net.cloudpath.xpressconnect.remoteservice.Static");
          lockStatic.setReferenceCounted(true);
        }
        localWakeLock = lockStatic;
      }
      finally
      {
      }
    }
  }

  public void onCreate()
  {
    super.onCreate();
    this.lockLocal = ((PowerManager)getSystemService("power")).newWakeLock(1, "net.cloudpath.xpressconnect.remoteservice.Local");
    this.lockLocal.setReferenceCounted(true);
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    this.lockLocal.release();
  }

  public void onStart(Intent paramIntent, int paramInt)
  {
    this.lockLocal.acquire();
    super.onStart(paramIntent, paramInt);
    getLock(this).release();
  }
}