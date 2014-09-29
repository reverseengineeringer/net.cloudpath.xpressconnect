package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Null;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;

public class SignaturePolicyIdentifier extends ASN1Encodable
{
  private boolean isSignaturePolicyImplied;
  private SignaturePolicyId signaturePolicyId;

  public SignaturePolicyIdentifier()
  {
    this.isSignaturePolicyImplied = true;
  }

  public SignaturePolicyIdentifier(SignaturePolicyId paramSignaturePolicyId)
  {
    this.signaturePolicyId = paramSignaturePolicyId;
    this.isSignaturePolicyImplied = false;
  }

  public static SignaturePolicyIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SignaturePolicyIdentifier)))
      return (SignaturePolicyIdentifier)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SignaturePolicyIdentifier(SignaturePolicyId.getInstance(paramObject));
    if ((paramObject instanceof ASN1Null))
      return new SignaturePolicyIdentifier();
    throw new IllegalArgumentException("unknown object in 'SignaturePolicyIdentifier' factory: " + paramObject.getClass().getName() + ".");
  }

  public SignaturePolicyId getSignaturePolicyId()
  {
    return this.signaturePolicyId;
  }

  public boolean isSignaturePolicyImplied()
  {
    return this.isSignaturePolicyImplied;
  }

  public DERObject toASN1Object()
  {
    if (this.isSignaturePolicyImplied)
      return new DERNull();
    return this.signaturePolicyId.getDERObject();
  }
}