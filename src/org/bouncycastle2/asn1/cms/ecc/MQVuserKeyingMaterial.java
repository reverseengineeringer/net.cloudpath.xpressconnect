package org.bouncycastle2.asn1.cms.ecc;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.cms.OriginatorPublicKey;

public class MQVuserKeyingMaterial extends ASN1Encodable
{
  private ASN1OctetString addedukm;
  private OriginatorPublicKey ephemeralPublicKey;

  private MQVuserKeyingMaterial(ASN1Sequence paramASN1Sequence)
  {
    this.ephemeralPublicKey = OriginatorPublicKey.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.addedukm = ASN1OctetString.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(1), true);
  }

  public MQVuserKeyingMaterial(OriginatorPublicKey paramOriginatorPublicKey, ASN1OctetString paramASN1OctetString)
  {
    this.ephemeralPublicKey = paramOriginatorPublicKey;
    this.addedukm = paramASN1OctetString;
  }

  public static MQVuserKeyingMaterial getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof MQVuserKeyingMaterial)))
      return (MQVuserKeyingMaterial)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new MQVuserKeyingMaterial((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid MQVuserKeyingMaterial: " + paramObject.getClass().getName());
  }

  public static MQVuserKeyingMaterial getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1OctetString getAddedukm()
  {
    return this.addedukm;
  }

  public OriginatorPublicKey getEphemeralPublicKey()
  {
    return this.ephemeralPublicKey;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.ephemeralPublicKey);
    if (this.addedukm != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.addedukm));
    return new DERSequence(localASN1EncodableVector);
  }
}