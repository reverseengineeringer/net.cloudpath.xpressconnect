package org.xbill.DNS;

class ResolveThread extends Thread
{
  private Object id;
  private ResolverListener listener;
  private Message query;
  private Resolver res;

  public ResolveThread(Resolver paramResolver, Message paramMessage, Object paramObject, ResolverListener paramResolverListener)
  {
    this.res = paramResolver;
    this.query = paramMessage;
    this.id = paramObject;
    this.listener = paramResolverListener;
  }

  public void run()
  {
    try
    {
      Message localMessage = this.res.send(this.query);
      this.listener.receiveMessage(this.id, localMessage);
      return;
    }
    catch (Exception localException)
    {
      this.listener.handleException(this.id, localException);
    }
  }
}