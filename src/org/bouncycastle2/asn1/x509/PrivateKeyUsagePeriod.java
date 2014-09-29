package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class PrivateKeyUsagePeriod extends ASN1Encodable
{
  private DERGeneralizedTime _notAfter;
  private DERGeneralizedTime _notBefore;

  private PrivateKeyUsagePeriod(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localEnumeration.nextElement();
      if (localASN1TaggedObject.getTagNo() == 0)
        this._notBefore = DERGeneralizedTime.getInstance(localASN1TaggedObject, false);
      else if (localASN1TaggedObject.getTagNo() == 1)
        this._notAfter = DERGeneralizedTime.getInstance(localASN1TaggedObject, false);
    }
  }

  public static PrivateKeyUsagePeriod getInstance(Object paramObject)
  {
    if ((paramObject instanceof PrivateKeyUsagePeriod))
      return (PrivateKeyUsagePeriod)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PrivateKeyUsagePeriod((ASN1Sequence)paramObject);
    if ((paramObject instanceof X509Extension))
      return getInstance(X509Extension.convertValueToObject((X509Extension)paramObject));
    throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
  }

  public DERGeneralizedTime getNotAfter()
  {
    return this._notAfter;
  }

  public DERGeneralizedTime getNotBefore()
  {
    return this._notBefore;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this._notBefore != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this._notBefore));
    if (this._notAfter != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this._notAfter));
    return new DERSequence(localASN1EncodableVector);
  }
}