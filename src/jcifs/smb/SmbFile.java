package jcifs.smb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import jcifs.Config;
import jcifs.UniAddress;
import jcifs.dcerpc.DcerpcHandle;
import jcifs.dcerpc.msrpc.MsrpcDfsRootEnum;
import jcifs.dcerpc.msrpc.MsrpcShareEnum;
import jcifs.dcerpc.msrpc.MsrpcShareGetInfo;
import jcifs.netbios.NbtAddress;
import jcifs.util.LogStream;

public class SmbFile extends URLConnection
  implements SmbConstants
{
  public static final int ATTR_ARCHIVE = 32;
  static final int ATTR_COMPRESSED = 2048;
  public static final int ATTR_DIRECTORY = 16;
  static final int ATTR_GET_MASK = 32767;
  public static final int ATTR_HIDDEN = 2;
  static final int ATTR_NORMAL = 128;
  public static final int ATTR_READONLY = 1;
  static final int ATTR_SET_MASK = 12455;
  public static final int ATTR_SYSTEM = 4;
  static final int ATTR_TEMPORARY = 256;
  public static final int ATTR_VOLUME = 8;
  static final int DEFAULT_ATTR_EXPIRATION_PERIOD = 5000;
  public static final int FILE_NO_SHARE = 0;
  public static final int FILE_SHARE_DELETE = 4;
  public static final int FILE_SHARE_READ = 1;
  public static final int FILE_SHARE_WRITE = 2;
  static final int HASH_DOT = 0;
  static final int HASH_DOT_DOT = 0;
  static final int O_APPEND = 4;
  static final int O_CREAT = 16;
  static final int O_EXCL = 32;
  static final int O_RDONLY = 1;
  static final int O_RDWR = 3;
  static final int O_TRUNC = 64;
  static final int O_WRONLY = 2;
  public static final int TYPE_COMM = 64;
  public static final int TYPE_FILESYSTEM = 1;
  public static final int TYPE_NAMED_PIPE = 16;
  public static final int TYPE_PRINTER = 32;
  public static final int TYPE_SERVER = 4;
  public static final int TYPE_SHARE = 8;
  public static final int TYPE_WORKGROUP = 2;
  static long attrExpirationPeriod;
  protected static Dfs dfs;
  static LogStream log = LogStream.getInstance();
  int addressIndex;
  UniAddress[] addresses;
  private long attrExpiration;
  private int attributes;
  NtlmPasswordAuthentication auth;
  private SmbComBlankResponse blank_resp = null;
  private String canon;
  private long createTime;
  private DfsReferral dfsReferral = null;
  int fid;
  private boolean isExists;
  private long lastModified;
  boolean opened;
  private String share;
  private int shareAccess = 7;
  private long size;
  private long sizeExpiration;
  SmbTree tree = null;
  int tree_num;
  int type;
  String unc;

  static
  {
    try
    {
      Class.forName("jcifs.Config");
      attrExpirationPeriod = Config.getLong("jcifs.smb.client.attrExpirationPeriod", 5000L);
      dfs = new Dfs();
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      while (true)
        localClassNotFoundException.printStackTrace();
    }
  }

  public SmbFile(String paramString)
    throws MalformedURLException
  {
    this(new URL(null, paramString, Handler.SMB_HANDLER));
  }

  public SmbFile(String paramString1, String paramString2)
    throws MalformedURLException
  {
    this(new URL(new URL(null, paramString1, Handler.SMB_HANDLER), paramString2, Handler.SMB_HANDLER));
  }

  public SmbFile(String paramString1, String paramString2, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws MalformedURLException
  {
    this(new URL(new URL(null, paramString1, Handler.SMB_HANDLER), paramString2, Handler.SMB_HANDLER), paramNtlmPasswordAuthentication);
  }

  public SmbFile(String paramString1, String paramString2, NtlmPasswordAuthentication paramNtlmPasswordAuthentication, int paramInt)
    throws MalformedURLException
  {
    this(new URL(new URL(null, paramString1, Handler.SMB_HANDLER), paramString2, Handler.SMB_HANDLER), paramNtlmPasswordAuthentication);
    if ((paramInt & 0xFFFFFFF8) != 0)
      throw new RuntimeException("Illegal shareAccess parameter");
    this.shareAccess = paramInt;
  }

  public SmbFile(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws MalformedURLException
  {
    this(new URL(null, paramString, Handler.SMB_HANDLER), paramNtlmPasswordAuthentication);
  }

  public SmbFile(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication, int paramInt)
    throws MalformedURLException
  {
    this(new URL(null, paramString, Handler.SMB_HANDLER), paramNtlmPasswordAuthentication);
    if ((paramInt & 0xFFFFFFF8) != 0)
      throw new RuntimeException("Illegal shareAccess parameter");
    this.shareAccess = paramInt;
  }

  public SmbFile(URL paramURL)
  {
    this(paramURL, new NtlmPasswordAuthentication(paramURL.getUserInfo()));
  }

  public SmbFile(URL paramURL, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
  {
    super(paramURL);
    if (paramNtlmPasswordAuthentication == null)
      paramNtlmPasswordAuthentication = new NtlmPasswordAuthentication(paramURL.getUserInfo());
    this.auth = paramNtlmPasswordAuthentication;
    getUncPath0();
  }

  public SmbFile(SmbFile paramSmbFile, String paramString)
    throws MalformedURLException, UnknownHostException
  {
  }

  public SmbFile(SmbFile paramSmbFile, String paramString, int paramInt)
    throws MalformedURLException, UnknownHostException
  {
  }

  SmbFile(SmbFile paramSmbFile, String paramString, int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3)
    throws MalformedURLException, UnknownHostException
  {
  }

  private SmbComBlankResponse blank_resp()
  {
    if (this.blank_resp == null)
      this.blank_resp = new SmbComBlankResponse();
    return this.blank_resp;
  }

  private void processAces(ACE[] paramArrayOfACE, boolean paramBoolean)
    throws IOException
  {
    String str = getServerWithDfs();
    if (paramBoolean)
    {
      SID[] arrayOfSID = new SID[paramArrayOfACE.length];
      for (int j = 0; j < paramArrayOfACE.length; j++)
        arrayOfSID[j] = paramArrayOfACE[j].sid;
      for (int k = 0; k < arrayOfSID.length; k += 10)
      {
        int m = arrayOfSID.length - k;
        if (m > 64)
          m = 64;
        SID.resolveSids(str, this.auth, arrayOfSID, k, m);
      }
    }
    for (int i = 0; i < paramArrayOfACE.length; i++)
    {
      paramArrayOfACE[i].sid.origin_server = str;
      paramArrayOfACE[i].sid.origin_auth = this.auth;
    }
  }

  private long queryFSInformation(int paramInt)
    throws SmbException
  {
    Trans2QueryFSInformationResponse localTrans2QueryFSInformationResponse = new Trans2QueryFSInformationResponse(paramInt);
    send(new Trans2QueryFSInformation(paramInt), localTrans2QueryFSInformationResponse);
    if (this.type == 8)
    {
      this.size = localTrans2QueryFSInformationResponse.info.getCapacity();
      this.sizeExpiration = (System.currentTimeMillis() + attrExpirationPeriod);
    }
    return localTrans2QueryFSInformationResponse.info.getFree();
  }

  static String queryLookup(String paramString1, String paramString2)
  {
    char[] arrayOfChar = paramString1.toCharArray();
    int i = 0;
    int j = 0;
    int k = 0;
    if (k < arrayOfChar.length)
    {
      int n = arrayOfChar[k];
      if (n == 38)
      {
        if ((i > j) && (new String(arrayOfChar, j, i - j).equalsIgnoreCase(paramString2)))
        {
          int i1 = i + 1;
          return new String(arrayOfChar, i1, k - i1);
        }
        j = k + 1;
      }
      while (true)
      {
        k++;
        break;
        if (n == 61)
          i = k;
      }
    }
    if ((i > j) && (new String(arrayOfChar, j, i - j).equalsIgnoreCase(paramString2)))
    {
      int m = i + 1;
      return new String(arrayOfChar, m, arrayOfChar.length - m);
    }
    return null;
  }

  public boolean canRead()
    throws SmbException
  {
    if (getType() == 16)
      return true;
    return exists();
  }

  public boolean canWrite()
    throws SmbException
  {
    if (getType() == 16);
    while ((exists()) && ((0x1 & this.attributes) == 0))
      return true;
    return false;
  }

  void close()
    throws SmbException
  {
    close(0L);
  }

  void close(int paramInt, long paramLong)
    throws SmbException
  {
    if (LogStream.level >= 3)
      log.println("close: " + paramInt);
    send(new SmbComClose(paramInt, paramLong), blank_resp());
  }

  void close(long paramLong)
    throws SmbException
  {
    if (!isOpen())
      return;
    close(this.fid, paramLong);
    this.opened = false;
  }

  public void connect()
    throws IOException
  {
    if (isConnected())
      return;
    getUncPath0();
    getFirstAddress();
    while (true)
    {
      try
      {
        doConnect();
        return;
      }
      catch (SmbException localSmbException)
      {
        if (getNextAddress() == null)
          throw localSmbException;
      }
      if (LogStream.level >= 3)
        localSmbException.printStackTrace(log);
    }
  }

  void connect0()
    throws SmbException
  {
    try
    {
      connect();
      return;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new SmbException("Failed to connect to server", localUnknownHostException);
    }
    catch (SmbException localSmbException)
    {
      throw localSmbException;
    }
    catch (IOException localIOException)
    {
      throw new SmbException("Failed to connect to server", localIOException);
    }
  }

  public void copyTo(SmbFile paramSmbFile)
    throws SmbException
  {
    if ((this.share == null) || (paramSmbFile.share == null))
      throw new SmbException("Invalid operation for workgroups or servers");
    SmbComReadAndX localSmbComReadAndX = new SmbComReadAndX();
    SmbComReadAndXResponse localSmbComReadAndXResponse = new SmbComReadAndXResponse();
    connect0();
    paramSmbFile.connect0();
    resolveDfs(null);
    try
    {
      if ((getAddress().equals(paramSmbFile.getAddress())) && (this.canon.regionMatches(true, 0, paramSmbFile.canon, 0, Math.min(this.canon.length(), paramSmbFile.canon.length()))))
        throw new SmbException("Source and destination paths overlap.");
    }
    catch (UnknownHostException localUnknownHostException)
    {
      WriterThread localWriterThread = new WriterThread();
      localWriterThread.setDaemon(true);
      localWriterThread.start();
      SmbTransport localSmbTransport1 = this.tree.session.transport;
      SmbTransport localSmbTransport2 = paramSmbFile.tree.session.transport;
      if (localSmbTransport1.snd_buf_size < localSmbTransport2.snd_buf_size)
        localSmbTransport2.snd_buf_size = localSmbTransport1.snd_buf_size;
      while (true)
      {
        int i = Math.min(-70 + localSmbTransport1.rcv_buf_size, -70 + localSmbTransport1.snd_buf_size);
        int[] arrayOfInt = { 2, i };
        byte[][] arrayOfByte = (byte[][])Array.newInstance(Byte.TYPE, arrayOfInt);
        try
        {
          copyTo0(paramSmbFile, arrayOfByte, i, localWriterThread, localSmbComReadAndX, localSmbComReadAndXResponse);
          return;
          localSmbTransport1.snd_buf_size = localSmbTransport2.snd_buf_size;
        }
        finally
        {
          localWriterThread.write(null, -1, null, 0);
        }
      }
    }
  }

  // ERROR //
  void copyTo0(SmbFile paramSmbFile, byte[][] paramArrayOfByte, int paramInt, WriterThread paramWriterThread, SmbComReadAndX paramSmbComReadAndX, SmbComReadAndXResponse paramSmbComReadAndXResponse)
    throws SmbException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 262	jcifs/smb/SmbFile:attrExpiration	J
    //   4: invokestatic 258	java/lang/System:currentTimeMillis	()J
    //   7: lcmp
    //   8: ifge +86 -> 94
    //   11: aload_0
    //   12: bipush 17
    //   14: putfield 244	jcifs/smb/SmbFile:attributes	I
    //   17: aload_0
    //   18: lconst_0
    //   19: putfield 246	jcifs/smb/SmbFile:createTime	J
    //   22: aload_0
    //   23: lconst_0
    //   24: putfield 248	jcifs/smb/SmbFile:lastModified	J
    //   27: aload_0
    //   28: iconst_0
    //   29: putfield 252	jcifs/smb/SmbFile:isExists	Z
    //   32: aload_0
    //   33: aload_0
    //   34: invokevirtual 196	jcifs/smb/SmbFile:getUncPath0	()Ljava/lang/String;
    //   37: sipush 257
    //   40: invokevirtual 508	jcifs/smb/SmbFile:queryPath	(Ljava/lang/String;I)Ljcifs/smb/Info;
    //   43: astore 24
    //   45: aload_0
    //   46: aload 24
    //   48: invokeinterface 513 1 0
    //   53: putfield 244	jcifs/smb/SmbFile:attributes	I
    //   56: aload_0
    //   57: aload 24
    //   59: invokeinterface 516 1 0
    //   64: putfield 246	jcifs/smb/SmbFile:createTime	J
    //   67: aload_0
    //   68: aload 24
    //   70: invokeinterface 519 1 0
    //   75: putfield 248	jcifs/smb/SmbFile:lastModified	J
    //   78: aload_0
    //   79: iconst_1
    //   80: putfield 252	jcifs/smb/SmbFile:isExists	Z
    //   83: aload_0
    //   84: invokestatic 258	java/lang/System:currentTimeMillis	()J
    //   87: getstatic 134	jcifs/smb/SmbFile:attrExpirationPeriod	J
    //   90: ladd
    //   91: putfield 262	jcifs/smb/SmbFile:attrExpiration	J
    //   94: aload_0
    //   95: invokevirtual 522	jcifs/smb/SmbFile:isDirectory	()Z
    //   98: ifeq +209 -> 307
    //   101: aload_1
    //   102: invokevirtual 196	jcifs/smb/SmbFile:getUncPath0	()Ljava/lang/String;
    //   105: invokevirtual 228	java/lang/String:length	()I
    //   108: iconst_1
    //   109: if_icmple +23 -> 132
    //   112: aload_1
    //   113: invokevirtual 525	jcifs/smb/SmbFile:mkdir	()V
    //   116: aload_1
    //   117: aload_0
    //   118: getfield 244	jcifs/smb/SmbFile:attributes	I
    //   121: aload_0
    //   122: getfield 246	jcifs/smb/SmbFile:createTime	J
    //   125: aload_0
    //   126: getfield 248	jcifs/smb/SmbFile:lastModified	J
    //   129: invokevirtual 529	jcifs/smb/SmbFile:setPathInformation	(IJJ)V
    //   132: aload_0
    //   133: ldc_w 531
    //   136: bipush 22
    //   138: aconst_null
    //   139: aconst_null
    //   140: invokevirtual 535	jcifs/smb/SmbFile:listFiles	(Ljava/lang/String;ILjcifs/smb/SmbFilenameFilter;Ljcifs/smb/SmbFileFilter;)[Ljcifs/smb/SmbFile;
    //   143: astore 17
    //   145: iconst_0
    //   146: istore 18
    //   148: aload 17
    //   150: arraylength
    //   151: istore 21
    //   153: iload 18
    //   155: iload 21
    //   157: if_icmpge +261 -> 418
    //   160: new 2	jcifs/smb/SmbFile
    //   163: dup
    //   164: aload_1
    //   165: aload 17
    //   167: iload 18
    //   169: aaload
    //   170: invokevirtual 538	jcifs/smb/SmbFile:getName	()Ljava/lang/String;
    //   173: aload 17
    //   175: iload 18
    //   177: aaload
    //   178: getfield 242	jcifs/smb/SmbFile:type	I
    //   181: aload 17
    //   183: iload 18
    //   185: aaload
    //   186: getfield 244	jcifs/smb/SmbFile:attributes	I
    //   189: aload 17
    //   191: iload 18
    //   193: aaload
    //   194: getfield 246	jcifs/smb/SmbFile:createTime	J
    //   197: aload 17
    //   199: iload 18
    //   201: aaload
    //   202: getfield 248	jcifs/smb/SmbFile:lastModified	J
    //   205: aload 17
    //   207: iload 18
    //   209: aaload
    //   210: getfield 250	jcifs/smb/SmbFile:size	J
    //   213: invokespecial 540	jcifs/smb/SmbFile:<init>	(Ljcifs/smb/SmbFile;Ljava/lang/String;IIJJJ)V
    //   216: astore 22
    //   218: aload 17
    //   220: iload 18
    //   222: aaload
    //   223: aload 22
    //   225: aload_2
    //   226: iload_3
    //   227: aload 4
    //   229: aload 5
    //   231: aload 6
    //   233: invokevirtual 494	jcifs/smb/SmbFile:copyTo0	(Ljcifs/smb/SmbFile;[[BILjcifs/smb/SmbFile$WriterThread;Ljcifs/smb/SmbComReadAndX;Ljcifs/smb/SmbComReadAndXResponse;)V
    //   236: iinc 18 1
    //   239: goto -91 -> 148
    //   242: astore 23
    //   244: aload 23
    //   246: invokevirtual 543	jcifs/smb/SmbException:getNtStatus	()I
    //   249: ldc_w 544
    //   252: if_icmpeq -120 -> 132
    //   255: aload 23
    //   257: invokevirtual 543	jcifs/smb/SmbException:getNtStatus	()I
    //   260: ldc_w 545
    //   263: if_icmpeq -131 -> 132
    //   266: aload 23
    //   268: athrow
    //   269: astore 20
    //   271: new 307	jcifs/smb/SmbException
    //   274: dup
    //   275: aload_0
    //   276: getfield 219	jcifs/smb/SmbFile:url	Ljava/net/URL;
    //   279: invokevirtual 546	java/net/URL:toString	()Ljava/lang/String;
    //   282: aload 20
    //   284: invokespecial 408	jcifs/smb/SmbException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   287: athrow
    //   288: astore 19
    //   290: new 307	jcifs/smb/SmbException
    //   293: dup
    //   294: aload_0
    //   295: getfield 219	jcifs/smb/SmbFile:url	Ljava/net/URL;
    //   298: invokevirtual 546	java/net/URL:toString	()Ljava/lang/String;
    //   301: aload 19
    //   303: invokespecial 408	jcifs/smb/SmbException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   306: athrow
    //   307: aload_0
    //   308: iconst_1
    //   309: iconst_0
    //   310: sipush 128
    //   313: iconst_0
    //   314: invokevirtual 550	jcifs/smb/SmbFile:open	(IIII)V
    //   317: aload_1
    //   318: bipush 82
    //   320: sipush 258
    //   323: aload_0
    //   324: getfield 244	jcifs/smb/SmbFile:attributes	I
    //   327: iconst_0
    //   328: invokevirtual 550	jcifs/smb/SmbFile:open	(IIII)V
    //   331: iconst_0
    //   332: istore 11
    //   334: iconst_0
    //   335: istore 12
    //   337: aload 5
    //   339: aload_0
    //   340: getfield 379	jcifs/smb/SmbFile:fid	I
    //   343: iload 11
    //   345: i2l
    //   346: iload_3
    //   347: invokevirtual 554	jcifs/smb/SmbComReadAndX:setParam	(IJI)V
    //   350: aload 6
    //   352: aload_2
    //   353: iload 12
    //   355: aaload
    //   356: iconst_0
    //   357: invokevirtual 557	jcifs/smb/SmbComReadAndXResponse:setParam	([BI)V
    //   360: aload_0
    //   361: aload 5
    //   363: aload 6
    //   365: invokevirtual 319	jcifs/smb/SmbFile:send	(Ljcifs/smb/ServerMessageBlock;Ljcifs/smb/ServerMessageBlock;)V
    //   368: aload 4
    //   370: monitorenter
    //   371: aload 4
    //   373: getfield 561	jcifs/smb/SmbFile$WriterThread:e	Ljcifs/smb/SmbException;
    //   376: ifnull +96 -> 472
    //   379: aload 4
    //   381: getfield 561	jcifs/smb/SmbFile$WriterThread:e	Ljcifs/smb/SmbException;
    //   384: athrow
    //   385: astore 13
    //   387: aload 4
    //   389: monitorexit
    //   390: aload 13
    //   392: athrow
    //   393: astore 8
    //   395: getstatic 114	jcifs/smb/SmbFile:log	Ljcifs/util/LogStream;
    //   398: pop
    //   399: getstatic 360	jcifs/util/LogStream:level	I
    //   402: iconst_1
    //   403: if_icmple +11 -> 414
    //   406: aload 8
    //   408: getstatic 114	jcifs/smb/SmbFile:log	Ljcifs/util/LogStream;
    //   411: invokevirtual 562	java/lang/Exception:printStackTrace	(Ljava/io/PrintStream;)V
    //   414: aload_0
    //   415: invokevirtual 564	jcifs/smb/SmbFile:close	()V
    //   418: return
    //   419: astore 10
    //   421: iconst_1
    //   422: aload_1
    //   423: getfield 244	jcifs/smb/SmbFile:attributes	I
    //   426: iand
    //   427: ifeq +42 -> 469
    //   430: aload_1
    //   431: bipush 254
    //   433: aload_1
    //   434: getfield 244	jcifs/smb/SmbFile:attributes	I
    //   437: iand
    //   438: lconst_0
    //   439: lconst_0
    //   440: invokevirtual 529	jcifs/smb/SmbFile:setPathInformation	(IJJ)V
    //   443: aload_1
    //   444: bipush 82
    //   446: sipush 258
    //   449: aload_0
    //   450: getfield 244	jcifs/smb/SmbFile:attributes	I
    //   453: iconst_0
    //   454: invokevirtual 550	jcifs/smb/SmbFile:open	(IIII)V
    //   457: goto -126 -> 331
    //   460: astore 7
    //   462: aload_0
    //   463: invokevirtual 564	jcifs/smb/SmbFile:close	()V
    //   466: aload 7
    //   468: athrow
    //   469: aload 10
    //   471: athrow
    //   472: aload 4
    //   474: getfield 567	jcifs/smb/SmbFile$WriterThread:ready	Z
    //   477: istore 14
    //   479: iload 14
    //   481: ifne +30 -> 511
    //   484: aload 4
    //   486: invokevirtual 572	java/lang/Object:wait	()V
    //   489: goto -17 -> 472
    //   492: astore 16
    //   494: new 307	jcifs/smb/SmbException
    //   497: dup
    //   498: aload_1
    //   499: getfield 219	jcifs/smb/SmbFile:url	Ljava/net/URL;
    //   502: invokevirtual 546	java/net/URL:toString	()Ljava/lang/String;
    //   505: aload 16
    //   507: invokespecial 408	jcifs/smb/SmbException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   510: athrow
    //   511: aload 4
    //   513: getfield 561	jcifs/smb/SmbFile$WriterThread:e	Ljcifs/smb/SmbException;
    //   516: ifnull +9 -> 525
    //   519: aload 4
    //   521: getfield 561	jcifs/smb/SmbFile$WriterThread:e	Ljcifs/smb/SmbException;
    //   524: athrow
    //   525: aload 6
    //   527: getfield 575	jcifs/smb/SmbComReadAndXResponse:dataLength	I
    //   530: ifgt +50 -> 580
    //   533: aload 4
    //   535: monitorexit
    //   536: aload_1
    //   537: new 577	jcifs/smb/Trans2SetFileInformation
    //   540: dup
    //   541: aload_1
    //   542: getfield 379	jcifs/smb/SmbFile:fid	I
    //   545: aload_0
    //   546: getfield 244	jcifs/smb/SmbFile:attributes	I
    //   549: aload_0
    //   550: getfield 246	jcifs/smb/SmbFile:createTime	J
    //   553: aload_0
    //   554: getfield 248	jcifs/smb/SmbFile:lastModified	J
    //   557: invokespecial 580	jcifs/smb/Trans2SetFileInformation:<init>	(IIJJ)V
    //   560: new 582	jcifs/smb/Trans2SetFileInformationResponse
    //   563: dup
    //   564: invokespecial 583	jcifs/smb/Trans2SetFileInformationResponse:<init>	()V
    //   567: invokevirtual 319	jcifs/smb/SmbFile:send	(Ljcifs/smb/ServerMessageBlock;Ljcifs/smb/ServerMessageBlock;)V
    //   570: aload_1
    //   571: lconst_0
    //   572: invokevirtual 356	jcifs/smb/SmbFile:close	(J)V
    //   575: aload_0
    //   576: invokevirtual 564	jcifs/smb/SmbFile:close	()V
    //   579: return
    //   580: aload 4
    //   582: aload_2
    //   583: iload 12
    //   585: aaload
    //   586: aload 6
    //   588: getfield 575	jcifs/smb/SmbComReadAndXResponse:dataLength	I
    //   591: aload_1
    //   592: iload 11
    //   594: invokevirtual 498	jcifs/smb/SmbFile$WriterThread:write	([BILjcifs/smb/SmbFile;I)V
    //   597: aload 4
    //   599: monitorexit
    //   600: iload 12
    //   602: iconst_1
    //   603: if_icmpne +23 -> 626
    //   606: iconst_0
    //   607: istore 12
    //   609: aload 6
    //   611: getfield 575	jcifs/smb/SmbComReadAndXResponse:dataLength	I
    //   614: istore 15
    //   616: iload 11
    //   618: iload 15
    //   620: iadd
    //   621: istore 11
    //   623: goto -286 -> 337
    //   626: iconst_1
    //   627: istore 12
    //   629: goto -20 -> 609
    //
    // Exception table:
    //   from	to	target	type
    //   112	132	242	jcifs/smb/SmbException
    //   148	153	269	java/net/UnknownHostException
    //   160	236	269	java/net/UnknownHostException
    //   148	153	288	java/net/MalformedURLException
    //   160	236	288	java/net/MalformedURLException
    //   371	385	385	finally
    //   387	390	385	finally
    //   472	479	385	finally
    //   484	489	385	finally
    //   494	511	385	finally
    //   511	525	385	finally
    //   525	536	385	finally
    //   580	600	385	finally
    //   307	317	393	java/lang/Exception
    //   317	331	393	java/lang/Exception
    //   337	371	393	java/lang/Exception
    //   390	393	393	java/lang/Exception
    //   421	457	393	java/lang/Exception
    //   469	472	393	java/lang/Exception
    //   536	575	393	java/lang/Exception
    //   609	616	393	java/lang/Exception
    //   317	331	419	jcifs/smb/SmbAuthException
    //   307	317	460	finally
    //   317	331	460	finally
    //   337	371	460	finally
    //   390	393	460	finally
    //   395	414	460	finally
    //   421	457	460	finally
    //   469	472	460	finally
    //   536	575	460	finally
    //   609	616	460	finally
    //   484	489	492	java/lang/InterruptedException
  }

  public void createNewFile()
    throws SmbException
  {
    if (getUncPath0().length() == 1)
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    close(open0(51, 0, 128, 0), 0L);
  }

  public long createTime()
    throws SmbException
  {
    if (getUncPath0().length() > 1)
    {
      exists();
      return this.createTime;
    }
    return 0L;
  }

  public void delete()
    throws SmbException
  {
    exists();
    getUncPath0();
    delete(this.unc);
  }

  void delete(String paramString)
    throws SmbException
  {
    if (getUncPath0().length() == 1)
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    if (System.currentTimeMillis() > this.attrExpiration)
    {
      this.attributes = 17;
      this.createTime = 0L;
      this.lastModified = 0L;
      this.isExists = false;
      Info localInfo = queryPath(getUncPath0(), 257);
      this.attributes = localInfo.getAttributes();
      this.createTime = localInfo.getCreateTime();
      this.lastModified = localInfo.getLastWriteTime();
      this.attrExpiration = (System.currentTimeMillis() + attrExpirationPeriod);
      this.isExists = true;
    }
    if ((0x1 & this.attributes) != 0)
      setReadWrite();
    if (LogStream.level >= 3)
      log.println("delete: " + paramString);
    if ((0x10 & this.attributes) != 0)
      try
      {
        SmbFile[] arrayOfSmbFile = listFiles("*", 22, null, null);
        for (int i = 0; i < arrayOfSmbFile.length; i++)
          arrayOfSmbFile[i].delete();
      }
      catch (SmbException localSmbException)
      {
        if (localSmbException.getNtStatus() != -1073741809)
          throw localSmbException;
        send(new SmbComDeleteDirectory(paramString), blank_resp());
      }
    while (true)
    {
      this.sizeExpiration = 0L;
      this.attrExpiration = 0L;
      return;
      send(new SmbComDelete(paramString), blank_resp());
    }
  }

  void doConnect()
    throws IOException
  {
    UniAddress localUniAddress = getAddress();
    SmbTransport localSmbTransport;
    if (this.tree != null)
      localSmbTransport = this.tree.session.transport;
    while (true)
    {
      String str = getServerWithDfs();
      SmbTree localSmbTree1 = this.tree;
      boolean bool1;
      if (dfs.resolve(str, this.tree.share, null, this.auth) != null)
      {
        bool1 = true;
        localSmbTree1.inDomainDfs = bool1;
        if (this.tree.inDomainDfs)
          this.tree.treeConnected = true;
      }
      try
      {
        if (LogStream.level >= 3)
          log.println("doConnect: " + localUniAddress);
        this.tree.treeConnect(null, null);
        return;
        localSmbTransport = SmbTransport.getSmbTransport(localUniAddress, this.url.getPort());
        this.tree = localSmbTransport.getSmbSession(this.auth).getSmbTree(this.share, null);
        continue;
        bool1 = false;
      }
      catch (SmbAuthException localSmbAuthException)
      {
        if (this.share == null)
        {
          this.tree = localSmbTransport.getSmbSession(NtlmPasswordAuthentication.NULL).getSmbTree(null, null);
          this.tree.treeConnect(null, null);
          return;
        }
        NtlmPasswordAuthentication localNtlmPasswordAuthentication = NtlmAuthenticator.requestNtlmPasswordAuthentication(this.url.toString(), localSmbAuthException);
        if (localNtlmPasswordAuthentication != null)
        {
          this.auth = localNtlmPasswordAuthentication;
          this.tree = localSmbTransport.getSmbSession(this.auth).getSmbTree(this.share, null);
          SmbTree localSmbTree2 = this.tree;
          DfsReferral localDfsReferral = dfs.resolve(str, this.tree.share, null, this.auth);
          boolean bool2 = false;
          if (localDfsReferral != null)
            bool2 = true;
          localSmbTree2.inDomainDfs = bool2;
          if (this.tree.inDomainDfs)
            this.tree.treeConnected = true;
          this.tree.treeConnect(null, null);
          return;
        }
        if ((LogStream.level >= 1) && (hasNextAddress()))
          localSmbAuthException.printStackTrace(log);
        throw localSmbAuthException;
      }
    }
  }

  FileEntry[] doDfsRootEnum()
    throws IOException
  {
    DcerpcHandle localDcerpcHandle = DcerpcHandle.getHandle("ncacn_np:" + getAddress().getHostAddress() + "[\\PIPE\\netdfs]", this.auth);
    try
    {
      localMsrpcDfsRootEnum = new MsrpcDfsRootEnum(getServer());
      localDcerpcHandle.sendrecv(localMsrpcDfsRootEnum);
      if (localMsrpcDfsRootEnum.retval != 0)
        throw new SmbException(localMsrpcDfsRootEnum.retval, true);
    }
    finally
    {
      FileEntry[] arrayOfFileEntry;
      try
      {
        MsrpcDfsRootEnum localMsrpcDfsRootEnum;
        localDcerpcHandle.close();
        throw localObject;
        arrayOfFileEntry = localMsrpcDfsRootEnum.getEntries();
      }
      catch (IOException localIOException1)
      {
        try
        {
          do
          {
            localDcerpcHandle.close();
            return arrayOfFileEntry;
            localIOException1 = localIOException1;
          }
          while (LogStream.level < 4);
          localIOException1.printStackTrace(log);
        }
        catch (IOException localIOException2)
        {
          while (LogStream.level < 4);
          localIOException2.printStackTrace(log);
        }
      }
      return arrayOfFileEntry;
    }
  }

  void doEnum(ArrayList paramArrayList, boolean paramBoolean, String paramString, int paramInt, SmbFilenameFilter paramSmbFilenameFilter, SmbFileFilter paramSmbFileFilter)
    throws SmbException
  {
    if ((paramSmbFileFilter != null) && ((paramSmbFileFilter instanceof DosFileFilter)))
    {
      DosFileFilter localDosFileFilter = (DosFileFilter)paramSmbFileFilter;
      if (localDosFileFilter.wildcard != null)
        paramString = localDosFileFilter.wildcard;
      paramInt = localDosFileFilter.attributes;
    }
    try
    {
      if ((this.url.getHost().length() == 0) || (getType() == 2))
      {
        doNetServerEnum(paramArrayList, paramBoolean, paramString, paramInt, paramSmbFilenameFilter, paramSmbFileFilter);
        return;
      }
      if (this.share == null)
      {
        doShareEnum(paramArrayList, paramBoolean, paramString, paramInt, paramSmbFilenameFilter, paramSmbFileFilter);
        return;
      }
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new SmbException(this.url.toString(), localUnknownHostException);
      doFindFirstNext(paramArrayList, paramBoolean, paramString, paramInt, paramSmbFilenameFilter, paramSmbFileFilter);
      return;
    }
    catch (MalformedURLException localMalformedURLException)
    {
      throw new SmbException(this.url.toString(), localMalformedURLException);
    }
  }

  void doFindFirstNext(ArrayList paramArrayList, boolean paramBoolean, String paramString, int paramInt, SmbFilenameFilter paramSmbFilenameFilter, SmbFileFilter paramSmbFileFilter)
    throws SmbException, UnknownHostException, MalformedURLException
  {
    String str1 = getUncPath0();
    String str2 = this.url.getPath();
    if (str2.lastIndexOf('/') != -1 + str2.length())
      throw new SmbException(this.url.toString() + " directory must end with '/'");
    Trans2FindFirst2 localTrans2FindFirst2 = new Trans2FindFirst2(str1, paramString, paramInt);
    Trans2FindFirst2Response localTrans2FindFirst2Response = new Trans2FindFirst2Response();
    if (LogStream.level >= 3)
      log.println("doFindFirstNext: " + localTrans2FindFirst2.path);
    send(localTrans2FindFirst2, localTrans2FindFirst2Response);
    int i = localTrans2FindFirst2Response.sid;
    Trans2FindNext2 localTrans2FindNext2 = new Trans2FindNext2(i, localTrans2FindFirst2Response.resumeKey, localTrans2FindFirst2Response.lastName);
    localTrans2FindFirst2Response.subCommand = 2;
    while (true)
    {
      int j = 0;
      int k = localTrans2FindFirst2Response.numEntries;
      if (j < k)
      {
        FileEntry localFileEntry = localTrans2FindFirst2Response.results[j];
        String str3 = localFileEntry.getName();
        if (str3.length() < 3)
        {
          int m = str3.hashCode();
          if (((m != HASH_DOT) && (m != HASH_DOT_DOT)) || ((!str3.equals(".")) && (!str3.equals(".."))));
        }
        while (true)
        {
          j++;
          break;
          if (((paramSmbFilenameFilter == null) || (paramSmbFilenameFilter.accept(this, str3))) && (str3.length() > 0))
          {
            SmbFile localSmbFile = new SmbFile(this, str3, 1, localFileEntry.getAttributes(), localFileEntry.createTime(), localFileEntry.lastModified(), localFileEntry.length());
            if ((paramSmbFileFilter == null) || (paramSmbFileFilter.accept(localSmbFile)))
              if (paramBoolean)
                paramArrayList.add(localSmbFile);
              else
                paramArrayList.add(str3);
          }
        }
      }
      if ((localTrans2FindFirst2Response.isEndOfSearch) || (localTrans2FindFirst2Response.numEntries == 0));
      try
      {
        send(new SmbComFindClose2(i), blank_resp());
        return;
        localTrans2FindNext2.reset(localTrans2FindFirst2Response.resumeKey, localTrans2FindFirst2Response.lastName);
        localTrans2FindFirst2Response.reset();
        send(localTrans2FindNext2, localTrans2FindFirst2Response);
      }
      catch (SmbException localSmbException)
      {
        while (LogStream.level < 4);
        localSmbException.printStackTrace(log);
      }
    }
  }

  FileEntry[] doMsrpcShareEnum()
    throws IOException
  {
    MsrpcShareEnum localMsrpcShareEnum = new MsrpcShareEnum(this.url.getHost());
    DcerpcHandle localDcerpcHandle = DcerpcHandle.getHandle("ncacn_np:" + getAddress().getHostAddress() + "[\\PIPE\\srvsvc]", this.auth);
    try
    {
      localDcerpcHandle.sendrecv(localMsrpcShareEnum);
      if (localMsrpcShareEnum.retval != 0)
        throw new SmbException(localMsrpcShareEnum.retval, true);
    }
    finally
    {
      FileEntry[] arrayOfFileEntry;
      try
      {
        localDcerpcHandle.close();
        throw localObject;
        arrayOfFileEntry = localMsrpcShareEnum.getEntries();
      }
      catch (IOException localIOException1)
      {
        try
        {
          do
          {
            localDcerpcHandle.close();
            return arrayOfFileEntry;
            localIOException1 = localIOException1;
          }
          while (LogStream.level < 4);
          localIOException1.printStackTrace(log);
        }
        catch (IOException localIOException2)
        {
          while (LogStream.level < 4);
          localIOException2.printStackTrace(log);
        }
      }
      return arrayOfFileEntry;
    }
  }

  void doNetServerEnum(ArrayList paramArrayList, boolean paramBoolean, String paramString, int paramInt, SmbFilenameFilter paramSmbFilenameFilter, SmbFileFilter paramSmbFileFilter)
    throws SmbException, UnknownHostException, MalformedURLException
  {
    int i;
    NetServerEnum2 localNetServerEnum2;
    NetServerEnum2Response localNetServerEnum2Response;
    if (this.url.getHost().length() == 0)
    {
      i = 0;
      if (i != 0)
        break label116;
      connect0();
      String str4 = this.tree.session.transport.server.oemDomainName;
      localNetServerEnum2 = new NetServerEnum2(str4, -2147483648);
      localNetServerEnum2Response = new NetServerEnum2Response();
    }
    label116: int j;
    label155: label189: label217: label220: label359: 
    do
    {
      while (true)
      {
        send(localNetServerEnum2, localNetServerEnum2Response);
        if ((localNetServerEnum2Response.status == 0) || (localNetServerEnum2Response.status == 234))
          break label189;
        throw new SmbException(localNetServerEnum2Response.status, true);
        i = getType();
        break;
        if (i != 2)
          break label155;
        String str1 = this.url.getHost();
        localNetServerEnum2 = new NetServerEnum2(str1, -1);
        localNetServerEnum2Response = new NetServerEnum2Response();
      }
      throw new SmbException("The requested list operations is invalid: " + this.url.toString());
      int k;
      int m;
      FileEntry localFileEntry;
      String str3;
      if (localNetServerEnum2Response.status == 234)
      {
        j = 1;
        if (j == 0)
          break label276;
        k = -1 + localNetServerEnum2Response.numEntries;
        m = 0;
        if (m >= k)
          break label359;
        localFileEntry = localNetServerEnum2Response.results[m];
        str3 = localFileEntry.getName();
        if ((paramSmbFilenameFilter == null) || (paramSmbFilenameFilter.accept(this, str3)))
          break label286;
      }
      while (true)
      {
        m++;
        break label220;
        j = 0;
        break;
        k = localNetServerEnum2Response.numEntries;
        break label217;
        if (str3.length() > 0)
        {
          SmbFile localSmbFile = new SmbFile(this, str3, localFileEntry.getType(), 17, 0L, 0L, 0L);
          if ((paramSmbFileFilter == null) || (paramSmbFileFilter.accept(localSmbFile)))
            if (paramBoolean)
              paramArrayList.add(localSmbFile);
            else
              paramArrayList.add(str3);
        }
      }
      if (getType() != 2)
        return;
      localNetServerEnum2.subCommand = -41;
      String str2 = ((NetServerEnum2Response)localNetServerEnum2Response).lastName;
      localNetServerEnum2.reset(0, str2);
      localNetServerEnum2Response.reset();
    }
    while (j != 0);
    label276: label286: return;
  }

  FileEntry[] doNetShareEnum()
    throws SmbException
  {
    NetShareEnum localNetShareEnum = new NetShareEnum();
    NetShareEnumResponse localNetShareEnumResponse = new NetShareEnumResponse();
    send(localNetShareEnum, localNetShareEnumResponse);
    if (localNetShareEnumResponse.status != 0)
      throw new SmbException(localNetShareEnumResponse.status, true);
    return localNetShareEnumResponse.results;
  }

  void doShareEnum(ArrayList paramArrayList, boolean paramBoolean, String paramString, int paramInt, SmbFilenameFilter paramSmbFilenameFilter, SmbFileFilter paramSmbFileFilter)
    throws SmbException, UnknownHostException, MalformedURLException
  {
    String str1 = this.url.getPath();
    Object localObject1 = null;
    if (str1.lastIndexOf('/') != -1 + str1.length())
      throw new SmbException(this.url.toString() + " directory must end with '/'");
    if (getType() != 4)
      throw new SmbException("The requested list operations is invalid: " + this.url.toString());
    HashMap localHashMap = new HashMap();
    if (dfs.isTrustedDomain(getServer(), this.auth))
      try
      {
        FileEntry[] arrayOfFileEntry3 = doDfsRootEnum();
        for (int j = 0; j < arrayOfFileEntry3.length; j++)
        {
          FileEntry localFileEntry2 = arrayOfFileEntry3[j];
          if (!localHashMap.containsKey(localFileEntry2))
            localHashMap.put(localFileEntry2, localFileEntry2);
        }
      }
      catch (IOException localIOException3)
      {
        if (LogStream.level >= 4)
          localIOException3.printStackTrace(log);
      }
    UniAddress localUniAddress = getFirstAddress();
    if (localUniAddress != null);
    while (true)
    {
      int i;
      try
      {
        doConnect();
        try
        {
          FileEntry[] arrayOfFileEntry2 = doMsrpcShareEnum();
          localObject2 = arrayOfFileEntry2;
          i = 0;
          if (i >= localObject2.length)
            break label336;
          Object localObject3 = localObject2[i];
          if (localHashMap.containsKey(localObject3))
            break label515;
          localHashMap.put(localObject3, localObject3);
        }
        catch (IOException localIOException2)
        {
          if (LogStream.level >= 3)
            localIOException2.printStackTrace(log);
          FileEntry[] arrayOfFileEntry1 = doNetShareEnum();
          Object localObject2 = arrayOfFileEntry1;
          continue;
        }
      }
      catch (IOException localIOException1)
      {
        if (LogStream.level >= 3)
          localIOException1.printStackTrace(log);
        localObject1 = localIOException1;
        localUniAddress = getNextAddress();
      }
      break;
      label336: if ((localObject1 != null) && (localHashMap.isEmpty()))
      {
        if (!(localObject1 instanceof SmbException))
          throw new SmbException(this.url.toString(), localObject1);
        throw ((SmbException)localObject1);
      }
      Iterator localIterator = localHashMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        FileEntry localFileEntry1 = (FileEntry)localIterator.next();
        String str2 = localFileEntry1.getName();
        if (((paramSmbFilenameFilter == null) || (paramSmbFilenameFilter.accept(this, str2))) && (str2.length() > 0))
        {
          SmbFile localSmbFile = new SmbFile(this, str2, localFileEntry1.getType(), 17, 0L, 0L, 0L);
          if ((paramSmbFileFilter == null) || (paramSmbFileFilter.accept(localSmbFile)))
            if (paramBoolean)
              paramArrayList.add(localSmbFile);
            else
              paramArrayList.add(str2);
        }
      }
      return;
      label515: i++;
    }
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof SmbFile))
    {
      SmbFile localSmbFile = (SmbFile)paramObject;
      if (this == localSmbFile)
        return true;
      if (pathNamesPossiblyEqual(this.url.getPath(), localSmbFile.url.getPath()))
      {
        getUncPath0();
        localSmbFile.getUncPath0();
        if (this.canon.equalsIgnoreCase(localSmbFile.canon))
          try
          {
            boolean bool = getAddress().equals(localSmbFile.getAddress());
            return bool;
          }
          catch (UnknownHostException localUnknownHostException)
          {
            return getServer().equalsIgnoreCase(localSmbFile.getServer());
          }
      }
    }
    return false;
  }

  public boolean exists()
    throws SmbException
  {
    if (this.attrExpiration > System.currentTimeMillis())
      return this.isExists;
    this.attributes = 17;
    this.createTime = 0L;
    this.lastModified = 0L;
    this.isExists = false;
    try
    {
      if (this.url.getHost().length() == 0);
      while (true)
      {
        this.isExists = true;
        label55: this.attrExpiration = (System.currentTimeMillis() + attrExpirationPeriod);
        return this.isExists;
        if (this.share != null)
          break;
        if (getType() == 2)
          UniAddress.getByName(this.url.getHost(), true);
        else
          UniAddress.getByName(this.url.getHost()).getHostName();
      }
    }
    catch (SmbException localSmbException)
    {
      while (true)
      {
        switch (localSmbException.getNtStatus())
        {
        case -1073741809:
        case -1073741773:
        case -1073741772:
        case -1073741766:
        }
        throw localSmbException;
        if ((getUncPath0().length() == 1) || (this.share.equalsIgnoreCase("IPC$")))
        {
          connect0();
        }
        else
        {
          Info localInfo = queryPath(getUncPath0(), 257);
          this.attributes = localInfo.getAttributes();
          this.createTime = localInfo.getCreateTime();
          this.lastModified = localInfo.getLastWriteTime();
        }
      }
    }
    catch (UnknownHostException localUnknownHostException)
    {
      break label55;
    }
  }

  UniAddress getAddress()
    throws UnknownHostException
  {
    if (this.addressIndex == 0)
      return getFirstAddress();
    return this.addresses[(-1 + this.addressIndex)];
  }

  public int getAttributes()
    throws SmbException
  {
    if (getUncPath0().length() == 1)
      return 0;
    exists();
    return 0x7FFF & this.attributes;
  }

  public String getCanonicalPath()
  {
    String str = this.url.getAuthority();
    getUncPath0();
    if (str.length() > 0)
      return "smb://" + this.url.getAuthority() + this.canon;
    return "smb://";
  }

  public int getContentLength()
  {
    try
    {
      long l = length();
      return (int)(l & 0xFFFFFFFF);
    }
    catch (SmbException localSmbException)
    {
    }
    return 0;
  }

  public long getDate()
  {
    try
    {
      long l = lastModified();
      return l;
    }
    catch (SmbException localSmbException)
    {
    }
    return 0L;
  }

  public String getDfsPath()
    throws SmbException
  {
    resolveDfs(null);
    DfsReferral localDfsReferral = this.dfsReferral;
    String str = null;
    if (localDfsReferral == null);
    do
    {
      return str;
      str = ("smb:/" + this.dfsReferral.server + "/" + this.dfsReferral.share + this.unc).replace('\\', '/');
    }
    while (!isDirectory());
    return str + '/';
  }

  public long getDiskFreeSpace()
    throws SmbException
  {
    if ((getType() == 8) || (this.type == 1))
    {
      try
      {
        long l = queryFSInformation(1007);
        return l;
      }
      catch (SmbException localSmbException)
      {
        switch (localSmbException.getNtStatus())
        {
        case -1073741822:
        default:
          throw localSmbException;
        case -1073741823:
        case -1073741821:
        }
      }
      return queryFSInformation(1);
    }
    return 0L;
  }

  UniAddress getFirstAddress()
    throws UnknownHostException
  {
    this.addressIndex = 0;
    String str1 = this.url.getHost();
    String str2 = this.url.getPath();
    String str3 = this.url.getQuery();
    if (str3 != null)
    {
      String str4 = queryLookup(str3, "server");
      if ((str4 != null) && (str4.length() > 0))
      {
        this.addresses = new UniAddress[1];
        this.addresses[0] = UniAddress.getByName(str4);
        return getNextAddress();
      }
    }
    if (str1.length() == 0);
    while (true)
    {
      try
      {
        NbtAddress localNbtAddress = NbtAddress.getByName("\001\002__MSBROWSE__\002", 1, null);
        this.addresses = new UniAddress[1];
        this.addresses[0] = UniAddress.getByName(localNbtAddress.getHostAddress());
        return getNextAddress();
      }
      catch (UnknownHostException localUnknownHostException)
      {
        NtlmPasswordAuthentication.initDefaults();
        if (NtlmPasswordAuthentication.DEFAULT_DOMAIN.equals("?"))
          throw localUnknownHostException;
        this.addresses = UniAddress.getAllByName(NtlmPasswordAuthentication.DEFAULT_DOMAIN, true);
        continue;
      }
      if ((str2.length() == 0) || (str2.equals("/")))
        this.addresses = UniAddress.getAllByName(str1, true);
      else
        this.addresses = UniAddress.getAllByName(str1, false);
    }
  }

  public InputStream getInputStream()
    throws IOException
  {
    return new SmbFileInputStream(this);
  }

  public long getLastModified()
  {
    try
    {
      long l = lastModified();
      return l;
    }
    catch (SmbException localSmbException)
    {
    }
    return 0L;
  }

  public String getName()
  {
    getUncPath0();
    if (this.canon.length() > 1)
    {
      for (int i = -2 + this.canon.length(); this.canon.charAt(i) != '/'; i--);
      return this.canon.substring(i + 1);
    }
    if (this.share != null)
      return this.share + '/';
    if (this.url.getHost().length() > 0)
      return this.url.getHost() + '/';
    return "smb://";
  }

  UniAddress getNextAddress()
  {
    int i = this.addressIndex;
    int j = this.addresses.length;
    UniAddress localUniAddress = null;
    if (i < j)
    {
      UniAddress[] arrayOfUniAddress = this.addresses;
      int k = this.addressIndex;
      this.addressIndex = (k + 1);
      localUniAddress = arrayOfUniAddress[k];
    }
    return localUniAddress;
  }

  public OutputStream getOutputStream()
    throws IOException
  {
    return new SmbFileOutputStream(this);
  }

  public String getParent()
  {
    String str1 = this.url.getAuthority();
    if (str1.length() > 0)
    {
      StringBuffer localStringBuffer = new StringBuffer("smb://");
      localStringBuffer.append(str1);
      getUncPath0();
      if (this.canon.length() > 1)
        localStringBuffer.append(this.canon);
      String str2;
      int i;
      while (true)
      {
        str2 = localStringBuffer.toString();
        for (i = -2 + str2.length(); str2.charAt(i) != '/'; i--);
        localStringBuffer.append('/');
      }
      return str2.substring(0, i + 1);
    }
    return "smb://";
  }

  public String getPath()
  {
    return this.url.toString();
  }

  public Principal getPrincipal()
  {
    return this.auth;
  }

  public ACE[] getSecurity()
    throws IOException
  {
    return getSecurity(false);
  }

  public ACE[] getSecurity(boolean paramBoolean)
    throws IOException
  {
    int i;
    if (isDirectory())
      i = 1;
    while (true)
    {
      int j = open0(1, 131072, 0, i);
      NtTransQuerySecurityDesc localNtTransQuerySecurityDesc = new NtTransQuerySecurityDesc(j, 4);
      NtTransQuerySecurityDescResponse localNtTransQuerySecurityDescResponse = new NtTransQuerySecurityDescResponse();
      try
      {
        send(localNtTransQuerySecurityDesc, localNtTransQuerySecurityDescResponse);
        close(j, 0L);
        ACE[] arrayOfACE = localNtTransQuerySecurityDescResponse.securityDescriptor.aces;
        if (arrayOfACE != null);
        return arrayOfACE;
        i = 0;
      }
      finally
      {
        close(j, 0L);
      }
    }
  }

  public String getServer()
  {
    String str = this.url.getHost();
    if (str.length() == 0)
      str = null;
    return str;
  }

  String getServerWithDfs()
  {
    if (this.dfsReferral != null)
      return this.dfsReferral.server;
    return getServer();
  }

  public String getShare()
  {
    return this.share;
  }

  public ACE[] getShareSecurity(boolean paramBoolean)
    throws IOException
  {
    this.url.getPath();
    resolveDfs(null);
    String str = getServerWithDfs();
    MsrpcShareGetInfo localMsrpcShareGetInfo = new MsrpcShareGetInfo(str, this.tree.share);
    DcerpcHandle localDcerpcHandle = DcerpcHandle.getHandle("ncacn_np:" + str + "[\\PIPE\\srvsvc]", this.auth);
    try
    {
      localDcerpcHandle.sendrecv(localMsrpcShareGetInfo);
      if (localMsrpcShareGetInfo.retval != 0)
        throw new SmbException(localMsrpcShareGetInfo.retval, true);
    }
    finally
    {
      ACE[] arrayOfACE;
      try
      {
        localDcerpcHandle.close();
        throw localObject;
        arrayOfACE = localMsrpcShareGetInfo.getSecurity();
        if (arrayOfACE != null)
          processAces(arrayOfACE, paramBoolean);
      }
      catch (IOException localIOException1)
      {
        try
        {
          do
          {
            localDcerpcHandle.close();
            return arrayOfACE;
            localIOException1 = localIOException1;
          }
          while (LogStream.level < 1);
          localIOException1.printStackTrace(log);
        }
        catch (IOException localIOException2)
        {
          while (LogStream.level < 1);
          localIOException2.printStackTrace(log);
        }
      }
      return arrayOfACE;
    }
  }

  public int getType()
    throws SmbException
  {
    if (this.type == 0)
    {
      if (getUncPath0().length() <= 1)
        break label28;
      this.type = 1;
    }
    while (true)
    {
      return this.type;
      label28: if (this.share != null)
      {
        connect0();
        if (this.share.equals("IPC$"))
          this.type = 16;
        else if (this.tree.service.equals("LPT1:"))
          this.type = 32;
        else if (this.tree.service.equals("COMM"))
          this.type = 64;
        else
          this.type = 8;
      }
      else if ((this.url.getAuthority() == null) || (this.url.getAuthority().length() == 0))
      {
        this.type = 2;
      }
      else
      {
        try
        {
          UniAddress localUniAddress = getAddress();
          if ((localUniAddress.getAddress() instanceof NbtAddress))
          {
            int i = ((NbtAddress)localUniAddress.getAddress()).getNameType();
            if ((i == 29) || (i == 27))
            {
              this.type = 2;
              return this.type;
            }
          }
        }
        catch (UnknownHostException localUnknownHostException)
        {
          throw new SmbException(this.url.toString(), localUnknownHostException);
        }
        this.type = 4;
      }
    }
  }

  public String getUncPath()
  {
    getUncPath0();
    if (this.share == null)
      return "\\\\" + this.url.getHost();
    return "\\\\" + this.url.getHost() + this.canon.replace('/', '\\');
  }

  String getUncPath0()
  {
    char[] arrayOfChar2;
    label234: int n;
    int i1;
    if (this.unc == null)
    {
      char[] arrayOfChar1 = this.url.getPath().toCharArray();
      arrayOfChar2 = new char[arrayOfChar1.length];
      int i = arrayOfChar1.length;
      int j = 0;
      int k = 0;
      int m = 0;
      if (k < i)
      {
        int i2;
        switch (j)
        {
        default:
          i2 = m;
        case 0:
        case 1:
        case 2:
        }
        while (true)
        {
          k++;
          break label234;
          m = i2;
          break;
          if (arrayOfChar1[k] != '/')
            return null;
          i2 = m + 1;
          arrayOfChar2[m] = arrayOfChar1[k];
          j = 1;
          continue;
          if (arrayOfChar1[k] == '/')
          {
            i2 = m;
          }
          else if ((arrayOfChar1[k] == '.') && ((k + 1 >= i) || (arrayOfChar1[(k + 1)] == '/')))
          {
            k++;
            i2 = m;
          }
          else if ((k + 1 < i) && (arrayOfChar1[k] == '.') && (arrayOfChar1[(k + 1)] == '.') && ((k + 2 >= i) || (arrayOfChar1[(k + 2)] == '/')))
          {
            k += 2;
            if (m == 1)
            {
              i2 = m;
            }
            else
            {
              i2 = m;
              i2--;
              if (i2 > 1)
                if (arrayOfChar2[(i2 - 1)] == '/');
            }
          }
          else
          {
            j = 2;
            if (arrayOfChar1[k] == '/')
              j = 1;
            i2 = m + 1;
            arrayOfChar2[m] = arrayOfChar1[k];
          }
        }
      }
      this.canon = new String(arrayOfChar2, 0, m);
      if (m <= 1)
        break label452;
      n = m - 1;
      i1 = this.canon.indexOf('/', 1);
      if (i1 >= 0)
        break label356;
      this.share = this.canon.substring(1);
      this.unc = "\\";
    }
    label452: 
    while (true)
    {
      return this.unc;
      label356: if (i1 == n)
      {
        this.share = this.canon.substring(1, i1);
        this.unc = "\\";
      }
      else
      {
        this.share = this.canon.substring(1, i1);
        String str = this.canon;
        if (arrayOfChar2[n] == '/');
        while (true)
        {
          this.unc = str.substring(i1, n);
          this.unc = this.unc.replace('/', '\\');
          break;
          n++;
        }
        this.share = null;
        this.unc = "\\";
      }
    }
  }

  boolean hasNextAddress()
  {
    return this.addressIndex < this.addresses.length;
  }

  public int hashCode()
  {
    try
    {
      int j = getAddress().hashCode();
      i = j;
      getUncPath0();
      return i + this.canon.toUpperCase().hashCode();
    }
    catch (UnknownHostException localUnknownHostException)
    {
      while (true)
        int i = getServer().toUpperCase().hashCode();
    }
  }

  boolean isConnected()
  {
    return (this.tree != null) && (this.tree.treeConnected);
  }

  public boolean isDirectory()
    throws SmbException
  {
    if (getUncPath0().length() == 1);
    do
    {
      return true;
      if (!exists())
        return false;
    }
    while ((0x10 & this.attributes) == 16);
    return false;
  }

  public boolean isFile()
    throws SmbException
  {
    int i = 1;
    if (getUncPath0().length() == i)
      return false;
    exists();
    if ((0x10 & this.attributes) == 0);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public boolean isHidden()
    throws SmbException
  {
    int i = 1;
    if (this.share == null);
    do
    {
      return false;
      if (getUncPath0().length() != i)
        break;
    }
    while (!this.share.endsWith("$"));
    return i;
    exists();
    if ((0x2 & this.attributes) == 2);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  boolean isOpen()
  {
    return (this.opened) && (isConnected()) && (this.tree_num == this.tree.tree_num);
  }

  boolean isWorkgroup0()
    throws UnknownHostException
  {
    if ((this.type == 2) || (this.url.getHost().length() == 0))
    {
      this.type = 2;
      return true;
    }
    getUncPath0();
    if (this.share == null)
    {
      UniAddress localUniAddress = getAddress();
      if ((localUniAddress.getAddress() instanceof NbtAddress))
      {
        int i = ((NbtAddress)localUniAddress.getAddress()).getNameType();
        if ((i == 29) || (i == 27))
        {
          this.type = 2;
          return true;
        }
      }
      this.type = 4;
    }
    return false;
  }

  public long lastModified()
    throws SmbException
  {
    if (getUncPath0().length() > 1)
    {
      exists();
      return this.lastModified;
    }
    return 0L;
  }

  public long length()
    throws SmbException
  {
    if (this.sizeExpiration > System.currentTimeMillis())
      return this.size;
    if (getType() == 8)
    {
      Trans2QueryFSInformationResponse localTrans2QueryFSInformationResponse = new Trans2QueryFSInformationResponse(1);
      send(new Trans2QueryFSInformation(1), localTrans2QueryFSInformationResponse);
      this.size = localTrans2QueryFSInformationResponse.info.getCapacity();
    }
    while (true)
    {
      this.sizeExpiration = (System.currentTimeMillis() + attrExpirationPeriod);
      return this.size;
      if ((getUncPath0().length() > 1) && (this.type != 16))
        this.size = queryPath(getUncPath0(), 258).getSize();
      else
        this.size = 0L;
    }
  }

  public String[] list()
    throws SmbException
  {
    return list("*", 22, null, null);
  }

  String[] list(String paramString, int paramInt, SmbFilenameFilter paramSmbFilenameFilter, SmbFileFilter paramSmbFileFilter)
    throws SmbException
  {
    ArrayList localArrayList = new ArrayList();
    doEnum(localArrayList, false, paramString, paramInt, paramSmbFilenameFilter, paramSmbFileFilter);
    return (String[])localArrayList.toArray(new String[localArrayList.size()]);
  }

  public String[] list(SmbFilenameFilter paramSmbFilenameFilter)
    throws SmbException
  {
    return list("*", 22, paramSmbFilenameFilter, null);
  }

  public SmbFile[] listFiles()
    throws SmbException
  {
    return listFiles("*", 22, null, null);
  }

  public SmbFile[] listFiles(String paramString)
    throws SmbException
  {
    return listFiles(paramString, 22, null, null);
  }

  SmbFile[] listFiles(String paramString, int paramInt, SmbFilenameFilter paramSmbFilenameFilter, SmbFileFilter paramSmbFileFilter)
    throws SmbException
  {
    ArrayList localArrayList = new ArrayList();
    doEnum(localArrayList, true, paramString, paramInt, paramSmbFilenameFilter, paramSmbFileFilter);
    return (SmbFile[])localArrayList.toArray(new SmbFile[localArrayList.size()]);
  }

  public SmbFile[] listFiles(SmbFileFilter paramSmbFileFilter)
    throws SmbException
  {
    return listFiles("*", 22, null, paramSmbFileFilter);
  }

  public SmbFile[] listFiles(SmbFilenameFilter paramSmbFilenameFilter)
    throws SmbException
  {
    return listFiles("*", 22, paramSmbFilenameFilter, null);
  }

  public void mkdir()
    throws SmbException
  {
    String str = getUncPath0();
    if (str.length() == 1)
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    if (LogStream.level >= 3)
      log.println("mkdir: " + str);
    send(new SmbComCreateDirectory(str), blank_resp());
    this.sizeExpiration = 0L;
    this.attrExpiration = 0L;
  }

  public void mkdirs()
    throws SmbException
  {
    try
    {
      SmbFile localSmbFile = new SmbFile(getParent(), this.auth);
      if (!localSmbFile.exists())
        localSmbFile.mkdirs();
      mkdir();
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  void open(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SmbException
  {
    if (isOpen())
      return;
    this.fid = open0(paramInt1, paramInt2, paramInt3, paramInt4);
    this.opened = true;
    this.tree_num = this.tree.tree_num;
  }

  int open0(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SmbException
  {
    connect0();
    if (LogStream.level >= 3)
      log.println("open0: " + this.unc);
    if (this.tree.session.transport.hasCapability(16))
    {
      SmbComNTCreateAndXResponse localSmbComNTCreateAndXResponse = new SmbComNTCreateAndXResponse();
      SmbComNTCreateAndX localSmbComNTCreateAndX = new SmbComNTCreateAndX(this.unc, paramInt1, paramInt2, this.shareAccess, paramInt3, paramInt4, null);
      if ((this instanceof SmbNamedPipe))
      {
        localSmbComNTCreateAndX.flags0 = (0x16 | localSmbComNTCreateAndX.flags0);
        localSmbComNTCreateAndX.desiredAccess = (0x20000 | localSmbComNTCreateAndX.desiredAccess);
        localSmbComNTCreateAndXResponse.isExtended = true;
      }
      send(localSmbComNTCreateAndX, localSmbComNTCreateAndXResponse);
      int i = localSmbComNTCreateAndXResponse.fid;
      this.attributes = (0x7FFF & localSmbComNTCreateAndXResponse.extFileAttributes);
      this.attrExpiration = (System.currentTimeMillis() + attrExpirationPeriod);
      this.isExists = true;
      return i;
    }
    SmbComOpenAndXResponse localSmbComOpenAndXResponse = new SmbComOpenAndXResponse();
    send(new SmbComOpenAndX(this.unc, paramInt2, paramInt1, null), localSmbComOpenAndXResponse);
    return localSmbComOpenAndXResponse.fid;
  }

  protected boolean pathNamesPossiblyEqual(String paramString1, String paramString2)
  {
    int i = paramString1.lastIndexOf('/');
    int j = paramString2.lastIndexOf('/');
    int k = paramString1.length() - i;
    int m = paramString2.length() - j;
    if ((k > 1) && (paramString1.charAt(i + 1) == '.'));
    while (((m > 1) && (paramString2.charAt(j + 1) == '.')) || ((k == m) && (paramString1.regionMatches(true, i, paramString2, j, k))))
      return true;
    return false;
  }

  Info queryPath(String paramString, int paramInt)
    throws SmbException
  {
    connect0();
    if (LogStream.level >= 3)
      log.println("queryPath: " + paramString);
    if (this.tree.session.transport.hasCapability(16))
    {
      Trans2QueryPathInformationResponse localTrans2QueryPathInformationResponse = new Trans2QueryPathInformationResponse(paramInt);
      send(new Trans2QueryPathInformation(paramString, paramInt), localTrans2QueryPathInformationResponse);
      return localTrans2QueryPathInformationResponse.info;
    }
    SmbComQueryInformationResponse localSmbComQueryInformationResponse = new SmbComQueryInformationResponse(60L * (1000 * this.tree.session.transport.server.serverTimeZone));
    send(new SmbComQueryInformation(paramString), localSmbComQueryInformationResponse);
    return localSmbComQueryInformationResponse;
  }

  public void renameTo(SmbFile paramSmbFile)
    throws SmbException
  {
    if ((getUncPath0().length() == 1) || (paramSmbFile.getUncPath0().length() == 1))
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    resolveDfs(null);
    paramSmbFile.resolveDfs(null);
    if (!this.tree.equals(paramSmbFile.tree))
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    if (LogStream.level >= 3)
      log.println("renameTo: " + this.unc + " -> " + paramSmbFile.unc);
    this.sizeExpiration = 0L;
    this.attrExpiration = 0L;
    paramSmbFile.attrExpiration = 0L;
    send(new SmbComRename(this.unc, paramSmbFile.unc), blank_resp());
  }

  void resolveDfs(ServerMessageBlock paramServerMessageBlock)
    throws SmbException
  {
    connect0();
    String str1 = getServerWithDfs();
    DfsReferral localDfsReferral = dfs.resolve(str1, this.tree.share, this.unc, this.auth);
    if (localDfsReferral != null);
    do
    {
      try
      {
        UniAddress localUniAddress = UniAddress.getByName(localDfsReferral.server);
        this.tree = SmbTransport.getSmbTransport(localUniAddress, this.url.getPort()).getSmbSession(this.auth).getSmbTree(localDfsReferral.share, null);
        if (LogStream.level >= 3)
          log.println(localDfsReferral);
        this.dfsReferral = localDfsReferral;
        String str2 = this.unc.substring(localDfsReferral.pathConsumed);
        if (str2.equals(""))
          str2 = "\\";
        if (!localDfsReferral.path.equals(""))
          str2 = "\\" + localDfsReferral.path + str2;
        this.unc = str2;
        if ((paramServerMessageBlock != null) && (paramServerMessageBlock.path != null) && (paramServerMessageBlock.path.endsWith("\\")) && (!str2.endsWith("\\")))
          str2 = str2 + "\\";
        if (paramServerMessageBlock != null)
        {
          paramServerMessageBlock.path = str2;
          paramServerMessageBlock.flags2 = (0x1000 | paramServerMessageBlock.flags2);
        }
        return;
      }
      catch (UnknownHostException localUnknownHostException)
      {
        throw new SmbException(localDfsReferral.server, localUnknownHostException);
      }
      if ((this.tree.inDomainDfs) && (!(paramServerMessageBlock instanceof NtTransQuerySecurityDesc)) && (!(paramServerMessageBlock instanceof SmbComClose)) && (!(paramServerMessageBlock instanceof SmbComFindClose2)))
        throw new SmbException(-1073741275, false);
    }
    while (paramServerMessageBlock == null);
    paramServerMessageBlock.flags2 = (0xFFFFEFFF & paramServerMessageBlock.flags2);
  }

  void send(ServerMessageBlock paramServerMessageBlock1, ServerMessageBlock paramServerMessageBlock2)
    throws SmbException
  {
    while (true)
    {
      resolveDfs(paramServerMessageBlock1);
      try
      {
        this.tree.send(paramServerMessageBlock1, paramServerMessageBlock2);
        return;
      }
      catch (DfsReferral localDfsReferral)
      {
        if (localDfsReferral.resolveHashes)
          throw localDfsReferral;
        paramServerMessageBlock1.reset();
      }
    }
  }

  public void setAttributes(int paramInt)
    throws SmbException
  {
    if (getUncPath0().length() == 1)
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    setPathInformation(paramInt & 0x30A7, 0L, 0L);
  }

  public void setCreateTime(long paramLong)
    throws SmbException
  {
    if (getUncPath0().length() == 1)
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    setPathInformation(0, paramLong, 0L);
  }

  public void setLastModified(long paramLong)
    throws SmbException
  {
    if (getUncPath0().length() == 1)
      throw new SmbException("Invalid operation for workgroups, servers, or shares");
    setPathInformation(0, 0L, paramLong);
  }

  void setPathInformation(int paramInt, long paramLong1, long paramLong2)
    throws SmbException
  {
    exists();
    int i = 0x10 & this.attributes;
    if (i != 0);
    for (int j = 1; ; j = 64)
    {
      int k = open0(1, 256, i, j);
      send(new Trans2SetFileInformation(k, paramInt | i, paramLong1, paramLong2), new Trans2SetFileInformationResponse());
      close(k, 0L);
      this.attrExpiration = 0L;
      return;
    }
  }

  public void setReadOnly()
    throws SmbException
  {
    setAttributes(0x1 | getAttributes());
  }

  public void setReadWrite()
    throws SmbException
  {
    setAttributes(0xFFFFFFFE & getAttributes());
  }

  public String toString()
  {
    return this.url.toString();
  }

  public URL toURL()
    throws MalformedURLException
  {
    return this.url;
  }

  class WriterThread extends Thread
  {
    byte[] b;
    SmbFile dest;
    SmbException e = null;
    int n;
    int off;
    boolean ready;
    SmbComWrite req;
    SmbComWriteAndX reqx;
    ServerMessageBlock resp;
    boolean useNTSmbs = SmbFile.this.tree.session.transport.hasCapability(16);

    WriterThread()
      throws SmbException
    {
      super();
      if (this.useNTSmbs)
        this.reqx = new SmbComWriteAndX();
      for (this.resp = new SmbComWriteAndXResponse(); ; this.resp = new SmbComWriteResponse())
      {
        this.ready = false;
        return;
        this.req = new SmbComWrite();
      }
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: invokevirtual 92	java/lang/Object:notify	()V
      //   6: aload_0
      //   7: iconst_1
      //   8: putfield 76	jcifs/smb/SmbFile$WriterThread:ready	Z
      //   11: aload_0
      //   12: getfield 76	jcifs/smb/SmbFile$WriterThread:ready	Z
      //   15: ifeq +23 -> 38
      //   18: aload_0
      //   19: invokevirtual 95	java/lang/Object:wait	()V
      //   22: goto -11 -> 11
      //   25: astore_3
      //   26: aload_0
      //   27: aload_3
      //   28: putfield 36	jcifs/smb/SmbFile$WriterThread:e	Ljcifs/smb/SmbException;
      //   31: aload_0
      //   32: invokevirtual 92	java/lang/Object:notify	()V
      //   35: aload_0
      //   36: monitorexit
      //   37: return
      //   38: aload_0
      //   39: getfield 97	jcifs/smb/SmbFile$WriterThread:n	I
      //   42: istore 4
      //   44: iload 4
      //   46: iconst_m1
      //   47: if_icmpne +11 -> 58
      //   50: aload_0
      //   51: monitorexit
      //   52: return
      //   53: astore_2
      //   54: aload_0
      //   55: monitorexit
      //   56: aload_2
      //   57: athrow
      //   58: aload_0
      //   59: getfield 62	jcifs/smb/SmbFile$WriterThread:useNTSmbs	Z
      //   62: ifeq +71 -> 133
      //   65: aload_0
      //   66: getfield 69	jcifs/smb/SmbFile$WriterThread:reqx	Ljcifs/smb/SmbComWriteAndX;
      //   69: aload_0
      //   70: getfield 99	jcifs/smb/SmbFile$WriterThread:dest	Ljcifs/smb/SmbFile;
      //   73: getfield 102	jcifs/smb/SmbFile:fid	I
      //   76: aload_0
      //   77: getfield 104	jcifs/smb/SmbFile$WriterThread:off	I
      //   80: i2l
      //   81: aload_0
      //   82: getfield 97	jcifs/smb/SmbFile$WriterThread:n	I
      //   85: aload_0
      //   86: getfield 106	jcifs/smb/SmbFile$WriterThread:b	[B
      //   89: iconst_0
      //   90: aload_0
      //   91: getfield 97	jcifs/smb/SmbFile$WriterThread:n	I
      //   94: invokevirtual 110	jcifs/smb/SmbComWriteAndX:setParam	(IJI[BII)V
      //   97: aload_0
      //   98: getfield 99	jcifs/smb/SmbFile$WriterThread:dest	Ljcifs/smb/SmbFile;
      //   101: aload_0
      //   102: getfield 69	jcifs/smb/SmbFile$WriterThread:reqx	Ljcifs/smb/SmbComWriteAndX;
      //   105: aload_0
      //   106: getfield 74	jcifs/smb/SmbFile$WriterThread:resp	Ljcifs/smb/ServerMessageBlock;
      //   109: invokevirtual 114	jcifs/smb/SmbFile:send	(Ljcifs/smb/ServerMessageBlock;Ljcifs/smb/ServerMessageBlock;)V
      //   112: goto -110 -> 2
      //   115: astore_1
      //   116: aload_0
      //   117: new 27	jcifs/smb/SmbException
      //   120: dup
      //   121: ldc 116
      //   123: aload_1
      //   124: invokespecial 119	jcifs/smb/SmbException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   127: putfield 36	jcifs/smb/SmbFile$WriterThread:e	Ljcifs/smb/SmbException;
      //   130: goto -99 -> 31
      //   133: aload_0
      //   134: getfield 81	jcifs/smb/SmbFile$WriterThread:req	Ljcifs/smb/SmbComWrite;
      //   137: aload_0
      //   138: getfield 99	jcifs/smb/SmbFile$WriterThread:dest	Ljcifs/smb/SmbFile;
      //   141: getfield 102	jcifs/smb/SmbFile:fid	I
      //   144: aload_0
      //   145: getfield 104	jcifs/smb/SmbFile$WriterThread:off	I
      //   148: i2l
      //   149: aload_0
      //   150: getfield 97	jcifs/smb/SmbFile$WriterThread:n	I
      //   153: aload_0
      //   154: getfield 106	jcifs/smb/SmbFile$WriterThread:b	[B
      //   157: iconst_0
      //   158: aload_0
      //   159: getfield 97	jcifs/smb/SmbFile$WriterThread:n	I
      //   162: invokevirtual 120	jcifs/smb/SmbComWrite:setParam	(IJI[BII)V
      //   165: aload_0
      //   166: getfield 99	jcifs/smb/SmbFile$WriterThread:dest	Ljcifs/smb/SmbFile;
      //   169: aload_0
      //   170: getfield 81	jcifs/smb/SmbFile$WriterThread:req	Ljcifs/smb/SmbComWrite;
      //   173: aload_0
      //   174: getfield 74	jcifs/smb/SmbFile$WriterThread:resp	Ljcifs/smb/ServerMessageBlock;
      //   177: invokevirtual 114	jcifs/smb/SmbFile:send	(Ljcifs/smb/ServerMessageBlock;Ljcifs/smb/ServerMessageBlock;)V
      //   180: goto -178 -> 2
      //
      // Exception table:
      //   from	to	target	type
      //   2	11	25	jcifs/smb/SmbException
      //   11	22	25	jcifs/smb/SmbException
      //   38	44	25	jcifs/smb/SmbException
      //   58	112	25	jcifs/smb/SmbException
      //   133	180	25	jcifs/smb/SmbException
      //   2	11	53	finally
      //   11	22	53	finally
      //   26	31	53	finally
      //   31	37	53	finally
      //   38	44	53	finally
      //   50	52	53	finally
      //   54	56	53	finally
      //   58	112	53	finally
      //   116	130	53	finally
      //   133	180	53	finally
      //   2	11	115	java/lang/Exception
      //   11	22	115	java/lang/Exception
      //   38	44	115	java/lang/Exception
      //   58	112	115	java/lang/Exception
      //   133	180	115	java/lang/Exception
    }

    void write(byte[] paramArrayOfByte, int paramInt1, SmbFile paramSmbFile, int paramInt2)
    {
      try
      {
        this.b = paramArrayOfByte;
        this.n = paramInt1;
        this.dest = paramSmbFile;
        this.off = paramInt2;
        this.ready = false;
        notify();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
}