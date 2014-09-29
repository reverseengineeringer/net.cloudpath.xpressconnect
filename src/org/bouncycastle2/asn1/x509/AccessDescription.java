package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class AccessDescription extends ASN1Encodable
{
  public static final DERObjectIdentifier id_ad_caIssuers = new DERObjectIdentifier("1.3.6.1.5.5.7.48.2");
  public static final DERObjectIdentifier id_ad_ocsp = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1");
  GeneralName accessLocation = null;
  DERObjectIdentifier accessMethod = null;

  public AccessDescription(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("wrong number of elements in sequence");
    this.accessMethod = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.accessLocation = GeneralName.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public AccessDescription(DERObjectIdentifier paramDERObjectIdentifier, GeneralName paramGeneralName)
  {
    this.accessMethod = paramDERObjectIdentifier;
    this.accessLocation = paramGeneralName;
  }

  public static AccessDescription getInstance(Object paramObject)
  {
    if ((paramObject instanceof AccessDescription))
      return (AccessDescription)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AccessDescription((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public GeneralName getAccessLocation()
  {
    return this.accessLocation;
  }

  public DERObjectIdentifier getAccessMethod()
  {
    return this.accessMethod;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.accessMethod);
    localASN1EncodableVector.add(this.accessLocation);
    return new DERSequence(localASN1EncodableVector);
  }

  public String toString()
  {
    return "AccessDescription: Oid(" + this.accessMethod.getId() + ")";
  }
}