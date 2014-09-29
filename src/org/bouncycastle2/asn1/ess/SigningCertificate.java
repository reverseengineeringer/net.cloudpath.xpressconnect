package org.bouncycastle2.asn1.ess;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.PolicyInformation;

public class SigningCertificate extends ASN1Encodable
{
  ASN1Sequence certs;
  ASN1Sequence policies;

  public SigningCertificate(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.certs = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.policies = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public SigningCertificate(ESSCertID paramESSCertID)
  {
    this.certs = new DERSequence(paramESSCertID);
  }

  public static SigningCertificate getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SigningCertificate)))
      return (SigningCertificate)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SigningCertificate((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'SigningCertificate' factory : " + paramObject.getClass().getName() + ".");
  }

  public ESSCertID[] getCerts()
  {
    ESSCertID[] arrayOfESSCertID = new ESSCertID[this.certs.size()];
    for (int i = 0; ; i++)
    {
      if (i == this.certs.size())
        return arrayOfESSCertID;
      arrayOfESSCertID[i] = ESSCertID.getInstance(this.certs.getObjectAt(i));
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