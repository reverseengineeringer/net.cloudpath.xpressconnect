package org.xbill.DNS;

public class DNSInput
{
  private byte[] array;
  private int end;
  private int pos;
  private int saved_end;
  private int saved_pos;

  public DNSInput(byte[] paramArrayOfByte)
  {
    this.array = paramArrayOfByte;
    this.pos = 0;
    this.end = this.array.length;
    this.saved_pos = -1;
    this.saved_end = -1;
  }

  private void require(int paramInt)
    throws WireParseException
  {
    if (paramInt > remaining())
      throw new WireParseException("end of input");
  }

  public void clearActive()
  {
    this.end = this.array.length;
  }

  public int current()
  {
    return this.pos;
  }

  public void jump(int paramInt)
  {
    if (paramInt >= this.array.length)
      throw new IllegalArgumentException("cannot jump past end of input");
    this.pos = paramInt;
    this.end = this.array.length;
  }

  public void readByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws WireParseException
  {
    require(paramInt2);
    System.arraycopy(this.array, this.pos, paramArrayOfByte, paramInt1, paramInt2);
    this.pos = (paramInt2 + this.pos);
  }

  public byte[] readByteArray()
  {
    int i = remaining();
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(this.array, this.pos, arrayOfByte, 0, i);
    this.pos = (i + this.pos);
    return arrayOfByte;
  }

  public byte[] readByteArray(int paramInt)
    throws WireParseException
  {
    require(paramInt);
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(this.array, this.pos, arrayOfByte, 0, paramInt);
    this.pos = (paramInt + this.pos);
    return arrayOfByte;
  }

  public byte[] readCountedString()
    throws WireParseException
  {
    require(1);
    byte[] arrayOfByte = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    return readByteArray(0xFF & arrayOfByte[i]);
  }

  public int readU16()
    throws WireParseException
  {
    require(2);
    byte[] arrayOfByte1 = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    int j = 0xFF & arrayOfByte1[i];
    byte[] arrayOfByte2 = this.array;
    int k = this.pos;
    this.pos = (k + 1);
    return (0xFF & arrayOfByte2[k]) + (j << 8);
  }

  public long readU32()
    throws WireParseException
  {
    require(4);
    byte[] arrayOfByte1 = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    int j = 0xFF & arrayOfByte1[i];
    byte[] arrayOfByte2 = this.array;
    int k = this.pos;
    this.pos = (k + 1);
    int m = 0xFF & arrayOfByte2[k];
    byte[] arrayOfByte3 = this.array;
    int n = this.pos;
    this.pos = (n + 1);
    int i1 = 0xFF & arrayOfByte3[n];
    byte[] arrayOfByte4 = this.array;
    int i2 = this.pos;
    this.pos = (i2 + 1);
    int i3 = 0xFF & arrayOfByte4[i2];
    return (j << 24) + (m << 16) + (i1 << 8) + i3;
  }

  public int readU8()
    throws WireParseException
  {
    require(1);
    byte[] arrayOfByte = this.array;
    int i = this.pos;
    this.pos = (i + 1);
    return 0xFF & arrayOfByte[i];
  }

  public int remaining()
  {
    return this.end - this.pos;
  }

  public void restore()
  {
    if (this.saved_pos < 0)
      throw new IllegalStateException("no previous state");
    this.pos = this.saved_pos;
    this.end = this.saved_end;
    this.saved_pos = -1;
    this.saved_end = -1;
  }

  public void restoreActive(int paramInt)
  {
    if (paramInt > this.array.length)
      throw new IllegalArgumentException("cannot set active region past end of input");
    this.end = paramInt;
  }

  public void save()
  {
    this.saved_pos = this.pos;
    this.saved_end = this.end;
  }

  public int saveActive()
  {
    return this.end;
  }

  public void setActive(int paramInt)
  {
    if (paramInt > this.array.length - this.pos)
      throw new IllegalArgumentException("cannot set active region past end of input");
    this.end = (paramInt + this.pos);
  }
}