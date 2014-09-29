package org.xbill.DNS;

import java.io.IOException;

public class MINFORecord extends Record
{
  private static final long serialVersionUID = -3962147172340353796L;
  private Name errorAddress;
  private Name responsibleAddress;

  MINFORecord()
  {
  }

  public MINFORecord(Name paramName1, int paramInt, long paramLong, Name paramName2, Name paramName3)
  {
    super(paramName1, 14, paramInt, paramLong);
    this.responsibleAddress = checkName("responsibleAddress", paramName2);
    this.errorAddress = checkName("errorAddress", paramName3);
  }

  public Name getErrorAddress()
  {
    return this.errorAddress;
  }

  Record getObject()
  {
    return new MINFORecord();
  }

  public Name getResponsibleAddress()
  {
    return this.responsibleAddress;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.responsibleAddress = paramTokenizer.getName(paramName);
    this.errorAddress = paramTokenizer.getName(paramName);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.responsibleAddress = new Name(paramDNSInput);
    this.errorAddress = new Name(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.responsibleAddress);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.errorAddress);
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.responsibleAddress.toWire(paramDNSOutput, null, paramBoolean);
    this.errorAddress.toWire(paramDNSOutput, null, paramBoolean);
  }
}