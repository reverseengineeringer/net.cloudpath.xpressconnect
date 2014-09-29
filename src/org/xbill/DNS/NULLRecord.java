package org.xbill.DNS;

import java.io.IOException;

public class NULLRecord extends Record
{
  private static final long serialVersionUID = -5796493183235216538L;
  private byte[] data;

  NULLRecord()
  {
  }

  public NULLRecord(Name paramName, int paramInt, long paramLong, byte[] paramArrayOfByte)
  {
    super(paramName, 10, paramInt, paramLong);
    if (paramArrayOfByte.length > 65535)
      throw new IllegalArgumentException("data must be <65536 bytes");
    this.data = paramArrayOfByte;
  }

  public byte[] getData()
  {
    return this.data;
  }

  Record getObject()
  {
    return new NULLRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    throw paramTokenizer.exception("no defined text format for NULL records");
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