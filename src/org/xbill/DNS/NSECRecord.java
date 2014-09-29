package org.xbill.DNS;

import java.io.IOException;

public class NSECRecord extends Record
{
  private static final long serialVersionUID = -5165065768816265385L;
  private Name next;
  private TypeBitmap types;

  NSECRecord()
  {
  }

  public NSECRecord(Name paramName1, int paramInt, long paramLong, Name paramName2, int[] paramArrayOfInt)
  {
    super(paramName1, 47, paramInt, paramLong);
    this.next = checkName("next", paramName2);
    for (int i = 0; i < paramArrayOfInt.length; i++)
      Type.check(paramArrayOfInt[i]);
    this.types = new TypeBitmap(paramArrayOfInt);
  }

  public Name getNext()
  {
    return this.next;
  }

  Record getObject()
  {
    return new NSECRecord();
  }

  public int[] getTypes()
  {
    return this.types.toArray();
  }

  public boolean hasType(int paramInt)
  {
    return this.types.contains(paramInt);
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.next = paramTokenizer.getName(paramName);
    this.types = new TypeBitmap(paramTokenizer);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.next = new Name(paramDNSInput);
    this.types = new TypeBitmap(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.next);
    if (!this.types.empty())
    {
      localStringBuffer.append(' ');
      localStringBuffer.append(this.types.toString());
    }
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.next.toWire(paramDNSOutput, null, false);
    this.types.toWire(paramDNSOutput);
  }
}