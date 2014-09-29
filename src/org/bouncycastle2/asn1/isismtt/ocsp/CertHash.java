package org.bouncycastle2.asn1.isismtt.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class CertHash extends ASN1Encodable
{
  private byte[] certificateHash;
  private AlgorithmIdentifier hashAlgorithm;

  private CertHash(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.hashAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.certificateHash = DEROctetString.getInstance(paramASN1Sequence.getObjectAt(1)).getOctets();
  }

  public CertHash(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.hashAlgorithm = paramAlgorithmIdentifier;
    this.certificateHash = new byte[paramArrayOfByte.length];
    System.arraycopy(paramArrayOfByte, 0, this.certificateHash, 0, paramArrayOfByte.length);
  }

  public static CertHash getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof CertHash)))
      return (CertHash)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertHash((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public byte[] getCertificateHash()
  {
    return this.certificateHash;
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    return this.hashAlgorithm;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.hashAlgorithm);
    localASN1EncodableVector.add(new DEROctetString(this.certificateHash));
    return new DERSequence(localASN1EncodableVector);
  }
}