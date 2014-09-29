package org.bouncycastle2.asn1.mozilla;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;

public class PublicKeyAndChallenge extends ASN1Encodable
{
  private DERIA5String challenge;
  private ASN1Sequence pkacSeq;
  private SubjectPublicKeyInfo spki;

  public PublicKeyAndChallenge(ASN1Sequence paramASN1Sequence)
  {
    this.pkacSeq = paramASN1Sequence;
    this.spki = SubjectPublicKeyInfo.getInstance(paramASN1Sequence.getObjectAt(0));
    this.challenge = DERIA5String.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public static PublicKeyAndChallenge getInstance(Object paramObject)
  {
    if ((paramObject instanceof PublicKeyAndChallenge))
      return (PublicKeyAndChallenge)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PublicKeyAndChallenge((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unkown object in factory: " + paramObject.getClass().getName());
  }

  public DERIA5String getChallenge()
  {
    return this.challenge;
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.spki;
  }

  public DERObject toASN1Object()
  {
    return this.pkacSeq;
  }
}