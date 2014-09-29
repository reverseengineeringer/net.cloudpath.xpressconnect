package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;

public class Pfx extends ASN1Encodable
  implements PKCSObjectIdentifiers
{
  private ContentInfo contentInfo;
  private MacData macData = null;

  public Pfx(ASN1Sequence paramASN1Sequence)
  {
    if (((DERInteger)paramASN1Sequence.getObjectAt(0)).getValue().intValue() != 3)
      throw new IllegalArgumentException("wrong version for PFX PDU");
    this.contentInfo = ContentInfo.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() == 3)
      this.macData = MacData.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public Pfx(ContentInfo paramContentInfo, MacData paramMacData)
  {
    this.contentInfo = paramContentInfo;
    this.macData = paramMacData;
  }

  public ContentInfo getAuthSafe()
  {
    return this.contentInfo;
  }

  public MacData getMacData()
  {
    return this.macData;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new DERInteger(3));
    localASN1EncodableVector.add(this.contentInfo);
    if (this.macData != null)
      localASN1EncodableVector.add(this.macData);
    return new BERSequence(localASN1EncodableVector);
  }
}