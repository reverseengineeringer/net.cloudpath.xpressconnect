package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class GenericEDNSOption extends EDNSOption
{
  private byte[] data;

  GenericEDNSOption(int paramInt)
  {
    super(paramInt);
  }

  public GenericEDNSOption(int paramInt, byte[] paramArrayOfByte)
  {
    super(paramInt);
    this.data = Record.checkByteArrayLength("option data", paramArrayOfByte, 65535);
  }

  void optionFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.data = paramDNSInput.readByteArray();
  }

  String optionToString()
  {
    return "<" + base16.toString(this.data) + ">";
  }

  void optionToWire(DNSOutput paramDNSOutput)
  {
    paramDNSOutput.writeByteArray(this.data);
  }
}