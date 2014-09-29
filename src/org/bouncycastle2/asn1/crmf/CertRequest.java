package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CertRequest extends ASN1Encodable
{
  private DERInteger certReqId;
  private CertTemplate certTemplate;
  private Controls controls;

  public CertRequest(int paramInt, CertTemplate paramCertTemplate, Controls paramControls)
  {
    this(new DERInteger(paramInt), paramCertTemplate, paramControls);
  }

  private CertRequest(ASN1Sequence paramASN1Sequence)
  {
    this.certReqId = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    this.certTemplate = CertTemplate.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() > 2)
      this.controls = Controls.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public CertRequest(DERInteger paramDERInteger, CertTemplate paramCertTemplate, Controls paramControls)
  {
    this.certReqId = paramDERInteger;
    this.certTemplate = paramCertTemplate;
    this.controls = paramControls;
  }

  public static CertRequest getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertRequest))
      return (CertRequest)paramObject;
    if (paramObject != null)
      return new CertRequest(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public DERInteger getCertReqId()
  {
    return this.certReqId;
  }

  public CertTemplate getCertTemplate()
  {
    return this.certTemplate;
  }

  public Controls getControls()
  {
    return this.controls;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certReqId);
    localASN1EncodableVector.add(this.certTemplate);
    if (this.controls != null)
      localASN1EncodableVector.add(this.controls);
    return new DERSequence(localASN1EncodableVector);
  }
}