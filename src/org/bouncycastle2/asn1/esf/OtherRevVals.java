package org.bouncycastle2.asn1.esf;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class OtherRevVals extends ASN1Encodable
{
  private DERObjectIdentifier otherRevValType;
  private ASN1Object otherRevVals;

  public OtherRevVals(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.otherRevValType = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    try
    {
      this.otherRevVals = ASN1Object.fromByteArray(paramASN1Sequence.getObjectAt(1).getDERObject().getDEREncoded());
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalStateException();
  }

  public OtherRevVals(DERObjectIdentifier paramDERObjectIdentifier, ASN1Object paramASN1Object)
  {
    this.otherRevValType = paramDERObjectIdentifier;
    this.otherRevVals = paramASN1Object;
  }

  public static OtherRevVals getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OtherRevVals)))
      return (OtherRevVals)paramObject;
    return new OtherRevVals((ASN1Sequence)paramObject);
  }

  public DERObjectIdentifier getOtherRevValType()
  {
    return this.otherRevValType;
  }

  public ASN1Object getOtherRevVals()
  {
    return this.otherRevVals;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.otherRevValType);
    localASN1EncodableVector.add(this.otherRevVals);
    return new DERSequence(localASN1EncodableVector);
  }
}