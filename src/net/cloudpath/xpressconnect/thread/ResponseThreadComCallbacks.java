package net.cloudpath.xpressconnect.thread;

import android.content.Intent;

public abstract interface ResponseThreadComCallbacks
{
  public abstract void dialogComplete(int paramInt, boolean paramBoolean);

  public abstract void intentComplete(int paramInt1, int paramInt2, Intent paramIntent);
}