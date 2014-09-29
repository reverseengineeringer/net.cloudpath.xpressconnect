package net.cloudpath.xpressconnect.remoteservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (paramContext == null)
    {
      Log.e("XPC Alarm Receiver", "Context was null in onReceive()!");
      return;
    }
    WakefulIntentService.acquireStaticLock(paramContext);
    paramContext.startService(new Intent(paramContext, SimpleReportService.class));
  }
}