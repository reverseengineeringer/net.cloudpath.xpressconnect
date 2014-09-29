package org.bouncycastle2.asn1;

import java.io.IOException;

public class BERNull extends DERNull
{
  public static final BERNull INSTANCE = new BERNull();

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(5);
      return;
    }
    super.encode(paramDEROutputStream);
  }
}