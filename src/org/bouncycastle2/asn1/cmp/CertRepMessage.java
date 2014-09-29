package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class CertRepMessage extends ASN1Encodable
{
  private ASN1Sequence caPubs;
  private ASN1Sequence response;

  private CertRepMessage(ASN1Sequence paramASN1Sequence)
  {
    int i = paramASN1Sequence.size();
    int j = 0;
    if (i > 1)
    {
      int k = 0 + 1;
      this.caPubs = ASN1Sequence.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0), true);
      j = k;
    }
    this.response = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(j));
  }

  public CertRepMessage(CMPCertificate[] paramArrayOfCMPCertificate, CertResponse[] paramArrayOfCertResponse)
  {
    if (paramArrayOfCertResponse == null)
      throw new IllegalArgumentException("'response' cannot be null");
    ASN1EncodableVector localASN1EncodableVector1;
    int i;
    ASN1EncodableVector localASN1EncodableVector2;
    if (paramArrayOfCMPCertificate != null)
    {
      localASN1EncodableVector1 = new ASN1EncodableVector();
      i = 0;
      if (i >= paramArrayOfCMPCertificate.length)
        this.caPubs = new DERSequence(localASN1EncodableVector1);
    }
    else
    {
      localASN1EncodableVector2 = new ASN1EncodableVector();
    }
    for (int j = 0; ; j++)
    {
      if (j >= paramArrayOfCertResponse.length)
      {
        this.response = new DERSequence(localASN1EncodableVector2);
        return;
        localASN1EncodableVector1.add(paramArrayOfCMPCertificate[i]);
        i++;
        break;
      }
      localASN1EncodableVector2.add(paramArrayOfCertResponse[j]);
    }
  }

  public static CertRepMessage getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertRepMessage))
      return (CertRepMessage)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertRepMessage((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CMPCertificate[] getCaPubs()
  {
    CMPCertificate[] arrayOfCMPCertificate;
    if (this.caPubs == null)
      arrayOfCMPCertificate = null;
    while (true)
    {
      return arrayOfCMPCertificate;
      arrayOfCMPCertificate = new CMPCertificate[this.caPubs.size()];
      for (int i = 0; i != arrayOfCMPCertificate.length; i++)
        arrayOfCMPCertificate[i] = CMPCertificate.getInstance(this.caPubs.getObjectAt(i));
    }
  }

  public CertResponse[] getResponse()
  {
    CertResponse[] arrayOfCertResponse = new CertResponse[this.response.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfCertResponse.length)
        return arrayOfCertResponse;
      arrayOfCertResponse[i] = CertResponse.getInstance(this.response.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.caPubs != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.caPubs));
    localASN1EncodableVector.add(this.response);
    return new DERSequence(localASN1EncodableVector);
  }
}