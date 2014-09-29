package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.io.InputStream;

class TlsInputStream extends InputStream
{
  private byte[] buf = new byte[1];
  private TlsProtocolHandler handler = null;

  TlsInputStream(TlsProtocolHandler paramTlsProtocolHandler)
  {
    this.handler = paramTlsProtocolHandler;
  }

  public void close()
    throws IOException
  {
    this.handler.close();
  }

  public int read()
    throws IOException
  {
    if (read(this.buf) < 0)
      return -1;
    return 0xFF & this.buf[0];
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return this.handler.readApplicationData(paramArrayOfByte, paramInt1, paramInt2);
  }
}