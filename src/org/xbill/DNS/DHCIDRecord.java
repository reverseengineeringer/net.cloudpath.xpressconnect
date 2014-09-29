package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base64;

public class DHCIDRecord extends Record
{
  private static final long serialVersionUID = -8214820200808997707L;
  private byte[] data;

  DHCIDRecord()
  {
  }

  public DHCIDRecord(Name paramName, int paramInt, long paramLong, byte[] paramArrayOfByte)
  {
    super(paramName, 49, paramInt, paramLong);
    this.data = paramArrayOfByte;
  }

  public byte[] getData()
  {
    return this.data;
  }

  Record getObject()
  {
    return new DHCIDRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.data = paramTokenizer.getBase64();
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.data = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    return base64.toString(this.data);
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeByteArray(this.data);
  }
}