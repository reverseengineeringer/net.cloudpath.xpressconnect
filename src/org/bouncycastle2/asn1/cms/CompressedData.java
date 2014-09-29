package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class CompressedData extends ASN1Encodable
{
  private AlgorithmIdentifier compressionAlgorithm;
  private ContentInfo encapContentInfo;
  private DERInteger version;

  public CompressedData(ASN1Sequence paramASN1Sequence)
  {
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    this.compressionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    this.encapContentInfo = ContentInfo.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public CompressedData(AlgorithmIdentifier paramAlgorithmIdentifier, ContentInfo paramContentInfo)
  {
    this.version = new DERInteger(0);
    this.compressionAlgorithm = paramAlgorithmIdentifier;
    this.encapContentInfo = paramContentInfo;
  }

  public static CompressedData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof CompressedData)))
      return (CompressedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CompressedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid CompressedData: " + paramObject.getClass().getName());
  }

  public static CompressedData getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getCompressionAlgorithmIdentifier()
  {
    return this.compressionAlgorithm;
  }

  public ContentInfo getEncapContentInfo()
  {
    return this.encapContentInfo;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.compressionAlgorithm);
    localASN1EncodableVector.add(this.encapContentInfo);
    return new BERSequence(localASN1EncodableVector);
  }
}