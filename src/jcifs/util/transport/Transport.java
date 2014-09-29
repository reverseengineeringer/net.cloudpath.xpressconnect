package jcifs.util.transport;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import jcifs.util.LogStream;

public abstract class Transport
  implements Runnable
{
  static int id = 0;
  static LogStream log = LogStream.getInstance();
  String name;
  protected HashMap response_map;
  public Object setupDiscoLock;
  int state = 0;
  TransportException te;
  Thread thread;

  public Transport()
  {
    StringBuffer localStringBuffer = new StringBuffer().append("Transport");
    int i = id;
    id = i + 1;
    this.name = i;
    this.response_map = new HashMap(4);
    this.setupDiscoLock = new Object();
  }

  private void loop()
  {
    while (true)
      if (this.thread == Thread.currentThread())
        try
        {
          localRequest = peekKey();
          if (localRequest == null)
            throw new IOException("end of stream");
        }
        catch (Exception localException)
        {
          Request localRequest;
          String str = localException.getMessage();
          int i;
          if ((str != null) && (str.equals("Read timed out")))
          {
            i = 1;
            label52: if (i != 0)
              break label195;
          }
          label195: for (boolean bool = true; ; bool = false)
          {
            while (true)
            {
              if ((i == 0) && (LogStream.level >= 3))
                localException.printStackTrace(log);
              try
              {
                disconnect(bool);
              }
              catch (IOException localIOException)
              {
                localIOException.printStackTrace(log);
              }
            }
            break;
            while (true)
            {
              Response localResponse;
              synchronized (this.response_map)
              {
                localResponse = (Response)this.response_map.get(localRequest);
                if (localResponse == null)
                {
                  if (LogStream.level >= 4)
                    log.println("Invalid key, skipping message");
                  doSkip();
                }
              }
              doRecv(localResponse);
              localResponse.isReceived = true;
              this.response_map.notifyAll();
            }
            i = 0;
            break label52;
          }
        }
  }

  public static int readn(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 0;
    while (true)
    {
      int j;
      if (i < paramInt2)
      {
        j = paramInputStream.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
        if (j > 0);
      }
      else
      {
        return i;
      }
      i += j;
    }
  }

  public void connect(long paramLong)
    throws TransportException
  {
    try
    {
      switch (this.state)
      {
      case 1:
      case 2:
      default:
        TransportException localTransportException = new TransportException("Invalid state: " + this.state);
        this.state = 0;
        throw localTransportException;
      case 4:
      case 0:
      case 3:
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      this.state = 0;
      this.thread = null;
      throw new TransportException(localInterruptedException);
    }
    finally
    {
      try
      {
        if ((this.state != 0) && (this.state != 3) && (this.state != 4))
        {
          if (LogStream.level >= 1)
            log.println("Invalid state: " + this.state);
          this.state = 0;
          this.thread = null;
        }
        throw localObject1;
      }
      finally
      {
      }
      this.state = 0;
    }
    this.state = 1;
    this.te = null;
    this.thread = new Thread(this, this.name);
    this.thread.setDaemon(true);
    while (true)
    {
      synchronized (this.thread)
      {
        this.thread.start();
        this.thread.wait(paramLong);
        switch (this.state)
        {
        default:
          if ((this.state != 0) && (this.state != 3) && (this.state != 4))
          {
            if (LogStream.level >= 1)
              log.println("Invalid state: " + this.state);
            this.state = 0;
            this.thread = null;
          }
          return;
        case 1:
          this.state = 0;
          this.thread = null;
          throw new TransportException("Connection timeout");
        case 2:
        }
      }
      if (this.te != null)
      {
        this.state = 4;
        this.thread = null;
        throw this.te;
      }
      this.state = 3;
      if ((this.state != 0) && (this.state != 3) && (this.state != 4))
      {
        if (LogStream.level >= 1)
          log.println("Invalid state: " + this.state);
        this.state = 0;
        this.thread = null;
        continue;
        if ((this.state != 0) && (this.state != 3) && (this.state != 4))
        {
          if (LogStream.level >= 1)
            log.println("Invalid state: " + this.state);
          this.state = 0;
          this.thread = null;
        }
      }
    }
  }

  public void disconnect(boolean paramBoolean)
    throws IOException
  {
    synchronized (this.setupDiscoLock)
    {
    }
    try
    {
      switch (this.state)
      {
      case 1:
      default:
        if (LogStream.level >= 1)
          log.println("Invalid state: " + this.state);
        this.thread = null;
      case 0:
      case 2:
      case 3:
      case 4:
      }
      for (this.state = 0; ; this.state = 0)
      {
        do
        {
          return;
          return;
          localObject2 = finally;
          throw localObject2;
          paramBoolean = true;
        }
        while ((this.response_map.size() != 0) && (!paramBoolean));
        doDisconnect(paramBoolean);
        this.thread = null;
      }
    }
    finally
    {
    }
  }

  protected abstract void doConnect()
    throws Exception;

  protected abstract void doDisconnect(boolean paramBoolean)
    throws IOException;

  protected abstract void doRecv(Response paramResponse)
    throws IOException;

  protected abstract void doSend(Request paramRequest)
    throws IOException;

  protected abstract void doSkip()
    throws IOException;

  protected abstract void makeKey(Request paramRequest)
    throws IOException;

  protected abstract Request peekKey()
    throws IOException;

  // ERROR //
  public void run()
  {
    // Byte code:
    //   0: invokestatic 78	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   3: astore_1
    //   4: aload_0
    //   5: invokevirtual 190	jcifs/util/transport/Transport:doConnect	()V
    //   8: aload_1
    //   9: monitorenter
    //   10: aload_1
    //   11: aload_0
    //   12: getfield 72	jcifs/util/transport/Transport:thread	Ljava/lang/Thread;
    //   15: if_acmpeq +75 -> 90
    //   18: iconst_0
    //   19: ifeq +7 -> 26
    //   22: aconst_null
    //   23: invokevirtual 192	java/lang/Exception:printStackTrace	()V
    //   26: aload_1
    //   27: monitorexit
    //   28: return
    //   29: astore 4
    //   31: aload_1
    //   32: monitorenter
    //   33: aload_1
    //   34: aload_0
    //   35: getfield 72	jcifs/util/transport/Transport:thread	Ljava/lang/Thread;
    //   38: if_acmpeq +111 -> 149
    //   41: aload 4
    //   43: ifnull +8 -> 51
    //   46: aload 4
    //   48: invokevirtual 192	java/lang/Exception:printStackTrace	()V
    //   51: aload_1
    //   52: monitorexit
    //   53: return
    //   54: astore 5
    //   56: aload_1
    //   57: monitorexit
    //   58: aload 5
    //   60: athrow
    //   61: iconst_0
    //   62: ifeq +15 -> 77
    //   65: aload_0
    //   66: new 147	jcifs/util/transport/TransportException
    //   69: dup
    //   70: aconst_null
    //   71: invokespecial 155	jcifs/util/transport/TransportException:<init>	(Ljava/lang/Throwable;)V
    //   74: putfield 159	jcifs/util/transport/Transport:te	Ljcifs/util/transport/TransportException;
    //   77: aload_0
    //   78: iconst_2
    //   79: putfield 38	jcifs/util/transport/Transport:state	I
    //   82: aload_1
    //   83: invokevirtual 195	java/lang/Object:notify	()V
    //   86: aload_1
    //   87: monitorexit
    //   88: aload_2
    //   89: athrow
    //   90: iconst_0
    //   91: ifeq +15 -> 106
    //   94: aload_0
    //   95: new 147	jcifs/util/transport/TransportException
    //   98: dup
    //   99: aconst_null
    //   100: invokespecial 155	jcifs/util/transport/TransportException:<init>	(Ljava/lang/Throwable;)V
    //   103: putfield 159	jcifs/util/transport/Transport:te	Ljcifs/util/transport/TransportException;
    //   106: aload_0
    //   107: iconst_2
    //   108: putfield 38	jcifs/util/transport/Transport:state	I
    //   111: aload_1
    //   112: invokevirtual 195	java/lang/Object:notify	()V
    //   115: aload_1
    //   116: monitorexit
    //   117: aload_0
    //   118: invokespecial 197	jcifs/util/transport/Transport:loop	()V
    //   121: return
    //   122: astore_2
    //   123: aload_1
    //   124: monitorenter
    //   125: aload_1
    //   126: aload_0
    //   127: getfield 72	jcifs/util/transport/Transport:thread	Ljava/lang/Thread;
    //   130: if_acmpeq -69 -> 61
    //   133: iconst_0
    //   134: ifeq +7 -> 141
    //   137: aconst_null
    //   138: invokevirtual 192	java/lang/Exception:printStackTrace	()V
    //   141: aload_1
    //   142: monitorexit
    //   143: return
    //   144: astore_3
    //   145: aload_1
    //   146: monitorexit
    //   147: aload_3
    //   148: athrow
    //   149: aload 4
    //   151: ifnull +16 -> 167
    //   154: aload_0
    //   155: new 147	jcifs/util/transport/TransportException
    //   158: dup
    //   159: aload 4
    //   161: invokespecial 155	jcifs/util/transport/TransportException:<init>	(Ljava/lang/Throwable;)V
    //   164: putfield 159	jcifs/util/transport/Transport:te	Ljcifs/util/transport/TransportException;
    //   167: aload_0
    //   168: iconst_2
    //   169: putfield 38	jcifs/util/transport/Transport:state	I
    //   172: aload_1
    //   173: invokevirtual 195	java/lang/Object:notify	()V
    //   176: aload_1
    //   177: monitorexit
    //   178: return
    //   179: astore 6
    //   181: aload_1
    //   182: monitorexit
    //   183: aload 6
    //   185: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   4	8	29	java/lang/Exception
    //   33	41	54	finally
    //   46	51	54	finally
    //   51	53	54	finally
    //   56	58	54	finally
    //   154	167	54	finally
    //   167	178	54	finally
    //   4	8	122	finally
    //   65	77	144	finally
    //   77	88	144	finally
    //   125	133	144	finally
    //   137	141	144	finally
    //   141	143	144	finally
    //   145	147	144	finally
    //   10	18	179	finally
    //   22	26	179	finally
    //   26	28	179	finally
    //   94	106	179	finally
    //   106	117	179	finally
    //   181	183	179	finally
  }

  // ERROR //
  public void sendrecv(Request paramRequest, Response paramResponse, long paramLong)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 63	jcifs/util/transport/Transport:response_map	Ljava/util/HashMap;
    //   4: astore 5
    //   6: aload 5
    //   8: monitorenter
    //   9: aload_0
    //   10: aload_1
    //   11: invokevirtual 201	jcifs/util/transport/Transport:makeKey	(Ljcifs/util/transport/Request;)V
    //   14: aload_2
    //   15: iconst_0
    //   16: putfield 132	jcifs/util/transport/Response:isReceived	Z
    //   19: aload_0
    //   20: getfield 63	jcifs/util/transport/Transport:response_map	Ljava/util/HashMap;
    //   23: aload_1
    //   24: aload_2
    //   25: invokevirtual 205	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   28: pop
    //   29: aload_0
    //   30: aload_1
    //   31: invokevirtual 207	jcifs/util/transport/Transport:doSend	(Ljcifs/util/transport/Request;)V
    //   34: aload_2
    //   35: lload_3
    //   36: invokestatic 213	java/lang/System:currentTimeMillis	()J
    //   39: ladd
    //   40: putfield 217	jcifs/util/transport/Response:expiration	J
    //   43: aload_2
    //   44: getfield 132	jcifs/util/transport/Response:isReceived	Z
    //   47: ifne +111 -> 158
    //   50: aload_0
    //   51: getfield 63	jcifs/util/transport/Transport:response_map	Ljava/util/HashMap;
    //   54: lload_3
    //   55: invokevirtual 174	java/lang/Object:wait	(J)V
    //   58: aload_2
    //   59: getfield 217	jcifs/util/transport/Response:expiration	J
    //   62: invokestatic 213	java/lang/System:currentTimeMillis	()J
    //   65: lsub
    //   66: lstore_3
    //   67: lload_3
    //   68: lconst_0
    //   69: lcmp
    //   70: ifgt -27 -> 43
    //   73: new 147	jcifs/util/transport/TransportException
    //   76: dup
    //   77: new 40	java/lang/StringBuffer
    //   80: dup
    //   81: invokespecial 41	java/lang/StringBuffer:<init>	()V
    //   84: aload_0
    //   85: getfield 56	jcifs/util/transport/Transport:name	Ljava/lang/String;
    //   88: invokevirtual 47	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   91: ldc 219
    //   93: invokevirtual 47	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   96: aload_1
    //   97: invokevirtual 222	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
    //   100: invokevirtual 54	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   103: invokespecial 152	jcifs/util/transport/TransportException:<init>	(Ljava/lang/String;)V
    //   106: athrow
    //   107: astore 10
    //   109: getstatic 33	jcifs/util/transport/Transport:log	Ljcifs/util/LogStream;
    //   112: pop
    //   113: getstatic 101	jcifs/util/LogStream:level	I
    //   116: iconst_2
    //   117: if_icmple +11 -> 128
    //   120: aload 10
    //   122: getstatic 33	jcifs/util/transport/Transport:log	Ljcifs/util/LogStream;
    //   125: invokevirtual 110	java/io/IOException:printStackTrace	(Ljava/io/PrintStream;)V
    //   128: aload_0
    //   129: iconst_1
    //   130: invokevirtual 109	jcifs/util/transport/Transport:disconnect	(Z)V
    //   133: aload 10
    //   135: athrow
    //   136: astore 8
    //   138: aload_0
    //   139: getfield 63	jcifs/util/transport/Transport:response_map	Ljava/util/HashMap;
    //   142: aload_1
    //   143: invokevirtual 225	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   146: pop
    //   147: aload 8
    //   149: athrow
    //   150: astore 6
    //   152: aload 5
    //   154: monitorexit
    //   155: aload 6
    //   157: athrow
    //   158: aload_0
    //   159: getfield 63	jcifs/util/transport/Transport:response_map	Ljava/util/HashMap;
    //   162: aload_1
    //   163: invokevirtual 225	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   166: pop
    //   167: aload 5
    //   169: monitorexit
    //   170: return
    //   171: astore 12
    //   173: aload 12
    //   175: getstatic 33	jcifs/util/transport/Transport:log	Ljcifs/util/LogStream;
    //   178: invokevirtual 110	java/io/IOException:printStackTrace	(Ljava/io/PrintStream;)V
    //   181: goto -48 -> 133
    //   184: astore 7
    //   186: new 147	jcifs/util/transport/TransportException
    //   189: dup
    //   190: aload 7
    //   192: invokespecial 155	jcifs/util/transport/TransportException:<init>	(Ljava/lang/Throwable;)V
    //   195: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   19	43	107	java/io/IOException
    //   43	67	107	java/io/IOException
    //   73	107	107	java/io/IOException
    //   19	43	136	finally
    //   43	67	136	finally
    //   73	107	136	finally
    //   109	128	136	finally
    //   128	133	136	finally
    //   133	136	136	finally
    //   173	181	136	finally
    //   186	196	136	finally
    //   9	19	150	finally
    //   138	150	150	finally
    //   152	155	150	finally
    //   158	170	150	finally
    //   128	133	171	java/io/IOException
    //   19	43	184	java/lang/InterruptedException
    //   43	67	184	java/lang/InterruptedException
    //   73	107	184	java/lang/InterruptedException
  }

  public String toString()
  {
    return this.name;
  }
}