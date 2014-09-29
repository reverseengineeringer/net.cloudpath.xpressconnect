package org.xbill.DNS;

import java.io.IOException;

public class UNKRecord extends Record
{
  private static final long serialVersionUID = -4193583311594626915L;
  private byte[] data;

  public byte[] getData()
  {
    return this.data;
  }

  Record getObject()
  {
    return new UNKRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    throw paramTokenizer.exception("invalid unknown RR encoding");
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.data = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    return unknownToString(this.data);
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeByteArray(this.data);
  }
}