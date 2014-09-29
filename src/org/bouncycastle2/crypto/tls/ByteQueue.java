package org.bouncycastle2.crypto.tls;

public class ByteQueue
{
  private static final int INITBUFSIZE = 1024;
  private int available = 0;
  private byte[] databuf = new byte[1024];
  private int skipped = 0;

  public static final int nextTwoPow(int paramInt)
  {
    int i = paramInt | paramInt >> 1;
    int j = i | i >> 2;
    int k = j | j >> 4;
    int m = k | k >> 8;
    return 1 + (m | m >> 16);
  }

  public void addData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 + (this.skipped + this.available) > this.databuf.length)
    {
      byte[] arrayOfByte = new byte[nextTwoPow(paramArrayOfByte.length)];
      System.arraycopy(this.databuf, this.skipped, arrayOfByte, 0, this.available);
      this.skipped = 0;
      this.databuf = arrayOfByte;
    }
    System.arraycopy(paramArrayOfByte, paramInt1, this.databuf, this.skipped + this.available, paramInt2);
    this.available = (paramInt2 + this.available);
  }

  public void read(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.available - paramInt3 < paramInt2)
      throw new TlsRuntimeException("Not enough data to read");
    if (paramArrayOfByte.length - paramInt1 < paramInt2)
      throw new TlsRuntimeException("Buffer size of " + paramArrayOfByte.length + " is too small for a read of " + paramInt2 + " bytes");
    System.arraycopy(this.databuf, paramInt3 + this.skipped, paramArrayOfByte, paramInt1, paramInt2);
  }

  public void removeData(int paramInt)
  {
    if (paramInt > this.available)
      throw new TlsRuntimeException("Cannot remove " + paramInt + " bytes, only got " + this.available);
    this.available -= paramInt;
    this.skipped = (paramInt + this.skipped);
    if (this.skipped > this.databuf.length / 2)
    {
      System.arraycopy(this.databuf, this.skipped, this.databuf, 0, this.available);
      this.skipped = 0;
    }
  }

  public int size()
  {
    return this.available;
  }
}