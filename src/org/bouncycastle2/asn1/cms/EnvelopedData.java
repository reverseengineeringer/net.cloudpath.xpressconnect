package org.bouncycastle2.asn1.cms;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class EnvelopedData extends ASN1Encodable
{
  private EncryptedContentInfo encryptedContentInfo;
  private OriginatorInfo originatorInfo;
  private ASN1Set recipientInfos;
  private ASN1Set unprotectedAttrs;
  private DERInteger version;

  public EnvelopedData(ASN1Sequence paramASN1Sequence)
  {
    int i = 0 + 1;
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    int j = i + 1;
    DEREncodable localDEREncodable = paramASN1Sequence.getObjectAt(i);
    if ((localDEREncodable instanceof ASN1TaggedObject))
    {
      this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)localDEREncodable, false);
      int m = j + 1;
      localDEREncodable = paramASN1Sequence.getObjectAt(j);
      j = m;
    }
    this.recipientInfos = ASN1Set.getInstance(localDEREncodable);
    int k = j + 1;
    this.encryptedContentInfo = EncryptedContentInfo.getInstance(paramASN1Sequence.getObjectAt(j));
    if (paramASN1Sequence.size() > k)
      this.unprotectedAttrs = ASN1Set.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(k), false);
  }

  public EnvelopedData(OriginatorInfo paramOriginatorInfo, ASN1Set paramASN1Set1, EncryptedContentInfo paramEncryptedContentInfo, ASN1Set paramASN1Set2)
  {
    if ((paramOriginatorInfo != null) || (paramASN1Set2 != null))
      this.version = new DERInteger(2);
    while (true)
    {
      this.originatorInfo = paramOriginatorInfo;
      this.recipientInfos = paramASN1Set1;
      this.encryptedContentInfo = paramEncryptedContentInfo;
      this.unprotectedAttrs = paramASN1Set2;
      return;
      this.version = new DERInteger(0);
      Enumeration localEnumeration = paramASN1Set1.getObjects();
      if (localEnumeration.hasMoreElements())
      {
        if (RecipientInfo.getInstance(localEnumeration.nextElement()).getVersion().equals(this.version))
          break;
        this.version = new DERInteger(2);
      }
    }
  }

  public static EnvelopedData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof EnvelopedData)))
      return (EnvelopedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new EnvelopedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid EnvelopedData: " + paramObject.getClass().getName());
  }

  public static EnvelopedData getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public EncryptedContentInfo getEncryptedContentInfo()
  {
    return this.encryptedContentInfo;
  }

  public OriginatorInfo getOriginatorInfo()
  {
    return this.originatorInfo;
  }

  public ASN1Set getRecipientInfos()
  {
    return this.recipientInfos;
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
    if (this.originatorInfo != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
    localASN1EncodableVector.add(this.recipientInfos);
    localASN1EncodableVector.add(this.encryptedContentInfo);
    if (this.unprotectedAttrs != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.unprotectedAttrs));
    return new BERSequence(localASN1EncodableVector);
  }
}