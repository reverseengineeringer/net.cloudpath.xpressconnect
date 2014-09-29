package net.cloudpath.xpressconnect.thread;

public abstract interface ThreadComCallbacks
{
  public abstract void showDialog(int paramInt);

  public abstract void threadChangeTab(int paramInt);

  public abstract void threadFailed(Boolean paramBoolean, int paramInt, String paramString);

  public abstract void threadSuccess();
}