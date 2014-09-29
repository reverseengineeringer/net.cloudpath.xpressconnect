package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class Challenge extends ASN1Encodable
{
  private ASN1OctetString challenge;
  private AlgorithmIdentifier owf;
  private ASN1OctetString witness;

  private Challenge(ASN1Sequence paramASN1Sequence)
  {
    int i = paramASN1Sequence.size();
    int j = 0;
    if (i == 3)
    {
      int m = 0 + 1;
      this.owf = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
      j = m;
    }
    int k = j + 1;
    this.witness = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(j));
    this.challenge = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(k));
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(paramASN1Encodable);
  }

  public static Challenge getInstance(Object paramObject)
  {
    if ((paramObject instanceof Challenge))
      return (Challenge)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Challenge((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public AlgorithmIdentifier getOwf()
  {
    return this.owf;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    addOptional(localASN1EncodableVector, this.owf);
    localASN1EncodableVector.add(this.witness);
    localASN1EncodableVector.add(this.challenge);
    return new DERSequence(localASN1EncodableVector);
  }
}