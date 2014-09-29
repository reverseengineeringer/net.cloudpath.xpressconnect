package org.bouncycastle2.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle2.crypto.Mac;

public class MacOutputStream extends FilterOutputStream
{
  protected Mac mac;

  public MacOutputStream(OutputStream paramOutputStream, Mac paramMac)
  {
    super(paramOutputStream);
    this.mac = paramMac;
  }

  public Mac getMac()
  {
    return this.mac;
  }

  public void write(int paramInt)
    throws IOException
  {
    this.mac.update((byte)paramInt);
    this.out.write(paramInt);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.mac.update(paramArrayOfByte, paramInt1, paramInt2);
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
  }
}