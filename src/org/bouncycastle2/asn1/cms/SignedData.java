package org.bouncycastle2.asn1.cms;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.BERSet;
import org.bouncycastle2.asn1.BERTaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERTaggedObject;

public class SignedData extends ASN1Encodable
{
  private ASN1Set certificates;
  private boolean certsBer;
  private ContentInfo contentInfo;
  private ASN1Set crls;
  private boolean crlsBer;
  private ASN1Set digestAlgorithms;
  private ASN1Set signerInfos;
  private DERInteger version;

  public SignedData(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.version = ((DERInteger)localEnumeration.nextElement());
    this.digestAlgorithms = ((ASN1Set)localEnumeration.nextElement());
    this.contentInfo = ContentInfo.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      DERObject localDERObject = (DERObject)localEnumeration.nextElement();
      if ((localDERObject instanceof ASN1TaggedObject))
      {
        ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localDERObject;
        switch (localASN1TaggedObject.getTagNo())
        {
        default:
          throw new IllegalArgumentException("unknown tag value " + localASN1TaggedObject.getTagNo());
        case 0:
          this.certsBer = (localASN1TaggedObject instanceof BERTaggedObject);
          this.certificates = ASN1Set.getInstance(localASN1TaggedObject, false);
          break;
        case 1:
          this.crlsBer = (localASN1TaggedObject instanceof BERTaggedObject);
          this.crls = ASN1Set.getInstance(localASN1TaggedObject, false);
          break;
        }
      }
      else
      {
        this.signerInfos = ((ASN1Set)localDERObject);
      }
    }
  }

  public SignedData(ASN1Set paramASN1Set1, ContentInfo paramContentInfo, ASN1Set paramASN1Set2, ASN1Set paramASN1Set3, ASN1Set paramASN1Set4)
  {
    this.version = calculateVersion(paramContentInfo.getContentType(), paramASN1Set2, paramASN1Set3, paramASN1Set4);
    this.digestAlgorithms = paramASN1Set1;
    this.contentInfo = paramContentInfo;
    this.certificates = paramASN1Set2;
    this.crls = paramASN1Set3;
    this.signerInfos = paramASN1Set4;
    this.crlsBer = (paramASN1Set3 instanceof BERSet);
    this.certsBer = (paramASN1Set2 instanceof BERSet);
  }

  private DERInteger calculateVersion(DERObjectIdentifier paramDERObjectIdentifier, ASN1Set paramASN1Set1, ASN1Set paramASN1Set2, ASN1Set paramASN1Set3)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    Enumeration localEnumeration2;
    if (paramASN1Set1 != null)
      localEnumeration2 = paramASN1Set1.getObjects();
    while (true)
    {
      if (!localEnumeration2.hasMoreElements())
      {
        if (k == 0)
          break;
        return new DERInteger(5);
      }
      Object localObject = localEnumeration2.nextElement();
      if ((localObject instanceof ASN1TaggedObject))
      {
        ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localObject;
        if (localASN1TaggedObject.getTagNo() == 1)
          i = 1;
        else if (localASN1TaggedObject.getTagNo() == 2)
          j = 1;
        else if (localASN1TaggedObject.getTagNo() == 3)
          k = 1;
      }
    }
    int m = 0;
    Enumeration localEnumeration1;
    if (paramASN1Set2 != null)
      localEnumeration1 = paramASN1Set2.getObjects();
    while (true)
    {
      if (!localEnumeration1.hasMoreElements())
      {
        if (m == 0)
          break;
        return new DERInteger(5);
      }
      if ((localEnumeration1.nextElement() instanceof ASN1TaggedObject))
        m = 1;
    }
    if (j != 0)
      return new DERInteger(4);
    if (i != 0)
      return new DERInteger(3);
    if (checkForVersion3(paramASN1Set3))
      return new DERInteger(3);
    if (!CMSObjectIdentifiers.data.equals(paramDERObjectIdentifier))
      return new DERInteger(3);
    return new DERInteger(1);
  }

  private boolean checkForVersion3(ASN1Set paramASN1Set)
  {
    Enumeration localEnumeration = paramASN1Set.getObjects();
    do
      if (!localEnumeration.hasMoreElements())
        return false;
    while (SignerInfo.getInstance(localEnumeration.nextElement()).getVersion().getValue().intValue() != 3);
    return true;
  }

  public static SignedData getInstance(Object paramObject)
  {
    if ((paramObject instanceof SignedData))
      return (SignedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SignedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public ASN1Set getCRLs()
  {
    return this.crls;
  }

  public ASN1Set getCertificates()
  {
    return this.certificates;
  }

  public ASN1Set getDigestAlgorithms()
  {
    return this.digestAlgorithms;
  }

  public ContentInfo getEncapContentInfo()
  {
    return this.contentInfo;
  }

  public ASN1Set getSignerInfos()
  {
    return this.signerInfos;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.digestAlgorithms);
    localASN1EncodableVector.add(this.contentInfo);
    if (this.certificates != null)
    {
      if (this.certsBer)
        localASN1EncodableVector.add(new BERTaggedObject(false, 0, this.certificates));
    }
    else if (this.crls != null)
    {
      if (!this.crlsBer)
        break label131;
      localASN1EncodableVector.add(new BERTaggedObject(false, 1, this.crls));
    }
    while (true)
    {
      localASN1EncodableVector.add(this.signerInfos);
      return new BERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.certificates));
      break;
      label131: localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
    }
  }
}