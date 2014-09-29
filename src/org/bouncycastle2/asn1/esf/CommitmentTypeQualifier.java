package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class CommitmentTypeQualifier extends ASN1Encodable
{
  private DERObjectIdentifier commitmentTypeIdentifier;
  private DEREncodable qualifier;

  public CommitmentTypeQualifier(ASN1Sequence paramASN1Sequence)
  {
    this.commitmentTypeIdentifier = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.qualifier = paramASN1Sequence.getObjectAt(1);
  }

  public CommitmentTypeQualifier(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this(paramDERObjectIdentifier, null);
  }

  public CommitmentTypeQualifier(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.commitmentTypeIdentifier = paramDERObjectIdentifier;
    this.qualifier = paramDEREncodable;
  }

  public static CommitmentTypeQualifier getInstance(Object paramObject)
  {
    if (((paramObject instanceof CommitmentTypeQualifier)) || (paramObject == null))
      return (CommitmentTypeQualifier)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CommitmentTypeQualifier((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in getInstance.");
  }

  public DERObjectIdentifier getCommitmentTypeIdentifier()
  {
    return this.commitmentTypeIdentifier;
  }

  public DEREncodable getQualifier()
  {
    return this.qualifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.commitmentTypeIdentifier);
    if (this.qualifier != null)
      localASN1EncodableVector.add(this.qualifier);
    return new DERSequence(localASN1EncodableVector);
  }
}