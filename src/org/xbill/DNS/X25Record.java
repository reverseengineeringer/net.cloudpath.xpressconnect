package org.xbill.DNS;

import java.io.IOException;

public class X25Record extends Record
{
  private static final long serialVersionUID = 4267576252335579764L;
  private byte[] address;

  X25Record()
  {
  }

  public X25Record(Name paramName, int paramInt, long paramLong, String paramString)
  {
    super(paramName, 19, paramInt, paramLong);
    this.address = checkAndConvertAddress(paramString);
    if (this.address == null)
      throw new IllegalArgumentException("invalid PSDN address " + paramString);
  }

  private static final byte[] checkAndConvertAddress(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i];
    for (int j = 0; ; j++)
    {
      char c;
      if (j < i)
      {
        c = paramString.charAt(j);
        if (!Character.isDigit(c))
          arrayOfByte = null;
      }
      else
      {
        return arrayOfByte;
      }
      arrayOfByte[j] = ((byte)c);
    }
  }

  public String getAddress()
  {
    return byteArrayToString(this.address, false);
  }

  Record getObject()
  {
    return new X25Record();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    String str = paramTokenizer.getString();
    this.address = checkAndConvertAddress(str);
    if (this.address == null)
      throw paramTokenizer.exception("invalid PSDN address " + str);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.address = paramDNSInput.readCountedString();
  }

  String rrToString()
  {
    return byteArrayToString(this.address, true);
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeCountedString(this.address);
  }
}