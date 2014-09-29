package org.xbill.DNS;

import java.io.IOException;
import java.util.List;

public abstract interface Resolver
{
  public abstract Message send(Message paramMessage)
    throws IOException;

  public abstract Object sendAsync(Message paramMessage, ResolverListener paramResolverListener);

  public abstract void setEDNS(int paramInt);

  public abstract void setEDNS(int paramInt1, int paramInt2, int paramInt3, List paramList);

  public abstract void setIgnoreTruncation(boolean paramBoolean);

  public abstract void setPort(int paramInt);

  public abstract void setTCP(boolean paramBoolean);

  public abstract void setTSIGKey(TSIG paramTSIG);

  public abstract void setTimeout(int paramInt);

  public abstract void setTimeout(int paramInt1, int paramInt2);
}