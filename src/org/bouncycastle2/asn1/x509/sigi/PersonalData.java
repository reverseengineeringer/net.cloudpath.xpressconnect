package org.bouncycastle2.asn1.x509.sigi;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x500.DirectoryString;

public class PersonalData extends ASN1Encodable
{
  private DERGeneralizedTime dateOfBirth;
  private String gender;
  private BigInteger nameDistinguisher;
  private NameOrPseudonym nameOrPseudonym;
  private DirectoryString placeOfBirth;
  private DirectoryString postalAddress;

  private PersonalData(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() < 1)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.nameOrPseudonym = NameOrPseudonym.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(localEnumeration.nextElement());
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("Bad tag number: " + localASN1TaggedObject.getTagNo());
      case 0:
        this.nameDistinguisher = DERInteger.getInstance(localASN1TaggedObject, false).getValue();
        break;
      case 1:
        this.dateOfBirth = DERGeneralizedTime.getInstance(localASN1TaggedObject, false);
        break;
      case 2:
        this.placeOfBirth = DirectoryString.getInstance(localASN1TaggedObject, true);
        break;
      case 3:
        this.gender = DERPrintableString.getInstance(localASN1TaggedObject, false).getString();
        break;
      case 4:
        this.postalAddress = DirectoryString.getInstance(localASN1TaggedObject, true);
      }
    }
  }

  public PersonalData(NameOrPseudonym paramNameOrPseudonym, BigInteger paramBigInteger, DERGeneralizedTime paramDERGeneralizedTime, DirectoryString paramDirectoryString1, String paramString, DirectoryString paramDirectoryString2)
  {
    this.nameOrPseudonym = paramNameOrPseudonym;
    this.dateOfBirth = paramDERGeneralizedTime;
    this.gender = paramString;
    this.nameDistinguisher = paramBigInteger;
    this.postalAddress = paramDirectoryString2;
    this.placeOfBirth = paramDirectoryString1;
  }

  public static PersonalData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof PersonalData)))
      return (PersonalData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PersonalData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public DERGeneralizedTime getDateOfBirth()
  {
    return this.dateOfBirth;
  }

  public String getGender()
  {
    return this.gender;
  }

  public BigInteger getNameDistinguisher()
  {
    return this.nameDistinguisher;
  }

  public NameOrPseudonym getNameOrPseudonym()
  {
    return this.nameOrPseudonym;
  }

  public DirectoryString getPlaceOfBirth()
  {
    return this.placeOfBirth;
  }

  public DirectoryString getPostalAddress()
  {
    return this.postalAddress;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.nameOrPseudonym);
    if (this.nameDistinguisher != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, new DERInteger(this.nameDistinguisher)));
    if (this.dateOfBirth != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.dateOfBirth));
    if (this.placeOfBirth != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.placeOfBirth));
    if (this.gender != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 3, new DERPrintableString(this.gender, true)));
    if (this.postalAddress != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 4, this.postalAddress));
    return new DERSequence(localASN1EncodableVector);
  }
}