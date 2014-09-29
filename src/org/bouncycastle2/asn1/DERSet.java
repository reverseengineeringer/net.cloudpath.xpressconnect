package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class DERSet extends ASN1Set
{
  public DERSet()
  {
  }

  public DERSet(ASN1EncodableVector paramASN1EncodableVector)
  {
    this(paramASN1EncodableVector, true);
  }

  DERSet(ASN1EncodableVector paramASN1EncodableVector, boolean paramBoolean)
  {
    for (int i = 0; ; i++)
    {
      if (i == paramASN1EncodableVector.size())
      {
        if (paramBoolean)
          sort();
        return;
      }
      addObject(paramASN1EncodableVector.get(i));
    }
  }

  public DERSet(DEREncodable paramDEREncodable)
  {
    addObject(paramDEREncodable);
  }

  public DERSet(ASN1Encodable[] paramArrayOfASN1Encodable)
  {
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfASN1Encodable.length)
      {
        sort();
        return;
      }
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
        paramDEROutputStream.writeEncoded(49, localByteArrayOutputStream.toByteArray());
        return;
      }
      localDEROutputStream.writeObject(localEnumeration.nextElement());
    }
  }
}