package org.bouncycastle2.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class SignedData extends ASN1Encodable
  implements PKCSObjectIdentifiers
{
  private ASN1Set certificates;
  private ContentInfo contentInfo;
  private ASN1Set crls;
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
      if ((localDERObject instanceof DERTaggedObject))
      {
        DERTaggedObject localDERTaggedObject = (DERTaggedObject)localDERObject;
        switch (localDERTaggedObject.getTagNo())
        {
        default:
          throw new IllegalArgumentException("unknown tag value " + localDERTaggedObject.getTagNo());
        case 0:
          this.certificates = ASN1Set.getInstance(localDERTaggedObject, false);
          break;
        case 1:
          this.crls = ASN1Set.getInstance(localDERTaggedObject, false);
          break;
        }
      }
      else
      {
        this.signerInfos = ((ASN1Set)localDERObject);
      }
    }
  }

  public SignedData(DERInteger paramDERInteger, ASN1Set paramASN1Set1, ContentInfo paramContentInfo, ASN1Set paramASN1Set2, ASN1Set paramASN1Set3, ASN1Set paramASN1Set4)
  {
    this.version = paramDERInteger;
    this.digestAlgorithms = paramASN1Set1;
    this.contentInfo = paramContentInfo;
    this.certificates = paramASN1Set2;
    this.crls = paramASN1Set3;
    this.signerInfos = paramASN1Set4;
  }

  public static SignedData getInstance(Object paramObject)
  {
    if ((paramObject instanceof SignedData))
      return (SignedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SignedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject);
  }

  public ASN1Set getCRLs()
  {
    return this.crls;
  }

  public ASN1Set getCertificates()
  {
    return this.certificates;
  }

  public ContentInfo getContentInfo()
  {
    return this.contentInfo;
  }

  public ASN1Set getDigestAlgorithms()
  {
    return this.digestAlgorithms;
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
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.certificates));
    if (this.crls != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
    localASN1EncodableVector.add(this.signerInfos);
    return new BERSequence(localASN1EncodableVector);
  }
}