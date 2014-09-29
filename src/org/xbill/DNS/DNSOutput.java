package org.xbill.DNS;

public class DNSOutput
{
  private byte[] array;
  private int pos;
  private int saved_pos;

  public DNSOutput()
  {
    this(32);
  }

  public DNSOutput(int paramInt)
  {
    this.array = new byte[paramInt];
    this.pos = 0;
    this.saved_pos = -1;
  }

  private void check(long paramLong, int paramInt)
  {
    long l = 1L << paramInt;
    if ((paramLong < 0L) || (paramLong > l))
      throw new IllegalArgumentException(paramLong + " out of range for " + paramInt + " bit value");
  }

  private void need(int paramInt)
  {
    if (this.array.length - this.pos >= paramInt)
      return;
    int i = 2 * this.array.length;
    if (i < paramInt + this.pos)
      i = paramInt + this.pos;
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(this.array, 0, arrayOfByte, 0, this.pos);
    this.array = arrayOfByte;
  }

  public int current()
  {
    return this.pos;
  }

  public void jump(int paramInt)
  {
    if (paramInt > this.pos)
      throw new IllegalArgumentException("cannot jump past end of data");
    this.pos = paramInt;
  }

  public void restore()
  {
    if (this.saved_pos < 0)
      throw new IllegalStateException("no previous state");
    this.pos = this.saved_pos;
    this.saved_pos = -1;
  }

  public void save()
  {
    this.saved_pos = this.pos;
  }

  public byte[] toByteArray()
  {
    byte[] arrayOfByte = new byte[this.pos];
    System.arraycopy(this.array, 0, arrayOfByte, 0, this.pos);
    return arrayOfByte;
  }

  public void writeByteArray(byte[] paramArrayOfByte)
  {
    writeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void writeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    need(paramInt2);
    System.arraycopy(paramArrayOfByte, paramInt1, this.array, this.pos, paramInt2);
    this.pos = (paramInt2 + this.pos);
  }

  public void writeCountedString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length > 255)
      throw new IllegalArgumentException("Invalid counted string");
    need(1 + paramArrayOfByte.length);
    byte[] arrayOfByte = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    arrayOfByte[i] = ((byte)(0xFF & paramArrayOfByte.length));
    writeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void writeU16(int paramInt)
  {
    check(paramInt, 16);
    need(2);
    byte[] arrayOfByte1 = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    arrayOfByte1[i] = ((byte)(0xFF & paramInt >>> 8));
    byte[] arrayOfByte2 = this.array;
    int j = this.pos;
    this.pos = (j + 1);
    arrayOfByte2[j] = ((byte)(paramInt & 0xFF));
  }

  public void writeU16At(int paramInt1, int paramInt2)
  {
    check(paramInt1, 16);
    if (paramInt2 > -2 + this.pos)
      throw new IllegalArgumentException("cannot write past end of data");
    byte[] arrayOfByte1 = this.array;
    int i = paramInt2 + 1;
    arrayOfByte1[paramInt2] = ((byte)(0xFF & paramInt1 >>> 8));
    byte[] arrayOfByte2 = this.array;
    (i + 1);
    arrayOfByte2[i] = ((byte)(paramInt1 & 0xFF));
  }

  public void writeU32(long paramLong)
  {
    check(paramLong, 32);
    need(4);
    byte[] arrayOfByte1 = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    arrayOfByte1[i] = ((byte)(int)(0xFF & paramLong >>> 24));
    byte[] arrayOfByte2 = this.array;
    int j = this.pos;
    this.pos = (j + 1);
    arrayOfByte2[j] = ((byte)(int)(0xFF & paramLong >>> 16));
    byte[] arrayOfByte3 = this.array;
    int k = this.pos;
    this.pos = (k + 1);
    arrayOfByte3[k] = ((byte)(int)(0xFF & paramLong >>> 8));
    byte[] arrayOfByte4 = this.array;
    int m = this.pos;
    this.pos = (m + 1);
    arrayOfByte4[m] = ((byte)(int)(paramLong & 0xFF));
  }

  public void writeU8(int paramInt)
  {
    check(paramInt, 8);
    need(1);
    byte[] arrayOfByte = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    arrayOfByte[i] = ((byte)(paramInt & 0xFF));
  }
}