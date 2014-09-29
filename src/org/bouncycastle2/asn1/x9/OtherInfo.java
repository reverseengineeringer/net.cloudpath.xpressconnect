package org.bouncycastle2.asn1.x9;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class OtherInfo extends ASN1Encodable
{
  private KeySpecificInfo keyInfo;
  private ASN1OctetString partyAInfo;
  private ASN1OctetString suppPubInfo;

  public OtherInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.keyInfo = new KeySpecificInfo((ASN1Sequence)localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      DERTaggedObject localDERTaggedObject = (DERTaggedObject)localEnumeration.nextElement();
      if (localDERTaggedObject.getTagNo() == 0)
        this.partyAInfo = ((ASN1OctetString)localDERTaggedObject.getObject());
      else if (localDERTaggedObject.getTagNo() == 2)
        this.suppPubInfo = ((ASN1OctetString)localDERTaggedObject.getObject());
    }
  }

  public OtherInfo(KeySpecificInfo paramKeySpecificInfo, ASN1OctetString paramASN1OctetString1, ASN1OctetString paramASN1OctetString2)
  {
    this.keyInfo = paramKeySpecificInfo;
    this.partyAInfo = paramASN1OctetString1;
    this.suppPubInfo = paramASN1OctetString2;
  }

  public KeySpecificInfo getKeyInfo()
  {
    return this.keyInfo;
  }

  public ASN1OctetString getPartyAInfo()
  {
    return this.partyAInfo;
  }

  public ASN1OctetString getSuppPubInfo()
  {
    return this.suppPubInfo;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.keyInfo);
    if (this.partyAInfo != null)
      localASN1EncodableVector.add(new DERTaggedObject(0, this.partyAInfo));
    localASN1EncodableVector.add(new DERTaggedObject(2, this.suppPubInfo));
    return new DERSequence(localASN1EncodableVector);
  }
}