package org.bouncycastle2.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.DERUTF8String;
import org.bouncycastle2.asn1.x500.DirectoryString;

public class SignerLocation extends ASN1Encodable
{
  private DERUTF8String countryName;
  private DERUTF8String localityName;
  private ASN1Sequence postalAddress;

  public SignerLocation(ASN1Sequence paramASN1Sequence)
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
        this.countryName = new DERUTF8String(DirectoryString.getInstance(localDERTaggedObject, true).getString());
        break;
      case 1:
        this.localityName = new DERUTF8String(DirectoryString.getInstance(localDERTaggedObject, true).getString());
        break;
      case 2:
        if (localDERTaggedObject.isExplicit());
        for (this.postalAddress = ASN1Sequence.getInstance(localDERTaggedObject, true); (this.postalAddress != null) && (this.postalAddress.size() > 6); this.postalAddress = ASN1Sequence.getInstance(localDERTaggedObject, false))
          throw new IllegalArgumentException("postal address must contain less than 6 strings");
      }
    }
  }

  public SignerLocation(DERUTF8String paramDERUTF8String1, DERUTF8String paramDERUTF8String2, ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence != null) && (paramASN1Sequence.size() > 6))
      throw new IllegalArgumentException("postal address must contain less than 6 strings");
    if (paramDERUTF8String1 != null)
      this.countryName = DERUTF8String.getInstance(paramDERUTF8String1.toASN1Object());
    if (paramDERUTF8String2 != null)
      this.localityName = DERUTF8String.getInstance(paramDERUTF8String2.toASN1Object());
    if (paramASN1Sequence != null)
      this.postalAddress = ASN1Sequence.getInstance(paramASN1Sequence.toASN1Object());
  }

  public static SignerLocation getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SignerLocation)))
      return (SignerLocation)paramObject;
    return new SignerLocation(ASN1Sequence.getInstance(paramObject));
  }

  public DERUTF8String getCountryName()
  {
    return this.countryName;
  }

  public DERUTF8String getLocalityName()
  {
    return this.localityName;
  }

  public ASN1Sequence getPostalAddress()
  {
    return this.postalAddress;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.countryName != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.countryName));
    if (this.localityName != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.localityName));
    if (this.postalAddress != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.postalAddress));
    return new DERSequence(localASN1EncodableVector);
  }
}