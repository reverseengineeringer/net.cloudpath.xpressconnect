package org.xbill.DNS;

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.security.SecureRandom;

final class UDPClient extends Client
{
  private static final int EPHEMERAL_RANGE = 64511;
  private static final int EPHEMERAL_START = 1024;
  private static final int EPHEMERAL_STOP = 65535;
  private static SecureRandom prng = new SecureRandom();
  private static volatile boolean prng_initializing = true;
  private boolean bound = false;

  static
  {
    new Thread(new UDPClient.1()).start();
  }

  public UDPClient(long paramLong)
    throws IOException
  {
    super(DatagramChannel.open(), paramLong);
  }

  private void bind_random(InetSocketAddress paramInetSocketAddress)
    throws IOException
  {
    if (prng_initializing);
    try
    {
      Thread.sleep(2L);
      if (prng_initializing);
      do
      {
        return;
        localDatagramChannel = (DatagramChannel)this.key.channel();
        i = 0;
      }
      while (i >= 1024);
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
        try
        {
          DatagramChannel localDatagramChannel;
          int j = 1024 + prng.nextInt(64511);
          if (paramInetSocketAddress != null)
          {
            localInetSocketAddress = new InetSocketAddress(paramInetSocketAddress.getAddress(), j);
            localDatagramChannel.socket().bind(localInetSocketAddress);
            this.bound = true;
            return;
          }
          InetSocketAddress localInetSocketAddress = new InetSocketAddress(j);
          continue;
          localInterruptedException = localInterruptedException;
        }
        catch (SocketException localSocketException)
        {
          int i;
          i++;
        }
    }
  }

  static byte[] sendrecv(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, byte[] paramArrayOfByte, int paramInt, long paramLong)
    throws IOException
  {
    UDPClient localUDPClient = new UDPClient(paramLong);
    try
    {
      localUDPClient.bind(paramSocketAddress1);
      localUDPClient.connect(paramSocketAddress2);
      localUDPClient.send(paramArrayOfByte);
      byte[] arrayOfByte = localUDPClient.recv(paramInt);
      return arrayOfByte;
    }
    finally
    {
      localUDPClient.cleanup();
    }
  }

  static byte[] sendrecv(SocketAddress paramSocketAddress, byte[] paramArrayOfByte, int paramInt, long paramLong)
    throws IOException
  {
    return sendrecv(null, paramSocketAddress, paramArrayOfByte, paramInt, paramLong);
  }

  void bind(SocketAddress paramSocketAddress)
    throws IOException
  {
    if ((paramSocketAddress == null) || (((paramSocketAddress instanceof InetSocketAddress)) && (((InetSocketAddress)paramSocketAddress).getPort() == 0)))
    {
      bind_random((InetSocketAddress)paramSocketAddress);
      if (!this.bound);
    }
    while (paramSocketAddress == null)
      return;
    ((DatagramChannel)this.key.channel()).socket().bind(paramSocketAddress);
    this.bound = true;
  }

  void connect(SocketAddress paramSocketAddress)
    throws IOException
  {
    if (!this.bound)
      bind(null);
    ((DatagramChannel)this.key.channel()).connect(paramSocketAddress);
  }

  byte[] recv(int paramInt)
    throws IOException
  {
    DatagramChannel localDatagramChannel = (DatagramChannel)this.key.channel();
    byte[] arrayOfByte1 = new byte[paramInt];
    this.key.interestOps(1);
    try
    {
      if (!this.key.isReadable())
        blockUntil(this.key, this.endTime);
    }
    finally
    {
      if (this.key.isValid())
        this.key.interestOps(0);
    }
    long l = localDatagramChannel.read(ByteBuffer.wrap(arrayOfByte1));
    if (l <= 0L)
      throw new EOFException();
    int i = (int)l;
    byte[] arrayOfByte2 = new byte[i];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
    verboseLog("UDP read", arrayOfByte2);
    return arrayOfByte2;
  }

  void send(byte[] paramArrayOfByte)
    throws IOException
  {
    DatagramChannel localDatagramChannel = (DatagramChannel)this.key.channel();
    verboseLog("UDP write", paramArrayOfByte);
    localDatagramChannel.write(ByteBuffer.wrap(paramArrayOfByte));
  }
}