package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class SignaturePolicyId extends ASN1Encodable
{
  private OtherHashAlgAndValue sigPolicyHash;
  private DERObjectIdentifier sigPolicyId;
  private SigPolicyQualifiers sigPolicyQualifiers;

  public SignaturePolicyId(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() != 2) && (paramASN1Sequence.size() != 3))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.sigPolicyId = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.sigPolicyHash = OtherHashAlgAndValue.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() == 3)
      this.sigPolicyQualifiers = SigPolicyQualifiers.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public SignaturePolicyId(DERObjectIdentifier paramDERObjectIdentifier, OtherHashAlgAndValue paramOtherHashAlgAndValue)
  {
    this(paramDERObjectIdentifier, paramOtherHashAlgAndValue, null);
  }

  public SignaturePolicyId(DERObjectIdentifier paramDERObjectIdentifier, OtherHashAlgAndValue paramOtherHashAlgAndValue, SigPolicyQualifiers paramSigPolicyQualifiers)
  {
    this.sigPolicyId = paramDERObjectIdentifier;
    this.sigPolicyHash = paramOtherHashAlgAndValue;
    this.sigPolicyQualifiers = paramSigPolicyQualifiers;
  }

  public static SignaturePolicyId getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SignaturePolicyId)))
      return (SignaturePolicyId)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SignaturePolicyId((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Unknown object in 'SignaturePolicyId' factory : " + paramObject.getClass().getName() + ".");
  }

  public OtherHashAlgAndValue getSigPolicyHash()
  {
    return this.sigPolicyHash;
  }

  public ASN1ObjectIdentifier getSigPolicyId()
  {
    return new ASN1ObjectIdentifier(this.sigPolicyId.getId());
  }

  public SigPolicyQualifiers getSigPolicyQualifiers()
  {
    return this.sigPolicyQualifiers;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.sigPolicyId);
    localASN1EncodableVector.add(this.sigPolicyHash);
    if (this.sigPolicyQualifiers != null)
      localASN1EncodableVector.add(this.sigPolicyQualifiers);
    return new DERSequence(localASN1EncodableVector);
  }
}