package org.bouncycastle2.asn1;

import java.io.IOException;

public abstract class ASN1Object extends DERObject
{
  public static ASN1Object fromByteArray(byte[] paramArrayOfByte)
    throws IOException
  {
    ASN1InputStream localASN1InputStream = new ASN1InputStream(paramArrayOfByte);
    try
    {
      ASN1Object localASN1Object = (ASN1Object)localASN1InputStream.readObject();
      return localASN1Object;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    throw new IOException("cannot recognise object in stream");
  }

  abstract boolean asn1Equals(DERObject paramDERObject);

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    while (((paramObject instanceof DEREncodable)) && (asn1Equals(((DEREncodable)paramObject).getDERObject())))
      return true;
    return false;
  }

  public abstract int hashCode();
}