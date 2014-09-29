package org.xbill.DNS;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

final class TCPClient extends Client
{
  public TCPClient(long paramLong)
    throws IOException
  {
    super(SocketChannel.open(), paramLong);
  }

  private byte[] _recv(int paramInt)
    throws IOException
  {
    SocketChannel localSocketChannel = (SocketChannel)this.key.channel();
    int i = 0;
    byte[] arrayOfByte = new byte[paramInt];
    ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
    this.key.interestOps(1);
    while (i < paramInt)
    {
      long l;
      try
      {
        if (!this.key.isReadable())
          break label127;
        l = localSocketChannel.read(localByteBuffer);
        if (l < 0L)
          throw new EOFException();
      }
      finally
      {
        if (this.key.isValid())
          this.key.interestOps(0);
      }
      i += (int)l;
      if ((i < paramInt) && (System.currentTimeMillis() > this.endTime))
      {
        throw new SocketTimeoutException();
        label127: blockUntil(this.key, this.endTime);
      }
    }
    if (this.key.isValid())
      this.key.interestOps(0);
    return arrayOfByte;
  }

  static byte[] sendrecv(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, byte[] paramArrayOfByte, long paramLong)
    throws IOException
  {
    TCPClient localTCPClient = new TCPClient(paramLong);
    if (paramSocketAddress1 != null);
    try
    {
      localTCPClient.bind(paramSocketAddress1);
      localTCPClient.connect(paramSocketAddress2);
      localTCPClient.send(paramArrayOfByte);
      byte[] arrayOfByte = localTCPClient.recv();
      return arrayOfByte;
    }
    finally
    {
      localTCPClient.cleanup();
    }
  }

  static byte[] sendrecv(SocketAddress paramSocketAddress, byte[] paramArrayOfByte, long paramLong)
    throws IOException
  {
    return sendrecv(null, paramSocketAddress, paramArrayOfByte, paramLong);
  }

  void bind(SocketAddress paramSocketAddress)
    throws IOException
  {
    ((SocketChannel)this.key.channel()).socket().bind(paramSocketAddress);
  }

  void connect(SocketAddress paramSocketAddress)
    throws IOException
  {
    SocketChannel localSocketChannel = (SocketChannel)this.key.channel();
    if (localSocketChannel.connect(paramSocketAddress))
      return;
    this.key.interestOps(8);
    try
    {
      while (!localSocketChannel.finishConnect())
        if (!this.key.isConnectable())
          blockUntil(this.key, this.endTime);
    }
    finally
    {
      if (this.key.isValid())
        this.key.interestOps(0);
    }
  }

  byte[] recv()
    throws IOException
  {
    byte[] arrayOfByte1 = _recv(2);
    byte[] arrayOfByte2 = _recv(((0xFF & arrayOfByte1[0]) << 8) + (0xFF & arrayOfByte1[1]));
    verboseLog("TCP read", arrayOfByte2);
    return arrayOfByte2;
  }

  void send(byte[] paramArrayOfByte)
    throws IOException
  {
    SocketChannel localSocketChannel = (SocketChannel)this.key.channel();
    verboseLog("TCP write", paramArrayOfByte);
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[0] = ((byte)(paramArrayOfByte.length >>> 8));
    arrayOfByte[1] = ((byte)(0xFF & paramArrayOfByte.length));
    ByteBuffer[] arrayOfByteBuffer = new ByteBuffer[2];
    arrayOfByteBuffer[0] = ByteBuffer.wrap(arrayOfByte);
    arrayOfByteBuffer[1] = ByteBuffer.wrap(paramArrayOfByte);
    int i = 0;
    this.key.interestOps(4);
    while (true)
    {
      long l;
      try
      {
        if (i >= 2 + paramArrayOfByte.length)
          break;
        if (!this.key.isWritable())
          break label176;
        l = localSocketChannel.write(arrayOfByteBuffer);
        if (l < 0L)
          throw new EOFException();
      }
      finally
      {
        if (this.key.isValid())
          this.key.interestOps(0);
      }
      i += (int)l;
      if ((i < 2 + paramArrayOfByte.length) && (System.currentTimeMillis() > this.endTime))
      {
        throw new SocketTimeoutException();
        label176: blockUntil(this.key, this.endTime);
      }
    }
    if (this.key.isValid())
      this.key.interestOps(0);
  }
}