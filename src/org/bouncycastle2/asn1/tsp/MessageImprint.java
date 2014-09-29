package org.bouncycastle2.asn1.tsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class MessageImprint extends ASN1Encodable
{
  AlgorithmIdentifier hashAlgorithm;
  byte[] hashedMessage;

  public MessageImprint(ASN1Sequence paramASN1Sequence)
  {
    this.hashAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.hashedMessage = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(1)).getOctets();
  }

  public MessageImprint(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.hashAlgorithm = paramAlgorithmIdentifier;
    this.hashedMessage = paramArrayOfByte;
  }

  public static MessageImprint getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof MessageImprint)))
      return (MessageImprint)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new MessageImprint((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Bad object in factory.");
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    return this.hashAlgorithm;
  }

  public byte[] getHashedMessage()
  {
    return this.hashedMessage;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.hashAlgorithm);
    localASN1EncodableVector.add(new DEROctetString(this.hashedMessage));
    return new DERSequence(localASN1EncodableVector);
  }
}