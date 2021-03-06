package org.bouncycastle2.util.io;

import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream extends OutputStream
{
  private OutputStream output1;
  private OutputStream output2;

  public TeeOutputStream(OutputStream paramOutputStream1, OutputStream paramOutputStream2)
  {
    this.output1 = paramOutputStream1;
    this.output2 = paramOutputStream2;
  }

  public void close()
    throws IOException
  {
    this.output1.close();
    this.output2.close();
  }

  public void flush()
    throws IOException
  {
    this.output1.flush();
    this.output2.flush();
  }

  public void write(int paramInt)
    throws IOException
  {
    this.output1.write(paramInt);
    this.output2.write(paramInt);
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    this.output1.write(paramArrayOfByte);
    this.output2.write(paramArrayOfByte);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.output1.write(paramArrayOfByte, paramInt1, paramInt2);
    this.output2.write(paramArrayOfByte, paramInt1, paramInt2);
  }
}