package org.bouncycastle2.asn1.ess;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.PolicyInformation;

public class SigningCertificateV2 extends ASN1Encodable
{
  ASN1Sequence certs;
  ASN1Sequence policies;

  public SigningCertificateV2(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.certs = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.policies = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public SigningCertificateV2(ESSCertIDv2[] paramArrayOfESSCertIDv2)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfESSCertIDv2.length)
      {
        this.certs = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfESSCertIDv2[i]);
    }
  }

  public SigningCertificateV2(ESSCertIDv2[] paramArrayOfESSCertIDv2, PolicyInformation[] paramArrayOfPolicyInformation)
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    int i = 0;
    ASN1EncodableVector localASN1EncodableVector2;
    if (i >= paramArrayOfESSCertIDv2.length)
    {
      this.certs = new DERSequence(localASN1EncodableVector1);
      if (paramArrayOfPolicyInformation != null)
        localASN1EncodableVector2 = new ASN1EncodableVector();
    }
    for (int j = 0; ; j++)
    {
      if (j >= paramArrayOfPolicyInformation.length)
      {
        this.policies = new DERSequence(localASN1EncodableVector2);
        return;
        localASN1EncodableVector1.add(paramArrayOfESSCertIDv2[i]);
        i++;
        break;
      }
      localASN1EncodableVector2.add(paramArrayOfPolicyInformation[j]);
    }
  }

  public static SigningCertificateV2 getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SigningCertificateV2)))
      return (SigningCertificateV2)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SigningCertificateV2((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'SigningCertificateV2' factory : " + paramObject.getClass().getName() + ".");
  }

  public ESSCertIDv2[] getCerts()
  {
    ESSCertIDv2[] arrayOfESSCertIDv2 = new ESSCertIDv2[this.certs.size()];
    for (int i = 0; ; i++)
    {
      if (i == this.certs.size())
        return arrayOfESSCertIDv2;
      arrayOfESSCertIDv2[i] = ESSCertIDv2.getInstance(this.certs.getObjectAt(i));
    }
  }

  public PolicyInformation[] getPolicies()
  {
    PolicyInformation[] arrayOfPolicyInformation;
    if (this.policies == null)
      arrayOfPolicyInformation = null;
    while (true)
    {
      return arrayOfPolicyInformation;
      arrayOfPolicyInformation = new PolicyInformation[this.policies.size()];
      for (int i = 0; i != this.policies.size(); i++)
        arrayOfPolicyInformation[i] = PolicyInformation.getInstance(this.policies.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certs);
    if (this.policies != null)
      localASN1EncodableVector.add(this.policies);
    return new DERSequence(localASN1EncodableVector);
  }
}