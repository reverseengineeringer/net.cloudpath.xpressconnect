package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class AttCertIssuer extends ASN1Encodable
  implements ASN1Choice
{
  DERObject choiceObj;
  ASN1Encodable obj;

  public AttCertIssuer(GeneralNames paramGeneralNames)
  {
    this.obj = paramGeneralNames;
    this.choiceObj = this.obj.getDERObject();
  }

  public AttCertIssuer(V2Form paramV2Form)
  {
    this.obj = paramV2Form;
    this.choiceObj = new DERTaggedObject(false, 0, this.obj);
  }

  public static AttCertIssuer getInstance(Object paramObject)
  {
    if ((paramObject instanceof AttCertIssuer))
      return (AttCertIssuer)paramObject;
    if ((paramObject instanceof V2Form))
      return new AttCertIssuer(V2Form.getInstance(paramObject));
    if ((paramObject instanceof GeneralNames))
      return new AttCertIssuer((GeneralNames)paramObject);
    if ((paramObject instanceof ASN1TaggedObject))
      return new AttCertIssuer(V2Form.getInstance((ASN1TaggedObject)paramObject, false));
    if ((paramObject instanceof ASN1Sequence))
      return new AttCertIssuer(GeneralNames.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static AttCertIssuer getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public ASN1Encodable getIssuer()
  {
    return this.obj;
  }

  public DERObject toASN1Object()
  {
    return this.choiceObj;
  }
}