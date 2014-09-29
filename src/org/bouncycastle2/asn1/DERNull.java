package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERNull extends ASN1Null
{
  public static final DERNull INSTANCE = new DERNull();
  byte[] zeroBytes = new byte[0];

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(5, this.zeroBytes);
  }
}