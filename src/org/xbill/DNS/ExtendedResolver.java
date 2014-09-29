package org.xbill.DNS;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ExtendedResolver
  implements Resolver
{
  private static final int quantum = 5;
  private int lbStart = 0;
  private boolean loadBalance = false;
  private List resolvers;
  private int retries = 3;

  public ExtendedResolver()
    throws UnknownHostException
  {
    init();
    String[] arrayOfString = ResolverConfig.getCurrentConfig().servers();
    if (arrayOfString != null)
      for (int i = 0; i < arrayOfString.length; i++)
      {
        SimpleResolver localSimpleResolver = new SimpleResolver(arrayOfString[i]);
        localSimpleResolver.setTimeout(5);
        this.resolvers.add(localSimpleResolver);
      }
    this.resolvers.add(new SimpleResolver());
  }

  public ExtendedResolver(String[] paramArrayOfString)
    throws UnknownHostException
  {
    init();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      SimpleResolver localSimpleResolver = new SimpleResolver(paramArrayOfString[i]);
      localSimpleResolver.setTimeout(5);
      this.resolvers.add(localSimpleResolver);
    }
  }

  public ExtendedResolver(Resolver[] paramArrayOfResolver)
    throws UnknownHostException
  {
    init();
    for (int i = 0; i < paramArrayOfResolver.length; i++)
      this.resolvers.add(paramArrayOfResolver[i]);
  }

  private void init()
  {
    this.resolvers = new ArrayList();
  }

  public void addResolver(Resolver paramResolver)
  {
    this.resolvers.add(paramResolver);
  }

  public void deleteResolver(Resolver paramResolver)
  {
    this.resolvers.remove(paramResolver);
  }

  public Resolver getResolver(int paramInt)
  {
    if (paramInt < this.resolvers.size())
      return (Resolver)this.resolvers.get(paramInt);
    return null;
  }

  public Resolver[] getResolvers()
  {
    return (Resolver[])this.resolvers.toArray(new Resolver[this.resolvers.size()]);
  }

  public Message send(Message paramMessage)
    throws IOException
  {
    return new Resolution(this, paramMessage).start();
  }

  public Object sendAsync(Message paramMessage, ResolverListener paramResolverListener)
  {
    Resolution localResolution = new Resolution(this, paramMessage);
    localResolution.startAsync(paramResolverListener);
    return localResolution;
  }

  public void setEDNS(int paramInt)
  {
    for (int i = 0; i < this.resolvers.size(); i++)
      ((Resolver)this.resolvers.get(i)).setEDNS(paramInt);
  }

  public void setEDNS(int paramInt1, int paramInt2, int paramInt3, List paramList)
  {
    for (int i = 0; i < this.resolvers.size(); i++)
      ((Resolver)this.resolvers.get(i)).setEDNS(paramInt1, paramInt2, paramInt3, paramList);
  }

  public void setIgnoreTruncation(boolean paramBoolean)
  {
    for (int i = 0; i < this.resolvers.size(); i++)
      ((Resolver)this.resolvers.get(i)).setIgnoreTruncation(paramBoolean);
  }

  public void setLoadBalance(boolean paramBoolean)
  {
    this.loadBalance = paramBoolean;
  }

  public void setPort(int paramInt)
  {
    for (int i = 0; i < this.resolvers.size(); i++)
      ((Resolver)this.resolvers.get(i)).setPort(paramInt);
  }

  public void setRetries(int paramInt)
  {
    this.retries = paramInt;
  }

  public void setTCP(boolean paramBoolean)
  {
    for (int i = 0; i < this.resolvers.size(); i++)
      ((Resolver)this.resolvers.get(i)).setTCP(paramBoolean);
  }

  public void setTSIGKey(TSIG paramTSIG)
  {
    for (int i = 0; i < this.resolvers.size(); i++)
      ((Resolver)this.resolvers.get(i)).setTSIGKey(paramTSIG);
  }

  public void setTimeout(int paramInt)
  {
    setTimeout(paramInt, 0);
  }

  public void setTimeout(int paramInt1, int paramInt2)
  {
    for (int i = 0; i < this.resolvers.size(); i++)
      ((Resolver)this.resolvers.get(i)).setTimeout(paramInt1, paramInt2);
  }

  private static class Resolution
    implements ResolverListener
  {
    boolean done;
    Object[] inprogress;
    ResolverListener listener;
    int outstanding;
    Message query;
    Resolver[] resolvers;
    Message response;
    int retries;
    int[] sent;
    Throwable thrown;

    public Resolution(ExtendedResolver paramExtendedResolver, Message paramMessage)
    {
      List localList = paramExtendedResolver.resolvers;
      this.resolvers = ((Resolver[])localList.toArray(new Resolver[localList.size()]));
      if (paramExtendedResolver.loadBalance)
      {
        int i = this.resolvers.length;
        int j = ExtendedResolver.access$208(paramExtendedResolver) % i;
        if (paramExtendedResolver.lbStart > i)
          ExtendedResolver.access$244(paramExtendedResolver, i);
        if (j > 0)
        {
          Resolver[] arrayOfResolver = new Resolver[i];
          for (int k = 0; k < i; k++)
          {
            int m = (k + j) % i;
            arrayOfResolver[k] = this.resolvers[m];
          }
          this.resolvers = arrayOfResolver;
        }
      }
      this.sent = new int[this.resolvers.length];
      this.inprogress = new Object[this.resolvers.length];
      this.retries = paramExtendedResolver.retries;
      this.query = paramMessage;
    }

    public void handleException(Object paramObject, Exception paramException)
    {
      if (Options.check("verbose"))
        System.err.println("ExtendedResolver: got " + paramException);
      while (true)
      {
        try
        {
          this.outstanding = (-1 + this.outstanding);
          if (!this.done)
            break label336;
          return;
          if ((i >= this.inprogress.length) || (this.inprogress[i] == paramObject))
          {
            if (i != this.inprogress.length)
              break label100;
            return;
          }
        }
        finally
        {
        }
        i++;
        continue;
        label100: int j = this.sent[i];
        int k = 0;
        if (j == 1)
        {
          int m = -1 + this.resolvers.length;
          k = 0;
          if (i < m)
            k = 1;
        }
        if ((paramException instanceof InterruptedIOException))
        {
          if (this.sent[i] < this.retries)
            send(i);
          if (this.thrown == null)
            this.thrown = paramException;
        }
        while (this.done)
        {
          return;
          if ((paramException instanceof SocketException))
          {
            if ((this.thrown == null) || ((this.thrown instanceof InterruptedIOException)))
              this.thrown = paramException;
          }
          else
            this.thrown = paramException;
        }
        if (k != 0)
          send(i + 1);
        if (this.done)
          return;
        if (this.outstanding == 0)
        {
          this.done = true;
          if (this.listener == null)
          {
            notifyAll();
            return;
          }
        }
        if (!this.done)
          return;
        if (!(this.thrown instanceof Exception))
          this.thrown = new RuntimeException(this.thrown.getMessage());
        this.listener.handleException(this, (Exception)this.thrown);
        return;
        label336: int i = 0;
      }
    }

    public void receiveMessage(Object paramObject, Message paramMessage)
    {
      if (Options.check("verbose"))
        System.err.println("ExtendedResolver: received message");
      try
      {
        if (this.done)
          return;
        this.response = paramMessage;
        this.done = true;
        if (this.listener == null)
        {
          notifyAll();
          return;
        }
      }
      finally
      {
      }
      this.listener.receiveMessage(this, this.response);
    }

    public void send(int paramInt)
    {
      int[] arrayOfInt = this.sent;
      arrayOfInt[paramInt] = (1 + arrayOfInt[paramInt]);
      this.outstanding = (1 + this.outstanding);
      try
      {
        this.inprogress[paramInt] = this.resolvers[paramInt].sendAsync(this.query, this);
        return;
      }
      catch (Throwable localThrowable)
      {
        try
        {
          this.thrown = localThrowable;
          this.done = true;
          if (this.listener == null)
          {
            notifyAll();
            return;
          }
        }
        finally
        {
        }
      }
    }

    public Message start()
      throws IOException
    {
      try
      {
        int[] arrayOfInt = this.sent;
        arrayOfInt[0] = (1 + arrayOfInt[0]);
        this.outstanding = (1 + this.outstanding);
        this.inprogress[0] = new Object();
        Message localMessage = this.resolvers[0].send(this.query);
        return localMessage;
      }
      catch (Exception localException)
      {
        handleException(this.inprogress[0], localException);
        try
        {
          while (true)
          {
            boolean bool = this.done;
            if (bool)
              break;
            try
            {
              wait();
            }
            catch (InterruptedException localInterruptedException)
            {
            }
          }
          if (this.response != null)
            return this.response;
        }
        finally
        {
        }
        if ((this.thrown instanceof IOException))
          throw ((IOException)this.thrown);
        if ((this.thrown instanceof RuntimeException))
          throw ((RuntimeException)this.thrown);
        if ((this.thrown instanceof Error))
          throw ((Error)this.thrown);
      }
      throw new IllegalStateException("ExtendedResolver failure");
    }

    public void startAsync(ResolverListener paramResolverListener)
    {
      this.listener = paramResolverListener;
      send(0);
    }
  }
}