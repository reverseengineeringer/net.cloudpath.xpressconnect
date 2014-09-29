package jcifs.netbios;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class SocketOutputStream extends FilterOutputStream
{
  SocketOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 > 65535)
      try
      {
        throw new IOException("write too large: " + paramInt2);
      }
      finally
      {
      }
    if (paramInt1 < 4)
      throw new IOException("NetBIOS socket output buffer requires 4 bytes available before off");
    int i = paramInt1 - 4;
    paramArrayOfByte[(i + 0)] = 0;
    paramArrayOfByte[(i + 1)] = 0;
    paramArrayOfByte[(i + 2)] = ((byte)(0xFF & paramInt2 >> 8));
    paramArrayOfByte[(i + 3)] = ((byte)(paramInt2 & 0xFF));
    this.out.write(paramArrayOfByte, i, paramInt2 + 4);
  }
}