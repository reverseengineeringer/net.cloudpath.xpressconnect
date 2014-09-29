package org.bouncycastle2.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSet extends DERSet
{
  public BERSet()
  {
  }

  public BERSet(ASN1EncodableVector paramASN1EncodableVector)
  {
    super(paramASN1EncodableVector, false);
  }

  BERSet(ASN1EncodableVector paramASN1EncodableVector, boolean paramBoolean)
  {
    super(paramASN1EncodableVector, paramBoolean);
  }

  public BERSet(DEREncodable paramDEREncodable)
  {
    super(paramDEREncodable);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(49);
      paramDEROutputStream.write(128);
      Enumeration localEnumeration = getObjects();
      while (true)
      {
        if (!localEnumeration.hasMoreElements())
        {
          paramDEROutputStream.write(0);
          paramDEROutputStream.write(0);
          return;
        }
        paramDEROutputStream.writeObject(localEnumeration.nextElement());
      }
    }
    super.encode(paramDEROutputStream);
  }
}