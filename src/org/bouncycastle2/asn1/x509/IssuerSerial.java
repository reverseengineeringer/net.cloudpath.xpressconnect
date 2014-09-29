package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class IssuerSerial extends ASN1Encodable
{
  GeneralNames issuer;
  DERBitString issuerUID;
  DERInteger serial;

  public IssuerSerial(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() != 2) && (paramASN1Sequence.size() != 3))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.issuer = GeneralNames.getInstance(paramASN1Sequence.getObjectAt(0));
    this.serial = DERInteger.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() == 3)
      this.issuerUID = DERBitString.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public IssuerSerial(GeneralNames paramGeneralNames, DERInteger paramDERInteger)
  {
    this.issuer = paramGeneralNames;
    this.serial = paramDERInteger;
  }

  public static IssuerSerial getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof IssuerSerial)))
      return (IssuerSerial)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new IssuerSerial((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static IssuerSerial getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public GeneralNames getIssuer()
  {
    return this.issuer;
  }

  public DERBitString getIssuerUID()
  {
    return this.issuerUID;
  }

  public DERInteger getSerial()
  {
    return this.serial;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.issuer);
    localASN1EncodableVector.add(this.serial);
    if (this.issuerUID != null)
      localASN1EncodableVector.add(this.issuerUID);
    return new DERSequence(localASN1EncodableVector);
  }
}