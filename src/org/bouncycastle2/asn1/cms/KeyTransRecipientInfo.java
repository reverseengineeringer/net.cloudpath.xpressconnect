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

public class KeyTransRecipientInfo extends ASN1Encodable
{
  private ASN1OctetString encryptedKey;
  private AlgorithmIdentifier keyEncryptionAlgorithm;
  private RecipientIdentifier rid;
  private DERInteger version;

  public KeyTransRecipientInfo(ASN1Sequence paramASN1Sequence)
  {
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    this.rid = RecipientIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(2));
    this.encryptedKey = ((ASN1OctetString)paramASN1Sequence.getObjectAt(3));
  }

  public KeyTransRecipientInfo(RecipientIdentifier paramRecipientIdentifier, AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString)
  {
    if ((paramRecipientIdentifier.getDERObject() instanceof ASN1TaggedObject));
    for (this.version = new DERInteger(2); ; this.version = new DERInteger(0))
    {
      this.rid = paramRecipientIdentifier;
      this.keyEncryptionAlgorithm = paramAlgorithmIdentifier;
      this.encryptedKey = paramASN1OctetString;
      return;
    }
  }

  public static KeyTransRecipientInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof KeyTransRecipientInfo)))
      return (KeyTransRecipientInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new KeyTransRecipientInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Illegal object in KeyTransRecipientInfo: " + paramObject.getClass().getName());
  }

  public ASN1OctetString getEncryptedKey()
  {
    return this.encryptedKey;
  }

  public AlgorithmIdentifier getKeyEncryptionAlgorithm()
  {
    return this.keyEncryptionAlgorithm;
  }

  public RecipientIdentifier getRecipientIdentifier()
  {
    return this.rid;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.rid);
    localASN1EncodableVector.add(this.keyEncryptionAlgorithm);
    localASN1EncodableVector.add(this.encryptedKey);
    return new DERSequence(localASN1EncodableVector);
  }
}