package jcifs.netbios;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import jcifs.Config;
import jcifs.util.LogStream;

public class NbtSocket extends Socket
{
  private static final int BUFFER_SIZE = 512;
  private static final int DEFAULT_SO_TIMEOUT = 5000;
  private static final int SSN_SRVC_PORT = 139;
  private static LogStream log = LogStream.getInstance();
  private NbtAddress address;
  private Name calledName;
  private int soTimeout;

  public NbtSocket()
  {
  }

  public NbtSocket(NbtAddress paramNbtAddress, int paramInt)
    throws IOException
  {
    this(paramNbtAddress, paramInt, null, 0);
  }

  public NbtSocket(NbtAddress paramNbtAddress, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException
  {
    this(paramNbtAddress, null, paramInt1, paramInetAddress, paramInt2);
  }

  public NbtSocket(NbtAddress paramNbtAddress, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException
  {
    super(localInetAddress, paramInt1, paramInetAddress, paramInt2);
    this.address = paramNbtAddress;
    if (paramString == null);
    for (this.calledName = paramNbtAddress.hostName; ; this.calledName = new Name(paramString, 32, null))
    {
      this.soTimeout = Config.getInt("jcifs.netbios.soTimeout", 5000);
      connect();
      return;
    }
  }

  private void connect()
    throws IOException
  {
    byte[] arrayOfByte = new byte[512];
    InputStream localInputStream;
    try
    {
      localInputStream = super.getInputStream();
      super.getOutputStream().write(arrayOfByte, 0, new SessionRequestPacket(this.calledName, NbtAddress.localhost.hostName).writeWireFormat(arrayOfByte, 0));
      setSoTimeout(this.soTimeout);
      int i = SessionServicePacket.readPacketType(localInputStream, arrayOfByte, 0);
      switch (i)
      {
      default:
        close();
        throw new NbtException(2, 0);
      case 130:
      case 131:
      case -1:
      }
    }
    catch (IOException localIOException)
    {
      close();
      throw localIOException;
    }
    if (LogStream.level > 2)
      log.println("session established ok with " + this.address);
    return;
    int j = 0xFF & localInputStream.read();
    close();
    throw new NbtException(2, j);
    throw new NbtException(2, -1);
  }

  public void close()
    throws IOException
  {
    if (LogStream.level > 3)
      log.println("close: " + this);
    super.close();
  }

  public InputStream getInputStream()
    throws IOException
  {
    return new SocketInputStream(super.getInputStream());
  }

  public InetAddress getLocalAddress()
  {
    return super.getLocalAddress();
  }

  public int getLocalPort()
  {
    return super.getLocalPort();
  }

  public NbtAddress getNbtAddress()
  {
    return this.address;
  }

  public OutputStream getOutputStream()
    throws IOException
  {
    return new SocketOutputStream(super.getOutputStream());
  }

  public int getPort()
  {
    return super.getPort();
  }

  public String toString()
  {
    return "NbtSocket[addr=" + this.address + ",port=" + super.getPort() + ",localport=" + super.getLocalPort() + "]";
  }
}