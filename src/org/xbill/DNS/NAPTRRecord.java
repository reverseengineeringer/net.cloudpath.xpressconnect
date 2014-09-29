package org.xbill.DNS;

import java.io.IOException;

public class NAPTRRecord extends Record
{
  private static final long serialVersionUID = 5191232392044947002L;
  private byte[] flags;
  private int order;
  private int preference;
  private byte[] regexp;
  private Name replacement;
  private byte[] service;

  NAPTRRecord()
  {
  }

  public NAPTRRecord(Name paramName1, int paramInt1, long paramLong, int paramInt2, int paramInt3, String paramString1, String paramString2, String paramString3, Name paramName2)
  {
    super(paramName1, 35, paramInt1, paramLong);
    this.order = checkU16("order", paramInt2);
    this.preference = checkU16("preference", paramInt3);
    try
    {
      this.flags = byteArrayFromString(paramString1);
      this.service = byteArrayFromString(paramString2);
      this.regexp = byteArrayFromString(paramString3);
      this.replacement = checkName("replacement", paramName2);
      return;
    }
    catch (TextParseException localTextParseException)
    {
      throw new IllegalArgumentException(localTextParseException.getMessage());
    }
  }

  public Name getAdditionalName()
  {
    return this.replacement;
  }

  public String getFlags()
  {
    return byteArrayToString(this.flags, false);
  }

  Record getObject()
  {
    return new NAPTRRecord();
  }

  public int getOrder()
  {
    return this.order;
  }

  public int getPreference()
  {
    return this.preference;
  }

  public String getRegexp()
  {
    return byteArrayToString(this.regexp, false);
  }

  public Name getReplacement()
  {
    return this.replacement;
  }

  public String getService()
  {
    return byteArrayToString(this.service, false);
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.order = paramTokenizer.getUInt16();
    this.preference = paramTokenizer.getUInt16();
    try
    {
      this.flags = byteArrayFromString(paramTokenizer.getString());
      this.service = byteArrayFromString(paramTokenizer.getString());
      this.regexp = byteArrayFromString(paramTokenizer.getString());
      this.replacement = paramTokenizer.getName(paramName);
      return;
    }
    catch (TextParseException localTextParseException)
    {
      throw paramTokenizer.exception(localTextParseException.getMessage());
    }
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.order = paramDNSInput.readU16();
    this.preference = paramDNSInput.readU16();
    this.flags = paramDNSInput.readCountedString();
    this.service = paramDNSInput.readCountedString();
    this.regexp = paramDNSInput.readCountedString();
    this.replacement = new Name(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.order);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.preference);
    localStringBuffer.append(" ");
    localStringBuffer.append(byteArrayToString(this.flags, true));
    localStringBuffer.append(" ");
    localStringBuffer.append(byteArrayToString(this.service, true));
    localStringBuffer.append(" ");
    localStringBuffer.append(byteArrayToString(this.regexp, true));
    localStringBuffer.append(" ");
    localStringBuffer.append(this.replacement);
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.order);
    paramDNSOutput.writeU16(this.preference);
    paramDNSOutput.writeCountedString(this.flags);
    paramDNSOutput.writeCountedString(this.service);
    paramDNSOutput.writeCountedString(this.regexp);
    this.replacement.toWire(paramDNSOutput, null, paramBoolean);
  }
}