package org.bouncycastle2.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.BERTaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERTaggedObject;

public class ContentInfo extends ASN1Encodable
  implements PKCSObjectIdentifiers
{
  private DEREncodable content;
  private DERObjectIdentifier contentType;

  public ContentInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.contentType = ((DERObjectIdentifier)localEnumeration.nextElement());
    if (localEnumeration.hasMoreElements())
      this.content = ((DERTaggedObject)localEnumeration.nextElement()).getObject();
  }

  public ContentInfo(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.contentType = paramDERObjectIdentifier;
    this.content = paramDEREncodable;
  }

  public static ContentInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof ContentInfo))
      return (ContentInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ContentInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public DEREncodable getContent()
  {
    return this.content;
  }

  public DERObjectIdentifier getContentType()
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