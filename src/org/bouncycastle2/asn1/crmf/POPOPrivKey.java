package org.bouncycastle2.asn1.crmf;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.cms.EnvelopedData;

public class POPOPrivKey extends ASN1Encodable
  implements ASN1Choice
{
  public static final int agreeMAC = 3;
  public static final int dhMAC = 2;
  public static final int encryptedKey = 4;
  public static final int subsequentMessage = 1;
  public static final int thisMessage;
  private ASN1Encodable obj;
  private int tagNo;

  private POPOPrivKey(ASN1TaggedObject paramASN1TaggedObject)
  {
    this.tagNo = paramASN1TaggedObject.getTagNo();
    switch (this.tagNo)
    {
    default:
      throw new IllegalArgumentException("unknown tag in POPOPrivKey");
    case 0:
      this.obj = DERBitString.getInstance(paramASN1TaggedObject, false);
      return;
    case 1:
      this.obj = SubsequentMessage.valueOf(DERInteger.getInstance(paramASN1TaggedObject, false).getValue().intValue());
      return;
    case 2:
      this.obj = DERBitString.getInstance(paramASN1TaggedObject, false);
      return;
    case 3:
      this.obj = PKMACValue.getInstance(paramASN1TaggedObject, false);
      return;
    case 4:
    }
    this.obj = EnvelopedData.getInstance(paramASN1TaggedObject, false);
  }

  public POPOPrivKey(SubsequentMessage paramSubsequentMessage)
  {
    this.tagNo = 1;
    this.obj = paramSubsequentMessage;
  }

  public static POPOPrivKey getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return new POPOPrivKey(ASN1TaggedObject.getInstance(paramASN1TaggedObject.getObject()));
  }

  public int getType()
  {
    return this.tagNo;
  }

  public ASN1Encodable getValue()
  {
    return this.obj;
  }

  public DERObject toASN1Object()
  {
    return new DERTaggedObject(false, this.tagNo, this.obj);
  }
}