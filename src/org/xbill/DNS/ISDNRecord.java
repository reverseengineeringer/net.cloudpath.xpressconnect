package org.xbill.DNS;

import java.io.IOException;

public class ISDNRecord extends Record
{
  private static final long serialVersionUID = -8730801385178968798L;
  private byte[] address;
  private byte[] subAddress;

  ISDNRecord()
  {
  }

  public ISDNRecord(Name paramName, int paramInt, long paramLong, String paramString1, String paramString2)
  {
    super(paramName, 20, paramInt, paramLong);
    try
    {
      this.address = byteArrayFromString(paramString1);
      if (paramString2 != null)
        this.subAddress = byteArrayFromString(paramString2);
      return;
    }
    catch (TextParseException localTextParseException)
    {
      throw new IllegalArgumentException(localTextParseException.getMessage());
    }
  }

  public String getAddress()
  {
    return byteArrayToString(this.address, false);
  }

  Record getObject()
  {
    return new ISDNRecord();
  }

  public String getSubAddress()
  {
    if (this.subAddress == null)
      return null;
    return byteArrayToString(this.subAddress, false);
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    try
    {
      this.address = byteArrayFromString(paramTokenizer.getString());
      Tokenizer.Token localToken = paramTokenizer.get();
      if (localToken.isString())
      {
        this.subAddress = byteArrayFromString(localToken.value);
        return;
      }
      paramTokenizer.unget();
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
    this.address = paramDNSInput.readCountedString();
    if (paramDNSInput.remaining() > 0)
      this.subAddress = paramDNSInput.readCountedString();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(byteArrayToString(this.address, true));
    if (this.subAddress != null)
    {
      localStringBuffer.append(" ");
      localStringBuffer.append(byteArrayToString(this.subAddress, true));
    }
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeCountedString(this.address);
    if (this.subAddress != null)
      paramDNSOutput.writeCountedString(this.subAddress);
  }
}