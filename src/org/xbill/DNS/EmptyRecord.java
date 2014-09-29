package org.xbill.DNS;

import java.io.IOException;

class EmptyRecord extends Record
{
  private static final long serialVersionUID = 3601852050646429582L;

  Record getObject()
  {
    return new EmptyRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
  }

  String rrToString()
  {
    return "";
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
  }
}