package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.GeneralName;

public class SinglePubInfo extends ASN1Encodable
{
  private GeneralName pubLocation;
  private DERInteger pubMethod;

  private SinglePubInfo(ASN1Sequence paramASN1Sequence)
  {
    this.pubMethod = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
      this.pubLocation = GeneralName.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public static SinglePubInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof SinglePubInfo))
      return (SinglePubInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SinglePubInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public GeneralName getPubLocation()
  {
    return this.pubLocation;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.pubMethod);
    if (this.pubLocation != null)
      localASN1EncodableVector.add(this.pubLocation);
    return new DERSequence(localASN1EncodableVector);
  }
}