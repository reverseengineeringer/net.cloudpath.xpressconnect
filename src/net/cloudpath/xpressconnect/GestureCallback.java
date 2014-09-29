package net.cloudpath.xpressconnect;

public abstract interface GestureCallback
{
  public abstract void handleDumpDatabase();

  public abstract void handleNfcProgrammerGesture();
}