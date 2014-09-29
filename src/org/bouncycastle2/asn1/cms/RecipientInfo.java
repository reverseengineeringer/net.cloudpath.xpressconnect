package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class RecipientInfo extends ASN1Encodable
  implements ASN1Choice
{
  DEREncodable info;

  public RecipientInfo(DERObject paramDERObject)
  {
    this.info = paramDERObject;
  }

  public RecipientInfo(KEKRecipientInfo paramKEKRecipientInfo)
  {
    this.info = new DERTaggedObject(false, 2, paramKEKRecipientInfo);
  }

  public RecipientInfo(KeyAgreeRecipientInfo paramKeyAgreeRecipientInfo)
  {
    this.info = new DERTaggedObject(false, 1, paramKeyAgreeRecipientInfo);
  }

  public RecipientInfo(KeyTransRecipientInfo paramKeyTransRecipientInfo)
  {
    this.info = paramKeyTransRecipientInfo;
  }

  public RecipientInfo(OtherRecipientInfo paramOtherRecipientInfo)
  {
    this.info = new DERTaggedObject(false, 4, paramOtherRecipientInfo);
  }

  public RecipientInfo(PasswordRecipientInfo paramPasswordRecipientInfo)
  {
    this.info = new DERTaggedObject(false, 3, paramPasswordRecipientInfo);
  }

  public static RecipientInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RecipientInfo)))
      return (RecipientInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RecipientInfo((ASN1Sequence)paramObject);
    if ((paramObject instanceof ASN1TaggedObject))
      return new RecipientInfo((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  private KEKRecipientInfo getKEKInfo(ASN1TaggedObject paramASN1TaggedObject)
  {
    if (paramASN1TaggedObject.isExplicit())
      return KEKRecipientInfo.getInstance(paramASN1TaggedObject, true);
    return KEKRecipientInfo.getInstance(paramASN1TaggedObject, false);
  }

  public DEREncodable getInfo()
  {
    if ((this.info instanceof ASN1TaggedObject))
    {
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)this.info;
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalStateException("unknown tag");
      case 1:
        return KeyAgreeRecipientInfo.getInstance(localASN1TaggedObject, false);
      case 2:
        return getKEKInfo(localASN1TaggedObject);
      case 3:
        return PasswordRecipientInfo.getInstance(localASN1TaggedObject, false);
      case 4:
      }
      return OtherRecipientInfo.getInstance(localASN1TaggedObject, false);
    }
    return KeyTransRecipientInfo.getInstance(this.info);
  }

  public DERInteger getVersion()
  {
    if ((this.info instanceof ASN1TaggedObject))
    {
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)this.info;
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalStateException("unknown tag");
      case 1:
        return KeyAgreeRecipientInfo.getInstance(localASN1TaggedObject, false).getVersion();
      case 2:
        return getKEKInfo(localASN1TaggedObject).getVersion();
      case 3:
        return PasswordRecipientInfo.getInstance(localASN1TaggedObject, false).getVersion();
      case 4:
      }
      return new DERInteger(0);
    }
    return KeyTransRecipientInfo.getInstance(this.info).getVersion();
  }

  public boolean isTagged()
  {
    return this.info instanceof ASN1TaggedObject;
  }

  public DERObject toASN1Object()
  {
    return this.info.getDERObject();
  }
}