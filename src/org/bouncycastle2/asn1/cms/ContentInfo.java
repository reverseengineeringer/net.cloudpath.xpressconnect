package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.BERTaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;

public class ContentInfo extends ASN1Encodable
  implements CMSObjectIdentifiers
{
  private DEREncodable content;
  private ASN1ObjectIdentifier contentType;

  public ContentInfo(ASN1ObjectIdentifier paramASN1ObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.contentType = paramASN1ObjectIdentifier;
    this.content = paramDEREncodable;
  }

  public ContentInfo(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.contentType = ((ASN1ObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
    {
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(1);
      if ((!localASN1TaggedObject.isExplicit()) || (localASN1TaggedObject.getTagNo() != 0))
        throw new IllegalArgumentException("Bad tag for 'content'");
      this.content = localASN1TaggedObject.getObject();
    }
  }

  public static ContentInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ContentInfo)))
      return (ContentInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ContentInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public DEREncodable getContent()
  {
    return this.content;
  }

  public ASN1ObjectIdentifier getContentType()
  {
    return this.contentType;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.contentType);
    if (this.content != null)
      localASN1EncodableVector.add(new BERTaggedObject(0, this.content));
    return new BERSequence(localASN1EncodableVector);
  }
}