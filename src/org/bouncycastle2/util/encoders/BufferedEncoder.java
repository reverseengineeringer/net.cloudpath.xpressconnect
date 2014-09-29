package org.bouncycastle2.util.encoders;

public class BufferedEncoder
{
  protected byte[] buf;
  protected int bufOff;
  protected Translator translator;

  public BufferedEncoder(Translator paramTranslator, int paramInt)
  {
    this.translator = paramTranslator;
    if (paramInt % paramTranslator.getEncodedBlockSize() != 0)
      throw new IllegalArgumentException("buffer size not multiple of input block size");
    this.buf = new byte[paramInt];
    this.bufOff = 0;
  }

  public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = this.buf;
    int i = this.bufOff;
    this.bufOff = (i + 1);
    arrayOfByte[i] = paramByte;
    int j = this.bufOff;
    int k = this.buf.length;
    int m = 0;
    if (j == k)
    {
      m = this.translator.encode(this.buf, 0, this.buf.length, paramArrayOfByte, paramInt);
      this.bufOff = 0;
    }
    return m;
  }

  public int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (paramInt2 < 0)
      throw new IllegalArgumentException("Can't have a negative input length!");
    int i = this.buf.length - this.bufOff;
    int j = 0;
    if (paramInt2 > i)
    {
      System.arraycopy(paramArrayOfByte1, paramInt1, this.buf, this.bufOff, i);
      int k = 0 + this.translator.encode(this.buf, 0, this.buf.length, paramArrayOfByte2, paramInt3);
      this.bufOff = 0;
      int m = paramInt2 - i;
      int n = paramInt1 + i;
      int i1 = paramInt3 + k;
      int i2 = m - m % this.buf.length;
      j = k + this.translator.encode(paramArrayOfByte1, n, i2, paramArrayOfByte2, i1);
      paramInt2 = m - i2;
      paramInt1 = n + i2;
    }
    if (paramInt2 != 0)
    {
      System.arraycopy(paramArrayOfByte1, paramInt1, this.buf, this.bufOff, paramInt2);
      this.bufOff = (paramInt2 + this.bufOff);
    }
    return j;
  }
}