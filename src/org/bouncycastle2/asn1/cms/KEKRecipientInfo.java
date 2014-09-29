package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class KEKRecipientInfo extends ASN1Encodable
{
  private ASN1OctetString encryptedKey;
  private KEKIdentifier kekid;
  private AlgorithmIdentifier keyEncryptionAlgorithm;
  private DERInteger version;

  public KEKRecipientInfo(ASN1Sequence paramASN1Sequence)
  {
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    this.kekid = KEKIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(2));
    this.encryptedKey = ((ASN1OctetString)paramASN1Sequence.getObjectAt(3));
  }

  public KEKRecipientInfo(KEKIdentifier paramKEKIdentifier, AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.version = new DERInteger(4);
    this.kekid = paramKEKIdentifier;
    this.keyEncryptionAlgorithm = paramAlgorithmIdentifier;
    this.encryptedKey = paramASN1OctetString;
  }

  public static KEKRecipientInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof KEKRecipientInfo)))
      return (KEKRecipientInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new KEKRecipientInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid KEKRecipientInfo: " + paramObject.getClass().getName());
  }

  public static KEKRecipientInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1OctetString getEncryptedKey()
  {
    return this.encryptedKey;
  }

  public KEKIdentifier getKekid()
  {
    return this.kekid;
  }

  public AlgorithmIdentifier getKeyEncryptionAlgorithm()
  {
    return this.keyEncryptionAlgorithm;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.kekid);
    localASN1EncodableVector.add(this.keyEncryptionAlgorithm);
    localASN1EncodableVector.add(this.encryptedKey);
    return new DERSequence(localASN1EncodableVector);
  }
}