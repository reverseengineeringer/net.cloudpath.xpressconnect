package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.crmf.CertId;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class OOBCertHash extends ASN1Encodable
{
  private CertId certId;
  private AlgorithmIdentifier hashAlg;
  private DERBitString hashVal;

  private OOBCertHash(ASN1Sequence paramASN1Sequence)
  {
    int i = -1 + paramASN1Sequence.size();
    int j = i - 1;
    this.hashVal = DERBitString.getInstance(paramASN1Sequence.getObjectAt(i));
    int k = j;
    if (k < 0)
      return;
    ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(k);
    if (localASN1TaggedObject.getTagNo() == 0)
      this.hashAlg = AlgorithmIdentifier.getInstance(localASN1TaggedObject, true);
    while (true)
    {
      k--;
      break;
      this.certId = CertId.getInstance(localASN1TaggedObject, true);
    }
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, int paramInt, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(new DERTaggedObject(true, paramInt, paramASN1Encodable));
  }

  public static OOBCertHash getInstance(Object paramObject)
  {
    if ((paramObject instanceof OOBCertHash))
      return (OOBCertHash)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OOBCertHash((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CertId getCertId()
  {
    return this.certId;
  }

  public AlgorithmIdentifier getHashAlg()
  {
    return this.hashAlg;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    addOptional(localASN1EncodableVector, 0, this.hashAlg);
    addOptional(localASN1EncodableVector, 1, this.certId);
    localASN1EncodableVector.add(this.hashVal);
    return new DERSequence(localASN1EncodableVector);
  }
}