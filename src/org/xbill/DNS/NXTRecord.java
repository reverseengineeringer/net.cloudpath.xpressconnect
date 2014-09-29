package org.xbill.DNS;

import java.io.IOException;
import java.util.BitSet;

public class NXTRecord extends Record
{
  private static final long serialVersionUID = -8851454400765507520L;
  private BitSet bitmap;
  private Name next;

  NXTRecord()
  {
  }

  public NXTRecord(Name paramName1, int paramInt, long paramLong, Name paramName2, BitSet paramBitSet)
  {
    super(paramName1, 30, paramInt, paramLong);
    this.next = checkName("next", paramName2);
    this.bitmap = paramBitSet;
  }

  public BitSet getBitmap()
  {
    return this.bitmap;
  }

  public Name getNext()
  {
    return this.next;
  }

  Record getObject()
  {
    return new NXTRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.next = paramTokenizer.getName(paramName);
    this.bitmap = new BitSet();
    while (true)
    {
      Tokenizer.Token localToken = paramTokenizer.get();
      if (!localToken.isString())
      {
        paramTokenizer.unget();
        return;
      }
      int i = Type.value(localToken.value, true);
      if ((i <= 0) || (i > 128))
        throw paramTokenizer.exception("Invalid type: " + localToken.value);
      this.bitmap.set(i);
    }
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.next = new Name(paramDNSInput);
    this.bitmap = new BitSet();
    int i = paramDNSInput.remaining();
    for (int j = 0; j < i; j++)
    {
      int k = paramDNSInput.readU8();
      for (int m = 0; m < 8; m++)
        if ((k & 1 << 7 - m) != 0)
          this.bitmap.set(m + j * 8);
    }
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.next);
    int i = this.bitmap.length();
    for (int j = 0; j < i; j = (short)(j + 1))
      if (this.bitmap.get(j))
      {
        localStringBuffer.append(" ");
        localStringBuffer.append(Type.string(j));
      }
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.next.toWire(paramDNSOutput, null, paramBoolean);
    int i = this.bitmap.length();
    int j = 0;
    int k = 0;
    if (j < i)
    {
      if (this.bitmap.get(j));
      for (int m = 1 << 7 - j % 8; ; m = 0)
      {
        k |= m;
        if ((j % 8 == 7) || (j == i - 1))
        {
          paramDNSOutput.writeU8(k);
          k = 0;
        }
        j++;
        break;
      }
    }
  }
}