package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class RecipientEncryptedKey extends ASN1Encodable
{
  private ASN1OctetString encryptedKey;
  private KeyAgreeRecipientIdentifier identifier;

  private RecipientEncryptedKey(ASN1Sequence paramASN1Sequence)
  {
    this.identifier = KeyAgreeRecipientIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.encryptedKey = ((ASN1OctetString)paramASN1Sequence.getObjectAt(1));
  }

  public RecipientEncryptedKey(KeyAgreeRecipientIdentifier paramKeyAgreeRecipientIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.identifier = paramKeyAgreeRecipientIdentifier;
    this.encryptedKey = paramASN1OctetString;
  }

  public static RecipientEncryptedKey getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RecipientEncryptedKey)))
      return (RecipientEncryptedKey)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RecipientEncryptedKey((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid RecipientEncryptedKey: " + paramObject.getClass().getName());
  }

  public static RecipientEncryptedKey getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1OctetString getEncryptedKey()
  {
    return this.encryptedKey;
  }

  public KeyAgreeRecipientIdentifier getIdentifier()
  {
    return this.identifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.identifier);
    localASN1EncodableVector.add(this.encryptedKey);
    return new DERSequence(localASN1EncodableVector);
  }
}