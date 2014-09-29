package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class AuthEnvelopedData extends ASN1Encodable
{
  private ASN1Set authAttrs;
  private EncryptedContentInfo authEncryptedContentInfo;
  private ASN1OctetString mac;
  private OriginatorInfo originatorInfo;
  private ASN1Set recipientInfos;
  private ASN1Set unauthAttrs;
  private DERInteger version;

  public AuthEnvelopedData(ASN1Sequence paramASN1Sequence)
  {
    int i = 0 + 1;
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0).getDERObject());
    int j = i + 1;
    DERObject localDERObject1 = paramASN1Sequence.getObjectAt(i).getDERObject();
    if ((localDERObject1 instanceof ASN1TaggedObject))
    {
      this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)localDERObject1, false);
      int i1 = j + 1;
      localDERObject1 = paramASN1Sequence.getObjectAt(j).getDERObject();
      j = i1;
    }
    this.recipientInfos = ASN1Set.getInstance(localDERObject1);
    int k = j + 1;
    this.authEncryptedContentInfo = EncryptedContentInfo.getInstance(paramASN1Sequence.getObjectAt(j).getDERObject());
    int m = k + 1;
    DERObject localDERObject2 = paramASN1Sequence.getObjectAt(k).getDERObject();
    if ((localDERObject2 instanceof ASN1TaggedObject))
    {
      this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)localDERObject2, false);
      int n = m + 1;
      localDERObject2 = paramASN1Sequence.getObjectAt(m).getDERObject();
      m = n;
    }
    this.mac = ASN1OctetString.getInstance(localDERObject2);
    if (paramASN1Sequence.size() > m)
    {
      (m + 1);
      this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(m).getDERObject(), false);
    }
  }

  public AuthEnvelopedData(OriginatorInfo paramOriginatorInfo, ASN1Set paramASN1Set1, EncryptedContentInfo paramEncryptedContentInfo, ASN1Set paramASN1Set2, ASN1OctetString paramASN1OctetString, ASN1Set paramASN1Set3)
  {
    this.version = new DERInteger(0);
    this.originatorInfo = paramOriginatorInfo;
    this.recipientInfos = paramASN1Set1;
    this.authEncryptedContentInfo = paramEncryptedContentInfo;
    this.authAttrs = paramASN1Set2;
    this.mac = paramASN1OctetString;
    this.unauthAttrs = paramASN1Set3;
  }

  public static AuthEnvelopedData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof AuthEnvelopedData)))
      return (AuthEnvelopedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AuthEnvelopedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid AuthEnvelopedData: " + paramObject.getClass().getName());
  }

  public static AuthEnvelopedData getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1Set getAuthAttrs()
  {
    return this.authAttrs;
  }

  public EncryptedContentInfo getAuthEncryptedContentInfo()
  {
    return this.authEncryptedContentInfo;
  }

  public ASN1OctetString getMac()
  {
    return this.mac;
  }

  public OriginatorInfo getOriginatorInfo()
  {
    return this.originatorInfo;
  }

  public ASN1Set getRecipientInfos()
  {
    return this.recipientInfos;
  }

  public ASN1Set getUnauthAttrs()
  {
    return this.unauthAttrs;
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
    localASN1EncodableVector.add(this.authEncryptedContentInfo);
    if (this.authAttrs != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.authAttrs));
    localASN1EncodableVector.add(this.mac);
    if (this.unauthAttrs != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 2, this.unauthAttrs));
    return new BERSequence(localASN1EncodableVector);
  }
}