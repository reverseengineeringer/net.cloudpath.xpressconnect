package jcifs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import jcifs.netbios.Lmhosts;
import jcifs.netbios.NbtAddress;
import jcifs.util.LogStream;

public class UniAddress
{
  private static final int RESOLVER_BCAST = 1;
  private static final int RESOLVER_DNS = 2;
  private static final int RESOLVER_LMHOSTS = 3;
  private static final int RESOLVER_WINS;
  private static InetAddress baddr;
  private static LogStream log = LogStream.getInstance();
  private static int[] resolveOrder;
  Object addr;
  String calledName;

  static
  {
    String str1 = Config.getProperty("jcifs.resolveOrder");
    InetAddress localInetAddress = NbtAddress.getWINSAddress();
    try
    {
      baddr = Config.getInetAddress("jcifs.netbios.baddr", InetAddress.getByName("255.255.255.255"));
      label29: if ((str1 == null) || (str1.length() == 0))
      {
        if (localInetAddress == null)
        {
          resolveOrder = new int[3];
          resolveOrder[0] = 3;
          resolveOrder[1] = 1;
          resolveOrder[2] = 2;
          return;
        }
        resolveOrder = new int[4];
        resolveOrder[0] = 3;
        resolveOrder[1] = 0;
        resolveOrder[2] = 1;
        resolveOrder[3] = 2;
        return;
      }
      int[] arrayOfInt = new int[4];
      StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",");
      int i = 0;
      while (localStringTokenizer.hasMoreTokens())
      {
        String str2 = localStringTokenizer.nextToken().trim();
        if (str2.equalsIgnoreCase("LMHOSTS"))
        {
          int n = i + 1;
          arrayOfInt[i] = 3;
          i = n;
        }
        else if (str2.equalsIgnoreCase("WINS"))
        {
          if (localInetAddress == null)
          {
            if (LogStream.level > 1)
              log.println("UniAddress resolveOrder specifies WINS however the jcifs.netbios.wins property has not been set");
          }
          else
          {
            int m = i + 1;
            arrayOfInt[i] = 0;
            i = m;
          }
        }
        else if (str2.equalsIgnoreCase("BCAST"))
        {
          int k = i + 1;
          arrayOfInt[i] = 1;
          i = k;
        }
        else if (str2.equalsIgnoreCase("DNS"))
        {
          int j = i + 1;
          arrayOfInt[i] = 2;
          i = j;
        }
        else if (LogStream.level > 1)
        {
          log.println("unknown resolver method: " + str2);
        }
      }
      resolveOrder = new int[i];
      System.arraycopy(arrayOfInt, 0, resolveOrder, 0, i);
      return;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      break label29;
    }
  }

  public UniAddress(Object paramObject)
  {
    if (paramObject == null)
      throw new IllegalArgumentException();
    this.addr = paramObject;
  }

  public static UniAddress[] getAllByName(String paramString, boolean paramBoolean)
    throws UnknownHostException
  {
    if ((paramString == null) || (paramString.length() == 0))
      throw new UnknownHostException();
    UniAddress[] arrayOfUniAddress1;
    if (isDotQuadIP(paramString))
    {
      arrayOfUniAddress1 = new UniAddress[1];
      arrayOfUniAddress1[0] = new UniAddress(NbtAddress.getByName(paramString));
      return arrayOfUniAddress1;
    }
    int i = 0;
    while (true)
      while (true)
      {
        if (i < resolveOrder.length);
        try
        {
          NbtAddress localNbtAddress;
          switch (resolveOrder[i])
          {
          default:
            throw new UnknownHostException(paramString);
          case 3:
            localNbtAddress = Lmhosts.getByName(paramString);
            if (localNbtAddress == null)
              break;
          case 0:
          case 1:
            while (true)
            {
              UniAddress[] arrayOfUniAddress2 = new UniAddress[1];
              arrayOfUniAddress2[0] = new UniAddress(localNbtAddress);
              return arrayOfUniAddress2;
              if ((paramString == "\001\002__MSBROWSE__\002") || (paramString.length() > 15))
                break;
              if (paramBoolean)
              {
                localNbtAddress = lookupServerOrWorkgroup(paramString, NbtAddress.getWINSAddress());
              }
              else
              {
                localNbtAddress = NbtAddress.getByName(paramString, 32, null, NbtAddress.getWINSAddress());
                continue;
                if (paramString.length() > 15)
                  break;
                if (paramBoolean)
                  localNbtAddress = lookupServerOrWorkgroup(paramString, baddr);
                else
                  localNbtAddress = NbtAddress.getByName(paramString, 32, null, baddr);
              }
            }
          case 2:
            if (isAllDigits(paramString))
              throw new UnknownHostException(paramString);
            InetAddress[] arrayOfInetAddress = InetAddress.getAllByName(paramString);
            arrayOfUniAddress1 = new UniAddress[arrayOfInetAddress.length];
            for (int j = 0; j < arrayOfInetAddress.length; j++)
              arrayOfUniAddress1[j] = new UniAddress(arrayOfInetAddress[j]);
            throw new UnknownHostException(paramString);
          }
        }
        catch (IOException localIOException)
        {
          i++;
        }
      }
  }

  public static UniAddress getByName(String paramString)
    throws UnknownHostException
  {
    return getByName(paramString, false);
  }

  public static UniAddress getByName(String paramString, boolean paramBoolean)
    throws UnknownHostException
  {
    return getAllByName(paramString, paramBoolean)[0];
  }

  static boolean isAllDigits(String paramString)
  {
    for (int i = 0; i < paramString.length(); i++)
      if (!Character.isDigit(paramString.charAt(i)))
        return false;
    return true;
  }

  static boolean isDotQuadIP(String paramString)
  {
    boolean bool1 = Character.isDigit(paramString.charAt(0));
    boolean bool2 = false;
    int i;
    int j;
    char[] arrayOfChar;
    int k;
    if (bool1)
    {
      i = 0;
      j = paramString.length();
      arrayOfChar = paramString.toCharArray();
      k = 0;
    }
    while (true)
    {
      bool2 = false;
      int m;
      if (k < j)
      {
        m = k + 1;
        boolean bool3 = Character.isDigit(arrayOfChar[k]);
        bool2 = false;
        if (bool3)
        {
          if ((m != j) || (i != 3))
            break label80;
          bool2 = true;
        }
      }
      return bool2;
      label80: if ((m < j) && (arrayOfChar[m] == '.'))
      {
        i++;
        k = m + 1;
      }
      else
      {
        k = m;
      }
    }
  }

  static NbtAddress lookupServerOrWorkgroup(String paramString, InetAddress paramInetAddress)
    throws UnknownHostException
  {
    Sem localSem = new Sem(2);
    if (NbtAddress.isWINS(paramInetAddress));
    QueryThread localQueryThread1;
    QueryThread localQueryThread2;
    for (int i = 27; ; i = 29)
    {
      localQueryThread1 = new QueryThread(localSem, paramString, i, null, paramInetAddress);
      localQueryThread2 = new QueryThread(localSem, paramString, 32, null, paramInetAddress);
      localQueryThread1.setDaemon(true);
      localQueryThread2.setDaemon(true);
      try
      {
        try
        {
          localQueryThread1.start();
          localQueryThread2.start();
          while ((localSem.count > 0) && (localQueryThread1.ans == null) && (localQueryThread2.ans == null))
            localSem.wait();
        }
        finally
        {
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        throw new UnknownHostException(paramString);
      }
    }
    if (localQueryThread1.ans != null)
      return localQueryThread1.ans;
    if (localQueryThread2.ans != null)
      return localQueryThread2.ans;
    throw localQueryThread1.uhe;
  }

  public boolean equals(Object paramObject)
  {
    return ((paramObject instanceof UniAddress)) && (this.addr.equals(((UniAddress)paramObject).addr));
  }

  public String firstCalledName()
  {
    if ((this.addr instanceof NbtAddress))
      return ((NbtAddress)this.addr).firstCalledName();
    this.calledName = ((InetAddress)this.addr).getHostName();
    if (isDotQuadIP(this.calledName))
      this.calledName = "*SMBSERVER     ";
    while (true)
    {
      return this.calledName;
      int i = this.calledName.indexOf('.');
      if ((i > 1) && (i < 15))
        this.calledName = this.calledName.substring(0, i).toUpperCase();
      else if (this.calledName.length() > 15)
        this.calledName = "*SMBSERVER     ";
      else
        this.calledName = this.calledName.toUpperCase();
    }
  }

  public Object getAddress()
  {
    return this.addr;
  }

  public String getHostAddress()
  {
    if ((this.addr instanceof NbtAddress))
      return ((NbtAddress)this.addr).getHostAddress();
    return ((InetAddress)this.addr).getHostAddress();
  }

  public String getHostName()
  {
    if ((this.addr instanceof NbtAddress))
      return ((NbtAddress)this.addr).getHostName();
    return ((InetAddress)this.addr).getHostName();
  }

  public int hashCode()
  {
    return this.addr.hashCode();
  }

  public String nextCalledName()
  {
    if ((this.addr instanceof NbtAddress))
      return ((NbtAddress)this.addr).nextCalledName();
    if (this.calledName != "*SMBSERVER     ")
    {
      this.calledName = "*SMBSERVER     ";
      return this.calledName;
    }
    return null;
  }

  public String toString()
  {
    return this.addr.toString();
  }

  static class QueryThread extends Thread
  {
    NbtAddress ans = null;
    String host;
    String scope;
    UniAddress.Sem sem;
    InetAddress svr;
    int type;
    UnknownHostException uhe;

    QueryThread(UniAddress.Sem paramSem, String paramString1, int paramInt, String paramString2, InetAddress paramInetAddress)
    {
      super();
      this.sem = paramSem;
      this.host = paramString1;
      this.type = paramInt;
      this.scope = paramString2;
      this.svr = paramInetAddress;
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_0
      //   2: getfield 43	jcifs/UniAddress$QueryThread:host	Ljava/lang/String;
      //   5: aload_0
      //   6: getfield 45	jcifs/UniAddress$QueryThread:type	I
      //   9: aload_0
      //   10: getfield 47	jcifs/UniAddress$QueryThread:scope	Ljava/lang/String;
      //   13: aload_0
      //   14: getfield 49	jcifs/UniAddress$QueryThread:svr	Ljava/net/InetAddress;
      //   17: invokestatic 60	jcifs/netbios/NbtAddress:getByName	(Ljava/lang/String;ILjava/lang/String;Ljava/net/InetAddress;)Ljcifs/netbios/NbtAddress;
      //   20: putfield 39	jcifs/UniAddress$QueryThread:ans	Ljcifs/netbios/NbtAddress;
      //   23: aload_0
      //   24: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   27: astore 13
      //   29: aload 13
      //   31: monitorenter
      //   32: aload_0
      //   33: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   36: astore 15
      //   38: aload 15
      //   40: iconst_m1
      //   41: aload 15
      //   43: getfield 65	jcifs/UniAddress$Sem:count	I
      //   46: iadd
      //   47: putfield 65	jcifs/UniAddress$Sem:count	I
      //   50: aload_0
      //   51: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   54: invokevirtual 70	java/lang/Object:notify	()V
      //   57: aload 13
      //   59: monitorexit
      //   60: return
      //   61: astore 9
      //   63: aload_0
      //   64: aload 9
      //   66: putfield 72	jcifs/UniAddress$QueryThread:uhe	Ljava/net/UnknownHostException;
      //   69: aload_0
      //   70: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   73: astore 10
      //   75: aload 10
      //   77: monitorenter
      //   78: aload_0
      //   79: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   82: astore 12
      //   84: aload 12
      //   86: iconst_m1
      //   87: aload 12
      //   89: getfield 65	jcifs/UniAddress$Sem:count	I
      //   92: iadd
      //   93: putfield 65	jcifs/UniAddress$Sem:count	I
      //   96: aload_0
      //   97: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   100: invokevirtual 70	java/lang/Object:notify	()V
      //   103: aload 10
      //   105: monitorexit
      //   106: return
      //   107: astore 11
      //   109: aload 10
      //   111: monitorexit
      //   112: aload 11
      //   114: athrow
      //   115: astore 5
      //   117: aload_0
      //   118: new 52	java/net/UnknownHostException
      //   121: dup
      //   122: aload 5
      //   124: invokevirtual 75	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   127: invokespecial 76	java/net/UnknownHostException:<init>	(Ljava/lang/String;)V
      //   130: putfield 72	jcifs/UniAddress$QueryThread:uhe	Ljava/net/UnknownHostException;
      //   133: aload_0
      //   134: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   137: astore 6
      //   139: aload 6
      //   141: monitorenter
      //   142: aload_0
      //   143: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   146: astore 8
      //   148: aload 8
      //   150: iconst_m1
      //   151: aload 8
      //   153: getfield 65	jcifs/UniAddress$Sem:count	I
      //   156: iadd
      //   157: putfield 65	jcifs/UniAddress$Sem:count	I
      //   160: aload_0
      //   161: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   164: invokevirtual 70	java/lang/Object:notify	()V
      //   167: aload 6
      //   169: monitorexit
      //   170: return
      //   171: astore 7
      //   173: aload 6
      //   175: monitorexit
      //   176: aload 7
      //   178: athrow
      //   179: astore_1
      //   180: aload_0
      //   181: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   184: astore_2
      //   185: aload_2
      //   186: monitorenter
      //   187: aload_0
      //   188: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   191: astore 4
      //   193: aload 4
      //   195: iconst_m1
      //   196: aload 4
      //   198: getfield 65	jcifs/UniAddress$Sem:count	I
      //   201: iadd
      //   202: putfield 65	jcifs/UniAddress$Sem:count	I
      //   205: aload_0
      //   206: getfield 41	jcifs/UniAddress$QueryThread:sem	Ljcifs/UniAddress$Sem;
      //   209: invokevirtual 70	java/lang/Object:notify	()V
      //   212: aload_2
      //   213: monitorexit
      //   214: aload_1
      //   215: athrow
      //   216: astore_3
      //   217: aload_2
      //   218: monitorexit
      //   219: aload_3
      //   220: athrow
      //   221: astore 14
      //   223: aload 13
      //   225: monitorexit
      //   226: aload 14
      //   228: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   0	23	61	java/net/UnknownHostException
      //   78	106	107	finally
      //   109	112	107	finally
      //   0	23	115	java/lang/Exception
      //   142	170	171	finally
      //   173	176	171	finally
      //   0	23	179	finally
      //   63	69	179	finally
      //   117	133	179	finally
      //   187	214	216	finally
      //   217	219	216	finally
      //   32	60	221	finally
      //   223	226	221	finally
    }
  }

  static class Sem
  {
    int count;

    Sem(int paramInt)
    {
      this.count = paramInt;
    }
  }
}