package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;

public class AAAARecord extends Record
{
  private static final long serialVersionUID = -4588601512069748050L;
  private InetAddress address;

  AAAARecord()
  {
  }

  public AAAARecord(Name paramName, int paramInt, long paramLong, InetAddress paramInetAddress)
  {
    super(paramName, 28, paramInt, paramLong);
    if (Address.familyOf(paramInetAddress) != 2)
      throw new IllegalArgumentException("invalid IPv6 address");
    this.address = paramInetAddress;
  }

  public InetAddress getAddress()
  {
    return this.address;
  }

  Record getObject()
  {
    return new AAAARecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.address = paramTokenizer.getAddress(2);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.address = InetAddress.getByAddress(paramDNSInput.readByteArray(16));
  }

  String rrToString()
  {
    return this.address.getHostAddress();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeByteArray(this.address.getAddress());
  }
}