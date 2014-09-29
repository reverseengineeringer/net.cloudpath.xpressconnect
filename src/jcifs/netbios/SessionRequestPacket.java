package jcifs.netbios;

import java.io.IOException;
import java.io.InputStream;

public class SessionRequestPacket extends SessionServicePacket
{
  private Name calledName;
  private Name callingName;

  SessionRequestPacket()
  {
    this.calledName = new Name();
    this.callingName = new Name();
  }

  public SessionRequestPacket(Name paramName1, Name paramName2)
  {
    this.type = 129;
    this.calledName = paramName1;
    this.callingName = paramName2;
  }

  int readTrailerWireFormat(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    if (paramInputStream.read(paramArrayOfByte, paramInt, this.length) != this.length)
      throw new IOException("invalid session request wire format");
    int i = paramInt + this.calledName.readWireFormat(paramArrayOfByte, paramInt);
    return i + this.callingName.readWireFormat(paramArrayOfByte, i) - paramInt;
  }

  int writeTrailerWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + this.calledName.writeWireFormat(paramArrayOfByte, paramInt);
    return i + this.callingName.writeWireFormat(paramArrayOfByte, i) - paramInt;
  }
}