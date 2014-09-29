package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.BERTaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;

public class EncryptedData extends ASN1Encodable
{
  private EncryptedContentInfo encryptedContentInfo;
  private ASN1Set unprotectedAttrs;
  private DERInteger version;

  private EncryptedData(ASN1Sequence paramASN1Sequence)
  {
    this.version = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    this.encryptedContentInfo = EncryptedContentInfo.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() == 3)
      this.unprotectedAttrs = ASN1Set.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public EncryptedData(EncryptedContentInfo paramEncryptedContentInfo)
  {
    this(paramEncryptedContentInfo, null);
  }

  public EncryptedData(EncryptedContentInfo paramEncryptedContentInfo, ASN1Set paramASN1Set)
  {
    if (paramASN1Set == null);
    for (int i = 0; ; i = 2)
    {
      this.version = new DERInteger(i);
      this.encryptedContentInfo = paramEncryptedContentInfo;
      this.unprotectedAttrs = paramASN1Set;
      return;
    }
  }

  public static EncryptedData getInstance(Object paramObject)
  {
    if ((paramObject instanceof EncryptedData))
      return (EncryptedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new EncryptedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid EncryptedData: " + paramObject.getClass().getName());
  }

  public EncryptedContentInfo getEncryptedContentInfo()
  {
    return this.encryptedContentInfo;
  }

  public ASN1Set getUnprotectedAttrs()
  {
    return this.unprotectedAttrs;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.encryptedContentInfo);
    if (this.unprotectedAttrs != null)
      localASN1EncodableVector.add(new BERTaggedObject(false, 1, this.unprotectedAttrs));
    return new BERSequence(localASN1EncodableVector);
  }
}