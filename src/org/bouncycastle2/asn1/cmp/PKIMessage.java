package org.bouncycastle2.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class PKIMessage extends ASN1Encodable
{
  private PKIBody body;
  private ASN1Sequence extraCerts;
  private PKIHeader header;
  private DERBitString protection;

  private PKIMessage(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.header = PKIHeader.getInstance(localEnumeration.nextElement());
    this.body = PKIBody.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localEnumeration.nextElement();
      if (localASN1TaggedObject.getTagNo() == 0)
        this.protection = DERBitString.getInstance(localASN1TaggedObject, true);
      else
        this.extraCerts = ASN1Sequence.getInstance(localASN1TaggedObject, true);
    }
  }

  public PKIMessage(PKIHeader paramPKIHeader, PKIBody paramPKIBody)
  {
    this(paramPKIHeader, paramPKIBody, null, null);
  }

  public PKIMessage(PKIHeader paramPKIHeader, PKIBody paramPKIBody, DERBitString paramDERBitString)
  {
    this(paramPKIHeader, paramPKIBody, paramDERBitString, null);
  }

  public PKIMessage(PKIHeader paramPKIHeader, PKIBody paramPKIBody, DERBitString paramDERBitString, CMPCertificate[] paramArrayOfCMPCertificate)
  {
    this.header = paramPKIHeader;
    this.body = paramPKIBody;
    this.protection = paramDERBitString;
    ASN1EncodableVector localASN1EncodableVector;
    if (paramArrayOfCMPCertificate != null)
      localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfCMPCertificate.length)
      {
        this.extraCerts = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfCMPCertificate[i]);
    }
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, int paramInt, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(new DERTaggedObject(true, paramInt, paramASN1Encodable));
  }

  public static PKIMessage getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKIMessage))
      return (PKIMessage)paramObject;
    if (paramObject != null)
      return new PKIMessage(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public PKIBody getBody()
  {
    return this.body;
  }

  public CMPCertificate[] getExtraCerts()
  {
    CMPCertificate[] arrayOfCMPCertificate;
    if (this.extraCerts == null)
      arrayOfCMPCertificate = null;
    while (true)
    {
      return arrayOfCMPCertificate;
      arrayOfCMPCertificate = new CMPCertificate[this.extraCerts.size()];
      for (int i = 0; i < arrayOfCMPCertificate.length; i++)
        arrayOfCMPCertificate[i] = CMPCertificate.getInstance(this.extraCerts.getObjectAt(i));
    }
  }

  public PKIHeader getHeader()
  {
    return this.header;
  }

  public DERBitString getProtection()
  {
    return this.protection;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.header);
    localASN1EncodableVector.add(this.body);
    addOptional(localASN1EncodableVector, 0, this.protection);
    addOptional(localASN1EncodableVector, 1, this.extraCerts);
    return new DERSequence(localASN1EncodableVector);
  }
}