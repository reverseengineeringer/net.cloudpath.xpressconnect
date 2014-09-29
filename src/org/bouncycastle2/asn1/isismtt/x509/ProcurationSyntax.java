package org.bouncycastle2.asn1.isismtt.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x500.DirectoryString;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.IssuerSerial;

public class ProcurationSyntax extends ASN1Encodable
{
  private IssuerSerial certRef;
  private String country;
  private GeneralName thirdPerson;
  private DirectoryString typeOfSubstitution;

  public ProcurationSyntax(String paramString, DirectoryString paramDirectoryString, GeneralName paramGeneralName)
  {
    this.country = paramString;
    this.typeOfSubstitution = paramDirectoryString;
    this.thirdPerson = paramGeneralName;
    this.certRef = null;
  }

  public ProcurationSyntax(String paramString, DirectoryString paramDirectoryString, IssuerSerial paramIssuerSerial)
  {
    this.country = paramString;
    this.typeOfSubstitution = paramDirectoryString;
    this.thirdPerson = null;
    this.certRef = paramIssuerSerial;
  }

  private ProcurationSyntax(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 3))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(localEnumeration.nextElement());
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("Bad tag number: " + localASN1TaggedObject.getTagNo());
      case 1:
        this.country = DERPrintableString.getInstance(localASN1TaggedObject, true).getString();
        break;
      case 2:
        this.typeOfSubstitution = DirectoryString.getInstance(localASN1TaggedObject, true);
        break;
      case 3:
        DERObject localDERObject = localASN1TaggedObject.getObject();
        if ((localDERObject instanceof ASN1TaggedObject))
          this.thirdPerson = GeneralName.getInstance(localDERObject);
        else
          this.certRef = IssuerSerial.getInstance(localDERObject);
        break;
      }
    }
  }

  public static ProcurationSyntax getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ProcurationSyntax)))
      return (ProcurationSyntax)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ProcurationSyntax((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public IssuerSerial getCertRef()
  {
    return this.certRef;
  }

  public String getCountry()
  {
    return this.country;
  }

  public GeneralName getThirdPerson()
  {
    return this.thirdPerson;
  }

  public DirectoryString getTypeOfSubstitution()
  {
    return this.typeOfSubstitution;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.country != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, new DERPrintableString(this.country, true)));
    if (this.typeOfSubstitution != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.typeOfSubstitution));
    if (this.thirdPerson != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 3, this.thirdPerson));
    while (true)
    {
      return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(new DERTaggedObject(true, 3, this.certRef));
    }
  }
}