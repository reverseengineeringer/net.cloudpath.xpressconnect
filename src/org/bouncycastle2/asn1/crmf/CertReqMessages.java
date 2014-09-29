package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CertReqMessages extends ASN1Encodable
{
  private ASN1Sequence content;

  private CertReqMessages(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public CertReqMessages(CertReqMsg paramCertReqMsg)
  {
    this.content = new DERSequence(paramCertReqMsg);
  }

  public CertReqMessages(CertReqMsg[] paramArrayOfCertReqMsg)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfCertReqMsg.length)
      {
        this.content = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfCertReqMsg[i]);
    }
  }

  public static CertReqMessages getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertReqMessages))
      return (CertReqMessages)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertReqMessages((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public CertReqMsg[] toCertReqMsgArray()
  {
    CertReqMsg[] arrayOfCertReqMsg = new CertReqMsg[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfCertReqMsg.length)
        return arrayOfCertReqMsg;
      arrayOfCertReqMsg[i] = CertReqMsg.getInstance(this.content.getObjectAt(i));
    }
  }
}