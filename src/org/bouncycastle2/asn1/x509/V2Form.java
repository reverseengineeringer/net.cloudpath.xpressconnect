package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class V2Form extends ASN1Encodable
{
  IssuerSerial baseCertificateID;
  GeneralNames issuerName;
  ObjectDigestInfo objectDigestInfo;

  public V2Form(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() > 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    boolean bool = paramASN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject;
    int i = 0;
    if (!bool)
    {
      i = 0 + 1;
      this.issuerName = GeneralNames.getInstance(paramASN1Sequence.getObjectAt(0));
    }
    int j = i;
    if (j == paramASN1Sequence.size())
      return;
    ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(j));
    if (localASN1TaggedObject.getTagNo() == 0)
      this.baseCertificateID = IssuerSerial.getInstance(localASN1TaggedObject, false);
    while (true)
    {
      j++;
      break;
      if (localASN1TaggedObject.getTagNo() != 1)
        break label140;
      this.objectDigestInfo = ObjectDigestInfo.getInstance(localASN1TaggedObject, false);
    }
    label140: throw new IllegalArgumentException("Bad tag number: " + localASN1TaggedObject.getTagNo());
  }

  public V2Form(GeneralNames paramGeneralNames)
  {
    this.issuerName = paramGeneralNames;
  }

  public static V2Form getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof V2Form)))
      return (V2Form)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new V2Form((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static V2Form getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public IssuerSerial getBaseCertificateID()
  {
    return this.baseCertificateID;
  }

  public GeneralNames getIssuerName()
  {
    return this.issuerName;
  }

  public ObjectDigestInfo getObjectDigestInfo()
  {
    return this.objectDigestInfo;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.issuerName != null)
      localASN1EncodableVector.add(this.issuerName);
    if (this.baseCertificateID != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
    if (this.objectDigestInfo != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.objectDigestInfo));
    return new DERSequence(localASN1EncodableVector);
  }
}