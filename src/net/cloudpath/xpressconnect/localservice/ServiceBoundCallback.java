package net.cloudpath.xpressconnect.localservice;

public abstract interface ServiceBoundCallback
{
  public abstract void onServiceBind(EncapsulationService paramEncapsulationService);

  public abstract void onServiceUnbind();
}