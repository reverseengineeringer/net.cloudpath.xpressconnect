package org.bouncycastle2.asn1.cryptopro;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class GOST28147Parameters extends ASN1Encodable
{
  ASN1OctetString iv;
  DERObjectIdentifier paramSet;

  public GOST28147Parameters(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.iv = ((ASN1OctetString)localEnumeration.nextElement());
    this.paramSet = ((DERObjectIdentifier)localEnumeration.nextElement());
  }

  public static GOST28147Parameters getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof GOST28147Parameters)))
      return (GOST28147Parameters)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new GOST28147Parameters((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid GOST3410Parameter: " + paramObject.getClass().getName());
  }

  public static GOST28147Parameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.iv);
    localASN1EncodableVector.add(this.paramSet);
    return new DERSequence(localASN1EncodableVector);
  }
}