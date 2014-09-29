package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CrlValidatedID extends ASN1Encodable
{
  private OtherHash crlHash;
  private CrlIdentifier crlIdentifier;

  private CrlValidatedID(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.crlHash = OtherHash.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.crlIdentifier = CrlIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public CrlValidatedID(OtherHash paramOtherHash)
  {
    this(paramOtherHash, null);
  }

  public CrlValidatedID(OtherHash paramOtherHash, CrlIdentifier paramCrlIdentifier)
  {
    this.crlHash = paramOtherHash;
    this.crlIdentifier = paramCrlIdentifier;
  }

  public static CrlValidatedID getInstance(Object paramObject)
  {
    if ((paramObject instanceof CrlValidatedID))
      return (CrlValidatedID)paramObject;
    if (paramObject != null)
      return new CrlValidatedID(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public OtherHash getCrlHash()
  {
    return this.crlHash;
  }

  public CrlIdentifier getCrlIdentifier()
  {
    return this.crlIdentifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.crlHash.toASN1Object());
    if (this.crlIdentifier != null)
      localASN1EncodableVector.add(this.crlIdentifier.toASN1Object());
    return new DERSequence(localASN1EncodableVector);
  }
}