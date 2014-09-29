package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;

public class DigestInfo extends ASN1Encodable
{
  private AlgorithmIdentifier algId;
  private byte[] digest;

  public DigestInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.algId = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    this.digest = ASN1OctetString.getInstance(localEnumeration.nextElement()).getOctets();
  }

  public DigestInfo(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.digest = paramArrayOfByte;
    this.algId = paramAlgorithmIdentifier;
  }

  public static DigestInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof DigestInfo))
      return (DigestInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new DigestInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static DigestInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getAlgorithmId()
  {
    return this.algId;
  }

  public byte[] getDigest()
  {
    return this.digest;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.algId);
    localASN1EncodableVector.add(new DEROctetString(this.digest));
    return new DERSequence(localASN1EncodableVector);
  }
}