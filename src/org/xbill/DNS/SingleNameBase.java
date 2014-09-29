package org.xbill.DNS;

import java.io.IOException;

abstract class SingleNameBase extends Record
{
  private static final long serialVersionUID = -18595042501413L;
  protected Name singleName;

  protected SingleNameBase()
  {
  }

  protected SingleNameBase(Name paramName, int paramInt1, int paramInt2, long paramLong)
  {
    super(paramName, paramInt1, paramInt2, paramLong);
  }

  protected SingleNameBase(Name paramName1, int paramInt1, int paramInt2, long paramLong, Name paramName2, String paramString)
  {
    super(paramName1, paramInt1, paramInt2, paramLong);
    this.singleName = checkName(paramString, paramName2);
  }

  protected Name getSingleName()
  {
    return this.singleName;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.singleName = paramTokenizer.getName(paramName);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.singleName = new Name(paramDNSInput);
  }

  String rrToString()
  {
    return this.singleName.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.singleName.toWire(paramDNSOutput, null, paramBoolean);
  }
}