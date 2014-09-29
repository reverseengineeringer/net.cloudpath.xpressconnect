package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class PKIPublicationInfo extends ASN1Encodable
{
  private DERInteger action;
  private ASN1Sequence pubInfos;

  private PKIPublicationInfo(ASN1Sequence paramASN1Sequence)
  {
    this.action = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    this.pubInfos = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public static PKIPublicationInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKIPublicationInfo))
      return (PKIPublicationInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PKIPublicationInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERInteger getAction()
  {
    return this.action;
  }

  public SinglePubInfo[] getPubInfos()
  {
    SinglePubInfo[] arrayOfSinglePubInfo;
    if (this.pubInfos == null)
      arrayOfSinglePubInfo = null;
    while (true)
    {
      return arrayOfSinglePubInfo;
      arrayOfSinglePubInfo = new SinglePubInfo[this.pubInfos.size()];
      for (int i = 0; i != arrayOfSinglePubInfo.length; i++)
        arrayOfSinglePubInfo[i] = SinglePubInfo.getInstance(this.pubInfos.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.action);
    localASN1EncodableVector.add(this.pubInfos);
    return new DERSequence(localASN1EncodableVector);
  }
}