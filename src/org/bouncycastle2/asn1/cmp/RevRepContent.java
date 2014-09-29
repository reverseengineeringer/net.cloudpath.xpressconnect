package org.bouncycastle2.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.crmf.CertId;
import org.bouncycastle2.asn1.x509.CertificateList;

public class RevRepContent extends ASN1Encodable
{
  private ASN1Sequence crls;
  private ASN1Sequence revCerts;
  private ASN1Sequence status;

  private RevRepContent(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.status = ASN1Sequence.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(localEnumeration.nextElement());
      if (localASN1TaggedObject.getTagNo() == 0)
        this.revCerts = ASN1Sequence.getInstance(localASN1TaggedObject, true);
      else
        this.crls = ASN1Sequence.getInstance(localASN1TaggedObject, true);
    }
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, int paramInt, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(new DERTaggedObject(true, paramInt, paramASN1Encodable));
  }

  public static RevRepContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof RevRepContent))
      return (RevRepContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RevRepContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CertificateList[] getCrls()
  {
    CertificateList[] arrayOfCertificateList;
    if (this.crls == null)
      arrayOfCertificateList = null;
    while (true)
    {
      return arrayOfCertificateList;
      arrayOfCertificateList = new CertificateList[this.crls.size()];
      for (int i = 0; i != arrayOfCertificateList.length; i++)
        arrayOfCertificateList[i] = CertificateList.getInstance(this.crls.getObjectAt(i));
    }
  }

  public CertId[] getRevCerts()
  {
    CertId[] arrayOfCertId;
    if (this.revCerts == null)
      arrayOfCertId = null;
    while (true)
    {
      return arrayOfCertId;
      arrayOfCertId = new CertId[this.revCerts.size()];
      for (int i = 0; i != arrayOfCertId.length; i++)
        arrayOfCertId[i] = CertId.getInstance(this.revCerts.getObjectAt(i));
    }
  }

  public PKIStatusInfo[] getStatus()
  {
    PKIStatusInfo[] arrayOfPKIStatusInfo = new PKIStatusInfo[this.status.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfPKIStatusInfo.length)
        return arrayOfPKIStatusInfo;
      arrayOfPKIStatusInfo[i] = PKIStatusInfo.getInstance(this.status.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.status);
    addOptional(localASN1EncodableVector, 0, this.revCerts);
    addOptional(localASN1EncodableVector, 1, this.crls);
    return new DERSequence(localASN1EncodableVector);
  }
}