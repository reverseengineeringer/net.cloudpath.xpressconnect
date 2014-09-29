package org.bouncycastle2.asn1;

import java.io.IOException;

public class DEROctetString extends ASN1OctetString
{
  public DEROctetString(DEREncodable paramDEREncodable)
  {
    super(paramDEREncodable);
  }

  public DEROctetString(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }

  static void encode(DEROutputStream paramDEROutputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(4, paramArrayOfByte);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(4, this.string);
  }
}