package org.bouncycastle2.asn1.isismtt.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.GeneralName;

public class Admissions extends ASN1Encodable
{
  private GeneralName admissionAuthority;
  private NamingAuthority namingAuthority;
  private ASN1Sequence professionInfos;

  private Admissions(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() > 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    DEREncodable localDEREncodable = (DEREncodable)localEnumeration.nextElement();
    if ((localDEREncodable instanceof ASN1TaggedObject))
      switch (((ASN1TaggedObject)localDEREncodable).getTagNo())
      {
      default:
        throw new IllegalArgumentException("Bad tag number: " + ((ASN1TaggedObject)localDEREncodable).getTagNo());
      case 0:
        this.admissionAuthority = GeneralName.getInstance((ASN1TaggedObject)localDEREncodable, true);
      case 1:
      }
    while (true)
    {
      localDEREncodable = (DEREncodable)localEnumeration.nextElement();
      if (!(localDEREncodable instanceof ASN1TaggedObject))
        break;
      switch (((ASN1TaggedObject)localDEREncodable).getTagNo())
      {
      default:
        throw new IllegalArgumentException("Bad tag number: " + ((ASN1TaggedObject)localDEREncodable).getTagNo());
        this.namingAuthority = NamingAuthority.getInstance((ASN1TaggedObject)localDEREncodable, true);
      case 1:
      }
    }
    this.namingAuthority = NamingAuthority.getInstance((ASN1TaggedObject)localDEREncodable, true);
    localDEREncodable = (DEREncodable)localEnumeration.nextElement();
    this.professionInfos = ASN1Sequence.getInstance(localDEREncodable);
    if (localEnumeration.hasMoreElements())
      throw new IllegalArgumentException("Bad object encountered: " + localEnumeration.nextElement().getClass());
  }

  public Admissions(GeneralName paramGeneralName, NamingAuthority paramNamingAuthority, ProfessionInfo[] paramArrayOfProfessionInfo)
  {
    this.admissionAuthority = paramGeneralName;
    this.namingAuthority = paramNamingAuthority;
    this.professionInfos = new DERSequence(paramArrayOfProfessionInfo);
  }

  public static Admissions getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof Admissions)))
      return (Admissions)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Admissions((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public GeneralName getAdmissionAuthority()
  {
    return this.admissionAuthority;
  }

  public NamingAuthority getNamingAuthority()
  {
    return this.namingAuthority;
  }

  public ProfessionInfo[] getProfessionInfos()
  {
    ProfessionInfo[] arrayOfProfessionInfo = new ProfessionInfo[this.professionInfos.size()];
    int i = 0;
    Enumeration localEnumeration = this.professionInfos.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return arrayOfProfessionInfo;
      int j = i + 1;
      arrayOfProfessionInfo[i] = ProfessionInfo.getInstance(localEnumeration.nextElement());
      i = j;
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.admissionAuthority != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.admissionAuthority));
    if (this.namingAuthority != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.namingAuthority));
    localASN1EncodableVector.add(this.professionInfos);
    return new DERSequence(localASN1EncodableVector);
  }
}