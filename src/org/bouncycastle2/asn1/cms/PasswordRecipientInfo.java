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

public class PasswordRecipientInfo extends ASN1Encodable
{
  private ASN1OctetString encryptedKey;
  private AlgorithmIdentifier keyDerivationAlgorithm;
  private AlgorithmIdentifier keyEncryptionAlgorithm;
  private DERInteger version;

  public PasswordRecipientInfo(ASN1Sequence paramASN1Sequence)
  {
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    if ((paramASN1Sequence.getObjectAt(1) instanceof ASN1TaggedObject))
    {
      this.keyDerivationAlgorithm = AlgorithmIdentifier.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(1), false);
      this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(2));
      this.encryptedKey = ((ASN1OctetString)paramASN1Sequence.getObjectAt(3));
      return;
    }
    this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    this.encryptedKey = ((ASN1OctetString)paramASN1Sequence.getObjectAt(2));
  }

  public PasswordRecipientInfo(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.version = new DERInteger(0);
    this.keyEncryptionAlgorithm = paramAlgorithmIdentifier;
    this.encryptedKey = paramASN1OctetString;
  }

  public PasswordRecipientInfo(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, ASN1OctetString paramASN1OctetString)
  {
    this.version = new DERInteger(0);
    this.keyDerivationAlgorithm = paramAlgorithmIdentifier1;
    this.keyEncryptionAlgorithm = paramAlgorithmIdentifier2;
    this.encryptedKey = paramASN1OctetString;
  }

  public static PasswordRecipientInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof PasswordRecipientInfo)))
      return (PasswordRecipientInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PasswordRecipientInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid PasswordRecipientInfo: " + paramObject.getClass().getName());
  }

  public static PasswordRecipientInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1OctetString getEncryptedKey()
  {
    return this.encryptedKey;
  }

  public AlgorithmIdentifier getKeyDerivationAlgorithm()
  {
    return this.keyDerivationAlgorithm;
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
    if (this.keyDerivationAlgorithm != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.keyDerivationAlgorithm));
    localASN1EncodableVector.add(this.keyEncryptionAlgorithm);
    localASN1EncodableVector.add(this.encryptedKey);
    return new DERSequence(localASN1EncodableVector);
  }
}