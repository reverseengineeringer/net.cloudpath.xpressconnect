package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class ProofOfPossession extends ASN1Encodable
  implements ASN1Choice
{
  public static final int TYPE_KEY_AGREEMENT = 3;
  public static final int TYPE_KEY_ENCIPHERMENT = 2;
  public static final int TYPE_RA_VERIFIED = 0;
  public static final int TYPE_SIGNING_KEY = 1;
  private ASN1Encodable obj;
  private int tagNo;

  public ProofOfPossession()
  {
    this.tagNo = 0;
    this.obj = DERNull.INSTANCE;
  }

  public ProofOfPossession(int paramInt, POPOPrivKey paramPOPOPrivKey)
  {
    this.tagNo = paramInt;
    this.obj = paramPOPOPrivKey;
  }

  private ProofOfPossession(ASN1TaggedObject paramASN1TaggedObject)
  {
    this.tagNo = paramASN1TaggedObject.getTagNo();
    switch (this.tagNo)
    {
    default:
      throw new IllegalArgumentException("unknown tag: " + this.tagNo);
    case 0:
      this.obj = DERNull.INSTANCE;
      return;
    case 1:
      this.obj = POPOSigningKey.getInstance(paramASN1TaggedObject, false);
      return;
    case 2:
    case 3:
    }
    this.obj = POPOPrivKey.getInstance(paramASN1TaggedObject, false);
  }

  public ProofOfPossession(POPOSigningKey paramPOPOSigningKey)
  {
    this.tagNo = 1;
    this.obj = paramPOPOSigningKey;
  }

  public static ProofOfPossession getInstance(Object paramObject)
  {
    if ((paramObject instanceof ProofOfPossession))
      return (ProofOfPossession)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return new ProofOfPossession((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public ASN1Encodable getObject()
  {
    return this.obj;
  }

  public int getType()
  {
    return this.tagNo;
  }

  public DERObject toASN1Object()
  {
    return new DERTaggedObject(false, this.tagNo, this.obj);
  }
}