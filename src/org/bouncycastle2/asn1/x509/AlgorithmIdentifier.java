package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class AlgorithmIdentifier extends ASN1Encodable
{
  private DERObjectIdentifier objectId;
  private DEREncodable parameters;
  private boolean parametersDefined = false;

  public AlgorithmIdentifier(String paramString)
  {
    this.objectId = new DERObjectIdentifier(paramString);
  }

  public AlgorithmIdentifier(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.objectId = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
    {
      this.parametersDefined = true;
      this.parameters = paramASN1Sequence.getObjectAt(1);
      return;
    }
    this.parameters = null;
  }

  public AlgorithmIdentifier(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.objectId = paramDERObjectIdentifier;
  }

  public AlgorithmIdentifier(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.parametersDefined = true;
    this.objectId = paramDERObjectIdentifier;
    this.parameters = paramDEREncodable;
  }

  public static AlgorithmIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof AlgorithmIdentifier)))
      return (AlgorithmIdentifier)paramObject;
    if ((paramObject instanceof DERObjectIdentifier))
      return new AlgorithmIdentifier((DERObjectIdentifier)paramObject);
    if ((paramObject instanceof String))
      return new AlgorithmIdentifier((String)paramObject);
    if ((paramObject instanceof ASN1Sequence))
      return new AlgorithmIdentifier((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static AlgorithmIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1ObjectIdentifier getAlgorithm()
  {
    return new ASN1ObjectIdentifier(this.objectId.getId());
  }

  public DERObjectIdentifier getObjectId()
  {
    return this.objectId;
  }

  public DEREncodable getParameters()
  {
    return this.parameters;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.objectId);
    if (this.parametersDefined)
    {
      if (this.parameters == null)
        break label47;
      localASN1EncodableVector.add(this.parameters);
    }
    while (true)
    {
      return new DERSequence(localASN1EncodableVector);
      label47: localASN1EncodableVector.add(DERNull.INSTANCE);
    }
  }
}