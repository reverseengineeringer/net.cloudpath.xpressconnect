package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class KeyAgreeRecipientInfo extends ASN1Encodable
{
  private AlgorithmIdentifier keyEncryptionAlgorithm;
  private OriginatorIdentifierOrKey originator;
  private ASN1Sequence recipientEncryptedKeys;
  private ASN1OctetString ukm;
  private DERInteger version;

  public KeyAgreeRecipientInfo(ASN1Sequence paramASN1Sequence)
  {
    int i = 0 + 1;
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    int j = i + 1;
    this.originator = OriginatorIdentifierOrKey.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(i), true);
    if ((paramASN1Sequence.getObjectAt(j) instanceof ASN1TaggedObject))
    {
      int m = j + 1;
      this.ukm = ASN1OctetString.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(j), true);
      j = m;
    }
    int k = j + 1;
    this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(j));
    (k + 1);
    this.recipientEncryptedKeys = ((ASN1Sequence)paramASN1Sequence.getObjectAt(k));
  }

  public KeyAgreeRecipientInfo(OriginatorIdentifierOrKey paramOriginatorIdentifierOrKey, ASN1OctetString paramASN1OctetString, AlgorithmIdentifier paramAlgorithmIdentifier, ASN1Sequence paramASN1Sequence)
  {
    this.version = new DERInteger(3);
    this.originator = paramOriginatorIdentifierOrKey;
    this.ukm = paramASN1OctetString;
    this.keyEncryptionAlgorithm = paramAlgorithmIdentifier;
    this.recipientEncryptedKeys = paramASN1Sequence;
  }

  public static KeyAgreeRecipientInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof KeyAgreeRecipientInfo)))
      return (KeyAgreeRecipientInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new KeyAgreeRecipientInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Illegal object in KeyAgreeRecipientInfo: " + paramObject.getClass().getName());
  }

  public static KeyAgreeRecipientInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getKeyEncryptionAlgorithm()
  {
    return this.keyEncryptionAlgorithm;
  }

  public OriginatorIdentifierOrKey getOriginator()
  {
    return this.originator;
  }

  public ASN1Sequence getRecipientEncryptedKeys()
  {
    return this.recipientEncryptedKeys;
  }

  public ASN1OctetString getUserKeyingMaterial()
  {
    return this.ukm;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.originator));
    if (this.ukm != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.ukm));
    localASN1EncodableVector.add(this.keyEncryptionAlgorithm);
    localASN1EncodableVector.add(this.recipientEncryptedKeys);
    return new DERSequence(localASN1EncodableVector);
  }
}