package org.bouncycastle2.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.x509.Attribute;

public class X509Attribute extends ASN1Encodable
{
  Attribute attr;

  public X509Attribute(String paramString, ASN1Encodable paramASN1Encodable)
  {
    this.attr = new Attribute(new DERObjectIdentifier(paramString), new DERSet(paramASN1Encodable));
  }

  public X509Attribute(String paramString, ASN1EncodableVector paramASN1EncodableVector)
  {
    this.attr = new Attribute(new DERObjectIdentifier(paramString), new DERSet(paramASN1EncodableVector));
  }

  X509Attribute(ASN1Encodable paramASN1Encodable)
  {
    this.attr = Attribute.getInstance(paramASN1Encodable);
  }

  public String getOID()
  {
    return this.attr.getAttrType().getId();
  }

  public ASN1Encodable[] getValues()
  {
    ASN1Set localASN1Set = this.attr.getAttrValues();
    ASN1Encodable[] arrayOfASN1Encodable = new ASN1Encodable[localASN1Set.size()];
    for (int i = 0; ; i++)
    {
      if (i == localASN1Set.size())
        return arrayOfASN1Encodable;
      arrayOfASN1Encodable[i] = ((ASN1Encodable)localASN1Set.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    return this.attr.toASN1Object();
  }
}