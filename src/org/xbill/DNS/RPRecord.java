package org.xbill.DNS;

import java.io.IOException;

public class RPRecord extends Record
{
  private static final long serialVersionUID = 8124584364211337460L;
  private Name mailbox;
  private Name textDomain;

  RPRecord()
  {
  }

  public RPRecord(Name paramName1, int paramInt, long paramLong, Name paramName2, Name paramName3)
  {
    super(paramName1, 17, paramInt, paramLong);
    this.mailbox = checkName("mailbox", paramName2);
    this.textDomain = checkName("textDomain", paramName3);
  }

  public Name getMailbox()
  {
    return this.mailbox;
  }

  Record getObject()
  {
    return new RPRecord();
  }

  public Name getTextDomain()
  {
    return this.textDomain;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.mailbox = paramTokenizer.getName(paramName);
    this.textDomain = paramTokenizer.getName(paramName);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.mailbox = new Name(paramDNSInput);
    this.textDomain = new Name(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.mailbox);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.textDomain);
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.mailbox.toWire(paramDNSOutput, null, paramBoolean);
    this.textDomain.toWire(paramDNSOutput, null, paramBoolean);
  }
}