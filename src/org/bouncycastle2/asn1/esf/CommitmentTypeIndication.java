package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class CommitmentTypeIndication extends ASN1Encodable
{
  private DERObjectIdentifier commitmentTypeId;
  private ASN1Sequence commitmentTypeQualifier;

  public CommitmentTypeIndication(ASN1Sequence paramASN1Sequence)
  {
    this.commitmentTypeId = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.commitmentTypeQualifier = ((ASN1Sequence)paramASN1Sequence.getObjectAt(1));
  }

  public CommitmentTypeIndication(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.commitmentTypeId = paramDERObjectIdentifier;
  }

  public CommitmentTypeIndication(DERObjectIdentifier paramDERObjectIdentifier, ASN1Sequence paramASN1Sequence)
  {
    this.commitmentTypeId = paramDERObjectIdentifier;
    this.commitmentTypeQualifier = paramASN1Sequence;
  }

  public static CommitmentTypeIndication getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof CommitmentTypeIndication)))
      return (CommitmentTypeIndication)paramObject;
    return new CommitmentTypeIndication(ASN1Sequence.getInstance(paramObject));
  }

  public DERObjectIdentifier getCommitmentTypeId()
  {
    return this.commitmentTypeId;
  }

  public ASN1Sequence getCommitmentTypeQualifier()
  {
    return this.commitmentTypeQualifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.commitmentTypeId);
    if (this.commitmentTypeQualifier != null)
      localASN1EncodableVector.add(this.commitmentTypeQualifier);
    return new DERSequence(localASN1EncodableVector);
  }
}