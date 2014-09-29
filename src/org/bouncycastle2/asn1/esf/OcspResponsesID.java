package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class OcspResponsesID extends ASN1Encodable
{
  private OcspIdentifier ocspIdentifier;
  private OtherHash ocspRepHash;

  private OcspResponsesID(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.ocspIdentifier = OcspIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.ocspRepHash = OtherHash.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public OcspResponsesID(OcspIdentifier paramOcspIdentifier)
  {
    this(paramOcspIdentifier, null);
  }

  public OcspResponsesID(OcspIdentifier paramOcspIdentifier, OtherHash paramOtherHash)
  {
    this.ocspIdentifier = paramOcspIdentifier;
    this.ocspRepHash = paramOtherHash;
  }

  public static OcspResponsesID getInstance(Object paramObject)
  {
    if ((paramObject instanceof OcspResponsesID))
      return (OcspResponsesID)paramObject;
    if (paramObject != null)
      return new OcspResponsesID(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public OcspIdentifier getOcspIdentifier()
  {
    return this.ocspIdentifier;
  }

  public OtherHash getOcspRepHash()
  {
    return this.ocspRepHash;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.ocspIdentifier);
    if (this.ocspRepHash != null)
      localASN1EncodableVector.add(this.ocspRepHash);
    return new DERSequence(localASN1EncodableVector);
  }
}