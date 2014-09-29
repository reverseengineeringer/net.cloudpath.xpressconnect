package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class OtherHashAlgAndValue extends ASN1Encodable
{
  private AlgorithmIdentifier hashAlgorithm;
  private ASN1OctetString hashValue;

  private OtherHashAlgAndValue(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.hashAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.hashValue = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public OtherHashAlgAndValue(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.hashAlgorithm = paramAlgorithmIdentifier;
    this.hashValue = paramASN1OctetString;
  }

  public static OtherHashAlgAndValue getInstance(Object paramObject)
  {
    if ((paramObject instanceof OtherHashAlgAndValue))
      return (OtherHashAlgAndValue)paramObject;
    if (paramObject != null)
      return new OtherHashAlgAndValue(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    return this.hashAlgorithm;
  }

  public ASN1OctetString getHashValue()
  {
    return this.hashValue;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.hashAlgorithm);
    localASN1EncodableVector.add(this.hashValue);
    return new DERSequence(localASN1EncodableVector);
  }
}