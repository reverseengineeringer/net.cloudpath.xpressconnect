package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CAKeyUpdAnnContent extends ASN1Encodable
{
  private CMPCertificate newWithNew;
  private CMPCertificate newWithOld;
  private CMPCertificate oldWithNew;

  private CAKeyUpdAnnContent(ASN1Sequence paramASN1Sequence)
  {
    this.oldWithNew = CMPCertificate.getInstance(paramASN1Sequence.getObjectAt(0));
    this.newWithOld = CMPCertificate.getInstance(paramASN1Sequence.getObjectAt(1));
    this.newWithNew = CMPCertificate.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public static CAKeyUpdAnnContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof CAKeyUpdAnnContent))
      return (CAKeyUpdAnnContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CAKeyUpdAnnContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CMPCertificate getNewWithNew()
  {
    return this.newWithNew;
  }

  public CMPCertificate getNewWithOld()
  {
    return this.newWithOld;
  }

  public CMPCertificate getOldWithNew()
  {
    return this.oldWithNew;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.oldWithNew);
    localASN1EncodableVector.add(this.newWithOld);
    localASN1EncodableVector.add(this.newWithNew);
    return new DERSequence(localASN1EncodableVector);
  }
}