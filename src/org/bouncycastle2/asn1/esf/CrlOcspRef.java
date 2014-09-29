package org.bouncycastle2.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class CrlOcspRef extends ASN1Encodable
{
  private CrlListID crlids;
  private OcspListID ocspids;
  private OtherRevRefs otherRev;

  private CrlOcspRef(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      DERTaggedObject localDERTaggedObject = (DERTaggedObject)localEnumeration.nextElement();
      switch (localDERTaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("illegal tag");
      case 0:
        this.crlids = CrlListID.getInstance(localDERTaggedObject.getObject());
        break;
      case 1:
        this.ocspids = OcspListID.getInstance(localDERTaggedObject.getObject());
        break;
      case 2:
        this.otherRev = OtherRevRefs.getInstance(localDERTaggedObject.getObject());
      }
    }
  }

  public CrlOcspRef(CrlListID paramCrlListID, OcspListID paramOcspListID, OtherRevRefs paramOtherRevRefs)
  {
    this.crlids = paramCrlListID;
    this.ocspids = paramOcspListID;
    this.otherRev = paramOtherRevRefs;
  }

  public static CrlOcspRef getInstance(Object paramObject)
  {
    if ((paramObject instanceof CrlOcspRef))
      return (CrlOcspRef)paramObject;
    if (paramObject != null)
      return new CrlOcspRef(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public CrlListID getCrlids()
  {
    return this.crlids;
  }

  public OcspListID getOcspids()
  {
    return this.ocspids;
  }

  public OtherRevRefs getOtherRev()
  {
    return this.otherRev;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.crlids != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.crlids.toASN1Object()));
    if (this.ocspids != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.ocspids.toASN1Object()));
    if (this.otherRev != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.otherRev.toASN1Object()));
    return new DERSequence(localASN1EncodableVector);
  }
}