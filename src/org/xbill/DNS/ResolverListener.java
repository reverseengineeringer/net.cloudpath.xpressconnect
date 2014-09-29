package org.xbill.DNS;

import java.util.EventListener;

public abstract interface ResolverListener extends EventListener
{
  public abstract void handleException(Object paramObject, Exception paramException);

  public abstract void receiveMessage(Object paramObject, Message paramMessage);
}