package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class DERSequence extends ASN1Sequence
{
  public DERSequence()
  {
  }

  public DERSequence(ASN1EncodableVector paramASN1EncodableVector)
  {
    for (int i = 0; ; i++)
    {
      if (i == paramASN1EncodableVector.size())
        return;
      addObject(paramASN1EncodableVector.get(i));
    }
  }

  public DERSequence(DEREncodable paramDEREncodable)
  {
    addObject(paramDEREncodable);
  }

  public DERSequence(ASN1Encodable[] paramArrayOfASN1Encodable)
  {
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfASN1Encodable.length)
        return;
      addObject(paramArrayOfASN1Encodable[i]);
    }
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    Enumeration localEnumeration = getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        localDEROutputStream.close();
        paramDEROutputStream.writeEncoded(48, localByteArrayOutputStream.toByteArray());
        return;
      }
      localDEROutputStream.writeObject(localEnumeration.nextElement());
    }
  }
}