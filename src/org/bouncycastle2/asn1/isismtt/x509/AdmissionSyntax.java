package org.bouncycastle2.asn1.isismtt.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.GeneralName;

public class AdmissionSyntax extends ASN1Encodable
{
  private GeneralName admissionAuthority;
  private ASN1Sequence contentsOfAdmissions;

  private AdmissionSyntax(ASN1Sequence paramASN1Sequence)
  {
    switch (paramASN1Sequence.size())
    {
    default:
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    case 1:
      this.contentsOfAdmissions = DERSequence.getInstance(paramASN1Sequence.getObjectAt(0));
      return;
    case 2:
    }
    this.admissionAuthority = GeneralName.getInstance(paramASN1Sequence.getObjectAt(0));
    this.contentsOfAdmissions = DERSequence.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public AdmissionSyntax(GeneralName paramGeneralName, ASN1Sequence paramASN1Sequence)
  {
    this.admissionAuthority = paramGeneralName;
    this.contentsOfAdmissions = paramASN1Sequence;
  }

  public static AdmissionSyntax getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof AdmissionSyntax)))
      return (AdmissionSyntax)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AdmissionSyntax((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public GeneralName getAdmissionAuthority()
  {
    return this.admissionAuthority;
  }

  public Admissions[] getContentsOfAdmissions()
  {
    Admissions[] arrayOfAdmissions = new Admissions[this.contentsOfAdmissions.size()];
    int i = 0;
    Enumeration localEnumeration = this.contentsOfAdmissions.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return arrayOfAdmissions;
      int j = i + 1;
      arrayOfAdmissions[i] = Admissions.getInstance(localEnumeration.nextElement());
      i = j;
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.admissionAuthority != null)
      localASN1EncodableVector.add(this.admissionAuthority);
    localASN1EncodableVector.add(this.contentsOfAdmissions);
    return new DERSequence(localASN1EncodableVector);
  }
}