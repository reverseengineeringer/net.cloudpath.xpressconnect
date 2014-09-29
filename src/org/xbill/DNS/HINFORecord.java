package org.xbill.DNS;

import java.io.IOException;

public class HINFORecord extends Record
{
  private static final long serialVersionUID = -4732870630947452112L;
  private byte[] cpu;
  private byte[] os;

  HINFORecord()
  {
  }

  public HINFORecord(Name paramName, int paramInt, long paramLong, String paramString1, String paramString2)
  {
    super(paramName, 13, paramInt, paramLong);
    try
    {
      this.cpu = byteArrayFromString(paramString1);
      this.os = byteArrayFromString(paramString2);
      return;
    }
    catch (TextParseException localTextParseException)
    {
      throw new IllegalArgumentException(localTextParseException.getMessage());
    }
  }

  public String getCPU()
  {
    return byteArrayToString(this.cpu, false);
  }

  public String getOS()
  {
    return byteArrayToString(this.os, false);
  }

  Record getObject()
  {
    return new HINFORecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    try
    {
      this.cpu = byteArrayFromString(paramTokenizer.getString());
      this.os = byteArrayFromString(paramTokenizer.getString());
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
    this.cpu = paramDNSInput.readCountedString();
    this.os = paramDNSInput.readCountedString();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(byteArrayToString(this.cpu, true));
    localStringBuffer.append(" ");
    localStringBuffer.append(byteArrayToString(this.os, true));
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeCountedString(this.cpu);
    paramDNSOutput.writeCountedString(this.os);
  }
}