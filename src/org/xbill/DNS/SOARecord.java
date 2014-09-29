package org.xbill.DNS;

import java.io.IOException;

public class SOARecord extends Record
{
  private static final long serialVersionUID = 1049740098229303931L;
  private Name admin;
  private long expire;
  private Name host;
  private long minimum;
  private long refresh;
  private long retry;
  private long serial;

  SOARecord()
  {
  }

  public SOARecord(Name paramName1, int paramInt, long paramLong1, Name paramName2, Name paramName3, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6)
  {
    super(paramName1, 6, paramInt, paramLong1);
    this.host = checkName("host", paramName2);
    this.admin = checkName("admin", paramName3);
    this.serial = checkU32("serial", paramLong2);
    this.refresh = checkU32("refresh", paramLong3);
    this.retry = checkU32("retry", paramLong4);
    this.expire = checkU32("expire", paramLong5);
    this.minimum = checkU32("minimum", paramLong6);
  }

  public Name getAdmin()
  {
    return this.admin;
  }

  public long getExpire()
  {
    return this.expire;
  }

  public Name getHost()
  {
    return this.host;
  }

  public long getMinimum()
  {
    return this.minimum;
  }

  Record getObject()
  {
    return new SOARecord();
  }

  public long getRefresh()
  {
    return this.refresh;
  }

  public long getRetry()
  {
    return this.retry;
  }

  public long getSerial()
  {
    return this.serial;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.host = paramTokenizer.getName(paramName);
    this.admin = paramTokenizer.getName(paramName);
    this.serial = paramTokenizer.getUInt32();
    this.refresh = paramTokenizer.getTTLLike();
    this.retry = paramTokenizer.getTTLLike();
    this.expire = paramTokenizer.getTTLLike();
    this.minimum = paramTokenizer.getTTLLike();
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.host = new Name(paramDNSInput);
    this.admin = new Name(paramDNSInput);
    this.serial = paramDNSInput.readU32();
    this.refresh = paramDNSInput.readU32();
    this.retry = paramDNSInput.readU32();
    this.expire = paramDNSInput.readU32();
    this.minimum = paramDNSInput.readU32();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.host);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.admin);
    if (Options.check("multiline"))
    {
      localStringBuffer.append(" (\n\t\t\t\t\t");
      localStringBuffer.append(this.serial);
      localStringBuffer.append("\t; serial\n\t\t\t\t\t");
      localStringBuffer.append(this.refresh);
      localStringBuffer.append("\t; refresh\n\t\t\t\t\t");
      localStringBuffer.append(this.retry);
      localStringBuffer.append("\t; retry\n\t\t\t\t\t");
      localStringBuffer.append(this.expire);
      localStringBuffer.append("\t; expire\n\t\t\t\t\t");
      localStringBuffer.append(this.minimum);
      localStringBuffer.append(" )\t; minimum");
    }
    while (true)
    {
      return localStringBuffer.toString();
      localStringBuffer.append(" ");
      localStringBuffer.append(this.serial);
      localStringBuffer.append(" ");
      localStringBuffer.append(this.refresh);
      localStringBuffer.append(" ");
      localStringBuffer.append(this.retry);
      localStringBuffer.append(" ");
      localStringBuffer.append(this.expire);
      localStringBuffer.append(" ");
      localStringBuffer.append(this.minimum);
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.host.toWire(paramDNSOutput, paramCompression, paramBoolean);
    this.admin.toWire(paramDNSOutput, paramCompression, paramBoolean);
    paramDNSOutput.writeU32(this.serial);
    paramDNSOutput.writeU32(this.refresh);
    paramDNSOutput.writeU32(this.retry);
    paramDNSOutput.writeU32(this.expire);
    paramDNSOutput.writeU32(this.minimum);
  }
}