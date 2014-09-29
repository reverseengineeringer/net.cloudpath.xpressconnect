package org.bouncycastle2.asn1.crmf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CertReqMsg extends ASN1Encodable
{
  private CertRequest certReq;
  private ProofOfPossession popo;
  private ASN1Sequence regInfo;

  private CertReqMsg(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.certReq = CertRequest.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      Object localObject = localEnumeration.nextElement();
      if (((localObject instanceof ASN1TaggedObject)) || ((localObject instanceof ProofOfPossession)))
        this.popo = ProofOfPossession.getInstance(localObject);
      else
        this.regInfo = ASN1Sequence.getInstance(localObject);
    }
  }

  public CertReqMsg(CertRequest paramCertRequest, ProofOfPossession paramProofOfPossession, AttributeTypeAndValue[] paramArrayOfAttributeTypeAndValue)
  {
    if (paramCertRequest == null)
      throw new IllegalArgumentException("'certReq' cannot be null");
    this.certReq = paramCertRequest;
    this.popo = paramProofOfPossession;
    if (paramArrayOfAttributeTypeAndValue != null)
      this.regInfo = new DERSequence(paramArrayOfAttributeTypeAndValue);
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(paramASN1Encodable);
  }

  public static CertReqMsg getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertReqMsg))
      return (CertReqMsg)paramObject;
    if (paramObject != null)
      return new CertReqMsg(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public CertRequest getCertReq()
  {
    return this.certReq;
  }

  public ProofOfPossession getPop()
  {
    return this.popo;
  }

  public ProofOfPossession getPopo()
  {
    return this.popo;
  }

  public AttributeTypeAndValue[] getRegInfo()
  {
    AttributeTypeAndValue[] arrayOfAttributeTypeAndValue;
    if (this.regInfo == null)
      arrayOfAttributeTypeAndValue = null;
    while (true)
    {
      return arrayOfAttributeTypeAndValue;
      arrayOfAttributeTypeAndValue = new AttributeTypeAndValue[this.regInfo.size()];
      for (int i = 0; i != arrayOfAttributeTypeAndValue.length; i++)
        arrayOfAttributeTypeAndValue[i] = AttributeTypeAndValue.getInstance(this.regInfo.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certReq);
    addOptional(localASN1EncodableVector, this.popo);
    addOptional(localASN1EncodableVector, this.regInfo);
    return new DERSequence(localASN1EncodableVector);
  }
}