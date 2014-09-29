package jcifs.netbios;

import java.io.IOException;
import java.io.InputStream;

class SessionRetargetResponsePacket extends SessionServicePacket
{
  private NbtAddress retargetAddress;
  private int retargetPort;

  SessionRetargetResponsePacket()
  {
    this.type = 132;
    this.length = 6;
  }

  int readTrailerWireFormat(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    if (paramInputStream.read(paramArrayOfByte, paramInt, this.length) != this.length)
      throw new IOException("unexpected EOF reading netbios retarget session response");
    int i = readInt4(paramArrayOfByte, paramInt);
    int j = paramInt + 4;
    this.retargetAddress = new NbtAddress(null, i, false, 0);
    this.retargetPort = readInt2(paramArrayOfByte, j);
    return this.length;
  }

  int writeTrailerWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}