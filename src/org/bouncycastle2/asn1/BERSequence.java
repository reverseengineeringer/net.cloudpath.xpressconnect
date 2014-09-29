package org.bouncycastle2.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSequence extends DERSequence
{
  public BERSequence()
  {
  }

  public BERSequence(ASN1EncodableVector paramASN1EncodableVector)
  {
    super(paramASN1EncodableVector);
  }

  public BERSequence(DEREncodable paramDEREncodable)
  {
    super(paramDEREncodable);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(48);
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