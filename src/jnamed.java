import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.xbill.DNS.Address;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.DNAMERecord;
import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.OPTRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;
import org.xbill.DNS.SetResponse;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TSIGRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.Zone;
import org.xbill.DNS.ZoneTransferException;

public class jnamed
{
  static final int FLAG_DNSSECOK = 1;
  static final int FLAG_SIGONLY = 2;
  Map TSIGs;
  Map caches;
  Map znames;

  public jnamed(String paramString)
    throws IOException, ZoneTransferException
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    FileInputStream localFileInputStream;
    while (true)
    {
      StringTokenizer localStringTokenizer;
      String str2;
      try
      {
        localFileInputStream = new FileInputStream(paramString);
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream));
        try
        {
          this.caches = new HashMap();
          this.znames = new HashMap();
          this.TSIGs = new HashMap();
          String str1 = localBufferedReader.readLine();
          if (str1 == null)
            break;
          localStringTokenizer = new StringTokenizer(str1);
          if (!localStringTokenizer.hasMoreTokens())
            continue;
          str2 = localStringTokenizer.nextToken();
          if (!localStringTokenizer.hasMoreTokens())
          {
            System.out.println("Invalid line: " + str1);
            continue;
          }
        }
        finally
        {
          localFileInputStream.close();
        }
      }
      catch (Exception localException)
      {
        System.out.println("Cannot open " + paramString);
        return;
      }
      if (str2.charAt(0) != '#')
        if (str2.equals("primary"))
        {
          addPrimaryZone(localStringTokenizer.nextToken(), localStringTokenizer.nextToken());
        }
        else if (str2.equals("secondary"))
        {
          addSecondaryZone(localStringTokenizer.nextToken(), localStringTokenizer.nextToken());
        }
        else if (str2.equals("cache"))
        {
          Cache localCache = new Cache(localStringTokenizer.nextToken());
          this.caches.put(new Integer(1), localCache);
        }
        else if (str2.equals("key"))
        {
          String str3 = localStringTokenizer.nextToken();
          String str4 = localStringTokenizer.nextToken();
          if (localStringTokenizer.hasMoreTokens())
            addTSIG(str3, str4, localStringTokenizer.nextToken());
          else
            addTSIG("hmac-md5", str3, str4);
        }
        else if (str2.equals("port"))
        {
          localArrayList1.add(Integer.valueOf(localStringTokenizer.nextToken()));
        }
        else if (str2.equals("address"))
        {
          localArrayList2.add(Address.getByAddress(localStringTokenizer.nextToken()));
        }
        else
        {
          System.out.println("unknown keyword: " + str2);
        }
    }
    if (localArrayList1.size() == 0)
      localArrayList1.add(new Integer(53));
    if (localArrayList2.size() == 0)
      localArrayList2.add(Address.getByAddress("0.0.0.0"));
    Iterator localIterator1 = localArrayList2.iterator();
    while (localIterator1.hasNext())
    {
      InetAddress localInetAddress = (InetAddress)localIterator1.next();
      Iterator localIterator2 = localArrayList1.iterator();
      while (localIterator2.hasNext())
      {
        int i = ((Integer)localIterator2.next()).intValue();
        addUDP(localInetAddress, i);
        addTCP(localInetAddress, i);
        System.out.println("jnamed: listening on " + addrport(localInetAddress, i));
      }
    }
    System.out.println("jnamed: running");
    localFileInputStream.close();
  }

  private final void addAdditional(Message paramMessage, int paramInt)
  {
    addAdditional2(paramMessage, 1, paramInt);
    addAdditional2(paramMessage, 2, paramInt);
  }

  private void addAdditional2(Message paramMessage, int paramInt1, int paramInt2)
  {
    Record[] arrayOfRecord = paramMessage.getSectionArray(paramInt1);
    for (int i = 0; i < arrayOfRecord.length; i++)
    {
      Name localName = arrayOfRecord[i].getAdditionalName();
      if (localName != null)
        addGlue(paramMessage, localName, paramInt2);
    }
  }

  private final void addCacheNS(Message paramMessage, Cache paramCache, Name paramName)
  {
    SetResponse localSetResponse = paramCache.lookupRecords(paramName, 2, 0);
    if (!localSetResponse.isDelegation());
    while (true)
    {
      return;
      Iterator localIterator = localSetResponse.getNS().rrs();
      while (localIterator.hasNext())
        paramMessage.addRecord((Record)localIterator.next(), 2);
    }
  }

  private void addGlue(Message paramMessage, Name paramName, int paramInt)
  {
    RRset localRRset = findExactMatch(paramName, 1, 1, true);
    if (localRRset == null)
      return;
    addRRset(paramName, paramMessage, localRRset, 3, paramInt);
  }

  private final void addNS(Message paramMessage, Zone paramZone, int paramInt)
  {
    RRset localRRset = paramZone.getNS();
    addRRset(localRRset.getName(), paramMessage, localRRset, 2, paramInt);
  }

  private final void addSOA(Message paramMessage, Zone paramZone)
  {
    paramMessage.addRecord(paramZone.getSOA(), 2);
  }

  private static String addrport(InetAddress paramInetAddress, int paramInt)
  {
    return paramInetAddress.getHostAddress() + "#" + paramInt;
  }

  public static void main(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length > 1)
    {
      System.out.println("usage: jnamed [conf]");
      System.exit(0);
    }
    try
    {
      if (paramArrayOfString.length == 1);
      for (String str = paramArrayOfString[0]; ; str = "jnamed.conf")
      {
        new jnamed(str);
        return;
      }
    }
    catch (IOException localIOException)
    {
      System.out.println(localIOException);
      return;
    }
    catch (ZoneTransferException localZoneTransferException)
    {
      System.out.println(localZoneTransferException);
    }
  }

  // ERROR //
  public void TCPclient(Socket paramSocket)
  {
    // Byte code:
    //   0: new 290	java/io/DataInputStream
    //   3: dup
    //   4: aload_1
    //   5: invokevirtual 296	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   8: invokespecial 297	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   11: astore_2
    //   12: aload_2
    //   13: invokevirtual 300	java/io/DataInputStream:readUnsignedShort	()I
    //   16: newarray byte
    //   18: astore 7
    //   20: aload_2
    //   21: aload 7
    //   23: invokevirtual 304	java/io/DataInputStream:readFully	([B)V
    //   26: aload_0
    //   27: new 205	org/xbill/DNS/Message
    //   30: dup
    //   31: aload 7
    //   33: invokespecial 306	org/xbill/DNS/Message:<init>	([B)V
    //   36: aload 7
    //   38: aload 7
    //   40: arraylength
    //   41: aload_1
    //   42: invokevirtual 310	jnamed:generateReply	(Lorg/xbill/DNS/Message;[BILjava/net/Socket;)[B
    //   45: astore 12
    //   47: aload 12
    //   49: astore 9
    //   51: aload 9
    //   53: ifnonnull +18 -> 71
    //   56: aload_1
    //   57: invokevirtual 311	java/net/Socket:close	()V
    //   60: return
    //   61: astore 8
    //   63: aload_0
    //   64: aload 7
    //   66: invokevirtual 315	jnamed:formerrMessage	([B)[B
    //   69: astore 9
    //   71: new 317	java/io/DataOutputStream
    //   74: dup
    //   75: aload_1
    //   76: invokevirtual 321	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   79: invokespecial 324	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   82: astore 10
    //   84: aload 10
    //   86: aload 9
    //   88: arraylength
    //   89: invokevirtual 327	java/io/DataOutputStream:writeShort	(I)V
    //   92: aload 10
    //   94: aload 9
    //   96: invokevirtual 330	java/io/DataOutputStream:write	([B)V
    //   99: aload_1
    //   100: invokevirtual 311	java/net/Socket:close	()V
    //   103: return
    //   104: astore 11
    //   106: return
    //   107: astore 5
    //   109: getstatic 70	java/lang/System:out	Ljava/io/PrintStream;
    //   112: new 72	java/lang/StringBuffer
    //   115: dup
    //   116: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   119: ldc_w 332
    //   122: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   125: aload_1
    //   126: invokevirtual 336	java/net/Socket:getLocalAddress	()Ljava/net/InetAddress;
    //   129: aload_1
    //   130: invokevirtual 339	java/net/Socket:getLocalPort	()I
    //   133: invokestatic 195	jnamed:addrport	(Ljava/net/InetAddress;I)Ljava/lang/String;
    //   136: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   139: ldc_w 341
    //   142: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   145: aload 5
    //   147: invokevirtual 344	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
    //   150: invokevirtual 82	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   153: invokevirtual 87	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   156: aload_1
    //   157: invokevirtual 311	java/net/Socket:close	()V
    //   160: return
    //   161: astore 6
    //   163: return
    //   164: astore_3
    //   165: aload_1
    //   166: invokevirtual 311	java/net/Socket:close	()V
    //   169: aload_3
    //   170: athrow
    //   171: astore 13
    //   173: return
    //   174: astore 4
    //   176: goto -7 -> 169
    //
    // Exception table:
    //   from	to	target	type
    //   26	47	61	java/io/IOException
    //   99	103	104	java/io/IOException
    //   0	26	107	java/io/IOException
    //   63	71	107	java/io/IOException
    //   71	99	107	java/io/IOException
    //   156	160	161	java/io/IOException
    //   0	26	164	finally
    //   26	47	164	finally
    //   63	71	164	finally
    //   71	99	164	finally
    //   109	156	164	finally
    //   56	60	171	java/io/IOException
    //   165	169	174	java/io/IOException
  }

  byte addAnswer(Message paramMessage, Name paramName, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt3 > 6)
      return 0;
    if ((paramInt1 == 24) || (paramInt1 == 46))
    {
      paramInt1 = 255;
      paramInt4 |= 2;
    }
    Zone localZone = findBestZone(paramName);
    SetResponse localSetResponse;
    byte b;
    if (localZone != null)
    {
      localSetResponse = localZone.findRecords(paramName, paramInt1);
      if (localSetResponse.isUnknown())
        addCacheNS(paramMessage, getCache(paramInt2), paramName);
      if (!localSetResponse.isNXDOMAIN())
        break label136;
      paramMessage.getHeader().setRcode(3);
      if (localZone != null)
      {
        addSOA(paramMessage, localZone);
        if (paramInt3 == 0)
          paramMessage.getHeader().setFlag(5);
      }
      b = 3;
    }
    while (true)
    {
      return b;
      localSetResponse = getCache(paramInt2).lookupRecords(paramName, paramInt1, 3);
      break;
      label136: if (localSetResponse.isNXRRSET())
      {
        b = 0;
        if (localZone != null)
        {
          addSOA(paramMessage, localZone);
          b = 0;
          if (paramInt3 == 0)
          {
            paramMessage.getHeader().setFlag(5);
            b = 0;
          }
        }
      }
      else if (localSetResponse.isDelegation())
      {
        RRset localRRset = localSetResponse.getNS();
        addRRset(localRRset.getName(), paramMessage, localRRset, 2, paramInt4);
        b = 0;
      }
      else if (localSetResponse.isCNAME())
      {
        CNAMERecord localCNAMERecord = localSetResponse.getCNAME();
        addRRset(paramName, paramMessage, new RRset(localCNAMERecord), 1, paramInt4);
        if ((localZone != null) && (paramInt3 == 0))
          paramMessage.getHeader().setFlag(5);
        Name localName2 = localCNAMERecord.getTarget();
        int m = paramInt3 + 1;
        b = addAnswer(paramMessage, localName2, paramInt1, paramInt2, m, paramInt4);
      }
      else if (localSetResponse.isDNAME())
      {
        DNAMERecord localDNAMERecord = localSetResponse.getDNAME();
        addRRset(paramName, paramMessage, new RRset(localDNAMERecord), 1, paramInt4);
        try
        {
          Name localName1 = paramName.fromDNAME(localDNAMERecord);
          addRRset(paramName, paramMessage, new RRset(new CNAMERecord(paramName, paramInt2, 0L, localName1)), 1, paramInt4);
          if ((localZone != null) && (paramInt3 == 0))
            paramMessage.getHeader().setFlag(5);
          int k = paramInt3 + 1;
          b = addAnswer(paramMessage, localName1, paramInt1, paramInt2, k, paramInt4);
        }
        catch (NameTooLongException localNameTooLongException)
        {
          return 6;
        }
      }
      else
      {
        boolean bool = localSetResponse.isSuccessful();
        b = 0;
        if (bool)
        {
          RRset[] arrayOfRRset = localSetResponse.answers();
          for (int i = 0; ; i++)
          {
            int j = arrayOfRRset.length;
            if (i >= j)
              break;
            addRRset(paramName, paramMessage, arrayOfRRset[i], 1, paramInt4);
          }
          if (localZone != null)
          {
            addNS(paramMessage, localZone, paramInt4);
            b = 0;
            if (paramInt3 == 0)
            {
              paramMessage.getHeader().setFlag(5);
              b = 0;
            }
          }
          else
          {
            addCacheNS(paramMessage, getCache(paramInt2), paramName);
            b = 0;
          }
        }
      }
    }
  }

  public void addPrimaryZone(String paramString1, String paramString2)
    throws IOException
  {
    Name localName = null;
    if (paramString1 != null)
      localName = Name.fromString(paramString1, Name.root);
    Zone localZone = new Zone(localName, paramString2);
    this.znames.put(localZone.getOrigin(), localZone);
  }

  void addRRset(Name paramName, Message paramMessage, RRset paramRRset, int paramInt1, int paramInt2)
  {
    int i = 1;
    if (i <= paramInt1)
      if (!paramMessage.findRRset(paramName, paramRRset.getType(), i));
    while (true)
    {
      return;
      i++;
      break;
      if ((paramInt2 & 0x2) == 0)
      {
        Iterator localIterator2 = paramRRset.rrs();
        while (localIterator2.hasNext())
        {
          Record localRecord2 = (Record)localIterator2.next();
          if ((localRecord2.getName().isWild()) && (!paramName.isWild()))
            localRecord2 = localRecord2.withName(paramName);
          paramMessage.addRecord(localRecord2, paramInt1);
        }
      }
      if ((paramInt2 & 0x3) != 0)
      {
        Iterator localIterator1 = paramRRset.sigs();
        while (localIterator1.hasNext())
        {
          Record localRecord1 = (Record)localIterator1.next();
          if ((localRecord1.getName().isWild()) && (!paramName.isWild()))
            localRecord1 = localRecord1.withName(paramName);
          paramMessage.addRecord(localRecord1, paramInt1);
        }
      }
    }
  }

  public void addSecondaryZone(String paramString1, String paramString2)
    throws IOException, ZoneTransferException
  {
    Name localName = Name.fromString(paramString1, Name.root);
    Zone localZone = new Zone(localName, 1, paramString2);
    this.znames.put(localName, localZone);
  }

  public void addTCP(InetAddress paramInetAddress, int paramInt)
  {
    new Thread(new jnamed.2(this, paramInetAddress, paramInt)).start();
  }

  public void addTSIG(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    Name localName = Name.fromString(paramString2, Name.root);
    this.TSIGs.put(localName, new TSIG(paramString1, paramString2, paramString3));
  }

  public void addUDP(InetAddress paramInetAddress, int paramInt)
  {
    new Thread(new jnamed.3(this, paramInetAddress, paramInt)).start();
  }

  byte[] buildErrorMessage(Header paramHeader, int paramInt, Record paramRecord)
  {
    Message localMessage = new Message();
    localMessage.setHeader(paramHeader);
    for (int i = 0; i < 4; i++)
      localMessage.removeAllRecords(i);
    if (paramInt == 2)
      localMessage.addRecord(paramRecord, 0);
    paramHeader.setRcode(paramInt);
    return localMessage.toWire();
  }

  byte[] doAXFR(Name paramName, Message paramMessage, TSIG paramTSIG, TSIGRecord paramTSIGRecord, Socket paramSocket)
  {
    Zone localZone = (Zone)this.znames.get(paramName);
    boolean bool = true;
    if (localZone == null)
      return errorMessage(paramMessage, 5);
    Iterator localIterator = localZone.AXFR();
    try
    {
      DataOutputStream localDataOutputStream = new DataOutputStream(paramSocket.getOutputStream());
      int i = paramMessage.getHeader().getID();
      while (localIterator.hasNext())
      {
        RRset localRRset = (RRset)localIterator.next();
        Message localMessage = new Message(i);
        Header localHeader = localMessage.getHeader();
        localHeader.setFlag(0);
        localHeader.setFlag(5);
        addRRset(localRRset.getName(), localMessage, localRRset, 1, 1);
        if (paramTSIG != null)
        {
          paramTSIG.applyStream(localMessage, paramTSIGRecord, bool);
          paramTSIGRecord = localMessage.getTSIG();
        }
        byte[] arrayOfByte = localMessage.toWire();
        localDataOutputStream.writeShort(arrayOfByte.length);
        localDataOutputStream.write(arrayOfByte);
        bool = false;
      }
    }
    catch (IOException localIOException1)
    {
      System.out.println("AXFR failed");
    }
    try
    {
      paramSocket.close();
      label192: return null;
    }
    catch (IOException localIOException2)
    {
      break label192;
    }
  }

  public byte[] errorMessage(Message paramMessage, int paramInt)
  {
    return buildErrorMessage(paramMessage.getHeader(), paramInt, paramMessage.getQuestion());
  }

  public Zone findBestZone(Name paramName)
  {
    Zone localZone1 = (Zone)this.znames.get(paramName);
    if (localZone1 != null)
      return localZone1;
    int i = paramName.labels();
    for (int j = 1; j < i; j++)
    {
      Name localName = new Name(paramName, j);
      Zone localZone2 = (Zone)this.znames.get(localName);
      if (localZone2 != null)
        return localZone2;
    }
    return null;
  }

  public RRset findExactMatch(Name paramName, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Zone localZone = findBestZone(paramName);
    if (localZone != null)
      return localZone.findExactMatch(paramName, paramInt1);
    Cache localCache = getCache(paramInt2);
    if (paramBoolean);
    for (RRset[] arrayOfRRset = localCache.findAnyRecords(paramName, paramInt1); arrayOfRRset == null; arrayOfRRset = localCache.findRecords(paramName, paramInt1))
      return null;
    return arrayOfRRset[0];
  }

  public byte[] formerrMessage(byte[] paramArrayOfByte)
  {
    try
    {
      Header localHeader = new Header(paramArrayOfByte);
      return buildErrorMessage(localHeader, 1, null);
    }
    catch (IOException localIOException)
    {
    }
    return null;
  }

  byte[] generateReply(Message paramMessage, byte[] paramArrayOfByte, int paramInt, Socket paramSocket)
    throws IOException
  {
    Header localHeader = paramMessage.getHeader();
    if (localHeader.getFlag(0))
      return null;
    if (localHeader.getRcode() != 0)
      return errorMessage(paramMessage, 1);
    if (localHeader.getOpcode() != 0)
      return errorMessage(paramMessage, 4);
    Record localRecord = paramMessage.getQuestion();
    TSIGRecord localTSIGRecord = paramMessage.getTSIG();
    TSIG localTSIG = null;
    if (localTSIGRecord != null)
    {
      localTSIG = (TSIG)this.TSIGs.get(localTSIGRecord.getName());
      if ((localTSIG == null) || (localTSIG.verify(paramMessage, paramArrayOfByte, paramInt, null) != 0))
        return formerrMessage(paramArrayOfByte);
    }
    OPTRecord localOPTRecord1 = paramMessage.getOPT();
    int i;
    if (((localOPTRecord1 == null) || (localOPTRecord1.getVersion() <= 0)) || (paramSocket != null))
      i = 65535;
    int j;
    Message localMessage;
    Name localName;
    int k;
    int m;
    while (true)
    {
      j = 0;
      if (localOPTRecord1 != null)
      {
        int i2 = 0x8000 & localOPTRecord1.getFlags();
        j = 0;
        if (i2 != 0)
          j = 1;
      }
      localMessage = new Message(paramMessage.getHeader().getID());
      localMessage.getHeader().setFlag(0);
      if (paramMessage.getHeader().getFlag(7))
        localMessage.getHeader().setFlag(7);
      localMessage.addRecord(localRecord, 0);
      localName = localRecord.getName();
      k = localRecord.getType();
      m = localRecord.getDClass();
      if ((k != 252) || (paramSocket == null))
        break;
      return doAXFR(localName, paramMessage, localTSIG, localTSIGRecord, paramSocket);
      if (localOPTRecord1 != null)
        i = Math.max(localOPTRecord1.getPayloadSize(), 512);
      else
        i = 512;
    }
    if ((!Type.isRR(k)) && (k != 255))
      return errorMessage(paramMessage, 4);
    int n = addAnswer(localMessage, localName, k, m, 0, j);
    if ((n != 0) && (n != 3))
      return errorMessage(paramMessage, n);
    addAdditional(localMessage, j);
    if (localOPTRecord1 != null)
      if (j != 1)
        break label426;
    label426: for (int i1 = 32768; ; i1 = 0)
    {
      OPTRecord localOPTRecord2 = new OPTRecord(4096, n, 0, i1);
      localMessage.addRecord(localOPTRecord2, 3);
      localMessage.setTSIG(localTSIG, 0, localTSIGRecord);
      return localMessage.toWire(i);
    }
  }

  public Cache getCache(int paramInt)
  {
    Cache localCache = (Cache)this.caches.get(new Integer(paramInt));
    if (localCache == null)
    {
      localCache = new Cache(paramInt);
      this.caches.put(new Integer(paramInt), localCache);
    }
    return localCache;
  }

  public void serveTCP(InetAddress paramInetAddress, int paramInt)
  {
    try
    {
      ServerSocket localServerSocket = new ServerSocket(paramInt, 128, paramInetAddress);
      while (true)
        new Thread(new jnamed.1(this, localServerSocket.accept())).start();
    }
    catch (IOException localIOException)
    {
      System.out.println("serveTCP(" + addrport(paramInetAddress, paramInt) + "): " + localIOException);
    }
  }

  // ERROR //
  public void serveUDP(InetAddress paramInetAddress, int paramInt)
  {
    // Byte code:
    //   0: new 631	java/net/DatagramSocket
    //   3: dup
    //   4: iload_2
    //   5: aload_1
    //   6: invokespecial 634	java/net/DatagramSocket:<init>	(ILjava/net/InetAddress;)V
    //   9: astore_3
    //   10: sipush 512
    //   13: newarray byte
    //   15: astore 5
    //   17: new 636	java/net/DatagramPacket
    //   20: dup
    //   21: aload 5
    //   23: aload 5
    //   25: arraylength
    //   26: invokespecial 639	java/net/DatagramPacket:<init>	([BI)V
    //   29: astore 6
    //   31: aconst_null
    //   32: astore 7
    //   34: aload 6
    //   36: aload 5
    //   38: arraylength
    //   39: invokevirtual 642	java/net/DatagramPacket:setLength	(I)V
    //   42: aload_3
    //   43: aload 6
    //   45: invokevirtual 646	java/net/DatagramSocket:receive	(Ljava/net/DatagramPacket;)V
    //   48: aload_0
    //   49: new 205	org/xbill/DNS/Message
    //   52: dup
    //   53: aload 5
    //   55: invokespecial 306	org/xbill/DNS/Message:<init>	([B)V
    //   58: aload 5
    //   60: aload 6
    //   62: invokevirtual 649	java/net/DatagramPacket:getLength	()I
    //   65: aconst_null
    //   66: invokevirtual 310	jnamed:generateReply	(Lorg/xbill/DNS/Message;[BILjava/net/Socket;)[B
    //   69: astore 11
    //   71: aload 11
    //   73: astore 10
    //   75: aload 10
    //   77: ifnull -43 -> 34
    //   80: aload 7
    //   82: ifnonnull +98 -> 180
    //   85: new 636	java/net/DatagramPacket
    //   88: dup
    //   89: aload 10
    //   91: aload 10
    //   93: arraylength
    //   94: aload 6
    //   96: invokevirtual 652	java/net/DatagramPacket:getAddress	()Ljava/net/InetAddress;
    //   99: aload 6
    //   101: invokevirtual 655	java/net/DatagramPacket:getPort	()I
    //   104: invokespecial 658	java/net/DatagramPacket:<init>	([BILjava/net/InetAddress;I)V
    //   107: astore 7
    //   109: aload_3
    //   110: aload 7
    //   112: invokevirtual 661	java/net/DatagramSocket:send	(Ljava/net/DatagramPacket;)V
    //   115: goto -81 -> 34
    //   118: astore 4
    //   120: getstatic 70	java/lang/System:out	Ljava/io/PrintStream;
    //   123: new 72	java/lang/StringBuffer
    //   126: dup
    //   127: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   130: ldc_w 663
    //   133: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   136: aload_1
    //   137: iload_2
    //   138: invokestatic 195	jnamed:addrport	(Ljava/net/InetAddress;I)Ljava/lang/String;
    //   141: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   144: ldc_w 341
    //   147: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   150: aload 4
    //   152: invokevirtual 344	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
    //   155: invokevirtual 82	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   158: invokevirtual 87	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   161: return
    //   162: astore 8
    //   164: goto -130 -> 34
    //   167: astore 9
    //   169: aload_0
    //   170: aload 5
    //   172: invokevirtual 315	jnamed:formerrMessage	([B)[B
    //   175: astore 10
    //   177: goto -97 -> 80
    //   180: aload 7
    //   182: aload 10
    //   184: invokevirtual 666	java/net/DatagramPacket:setData	([B)V
    //   187: aload 7
    //   189: aload 10
    //   191: arraylength
    //   192: invokevirtual 642	java/net/DatagramPacket:setLength	(I)V
    //   195: aload 7
    //   197: aload 6
    //   199: invokevirtual 652	java/net/DatagramPacket:getAddress	()Ljava/net/InetAddress;
    //   202: invokevirtual 670	java/net/DatagramPacket:setAddress	(Ljava/net/InetAddress;)V
    //   205: aload 7
    //   207: aload 6
    //   209: invokevirtual 655	java/net/DatagramPacket:getPort	()I
    //   212: invokevirtual 673	java/net/DatagramPacket:setPort	(I)V
    //   215: goto -106 -> 109
    //
    // Exception table:
    //   from	to	target	type
    //   0	31	118	java/io/IOException
    //   34	42	118	java/io/IOException
    //   42	48	118	java/io/IOException
    //   85	109	118	java/io/IOException
    //   109	115	118	java/io/IOException
    //   169	177	118	java/io/IOException
    //   180	215	118	java/io/IOException
    //   42	48	162	java/io/InterruptedIOException
    //   48	71	167	java/io/IOException
  }
}