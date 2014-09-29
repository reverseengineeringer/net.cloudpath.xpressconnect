package org.bouncycastle2.asn1.cryptopro;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class GOST3410PublicKeyAlgParameters extends ASN1Encodable
{
  private DERObjectIdentifier digestParamSet;
  private DERObjectIdentifier encryptionParamSet;
  private DERObjectIdentifier publicKeyParamSet;

  public GOST3410PublicKeyAlgParameters(ASN1Sequence paramASN1Sequence)
  {
    this.publicKeyParamSet = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    this.digestParamSet = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() > 2)
      this.encryptionParamSet = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(2));
  }

  public GOST3410PublicKeyAlgParameters(DERObjectIdentifier paramDERObjectIdentifier1, DERObjectIdentifier paramDERObjectIdentifier2)
  {
    this.publicKeyParamSet = paramDERObjectIdentifier1;
    this.digestParamSet = paramDERObjectIdentifier2;
    this.encryptionParamSet = null;
  }

  public GOST3410PublicKeyAlgParameters(DERObjectIdentifier paramDERObjectIdentifier1, DERObjectIdentifier paramDERObjectIdentifier2, DERObjectIdentifier paramDERObjectIdentifier3)
  {
    this.publicKeyParamSet = paramDERObjectIdentifier1;
    this.digestParamSet = paramDERObjectIdentifier2;
    this.encryptionParamSet = paramDERObjectIdentifier3;
  }

  public static GOST3410PublicKeyAlgParameters getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof GOST3410PublicKeyAlgParameters)))
      return (GOST3410PublicKeyAlgParameters)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new GOST3410PublicKeyAlgParameters((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid GOST3410Parameter: " + paramObject.getClass().getName());
  }

  public static GOST3410PublicKeyAlgParameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERObjectIdentifier getDigestParamSet()
  {
    return this.digestParamSet;
  }

  public DERObjectIdentifier getEncryptionParamSet()
  {
    return this.encryptionParamSet;
  }

  public DERObjectIdentifier getPublicKeyParamSet()
  {
    return this.publicKeyParamSet;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.publicKeyParamSet);
    localASN1EncodableVector.add(this.digestParamSet);
    if (this.encryptionParamSet != null)
      localASN1EncodableVector.add(this.encryptionParamSet);
    return new DERSequence(localASN1EncodableVector);
  }
}