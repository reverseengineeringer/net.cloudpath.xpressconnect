package org.bouncycastle2.asn1.esf;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class OtherRevRefs extends ASN1Encodable
{
  private ASN1ObjectIdentifier otherRevRefType;
  private ASN1Object otherRevRefs;

  private OtherRevRefs(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.otherRevRefType = new ASN1ObjectIdentifier(((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0)).getId());
    try
    {
      this.otherRevRefs = ASN1Object.fromByteArray(paramASN1Sequence.getObjectAt(1).getDERObject().getDEREncoded());
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalStateException();
  }

  public static OtherRevRefs getInstance(Object paramObject)
  {
    if ((paramObject instanceof OtherRevRefs))
      return (OtherRevRefs)paramObject;
    if (paramObject != null)
      return new OtherRevRefs(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public ASN1ObjectIdentifier getOtherRevRefType()
  {
    return this.otherRevRefType;
  }

  public ASN1Object getOtherRevRefs()
  {
    return this.otherRevRefs;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.otherRevRefType);
    localASN1EncodableVector.add(this.otherRevRefs);
    return new DERSequence(localASN1EncodableVector);
  }
}