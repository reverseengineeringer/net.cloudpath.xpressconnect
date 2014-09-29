package org.bouncycastle2.asn1.cms;

import java.util.Enumeration;
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
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class AuthenticatedData extends ASN1Encodable
{
  private ASN1Set authAttrs;
  private AlgorithmIdentifier digestAlgorithm;
  private ContentInfo encapsulatedContentInfo;
  private ASN1OctetString mac;
  private AlgorithmIdentifier macAlgorithm;
  private OriginatorInfo originatorInfo;
  private ASN1Set recipientInfos;
  private ASN1Set unauthAttrs;
  private DERInteger version;

  public AuthenticatedData(ASN1Sequence paramASN1Sequence)
  {
    int i = 0 + 1;
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    int j = i + 1;
    DEREncodable localDEREncodable1 = paramASN1Sequence.getObjectAt(i);
    if ((localDEREncodable1 instanceof ASN1TaggedObject))
    {
      this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)localDEREncodable1, false);
      int i3 = j + 1;
      localDEREncodable1 = paramASN1Sequence.getObjectAt(j);
      j = i3;
    }
    this.recipientInfos = ASN1Set.getInstance(localDEREncodable1);
    int k = j + 1;
    this.macAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(j));
    int m = k + 1;
    DEREncodable localDEREncodable2 = paramASN1Sequence.getObjectAt(k);
    if ((localDEREncodable2 instanceof ASN1TaggedObject))
    {
      this.digestAlgorithm = AlgorithmIdentifier.getInstance((ASN1TaggedObject)localDEREncodable2, false);
      int i2 = m + 1;
      localDEREncodable2 = paramASN1Sequence.getObjectAt(m);
      m = i2;
    }
    this.encapsulatedContentInfo = ContentInfo.getInstance(localDEREncodable2);
    int n = m + 1;
    DEREncodable localDEREncodable3 = paramASN1Sequence.getObjectAt(m);
    int i1;
    if ((localDEREncodable3 instanceof ASN1TaggedObject))
    {
      this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)localDEREncodable3, false);
      i1 = n + 1;
      localDEREncodable3 = paramASN1Sequence.getObjectAt(n);
    }
    while (true)
    {
      this.mac = ASN1OctetString.getInstance(localDEREncodable3);
      if (paramASN1Sequence.size() > i1)
        this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(i1), false);
      return;
      i1 = n;
    }
  }

  public AuthenticatedData(OriginatorInfo paramOriginatorInfo, ASN1Set paramASN1Set1, AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, ContentInfo paramContentInfo, ASN1Set paramASN1Set2, ASN1OctetString paramASN1OctetString, ASN1Set paramASN1Set3)
  {
    if (((paramAlgorithmIdentifier2 != null) || (paramASN1Set2 != null)) && ((paramAlgorithmIdentifier2 == null) || (paramASN1Set2 == null)))
      throw new IllegalArgumentException("digestAlgorithm and authAttrs must be set together");
    this.version = new DERInteger(calculateVersion(paramOriginatorInfo));
    this.originatorInfo = paramOriginatorInfo;
    this.macAlgorithm = paramAlgorithmIdentifier1;
    this.digestAlgorithm = paramAlgorithmIdentifier2;
    this.recipientInfos = paramASN1Set1;
    this.encapsulatedContentInfo = paramContentInfo;
    this.authAttrs = paramASN1Set2;
    this.mac = paramASN1OctetString;
    this.unauthAttrs = paramASN1Set3;
  }

  public static int calculateVersion(OriginatorInfo paramOriginatorInfo)
  {
    int i;
    label6: Enumeration localEnumeration1;
    if (paramOriginatorInfo == null)
    {
      i = 0;
      break label36;
      return i;
    }
    else
    {
      i = 0;
      localEnumeration1 = paramOriginatorInfo.getCertificates().getObjects();
      label18: if (localEnumeration1.hasMoreElements())
        break label77;
    }
    while (true)
    {
      Enumeration localEnumeration2 = paramOriginatorInfo.getCRLs().getObjects();
      label36: if (!localEnumeration2.hasMoreElements())
        break label6;
      Object localObject2 = localEnumeration2.nextElement();
      if ((!(localObject2 instanceof ASN1TaggedObject)) || (((ASN1TaggedObject)localObject2).getTagNo() != 1))
        break;
      return 3;
      label77: Object localObject1 = localEnumeration1.nextElement();
      if (!(localObject1 instanceof ASN1TaggedObject))
        break label18;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localObject1;
      if (localASN1TaggedObject.getTagNo() == 2)
      {
        i = 1;
        break label18;
      }
      if (localASN1TaggedObject.getTagNo() != 3)
        break label18;
      i = 3;
    }
  }

  public static AuthenticatedData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof AuthenticatedData)))
      return (AuthenticatedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AuthenticatedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid AuthenticatedData: " + paramObject.getClass().getName());
  }

  public static AuthenticatedData getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1Set getAuthAttrs()
  {
    return this.authAttrs;
  }

  public AlgorithmIdentifier getDigestAlgorithm()
  {
    return this.digestAlgorithm;
  }

  public ContentInfo getEncapsulatedContentInfo()
  {
    return this.encapsulatedContentInfo;
  }

  public ASN1OctetString getMac()
  {
    return this.mac;
  }

  public AlgorithmIdentifier getMacAlgorithm()
  {
    return this.macAlgorithm;
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
    localASN1EncodableVector.add(this.macAlgorithm);
    if (this.digestAlgorithm != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.digestAlgorithm));
    localASN1EncodableVector.add(this.encapsulatedContentInfo);
    if (this.authAttrs != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 2, this.authAttrs));
    localASN1EncodableVector.add(this.mac);
    if (this.unauthAttrs != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 3, this.unauthAttrs));
    return new BERSequence(localASN1EncodableVector);
  }
}