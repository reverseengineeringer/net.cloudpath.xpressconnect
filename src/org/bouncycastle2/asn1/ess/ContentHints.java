package org.bouncycastle2.asn1.ess;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERUTF8String;

public class ContentHints extends ASN1Encodable
{
  private DERUTF8String contentDescription;
  private DERObjectIdentifier contentType;

  private ContentHints(ASN1Sequence paramASN1Sequence)
  {
    DEREncodable localDEREncodable = paramASN1Sequence.getObjectAt(0);
    if ((localDEREncodable.getDERObject() instanceof DERUTF8String))
    {
      this.contentDescription = DERUTF8String.getInstance(localDEREncodable);
      this.contentType = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
      return;
    }
    this.contentType = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
  }

  public ContentHints(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.contentType = paramDERObjectIdentifier;
    this.contentDescription = null;
  }

  public ContentHints(DERObjectIdentifier paramDERObjectIdentifier, DERUTF8String paramDERUTF8String)
  {
    this.contentType = paramDERObjectIdentifier;
    this.contentDescription = paramDERUTF8String;
  }

  public static ContentHints getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ContentHints)))
      return (ContentHints)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ContentHints((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'ContentHints' factory : " + paramObject.getClass().getName() + ".");
  }

  public DERUTF8String getContentDescription()
  {
    return this.contentDescription;
  }

  public DERObjectIdentifier getContentType()
  {
    return this.contentType;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.contentDescription != null)
      localASN1EncodableVector.add(this.contentDescription);
    localASN1EncodableVector.add(this.contentType);
    return new DERSequence(localASN1EncodableVector);
  }
}