package org.xbill.DNS;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ZoneTransferIn
{
  private static final int AXFR = 6;
  private static final int END = 7;
  private static final int FIRSTDATA = 1;
  private static final int INITIALSOA = 0;
  private static final int IXFR_ADD = 5;
  private static final int IXFR_ADDSOA = 4;
  private static final int IXFR_DEL = 3;
  private static final int IXFR_DELSOA = 2;
  private SocketAddress address;
  private TCPClient client;
  private long current_serial;
  private int dclass;
  private long end_serial;
  private ZoneTransferHandler handler;
  private Record initialsoa;
  private long ixfr_serial;
  private SocketAddress localAddress;
  private int qtype;
  private int rtype;
  private int state;
  private long timeout = 900000L;
  private TSIG tsig;
  private TSIG.StreamVerifier verifier;
  private boolean want_fallback;
  private Name zname;

  private ZoneTransferIn()
  {
  }

  private ZoneTransferIn(Name paramName, int paramInt, long paramLong, boolean paramBoolean, SocketAddress paramSocketAddress, TSIG paramTSIG)
  {
    this.address = paramSocketAddress;
    this.tsig = paramTSIG;
    if (paramName.isAbsolute())
      this.zname = paramName;
    while (true)
    {
      this.qtype = paramInt;
      this.dclass = 1;
      this.ixfr_serial = paramLong;
      this.want_fallback = paramBoolean;
      this.state = 0;
      return;
      try
      {
        this.zname = Name.concatenate(paramName, Name.root);
      }
      catch (NameTooLongException localNameTooLongException)
      {
      }
    }
    throw new IllegalArgumentException("ZoneTransferIn: name too long");
  }

  private void closeConnection()
  {
    try
    {
      if (this.client != null)
        this.client.cleanup();
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  private void doxfr()
    throws IOException, ZoneTransferException
  {
    sendQuery();
    while (true)
    {
      Message localMessage;
      Record[] arrayOfRecord;
      int j;
      if (this.state != 7)
      {
        byte[] arrayOfByte = this.client.recv();
        localMessage = parseMessage(arrayOfByte);
        if ((localMessage.getHeader().getRcode() == 0) && (this.verifier != null))
        {
          localMessage.getTSIG();
          if (this.verifier.verify(localMessage, arrayOfByte) != 0)
            fail("TSIG failure");
        }
        arrayOfRecord = localMessage.getSectionArray(1);
        if (this.state != 0)
          break label178;
        j = localMessage.getRcode();
        if (j == 0)
          break label125;
        if ((this.qtype == 251) && (j == 4))
        {
          fallback();
          doxfr();
        }
      }
      else
      {
        return;
      }
      fail(Rcode.string(j));
      label125: Record localRecord = localMessage.getQuestion();
      if ((localRecord != null) && (localRecord.getType() != this.qtype))
        fail("invalid question section");
      if ((arrayOfRecord.length == 0) && (this.qtype == 251))
      {
        fallback();
        doxfr();
        return;
      }
      label178: for (int i = 0; i < arrayOfRecord.length; i++)
        parseRR(arrayOfRecord[i]);
      if ((this.state == 7) && (this.verifier != null) && (!localMessage.isVerified()))
        fail("last message must be signed");
    }
  }

  private void fail(String paramString)
    throws ZoneTransferException
  {
    throw new ZoneTransferException(paramString);
  }

  private void fallback()
    throws ZoneTransferException
  {
    if (!this.want_fallback)
      fail("server doesn't support IXFR");
    logxfr("falling back to AXFR");
    this.qtype = 252;
    this.state = 0;
  }

  private BasicHandler getBasicHandler()
    throws IllegalArgumentException
  {
    if ((this.handler instanceof BasicHandler))
      return (BasicHandler)this.handler;
    throw new IllegalArgumentException("ZoneTransferIn used callback interface");
  }

  private static long getSOASerial(Record paramRecord)
  {
    return ((SOARecord)paramRecord).getSerial();
  }

  private void logxfr(String paramString)
  {
    if (Options.check("verbose"))
      System.out.println(this.zname + ": " + paramString);
  }

  public static ZoneTransferIn newAXFR(Name paramName, String paramString, int paramInt, TSIG paramTSIG)
    throws UnknownHostException
  {
    if (paramInt == 0)
      paramInt = 53;
    return newAXFR(paramName, new InetSocketAddress(paramString, paramInt), paramTSIG);
  }

  public static ZoneTransferIn newAXFR(Name paramName, String paramString, TSIG paramTSIG)
    throws UnknownHostException
  {
    return newAXFR(paramName, paramString, 0, paramTSIG);
  }

  public static ZoneTransferIn newAXFR(Name paramName, SocketAddress paramSocketAddress, TSIG paramTSIG)
  {
    return new ZoneTransferIn(paramName, 252, 0L, false, paramSocketAddress, paramTSIG);
  }

  public static ZoneTransferIn newIXFR(Name paramName, long paramLong, boolean paramBoolean, String paramString, int paramInt, TSIG paramTSIG)
    throws UnknownHostException
  {
    if (paramInt == 0)
      paramInt = 53;
    return newIXFR(paramName, paramLong, paramBoolean, new InetSocketAddress(paramString, paramInt), paramTSIG);
  }

  public static ZoneTransferIn newIXFR(Name paramName, long paramLong, boolean paramBoolean, String paramString, TSIG paramTSIG)
    throws UnknownHostException
  {
    return newIXFR(paramName, paramLong, paramBoolean, paramString, 0, paramTSIG);
  }

  public static ZoneTransferIn newIXFR(Name paramName, long paramLong, boolean paramBoolean, SocketAddress paramSocketAddress, TSIG paramTSIG)
  {
    return new ZoneTransferIn(paramName, 251, paramLong, paramBoolean, paramSocketAddress, paramTSIG);
  }

  private void openConnection()
    throws IOException
  {
    this.client = new TCPClient(System.currentTimeMillis() + this.timeout);
    if (this.localAddress != null)
      this.client.bind(this.localAddress);
    this.client.connect(this.address);
  }

  private Message parseMessage(byte[] paramArrayOfByte)
    throws WireParseException
  {
    try
    {
      Message localMessage = new Message(paramArrayOfByte);
      return localMessage;
    }
    catch (IOException localIOException)
    {
      if ((localIOException instanceof WireParseException))
        throw ((WireParseException)localIOException);
    }
    throw new WireParseException("Error parsing message");
  }

  private void parseRR(Record paramRecord)
    throws ZoneTransferException
  {
    int i = paramRecord.getType();
    switch (this.state)
    {
    default:
      fail("invalid state");
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
      do
      {
        do
        {
          return;
          if (i != 6)
            fail("missing initial SOA");
          this.initialsoa = paramRecord;
          this.end_serial = getSOASerial(paramRecord);
          if ((this.qtype == 251) && (Serial.compare(this.end_serial, this.ixfr_serial) <= 0))
          {
            logxfr("up to date");
            this.state = 7;
            return;
          }
          this.state = 1;
          return;
          if ((this.qtype == 251) && (i == 6) && (getSOASerial(paramRecord) == this.ixfr_serial))
          {
            this.rtype = 251;
            this.handler.startIXFR();
            logxfr("got incremental response");
          }
          for (this.state = 2; ; this.state = 6)
          {
            parseRR(paramRecord);
            return;
            this.rtype = 251;
            this.handler.startAXFR();
            this.handler.handleRecord(this.initialsoa);
            logxfr("got nonincremental response");
          }
          this.handler.startIXFRDeletes(paramRecord);
          this.state = 3;
          return;
          if (i == 6)
          {
            this.current_serial = getSOASerial(paramRecord);
            this.state = 4;
            parseRR(paramRecord);
            return;
          }
          this.handler.handleRecord(paramRecord);
          return;
          this.handler.startIXFRAdds(paramRecord);
          this.state = 5;
          return;
          if (i == 6)
          {
            long l = getSOASerial(paramRecord);
            if (l == this.end_serial)
            {
              this.state = 7;
              return;
            }
            if (l != this.current_serial)
              fail("IXFR out of sync: expected serial " + this.current_serial + " , got " + l);
          }
          else
          {
            this.handler.handleRecord(paramRecord);
            return;
          }
          this.state = 2;
          parseRR(paramRecord);
          return;
        }
        while ((i == 1) && (paramRecord.getDClass() != this.dclass));
        this.handler.handleRecord(paramRecord);
      }
      while (i != 6);
      this.state = 7;
      return;
    case 7:
    }
    fail("extra data");
  }

  private void sendQuery()
    throws IOException
  {
    Record localRecord = Record.newRecord(this.zname, this.qtype, this.dclass);
    Message localMessage = new Message();
    localMessage.getHeader().setOpcode(0);
    localMessage.addRecord(localRecord, 0);
    if (this.qtype == 251)
      localMessage.addRecord(new SOARecord(this.zname, this.dclass, 0L, Name.root, Name.root, this.ixfr_serial, 0L, 0L, 0L, 0L), 2);
    if (this.tsig != null)
    {
      this.tsig.apply(localMessage, null);
      this.verifier = new TSIG.StreamVerifier(this.tsig, localMessage.getTSIG());
    }
    byte[] arrayOfByte = localMessage.toWire(65535);
    this.client.send(arrayOfByte);
  }

  public List getAXFR()
  {
    return getBasicHandler().axfr;
  }

  public List getIXFR()
  {
    return getBasicHandler().ixfr;
  }

  public Name getName()
  {
    return this.zname;
  }

  public int getType()
  {
    return this.qtype;
  }

  public boolean isAXFR()
  {
    return this.rtype == 252;
  }

  public boolean isCurrent()
  {
    BasicHandler localBasicHandler = getBasicHandler();
    return (localBasicHandler.axfr == null) && (localBasicHandler.ixfr == null);
  }

  public boolean isIXFR()
  {
    return this.rtype == 251;
  }

  public List run()
    throws IOException, ZoneTransferException
  {
    BasicHandler localBasicHandler = new BasicHandler(null);
    run(localBasicHandler);
    if (localBasicHandler.axfr != null)
      return localBasicHandler.axfr;
    return localBasicHandler.ixfr;
  }

  public void run(ZoneTransferHandler paramZoneTransferHandler)
    throws IOException, ZoneTransferException
  {
    this.handler = paramZoneTransferHandler;
    try
    {
      openConnection();
      doxfr();
      return;
    }
    finally
    {
      closeConnection();
    }
  }

  public void setDClass(int paramInt)
  {
    DClass.check(paramInt);
    this.dclass = paramInt;
  }

  public void setLocalAddress(SocketAddress paramSocketAddress)
  {
    this.localAddress = paramSocketAddress;
  }

  public void setTimeout(int paramInt)
  {
    if (paramInt < 0)
      throw new IllegalArgumentException("timeout cannot be negative");
    this.timeout = (1000L * paramInt);
  }

  private static class BasicHandler
    implements ZoneTransferIn.ZoneTransferHandler
  {
    private List axfr;
    private List ixfr;

    private BasicHandler()
    {
    }

    BasicHandler(ZoneTransferIn.1 param1)
    {
      this();
    }

    public void handleRecord(Record paramRecord)
    {
      ZoneTransferIn.Delta localDelta;
      List localList;
      if (this.ixfr != null)
      {
        localDelta = (ZoneTransferIn.Delta)this.ixfr.get(-1 + this.ixfr.size());
        if (localDelta.adds.size() > 0)
          localList = localDelta.adds;
      }
      while (true)
      {
        localList.add(paramRecord);
        return;
        localList = localDelta.deletes;
        continue;
        localList = this.axfr;
      }
    }

    public void startAXFR()
    {
      this.axfr = new ArrayList();
    }

    public void startIXFR()
    {
      this.ixfr = new ArrayList();
    }

    public void startIXFRAdds(Record paramRecord)
    {
      ZoneTransferIn.Delta localDelta = (ZoneTransferIn.Delta)this.ixfr.get(-1 + this.ixfr.size());
      localDelta.adds.add(paramRecord);
      localDelta.end = ZoneTransferIn.getSOASerial(paramRecord);
    }

    public void startIXFRDeletes(Record paramRecord)
    {
      ZoneTransferIn.Delta localDelta = new ZoneTransferIn.Delta(null);
      localDelta.deletes.add(paramRecord);
      localDelta.start = ZoneTransferIn.getSOASerial(paramRecord);
      this.ixfr.add(localDelta);
    }
  }

  public static class Delta
  {
    public List adds = new ArrayList();
    public List deletes = new ArrayList();
    public long end;
    public long start;

    private Delta()
    {
    }

    Delta(ZoneTransferIn.1 param1)
    {
      this();
    }
  }

  public static abstract interface ZoneTransferHandler
  {
    public abstract void handleRecord(Record paramRecord)
      throws ZoneTransferException;

    public abstract void startAXFR()
      throws ZoneTransferException;

    public abstract void startIXFR()
      throws ZoneTransferException;

    public abstract void startIXFRAdds(Record paramRecord)
      throws ZoneTransferException;

    public abstract void startIXFRDeletes(Record paramRecord)
      throws ZoneTransferException;
  }
}