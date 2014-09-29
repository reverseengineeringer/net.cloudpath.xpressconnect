package org.bouncycastle2.asn1;

public class ASN1ObjectIdentifier extends DERObjectIdentifier
{
  public ASN1ObjectIdentifier(String paramString)
  {
    super(paramString);
  }

  ASN1ObjectIdentifier(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }

  public ASN1ObjectIdentifier branch(String paramString)
  {
    return new ASN1ObjectIdentifier(getId() + "." + paramString);
  }
}