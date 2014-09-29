package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class ProtectedPart extends ASN1Encodable
{
  private PKIBody body;
  private PKIHeader header;

  private ProtectedPart(ASN1Sequence paramASN1Sequence)
  {
    this.header = PKIHeader.getInstance(paramASN1Sequence.getObjectAt(0));
    this.body = PKIBody.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public ProtectedPart(PKIHeader paramPKIHeader, PKIBody paramPKIBody)
  {
    this.header = paramPKIHeader;
    this.body = paramPKIBody;
  }

  public static ProtectedPart getInstance(Object paramObject)
  {
    if ((paramObject instanceof ProtectedPart))
      return (ProtectedPart)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ProtectedPart((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public PKIBody getBody()
  {
    return this.body;
  }

  public PKIHeader getHeader()
  {
    return this.header;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.header);
    localASN1EncodableVector.add(this.body);
    return new DERSequence(localASN1EncodableVector);
  }
}