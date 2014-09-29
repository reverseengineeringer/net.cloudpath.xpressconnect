package jcifs.dcerpc.ndr;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import jcifs.util.Encdec;

public class NdrBuffer
{
  public byte[] buf;
  public NdrBuffer deferred;
  public int index;
  public int length;
  int referent;
  HashMap referents;
  public int start;

  public NdrBuffer(byte[] paramArrayOfByte, int paramInt)
  {
    this.buf = paramArrayOfByte;
    this.index = paramInt;
    this.start = paramInt;
    this.length = 0;
    this.deferred = this;
  }

  private int getDceReferent(Object paramObject)
  {
    if (this.referents == null)
    {
      this.referents = new HashMap();
      this.referent = 1;
    }
    Entry localEntry = (Entry)this.referents.get(paramObject);
    if (localEntry == null)
    {
      localEntry = new Entry();
      int i = this.referent;
      this.referent = (i + 1);
      localEntry.referent = i;
      localEntry.obj = paramObject;
      this.referents.put(paramObject, localEntry);
    }
    return localEntry.referent;
  }

  public void advance(int paramInt)
  {
    this.index = (paramInt + this.index);
    if (this.index - this.start > this.deferred.length)
      this.deferred.length = (this.index - this.start);
  }

  public int align(int paramInt)
  {
    int i = paramInt - 1;
    int j = this.index - this.start;
    int k = (j + i & (i ^ 0xFFFFFFFF)) - j;
    advance(k);
    return k;
  }

  public int align(int paramInt, byte paramByte)
  {
    int i = align(paramInt);
    for (int j = i; j > 0; j--)
      this.buf[(this.index - j)] = paramByte;
    return i;
  }

  public long dec_ndr_hyper()
  {
    align(8);
    long l = Encdec.dec_uint64le(this.buf, this.index);
    advance(8);
    return l;
  }

  public int dec_ndr_long()
  {
    align(4);
    int i = Encdec.dec_uint32le(this.buf, this.index);
    advance(4);
    return i;
  }

  public int dec_ndr_short()
  {
    align(2);
    int i = Encdec.dec_uint16le(this.buf, this.index);
    advance(2);
    return i;
  }

  public int dec_ndr_small()
  {
    int i = 0xFF & this.buf[this.index];
    advance(1);
    return i;
  }

  public String dec_ndr_string()
    throws NdrException
  {
    align(4);
    int i = this.index;
    int j = Encdec.dec_uint32le(this.buf, i);
    int k = i + 12;
    Object localObject = null;
    int m;
    if (j != 0)
    {
      m = 2 * (j - 1);
      if ((m >= 0) && (m <= 65535))
        break label78;
      try
      {
        throw new NdrException("invalid array conformance");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
      }
    }
    while (true)
    {
      advance(k - this.index);
      return localObject;
      label78: String str = new String(this.buf, k, m, "UnicodeLittleUnmarked");
      k += m + 2;
      localObject = str;
    }
  }

  public NdrBuffer derive(int paramInt)
  {
    NdrBuffer localNdrBuffer = new NdrBuffer(this.buf, this.start);
    localNdrBuffer.index = paramInt;
    localNdrBuffer.deferred = this.deferred;
    return localNdrBuffer;
  }

  public void enc_ndr_hyper(long paramLong)
  {
    align(8);
    Encdec.enc_uint64le(paramLong, this.buf, this.index);
    advance(8);
  }

  public void enc_ndr_long(int paramInt)
  {
    align(4);
    Encdec.enc_uint32le(paramInt, this.buf, this.index);
    advance(4);
  }

  public void enc_ndr_referent(Object paramObject, int paramInt)
  {
    if (paramObject == null)
    {
      enc_ndr_long(0);
      return;
    }
    switch (paramInt)
    {
    default:
      return;
    case 1:
    case 3:
      enc_ndr_long(System.identityHashCode(paramObject));
      return;
    case 2:
    }
    enc_ndr_long(getDceReferent(paramObject));
  }

  public void enc_ndr_short(int paramInt)
  {
    align(2);
    Encdec.enc_uint16le((short)paramInt, this.buf, this.index);
    advance(2);
  }

  public void enc_ndr_small(int paramInt)
  {
    this.buf[this.index] = ((byte)(paramInt & 0xFF));
    advance(1);
  }

  public void enc_ndr_string(String paramString)
  {
    align(4);
    int i = this.index;
    int j = paramString.length();
    Encdec.enc_uint32le(j + 1, this.buf, i);
    int k = i + 4;
    Encdec.enc_uint32le(0, this.buf, k);
    int m = k + 4;
    Encdec.enc_uint32le(j + 1, this.buf, m);
    int n = m + 4;
    try
    {
      System.arraycopy(paramString.getBytes("UnicodeLittleUnmarked"), 0, this.buf, n, j * 2);
      label92: int i1 = n + j * 2;
      byte[] arrayOfByte1 = this.buf;
      int i2 = i1 + 1;
      arrayOfByte1[i1] = 0;
      byte[] arrayOfByte2 = this.buf;
      int i3 = i2 + 1;
      arrayOfByte2[i2] = 0;
      advance(i3 - this.index);
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      break label92;
    }
  }

  public byte[] getBuffer()
  {
    return this.buf;
  }

  public int getCapacity()
  {
    return this.buf.length - this.start;
  }

  public int getIndex()
  {
    return this.index;
  }

  public int getLength()
  {
    return this.deferred.length;
  }

  public int getTailSpace()
  {
    return this.buf.length - this.index;
  }

  public void readOctetArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(this.buf, this.index, paramArrayOfByte, paramInt1, paramInt2);
    advance(paramInt2);
  }

  public void reset()
  {
    this.index = this.start;
    this.length = 0;
    this.deferred = this;
  }

  public void setIndex(int paramInt)
  {
    this.index = paramInt;
  }

  public String toString()
  {
    return "start=" + this.start + ",index=" + this.index + ",length=" + getLength();
  }

  public void writeOctetArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.index, paramInt2);
    advance(paramInt2);
  }

  static class Entry
  {
    Object obj;
    int referent;
  }
}