package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.BERTaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class EncryptedContentInfo extends ASN1Encodable
{
  private AlgorithmIdentifier contentEncryptionAlgorithm;
  private DERObjectIdentifier contentType;
  private ASN1OctetString encryptedContent;

  public EncryptedContentInfo(ASN1Sequence paramASN1Sequence)
  {
    this.contentType = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    this.contentEncryptionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() > 2)
      this.encryptedContent = ASN1OctetString.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(2), false);
  }

  public EncryptedContentInfo(DERObjectIdentifier paramDERObjectIdentifier, AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.contentType = paramDERObjectIdentifier;
    this.contentEncryptionAlgorithm = paramAlgorithmIdentifier;
    this.encryptedContent = paramASN1OctetString;
  }

  public static EncryptedContentInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof EncryptedContentInfo)))
      return (EncryptedContentInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new EncryptedContentInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid EncryptedContentInfo: " + paramObject.getClass().getName());
  }

  public AlgorithmIdentifier getContentEncryptionAlgorithm()
  {
    return this.contentEncryptionAlgorithm;
  }

  public DERObjectIdentifier getContentType()
  {
    return this.contentType;
  }

  public ASN1OctetString getEncryptedContent()
  {
    return this.encryptedContent;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.contentType);
    localASN1EncodableVector.add(this.contentEncryptionAlgorithm);
    if (this.encryptedContent != null)
      localASN1EncodableVector.add(new BERTaggedObject(false, 0, this.encryptedContent));
    return new BERSequence(localASN1EncodableVector);
  }
}