package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.cmp.CMPObjectIdentifiers;
import org.bouncycastle2.asn1.cmp.PBMParameter;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class PKMACValue extends ASN1Encodable
{
  private AlgorithmIdentifier algId;
  private DERBitString value;

  private PKMACValue(ASN1Sequence paramASN1Sequence)
  {
    this.algId = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.value = DERBitString.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public PKMACValue(PBMParameter paramPBMParameter, DERBitString paramDERBitString)
  {
    this(new AlgorithmIdentifier(CMPObjectIdentifiers.passwordBasedMac, paramPBMParameter), paramDERBitString);
  }

  public PKMACValue(AlgorithmIdentifier paramAlgorithmIdentifier, DERBitString paramDERBitString)
  {
    this.algId = paramAlgorithmIdentifier;
    this.value = paramDERBitString;
  }

  public static PKMACValue getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKMACValue))
      return (PKMACValue)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PKMACValue((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public static PKMACValue getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getAlgId()
  {
    return this.algId;
  }

  public DERBitString getValue()
  {
    return this.value;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.algId);
    localASN1EncodableVector.add(this.value);
    return new DERSequence(localASN1EncodableVector);
  }
}