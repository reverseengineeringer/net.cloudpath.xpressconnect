package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.cms.EnvelopedData;

public class EncryptedKey extends ASN1Encodable
  implements ASN1Choice
{
  private EncryptedValue encryptedValue;
  private EnvelopedData envelopedData;

  public EncryptedKey(EnvelopedData paramEnvelopedData)
  {
    this.envelopedData = paramEnvelopedData;
  }

  public EncryptedKey(EncryptedValue paramEncryptedValue)
  {
    this.encryptedValue = paramEncryptedValue;
  }

  public static EncryptedKey getInstance(Object paramObject)
  {
    if ((paramObject instanceof EncryptedKey))
      return (EncryptedKey)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return new EncryptedKey(EnvelopedData.getInstance((ASN1TaggedObject)paramObject, false));
    if ((paramObject instanceof EncryptedValue))
      return new EncryptedKey((EncryptedValue)paramObject);
    return new EncryptedKey(EncryptedValue.getInstance(paramObject));
  }

  public ASN1Encodable getValue()
  {
    if (this.encryptedValue != null)
      return this.encryptedValue;
    return this.envelopedData;
  }

  public boolean isEncryptedValue()
  {
    return this.encryptedValue != null;
  }

  public DERObject toASN1Object()
  {
    if (this.encryptedValue != null)
      return this.encryptedValue.toASN1Object();
    return new DERTaggedObject(false, 0, this.envelopedData);
  }
}