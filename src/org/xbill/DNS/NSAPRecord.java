package org.xbill.DNS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class NSAPRecord extends Record
{
  private static final long serialVersionUID = -1037209403185658593L;
  private byte[] address;

  NSAPRecord()
  {
  }

  public NSAPRecord(Name paramName, int paramInt, long paramLong, String paramString)
  {
    super(paramName, 22, paramInt, paramLong);
    this.address = checkAndConvertAddress(paramString);
    if (this.address == null)
      throw new IllegalArgumentException("invalid NSAP address " + paramString);
  }

  private static final byte[] checkAndConvertAddress(String paramString)
  {
    if (!paramString.substring(0, 2).equalsIgnoreCase("0x"));
    ByteArrayOutputStream localByteArrayOutputStream;
    int i;
    label31: 
    do
    {
      return null;
      localByteArrayOutputStream = new ByteArrayOutputStream();
      i = 0;
      int j = 0;
      int k = 2;
      if (k < paramString.length())
      {
        char c = paramString.charAt(k);
        if (c == '.');
        while (true)
        {
          k++;
          break label31;
          int m = Character.digit(c, 16);
          if (m == -1)
            break;
          if (i != 0)
          {
            j += m;
            localByteArrayOutputStream.write(j);
            i = 0;
          }
          else
          {
            j = m << 4;
            i = 1;
          }
        }
      }
    }
    while (i != 0);
    return localByteArrayOutputStream.toByteArray();
  }

  public String getAddress()
  {
    return byteArrayToString(this.address, false);
  }

  Record getObject()
  {
    return new NSAPRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    String str = paramTokenizer.getString();
    this.address = checkAndConvertAddress(str);
    if (this.address == null)
      throw paramTokenizer.exception("invalid NSAP address " + str);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.address = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    return "0x" + base16.toString(this.address);
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeByteArray(this.address);
  }
}