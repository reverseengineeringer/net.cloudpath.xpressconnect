package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.GeneralName;

public class CertId extends ASN1Encodable
{
  private GeneralName issuer;
  private DERInteger serialNumber;

  private CertId(ASN1Sequence paramASN1Sequence)
  {
    this.issuer = GeneralName.getInstance(paramASN1Sequence.getObjectAt(0));
    this.serialNumber = DERInteger.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public static CertId getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertId))
      return (CertId)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertId((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public static CertId getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public GeneralName getIssuer()
  {
    return this.issuer;
  }

  public DERInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.issuer);
    localASN1EncodableVector.add(this.serialNumber);
    return new DERSequence(localASN1EncodableVector);
  }
}