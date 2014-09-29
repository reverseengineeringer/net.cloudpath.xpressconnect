package org.xbill.DNS;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

public class SimpleResolver
  implements Resolver
{
  public static final int DEFAULT_EDNS_PAYLOADSIZE = 1280;
  public static final int DEFAULT_PORT = 53;
  private static final short DEFAULT_UDPSIZE = 512;
  private static String defaultResolver = "localhost";
  private static int uniqueID = 0;
  private InetSocketAddress address;
  private boolean ignoreTruncation;
  private InetSocketAddress localAddress;
  private OPTRecord queryOPT;
  private long timeoutValue = 10000L;
  private TSIG tsig;
  private boolean useTCP;

  public SimpleResolver()
    throws UnknownHostException
  {
    this(null);
  }

  public SimpleResolver(String paramString)
    throws UnknownHostException
  {
    if (paramString == null)
    {
      paramString = ResolverConfig.getCurrentConfig().server();
      if (paramString == null)
        paramString = defaultResolver;
    }
    if (paramString.equals("0"));
    for (InetAddress localInetAddress = InetAddress.getLocalHost(); ; localInetAddress = InetAddress.getByName(paramString))
    {
      this.address = new InetSocketAddress(localInetAddress, 53);
      return;
    }
  }

  private void applyEDNS(Message paramMessage)
  {
    if ((this.queryOPT == null) || (paramMessage.getOPT() != null))
      return;
    paramMessage.addRecord(this.queryOPT, 3);
  }

  private int maxUDPSize(Message paramMessage)
  {
    OPTRecord localOPTRecord = paramMessage.getOPT();
    if (localOPTRecord == null)
      return 512;
    return localOPTRecord.getPayloadSize();
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
      if (Options.check("verbose"))
        localIOException.printStackTrace();
      WireParseException localWireParseException;
      if (!(localIOException instanceof WireParseException))
        localWireParseException = new WireParseException("Error parsing message");
      throw ((WireParseException)localWireParseException);
    }
  }

  private Message sendAXFR(Message paramMessage)
    throws IOException
  {
    ZoneTransferIn localZoneTransferIn = ZoneTransferIn.newAXFR(paramMessage.getQuestion().getName(), this.address, this.tsig);
    localZoneTransferIn.setTimeout((int)(getTimeout() / 1000L));
    localZoneTransferIn.setLocalAddress(this.localAddress);
    Message localMessage;
    try
    {
      localZoneTransferIn.run();
      List localList = localZoneTransferIn.getAXFR();
      localMessage = new Message(paramMessage.getHeader().getID());
      localMessage.getHeader().setFlag(5);
      localMessage.getHeader().setFlag(0);
      localMessage.addRecord(paramMessage.getQuestion(), 0);
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
        localMessage.addRecord((Record)localIterator.next(), 1);
    }
    catch (ZoneTransferException localZoneTransferException)
    {
      throw new WireParseException(localZoneTransferException.getMessage());
    }
    return localMessage;
  }

  public static void setDefaultResolver(String paramString)
  {
    defaultResolver = paramString;
  }

  private void verifyTSIG(Message paramMessage1, Message paramMessage2, byte[] paramArrayOfByte, TSIG paramTSIG)
  {
    if (paramTSIG == null);
    int i;
    do
    {
      return;
      i = paramTSIG.verify(paramMessage2, paramArrayOfByte, paramMessage1.getTSIG());
    }
    while (!Options.check("verbose"));
    System.err.println("TSIG verify: " + Rcode.TSIGstring(i));
  }

  InetSocketAddress getAddress()
  {
    return this.address;
  }

  TSIG getTSIGKey()
  {
    return this.tsig;
  }

  long getTimeout()
  {
    return this.timeoutValue;
  }

  public Message send(Message paramMessage)
    throws IOException
  {
    if (Options.check("verbose"))
      System.err.println("Sending to " + this.address.getAddress().getHostAddress() + ":" + this.address.getPort());
    Message localMessage2;
    if (paramMessage.getHeader().getOpcode() == 0)
    {
      Record localRecord = paramMessage.getQuestion();
      if ((localRecord != null) && (localRecord.getType() == 252))
      {
        localMessage2 = sendAXFR(paramMessage);
        return localMessage2;
      }
    }
    Message localMessage1 = (Message)paramMessage.clone();
    applyEDNS(localMessage1);
    if (this.tsig != null)
      this.tsig.apply(localMessage1, null);
    byte[] arrayOfByte1 = localMessage1.toWire(65535);
    int i = maxUDPSize(localMessage1);
    int j = 0;
    long l = System.currentTimeMillis() + this.timeoutValue;
    while (true)
    {
      if ((this.useTCP) || (arrayOfByte1.length > i))
        j = 1;
      if (j != 0);
      for (byte[] arrayOfByte2 = TCPClient.sendrecv(this.localAddress, this.address, arrayOfByte1, l); arrayOfByte2.length < 12; arrayOfByte2 = UDPClient.sendrecv(this.localAddress, this.address, arrayOfByte1, i, l))
        throw new WireParseException("invalid DNS header - too short");
      int k = ((0xFF & arrayOfByte2[0]) << 8) + (0xFF & arrayOfByte2[1]);
      int m = localMessage1.getHeader().getID();
      if (k != m)
      {
        String str = "invalid message id: expected " + m + "; got id " + k;
        if (j != 0)
          throw new WireParseException(str);
        if (Options.check("verbose"))
          System.err.println(str);
      }
      else
      {
        localMessage2 = parseMessage(arrayOfByte2);
        verifyTSIG(localMessage1, localMessage2, arrayOfByte2, this.tsig);
        if ((j != 0) || (this.ignoreTruncation) || (!localMessage2.getHeader().getFlag(6)))
          break;
        j = 1;
      }
    }
  }

  public Object sendAsync(Message paramMessage, ResolverListener paramResolverListener)
  {
    while (true)
    {
      try
      {
        int i = uniqueID;
        uniqueID = i + 1;
        Integer localInteger = new Integer(i);
        Record localRecord = paramMessage.getQuestion();
        if (localRecord != null)
        {
          str1 = localRecord.getName().toString();
          String str2 = getClass() + ": " + str1;
          ResolveThread localResolveThread = new ResolveThread(this, paramMessage, localInteger, paramResolverListener);
          localResolveThread.setName(str2);
          localResolveThread.setDaemon(true);
          localResolveThread.start();
          return localInteger;
        }
      }
      finally
      {
      }
      String str1 = "(none)";
    }
  }

  public void setAddress(InetAddress paramInetAddress)
  {
    this.address = new InetSocketAddress(paramInetAddress, this.address.getPort());
  }

  public void setAddress(InetSocketAddress paramInetSocketAddress)
  {
    this.address = paramInetSocketAddress;
  }

  public void setEDNS(int paramInt)
  {
    setEDNS(paramInt, 0, 0, null);
  }

  public void setEDNS(int paramInt1, int paramInt2, int paramInt3, List paramList)
  {
    if ((paramInt1 != 0) && (paramInt1 != -1))
      throw new IllegalArgumentException("invalid EDNS level - must be 0 or -1");
    if (paramInt2 == 0)
      paramInt2 = 1280;
    this.queryOPT = new OPTRecord(paramInt2, 0, paramInt1, paramInt3, paramList);
  }

  public void setIgnoreTruncation(boolean paramBoolean)
  {
    this.ignoreTruncation = paramBoolean;
  }

  public void setLocalAddress(InetAddress paramInetAddress)
  {
    this.localAddress = new InetSocketAddress(paramInetAddress, 0);
  }

  public void setLocalAddress(InetSocketAddress paramInetSocketAddress)
  {
    this.localAddress = paramInetSocketAddress;
  }

  public void setPort(int paramInt)
  {
    this.address = new InetSocketAddress(this.address.getAddress(), paramInt);
  }

  public void setTCP(boolean paramBoolean)
  {
    this.useTCP = paramBoolean;
  }

  public void setTSIGKey(TSIG paramTSIG)
  {
    this.tsig = paramTSIG;
  }

  public void setTimeout(int paramInt)
  {
    setTimeout(paramInt, 0);
  }

  public void setTimeout(int paramInt1, int paramInt2)
  {
    this.timeoutValue = (1000L * paramInt1 + paramInt2);
  }
}