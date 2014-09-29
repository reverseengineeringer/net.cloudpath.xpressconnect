package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class Holder extends ASN1Encodable
{
  IssuerSerial baseCertificateID;
  GeneralNames entityName;
  ObjectDigestInfo objectDigestInfo;
  private int version = 1;

  public Holder(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() > 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    int i = 0;
    if (i == paramASN1Sequence.size())
    {
      this.version = 1;
      return;
    }
    ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(i));
    switch (localASN1TaggedObject.getTagNo())
    {
    default:
      throw new IllegalArgumentException("unknown tag in Holder");
    case 0:
      this.baseCertificateID = IssuerSerial.getInstance(localASN1TaggedObject, false);
    case 1:
    case 2:
    }
    while (true)
    {
      i++;
      break;
      this.entityName = GeneralNames.getInstance(localASN1TaggedObject, false);
      continue;
      this.objectDigestInfo = ObjectDigestInfo.getInstance(localASN1TaggedObject, false);
    }
  }

  public Holder(ASN1TaggedObject paramASN1TaggedObject)
  {
    switch (paramASN1TaggedObject.getTagNo())
    {
    default:
      throw new IllegalArgumentException("unknown tag in Holder");
    case 0:
      this.baseCertificateID = IssuerSerial.getInstance(paramASN1TaggedObject, false);
    case 1:
    }
    while (true)
    {
      this.version = 0;
      return;
      this.entityName = GeneralNames.getInstance(paramASN1TaggedObject, false);
    }
  }

  public Holder(GeneralNames paramGeneralNames)
  {
    this.entityName = paramGeneralNames;
  }

  public Holder(GeneralNames paramGeneralNames, int paramInt)
  {
    this.entityName = paramGeneralNames;
    this.version = paramInt;
  }

  public Holder(IssuerSerial paramIssuerSerial)
  {
    this.baseCertificateID = paramIssuerSerial;
  }

  public Holder(IssuerSerial paramIssuerSerial, int paramInt)
  {
    this.baseCertificateID = paramIssuerSerial;
    this.version = paramInt;
  }

  public Holder(ObjectDigestInfo paramObjectDigestInfo)
  {
    this.objectDigestInfo = paramObjectDigestInfo;
  }

  public static Holder getInstance(Object paramObject)
  {
    if ((paramObject instanceof Holder))
      return (Holder)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Holder((ASN1Sequence)paramObject);
    if ((paramObject instanceof ASN1TaggedObject))
      return new Holder((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public IssuerSerial getBaseCertificateID()
  {
    return this.baseCertificateID;
  }

  public GeneralNames getEntityName()
  {
    return this.entityName;
  }

  public ObjectDigestInfo getObjectDigestInfo()
  {
    return this.objectDigestInfo;
  }

  public int getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    if (this.version == 1)
    {
      ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
      if (this.baseCertificateID != null)
        localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
      if (this.entityName != null)
        localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.entityName));
      if (this.objectDigestInfo != null)
        localASN1EncodableVector.add(new DERTaggedObject(false, 2, this.objectDigestInfo));
      return new DERSequence(localASN1EncodableVector);
    }
    if (this.entityName != null)
      return new DERTaggedObject(false, 1, this.entityName);
    return new DERTaggedObject(false, 0, this.baseCertificateID);
  }
}